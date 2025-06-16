package org.fabiojava.timebank.gui.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import lombok.extern.java.Log;
import org.fabiojava.timebank.domain.dto.MatchingInfoDTO;
import org.fabiojava.timebank.domain.dto.PrenotazioneDTO;
import org.fabiojava.timebank.domain.dto.RichiestaOffertaDTO;
import org.fabiojava.timebank.domain.model.*;
import org.fabiojava.timebank.domain.ports.repositories.ValutazioneRepository;
import org.fabiojava.timebank.domain.services.PrenotazioniService;
import org.fabiojava.timebank.domain.services.UtenteService;
import org.fabiojava.timebank.gui.controllers.dialogs.ValutazioneDialogController;
import org.fabiojava.timebank.gui.utils.SceneManager;
import org.fabiojava.timebank.gui.utils.SessionManager;
import org.fabiojava.timebank.gui.utils.SpringFXMLLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.Optional;

@Log
@Controller
public class PrenotazioneController {
    private final SceneManager sceneManager;
    private final SessionManager sessionManager;
    private final PrenotazioniService prenotazioniService;
    private final SpringFXMLLoader fxmlLoader;
    private final ValutazioneRepository valutazioneRepository;
    private final UtenteService utenteService;

    private RichiestaOffertaDTO richiestaOffertaDTO;
    private int paginaAttuale = 0;
    private int totalePagine = 0;

    @FXML    private Label attivitaLabel;
    @FXML    private Label tipoLabel;
    @FXML    private Label statoLabel;

    @FXML    private TableView<PrenotazioneDTO> tabellaPrenotazioni;
    @FXML    private TableColumn<PrenotazioneDTO, Date> dataPrenotazioneColonna;
    @FXML    private TableColumn<PrenotazioneDTO, String> utenteColonna;
    @FXML    private TableColumn<PrenotazioneDTO, String> noteColonna;
    @FXML    private TableColumn<PrenotazioneDTO, Integer> oreColonna;
    @FXML    private TableColumn<PrenotazioneDTO, Void> azioniColonna;

    @FXML    private Label paginaCorrente;
    @FXML    private Label totaleRecord;

    public void initialize(){
        if(sessionManager.getDataTransferObject() instanceof RichiestaOffertaDTO dto) {
            this.richiestaOffertaDTO = dto;
            attivitaLabel.setText(richiestaOffertaDTO.getNomeAttivita());
            tipoLabel.setText(richiestaOffertaDTO.getTipoInserimento());
            statoLabel.setText(richiestaOffertaDTO.getStato());
            sessionManager.setDataTransferObject(null);
        }
        sceneManager.registerNavigationCallback(SceneManager.SceneType.PRENOTAZIONI_LIST,
                () -> sessionManager.setDataTransferObject(Inserimento.TIPO_INSERIMENTO.valueOf(richiestaOffertaDTO.getTipoInserimento())));
        configuraTabellaColonne();
        caricaDati();
    }

    @Autowired
    public PrenotazioneController(SceneManager sceneManager, SessionManager sessionManager, PrenotazioniService prenotazioniService, SpringFXMLLoader fxmlLoader, ValutazioneRepository valutazioneRepository, UtenteService utenteService) {
        this.sceneManager = sceneManager;
        this.sessionManager = sessionManager;
        this.prenotazioniService = prenotazioniService;
        this.fxmlLoader = fxmlLoader;
        this.valutazioneRepository = valutazioneRepository;
        this.utenteService = utenteService;
    }

    private void configuraTabellaColonne() {
        dataPrenotazioneColonna.setCellValueFactory(new PropertyValueFactory<>("dataMatching"));
        noteColonna.setCellValueFactory(new PropertyValueFactory<>("noteFeedback"));
        oreColonna.setCellValueFactory(new PropertyValueFactory<>("oreConcordate"));

        utenteColonna.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    PrenotazioneDTO prenotazioneDTO = getTableView().getItems().get(getIndex());
                    Optional<MatchingInfoDTO> matchingInfo = prenotazioniService.getMatchingInfoById(prenotazioneDTO.getIdPrenotazione());
                    if (matchingInfo.isPresent()) {
                        String matricolaCorrente = sessionManager.getCurrentUser().getMatricola();
                        String matricolaDaMostrare;

                        if (matricolaCorrente.equals(matchingInfo.get().getMatricolaOfferente()))
                            matricolaDaMostrare = matchingInfo.get().getMatricolaRichiedente();
                        else matricolaDaMostrare = matchingInfo.get().getMatricolaOfferente();
                        setText(matricolaDaMostrare);
                    } else {
                        setText("N/D");
                    }
                }
            }
        });

        azioniColonna.setCellFactory(col -> new TableCell<>() {
            private final Button btnAccetta = new Button("✔");
            private final Button btnRifiuta = new Button("✘");
            private final HBox hbox = new HBox(5, btnAccetta, btnRifiuta);

            {
                if(richiestaOffertaDTO.getStato().equals(Richiesta.StatoRichiesta.ASSEGNATA.name())
                    || richiestaOffertaDTO.getStato().equals(Offerta.StatoOfferta.PRENOTATA.name())) {
                    btnAccetta.setDisable(true);
                    btnRifiuta.setDisable(true);
                } else {
                    btnAccetta.setOnAction(event -> {
                        PrenotazioneDTO dto = getTableRow().getItem();
                        if (dto != null) {
                            Dialog<ButtonType> dialog = new Dialog<>();
                            dialog.setTitle("Conferma prenotazione");
                            dialog.setHeaderText(null);

                            DialogPane dialogPane = dialog.getDialogPane();
                            dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

                            DatePicker datePicker = new DatePicker();
                            datePicker.setShowWeekNumbers(false);
                            TextArea noteArea = new TextArea();
                            noteArea.setPromptText("Note (opzionale)");

                            GridPane grid = new GridPane();
                            grid.setHgap(10);
                            grid.setVgap(10);
                            grid.setPadding(new Insets(20, 150, 10, 10));
                            grid.add(new Label("Data esecuzione:"), 0, 0);
                            grid.add(datePicker, 1, 0);
                            grid.add(new Label("Note:"), 0, 1);
                            grid.add(noteArea, 1, 1);

                            dialogPane.setContent(grid);

                            dialog.showAndWait().ifPresent(response -> {
                                if (response == ButtonType.OK) {
                                    Optional<MatchingInfoDTO> matchingInfoDTO = prenotazioniService.getMatchingInfoById(dto.getIdPrenotazione());
                                    if (matchingInfoDTO.isEmpty()) return;
                                    MatchingInfoDTO info = matchingInfoDTO.get();
                                    if (sessionManager.getCurrentUser().getMatricola().equals(info.getMatricolaOfferente())) {
                                        // io ho fatto l'offerta e sto guardando le richieste prenotate
                                        prenotazioniService.getMatchingOffertaById(dto.getIdOfferta()).ifPresent(offerta -> {
                                            if (datePicker.getValue().isBefore(offerta.getDataDisponibilitaInizio().toLocalDate())
                                                    || datePicker.getValue().isAfter(offerta.getDataDisponibilitaFine().toLocalDate())) {
                                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                                alert.setTitle("Errore nella prenotazione");
                                                alert.setHeaderText("La data di esecuzione non è nel range dell'offerta");
                                                alert.setContentText(null);
                                                alert.show();
                                            } else {
                                                dto.setDataEsecuzione(Date.valueOf(datePicker.getValue()));
                                                if (!noteArea.getText().isEmpty())
                                                    dto.setNoteFeedback(noteArea.getText());
                                                accettaPrenotazione(dto);
                                            }
                                        });
                                    } else if (sessionManager.getCurrentUser().getMatricola().equals(info.getMatricolaRichiedente())) {
                                        prenotazioniService.getMatchingRichiestaById(dto.getIdRichiesta()).ifPresent(richiesta -> {
                                            if (datePicker.getValue().isBefore(richiesta.getDataRichiestaInizio().toLocalDate())
                                                    || datePicker.getValue().isAfter(richiesta.getDataRichiestaFine().toLocalDate())) {
                                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                                alert.setTitle("Errore nella prenotazione");
                                                alert.setHeaderText("La data di esecuzione non è nel range della richiesta");
                                                alert.setContentText(null);
                                                alert.show();
                                            } else {
                                                dto.setDataEsecuzione(Date.valueOf(datePicker.getValue()));
                                                if (!noteArea.getText().isEmpty())
                                                    dto.setNoteFeedback(noteArea.getText());
                                                accettaPrenotazione(dto);
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    });
                    btnRifiuta.setOnAction(event -> {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Rifiuta prenotazione");
                        alert.setHeaderText(null);
                        alert.setGraphic(null);
                        alert.setContentText("Sei sicuro di voler rifiutare questa prenotazione?");
                        alert.showAndWait().ifPresent(response -> {
                            if (response == ButtonType.OK) rifiutaPrenotazione(getTableRow().getItem());
                            else if (response == ButtonType.CANCEL) alert.close();
                            else alert.close();
                        });
                    });
                }
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                hbox.setAlignment(Pos.CENTER);
                setGraphic(empty ? null : hbox);
                if(!empty){
                    PrenotazioneDTO dto = getTableRow().getItem();
                    if(dto != null) {
                        if (dto.getStato().name().equals(Prenotazione.StatoPrenotazione.PROGRAMMATO.name())) {
                            btnAccetta.setStyle("-fx-background-color: #008000; -fx-text-fill: white;");
                            btnAccetta.setText("\uD83D\uDCAF");
                            btnAccetta.setDisable(false);
                            btnAccetta.setOnAction(event -> mostraDialogValutazione(dto));
                        } else if(dto.getStato().name().equals(Prenotazione.StatoPrenotazione.CANCELLATO.name())) {
                            btnRifiuta.setStyle("-fx-background-color: #ff0000; -fx-text-fill: white;");
                        }
                    }
                }
            }
        });
    }

    private void accettaPrenotazione(PrenotazioneDTO dto) {
        dto.setStato(Prenotazione.StatoPrenotazione.PROGRAMMATO);
        prenotazioniService.aggiornaPrenotazione(dto);
        caricaDati();
    }

    private void rifiutaPrenotazione(PrenotazioneDTO dto) {
        dto.setStato(Prenotazione.StatoPrenotazione.CANCELLATO);
        prenotazioniService.aggiornaPrenotazione(dto);
        caricaDati();
    }

    private void caricaDati() {
        AllInsertionController.RichiestaCriteria criteria = AllInsertionController.RichiestaCriteria.builder()
                .pagina(paginaAttuale)
                .dimensionePagina(14)
                .build();
        Page<PrenotazioneDTO> record = prenotazioniService.filterByInserimento(richiestaOffertaDTO.getId(), criteria,
                Inserimento.TIPO_INSERIMENTO.valueOf(richiestaOffertaDTO.getTipoInserimento()));
        tabellaPrenotazioni.setItems(FXCollections.observableList(record.getContent()));
        totalePagine = record.getTotalPages();
        aggiornaPaginazione(record.getTotalElements());
    }

    private void mostraDialogValutazione(PrenotazioneDTO prenotazione) {
        try {
            URL location = getClass().getResource("/org/fabiojava/timebank/fxml/dialogs/valutazione-dialog.fxml");
            FXMLLoader loader = fxmlLoader.getLoader(location);
            DialogPane dialogPane = loader.load();

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Valutazione Attività");

            URL cssPath = getClass().getResource("/org/fabiojava/timebank/styles/dialog-style.css");
            if(cssPath != null && Objects.nonNull(dialog.getDialogPane().getStylesheets()) && !dialog.getDialogPane().getStylesheets().contains(cssPath.toExternalForm()))
                dialog.getDialogPane().getStylesheets().add(cssPath.toExternalForm());

            ValutazioneDialogController controller = loader.getController();
            controller.setDialog(dialog);
            String matricolaValutato = "";
            String nomeCompletoValutato = "";
            Valutazione.TipoValutatore tipoValutatore;
            if(richiestaOffertaDTO.getTipoInserimento().equals(Inserimento.TIPO_INSERIMENTO.RICHIESTA.name())){
                tipoValutatore = Valutazione.TipoValutatore.RICHIEDENTE;
                Optional<Offerta> offerta = prenotazioniService.getMatchingOffertaById(prenotazione.getIdOfferta());
                if(offerta.isPresent()){
                    matricolaValutato = offerta.get().getMatricolaOfferente();
                    Optional<Utente> utente = utenteService.findByIdOfferta(prenotazione.getIdOfferta());
                    if(utente.isPresent()){
                        nomeCompletoValutato = utente.get().getNome() + " " + utente.get().getCognome();
                    }
                }
            } else {
                tipoValutatore = Valutazione.TipoValutatore.OFFERENTE;
                Optional<Richiesta> richiesta = prenotazioniService.getMatchingRichiestaById(prenotazione.getIdRichiesta());
                if(richiesta.isPresent()){
                    matricolaValutato = richiesta.get().getMatricolaRichiedente();
                    Optional<Utente> utente = utenteService.findByIdRichiesta(prenotazione.getIdRichiesta());
                    if(utente.isPresent()){
                        nomeCompletoValutato = utente.get().getNome() + " " + utente.get().getCognome();
                    }
                }
            }
            if(matricolaValutato.isEmpty() || nomeCompletoValutato.isEmpty()) return;
            controller.setUtenteValutato(matricolaValutato, nomeCompletoValutato);

            Valutazione.TipoValutatore finalTipoValutatore = tipoValutatore;
            dialog.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    Valutazione valutazione = controller.getValutazione();
                    valutazione.setDataValutazione(new Timestamp(System.currentTimeMillis()));
                    valutazione.setIdPrenotazione(prenotazione.getIdPrenotazione());
                    valutazione.setTipoValutatore(finalTipoValutatore);
                    valutazioneRepository.save(valutazione);
                    completaAttivita(prenotazione);
                }
            });
        } catch (IOException e) {
            log.severe("Errore durante l'apertura del dialog di valutazione: " + e.getMessage());
        }
    }

    private void completaAttivita(PrenotazioneDTO prenotazione) {
        prenotazione.setStato(Prenotazione.StatoPrenotazione.COMPLETATO);
        prenotazioniService.aggiornaPrenotazione(prenotazione);
    }

    @FXML private void primaPagina() {
        if (paginaAttuale != 0) {
            paginaAttuale = 0;
            caricaDati();
        }
    }

    @FXML private void paginaPrecedente() {
        if (paginaAttuale > 0) {
            paginaAttuale--;
            caricaDati();
        }
    }

    @FXML private void paginaSuccessiva() {
        if (paginaAttuale < totalePagine - 1) {
            paginaAttuale++;
            caricaDati();
        }
    }

    @FXML private void ultimaPagina() {
        if (paginaAttuale != totalePagine - 1) {
            paginaAttuale = totalePagine - 1;
            caricaDati();
        }
    }

    private void aggiornaPaginazione(long totaleElementi) {
        paginaCorrente.setText("Pagina " + (paginaAttuale + 1) + " di " + totalePagine);
        totaleRecord.setText("Totale: " + totaleElementi + " record");
    }
}
