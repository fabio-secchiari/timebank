package org.fabiojava.timebank.gui.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import org.fabiojava.timebank.domain.dto.PrenotazioneDTO;
import org.fabiojava.timebank.domain.dto.RichiestaOffertaDTO;
import org.fabiojava.timebank.domain.model.Inserimento;
import org.fabiojava.timebank.domain.model.Prenotazione;
import org.fabiojava.timebank.domain.ports.repositories.PrenotazioneRepository;
import org.fabiojava.timebank.domain.services.PrenotazioniService;
import org.fabiojava.timebank.gui.services.SceneManager;
import org.fabiojava.timebank.gui.services.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;

import java.sql.Date;

@Controller
public class PrenotazioneController {
    private final SceneManager sceneManager;
    private final SessionManager sessionManager;
    private final PrenotazioneRepository prenotazioneRepository;
    private final PrenotazioniService prenotazioniService;

    private RichiestaOffertaDTO richiestaOffertaDTO;
    private int paginaAttuale = 0;
    private int totalePagine = 0;

    @FXML    private Button indietroButton;
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
        configuraTabellaColonne();
        caricaDati();
    }

    @Autowired
    public PrenotazioneController(SceneManager sceneManager, SessionManager sessionManager, PrenotazioneRepository prenotazioneRepository, PrenotazioniService prenotazioniService) {
        this.sceneManager = sceneManager;
        this.sessionManager = sessionManager;
        this.prenotazioneRepository = prenotazioneRepository;
        this.prenotazioniService = prenotazioniService;
    }

    private void configuraTabellaColonne() {
        dataPrenotazioneColonna.setCellValueFactory(new PropertyValueFactory<>("dataMatching"));
        utenteColonna.setCellValueFactory(new PropertyValueFactory<>("matricola"));
        noteColonna.setCellValueFactory(new PropertyValueFactory<>("note"));
        oreColonna.setCellValueFactory(new PropertyValueFactory<>("oreConcordate"));

        azioniColonna.setCellFactory(col -> new TableCell<>() {
            private final Button btnAccetta = new Button("✔");
            private final Button btnRifiuta = new Button("✘");
            private final HBox hbox = new HBox(5, btnAccetta, btnRifiuta);

            {
                btnAccetta.setOnAction(event -> {
                    PrenotazioneDTO dto = getTableRow().getItem();
                    if (dto != null) {
                        Dialog<ButtonType> dialog = new Dialog<>();
                        dialog.setTitle("Conferma prenotazione");
                        dialog.setHeaderText(null);

                        DialogPane dialogPane = dialog.getDialogPane();
                        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

                        DatePicker datePicker = new DatePicker();
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
                                dto.setDataEsecuzione(Date.valueOf(datePicker.getValue()));
                                dto.setNoteFeedback(noteArea.getText());
                                accettaPrenotazione(dto);
                            }
                        });
                    }
                });
                btnRifiuta.setOnAction(event -> {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Rifiuta prenotazione");
                    alert.setHeaderText(null); alert.setGraphic(null);
                    alert.setContentText("Sei sicuro di voler rifiutare questa prenotazione?");
                    alert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) rifiutaPrenotazione(getTableRow().getItem());
                        else if (response == ButtonType.CANCEL) alert.close();
                        else alert.close();
                    });
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : hbox);
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
                .dimensionePagina(10)
                .build();
        Page<PrenotazioneDTO> record = prenotazioniService.filterByInserimento(richiestaOffertaDTO.getId(), criteria,
                Inserimento.TIPO_INSERIMENTO.valueOf(richiestaOffertaDTO.getTipoInserimento()));
        tabellaPrenotazioni.setItems(FXCollections.observableList(record.getContent()));
        totalePagine = record.getTotalPages();
        aggiornaPaginazione(record.getTotalElements());
    }

    @FXML private void handleIndietro() {
        sessionManager.setDataTransferObject(Inserimento.TIPO_INSERIMENTO.valueOf(richiestaOffertaDTO.getTipoInserimento()));
        sceneManager.navigateLastScene();
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
        paginaCorrente.setText(String.format("Pagina %d di %d", paginaAttuale + 1, totalePagine));
        totaleRecord.setText(String.format("Totale: %d record", totaleElementi));
    }
}
