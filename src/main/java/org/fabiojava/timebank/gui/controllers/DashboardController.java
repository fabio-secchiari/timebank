package org.fabiojava.timebank.gui.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import lombok.extern.java.Log;
import org.fabiojava.timebank.domain.dto.PrenotazioneDTO;
import org.fabiojava.timebank.domain.dto.RichiestaOffertaDTO;
import org.fabiojava.timebank.domain.model.*;
import org.fabiojava.timebank.domain.ports.repositories.*;
import org.fabiojava.timebank.domain.services.InserimentiService;
import org.fabiojava.timebank.domain.services.PrenotazioniService;
import org.fabiojava.timebank.domain.services.UtenteService;
import org.fabiojava.timebank.gui.components.RatingControl;
import org.fabiojava.timebank.gui.controllers.dialogs.UtenteDetailsDialogController;
import org.fabiojava.timebank.gui.utils.SceneManager;
import org.fabiojava.timebank.gui.utils.SessionManager;
import org.fabiojava.timebank.gui.utils.SpringFXMLLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Log
@Controller
public class DashboardController {
    private final SceneManager sceneManager;
    private final OffertaRepository offertaRepository;
    private final RichiestaRepository richiestaRepository;
    private final ValutazioneRepository valutazioneRepository;
    private final AttivitaRepository attivitaRepository;
    private final SessionManager sessionManager;
    private final InserimentiService inserimentiService;
    private final PrenotazioniService prenotazioniService;
    private final SpringFXMLLoader fxmlLoader;
    private final UtenteService utenteService;

    @FXML    private TableView<PrenotazioneDTO> toDoListTable;
    @FXML    private TableColumn<PrenotazioneDTO, String> toDoListDateByColonna;
    @FXML    private TableColumn<PrenotazioneDTO, String> toDoListAttivitaColonna;
    @FXML    private TableColumn<PrenotazioneDTO, Void> toDoListAzioniColonna;


    @FXML    private Label creditLabel;
    @FXML    private ProgressIndicator creditBar;

    @FXML    private TableView<Valutazione> ratingsTable;
    @FXML    private TableColumn<Valutazione, String> ratingTypeColumn;
    @FXML    private TableColumn<Valutazione, Integer> ratingValueColumn;
    @FXML    private TableColumn<Valutazione, String> ratingCommentColumn;
    @FXML    private TableColumn<Valutazione, String> ratingDateColumn;

    @FXML    private TableView<RichiestaOffertaDTO> hotRecordsTable;
    @FXML    private TableColumn<RichiestaOffertaDTO, String> hotTipoColonna;
    @FXML    private TableColumn<RichiestaOffertaDTO, String> hotAttivitaColonna;
    @FXML    private TableColumn<RichiestaOffertaDTO, Date> hotDataInizioColonna;
    @FXML    private TableColumn<RichiestaOffertaDTO, Date> hotDataFineColonna;
    @FXML    private TableColumn<RichiestaOffertaDTO, Void> hotAzioneColonna;

    @FXML    private ListView<Inserimento> userOffersList;
    @FXML    private ListView<Inserimento> userRequestsList;

    //@FXML private Button allRequestButton;
    @FXML    private Button allOfferButton;

    @Autowired
    public DashboardController(SceneManager sceneManager, OffertaRepository offertaRepository,
                               RichiestaRepository richiestaRepository, ValutazioneRepository valutazioneRepository,
                               SessionManager sessionManager, AttivitaRepository attivitaRepository,
                               InserimentiService inserimentiService, PrenotazioniService prenotazioniService, SpringFXMLLoader fxmlLoader, UtenteService utenteService) {
        this.sceneManager = sceneManager;
        this.offertaRepository = offertaRepository;
        this.richiestaRepository = richiestaRepository;
        this.valutazioneRepository = valutazioneRepository;
        this.sessionManager = sessionManager;
        this.attivitaRepository = attivitaRepository;
        this.inserimentiService = inserimentiService;
        this.prenotazioniService = prenotazioniService;
        this.fxmlLoader = fxmlLoader;
        this.utenteService = utenteService;
    }

    public double map(int startMin, int startMax, int endMin, int endMax, int value){
        if(value < startMin) return endMin;
        if(value > startMax) return endMax;
        return (double) (value - startMin) / (startMax - startMin) * (endMax - endMin) + endMin;
    }

    public void initialize() {
        log.info("DashboardController initialized");
        configuraTabellaColonne();
        if(sessionManager.getCurrentUser() != null){
            creditBar.setProgress(map(0, 30, 0, 1, sessionManager.getCurrentUser().getOreTotali()));
            creditLabel.setText("(" + sessionManager.getCurrentUser().getOreTotali() + ")");
            initRichieste();
            initOfferte();
            initHotInsertion();
            initValutazioni();
            initToDoList();
        }
    }

    public static void configuraRatingHistory(TableColumn<Valutazione, String> ratingTypeColumn,
                                              TableColumn<Valutazione, Integer> ratingValueColumn,
                                              TableColumn<Valutazione, String> ratingCommentColumn,
                                              TableColumn<Valutazione, String> ratingDateColumn){
        ratingTypeColumn.setCellValueFactory(cellData -> {
            Valutazione.TipoValutatore tipo = cellData.getValue().getTipoValutatore();
            String visualizzazione = switch (tipo) {
                case RICHIEDENTE -> "Offerente";
                case OFFERENTE -> "Richiedente";
            };
            return new javafx.beans.property.SimpleStringProperty(visualizzazione);
        });
        ratingValueColumn.setCellValueFactory(new PropertyValueFactory<>("punteggio"));
        ratingValueColumn.setCellFactory(column -> new TableCell<>() {
            private final RatingControl ratingControl = new RatingControl();

            {
                ratingControl.setMouseTransparent(true);
                ratingControl.setFocusTraversable(false);
                ratingControl.setMaxHeight(20);
            }

            @Override
            protected void updateItem(Integer punteggio, boolean empty) {
                super.updateItem(punteggio, empty);
                if (empty || punteggio == null) {
                    setGraphic(null);
                } else {
                    ratingControl.setRating(punteggio);
                    setGraphic(ratingControl);
                }
            }
        });
        ratingCommentColumn.setCellValueFactory(new PropertyValueFactory<>("commento"));
        ratingDateColumn.setCellValueFactory(cellData -> {
            Timestamp timestamp = cellData.getValue().getDataValutazione();
            return new javafx.beans.property.SimpleStringProperty(
                    timestamp != null ? new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(timestamp) : ""
            );
        });
    }

    private void configuraTabellaColonne() {
        hotTipoColonna.setCellValueFactory(new PropertyValueFactory<>("tipoInserimento"));
        hotAttivitaColonna.setCellValueFactory(new PropertyValueFactory<>("nomeAttivita"));
        hotDataInizioColonna.setCellValueFactory(new PropertyValueFactory<>("dataInizio"));
        hotDataFineColonna.setCellValueFactory(new PropertyValueFactory<>("dataFine"));
        hotAzioneColonna.setCellFactory(col -> new AllInsertionController.AzioneTableCell("Dettagli", sceneManager, sessionManager));

        userRequestsList.setCellFactory(lv -> new AzioneListViewCell());
        userOffersList.setCellFactory(lv -> new AzioneListViewCell());

        configuraRatingHistory(ratingTypeColumn, ratingValueColumn, ratingCommentColumn, ratingDateColumn);

        toDoListDateByColonna.setCellValueFactory(new PropertyValueFactory<>("dataEsecuzione"));
        toDoListAttivitaColonna.setCellValueFactory(new PropertyValueFactory<>("noteFeedback"));
        toDoListAzioniColonna.setCellFactory(col -> new ToDoListActionCell(sceneManager, sessionManager, prenotazioniService, attivitaRepository, utenteService));
    }

    private class ToDoListActionCell extends TableCell<PrenotazioneDTO, Void> {
        private final Button contattaButton = new Button("Contatta");
        private final Button dettagliButton = new Button("Dettagli");
        private final HBox hbox = new HBox(5, contattaButton, dettagliButton);

        public ToDoListActionCell(SceneManager sceneManager, SessionManager sessionManager,
                                  PrenotazioniService prenotazioneService, AttivitaRepository attivitaRepository,
                                  UtenteService utenteService) {
            contattaButton.setOnAction(event -> {
                if (getTableRow() != null) {
                    PrenotazioneDTO dto = getTableRow().getItem();
                    if (dto != null) {
                        utenteService.findByIdRichiesta(dto.getIdRichiesta())
                                .ifPresent(DashboardController.this::mostraDettagliUtente);
                    }
                }
            });
            dettagliButton.setOnAction(event -> {
                if (getTableRow() != null) {
                    PrenotazioneDTO dto = getTableRow().getItem();
                    if (dto != null) {
                        Optional<Richiesta> richiesta = prenotazioneService.getMatchingRichiestaById(dto.getIdRichiesta());
                        if(richiesta.isPresent()){
                            RichiestaOffertaDTO richiestaOffertaDTO = new RichiestaOffertaDTO(richiesta.get());
                            Optional<Attivita> attivita = attivitaRepository.findById(richiestaOffertaDTO.getIdAttivita());
                            if(attivita.isPresent()){
                                richiestaOffertaDTO.setNomeAttivita(attivita.get().getNomeAttivita());
                                richiestaOffertaDTO.setCategoria(attivita.get().getCategoria());
                                sessionManager.setDataTransferObject(richiestaOffertaDTO);
                                sceneManager.switchContent(SceneManager.SceneType.INSERTION_DETAILS, "TimeBank - Dettagli richiesta");
                            }
                        }
                    }
                }
            });
        }

        @Override
        protected void updateItem(Void item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setGraphic(null);
            } else {
                hbox.setAlignment(Pos.CENTER);
                setGraphic(hbox);
            }
        }
    }

    private void initValutazioni() {
        List<Valutazione> valutazioni = valutazioneRepository.findByUtente(sessionManager.getCurrentUser().getMatricola());
        ratingsTable.setItems(FXCollections.observableList(valutazioni));
    }

    private void initHotInsertion() {
        List<RichiestaOffertaDTO> inserimentiRecenti = inserimentiService.trovaRichiesteOfferteRecenti(sessionManager.getCurrentUser().getMatricola());
        hotRecordsTable.setItems(FXCollections.observableList(inserimentiRecenti));
    }

    private void initToDoList() {
        List<PrenotazioneDTO> toDoArray = prenotazioniService.getToDoList(sessionManager.getCurrentUser().getMatricola());
        toDoListTable.setItems(FXCollections.observableList(toDoArray));
    }

    @FXML
    private void viewHotInsertion(){
        sceneManager.switchContent(SceneManager.SceneType.HOT_INSERTION_LIST, "TimeBank - Inserimenti recenti");
    }

    private void mostraDettagliUtente(Utente utente) {
        try {
            URL location = getClass().getResource("/org/fabiojava/timebank/fxml/dialogs/utente-details-dialog.fxml");
            FXMLLoader loader = fxmlLoader.getLoader(location);
            DialogPane dialogPane = loader.load();

            Dialog<ButtonType> dialog = new Dialog<>();
            ((Stage) dialog.getDialogPane().getScene().getWindow()).getIcons().add(new Image(Objects.requireNonNull(Objects.requireNonNull(getClass().getResource("/org/fabiojava/timebank/images/icon.png")).toExternalForm())));
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Dettagli Utente");

            URL cssPath = getClass().getResource("/org/fabiojava/timebank/styles/dialog-style.css");
            if(cssPath != null && Objects.nonNull(dialog.getDialogPane().getStylesheets()) && !dialog.getDialogPane().getStylesheets().contains(cssPath.toExternalForm()))
                dialog.getDialogPane().getStylesheets().add(cssPath.toExternalForm());

            UtenteDetailsDialogController controller = loader.getController();
            controller.setDialog(dialog);

            List<Valutazione> valutazioni = valutazioneRepository.findByUtente(utente.getMatricola());
            double mediaValutazioni = valutazioni.stream().collect(Collectors.averagingInt(Valutazione::getPunteggio));
            controller.setData(utente, mediaValutazioni, valutazioni);

            dialog.setOnCloseRequest(event -> dialog.close());
            dialog.showAndWait();

        } catch (IOException e) {
            log.severe("Errore durante la visualizzazione dei dettagli dell'utente" + e.getMessage());
        }
    }


    public class AzioneListViewCell extends ListCell<Inserimento> {
        //private final Button prenotazioniButton = new Button("Prenotazioni");
        private final HBox hbox = new HBox(10); // 10 è lo spacing tra gli elementi

        public AzioneListViewCell() {
            /*prenotazioniButton.setOnAction(event -> {
                if(getItem() instanceof Richiesta richiesta){
                    mostraPrenotazioniRichiesta(richiesta);
                }else if(getItem() instanceof Offerta offerta){
                    mostraPrenotazioniOfferta(offerta);
                }
            });*/
        }

        @Override
        protected void updateItem(Inserimento inserimento, boolean empty) {
            super.updateItem(inserimento, empty);
            if (empty || inserimento == null) {
                setText(null);
                setGraphic(null);
            } else {
                attivitaRepository.findById(inserimento.getIdAttivita()).ifPresent(attivita -> {
                    String text = "";
                    if (inserimento instanceof Richiesta richiesta) {
                        text += " [" + richiesta.getStato() + "] ";
                    } else if (inserimento instanceof Offerta offerta) {
                        text += " [" + offerta.getStato() + "] ";
                    }
                    text += attivita.getNomeAttivita();
                    Label label = new Label(text);
                    hbox.setAlignment(Pos.CENTER);
                    hbox.getChildren().setAll(/*prenotazioniButton,*/ label);
                    setGraphic(hbox);
                });
            }
        }
    }

    private void initRichieste() {
        List<Richiesta> arrRichiesta = richiestaRepository.findByUtente(
                sessionManager.getCurrentUser().getMatricola());
        ObservableList<Inserimento> items = FXCollections.observableArrayList(arrRichiesta);
        this.userRequestsList.setItems(items);
    }

    private void initOfferte() {
        List<Offerta> arrOfferta = offertaRepository.findByUtente(
                sessionManager.getCurrentUser().getMatricola());
        ObservableList<Inserimento> items = FXCollections.observableArrayList(arrOfferta);
        this.userOffersList.setItems(items);
    }

    @FXML private void nuovaRichiesta(){
        if (sessionManager.getCurrentUser().getOreTotali() < 3) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Richiesta non concessa");
            alert.setHeaderText(null);
            alert.setContentText("Non hai abbastanza crediti per effettuare una richiesta");

            DialogPane dialogPane = alert.getDialogPane();
            ((Stage) dialogPane.getScene().getWindow()).getIcons().add(new Image(Objects.requireNonNull(Objects.requireNonNull(getClass().getResource("/org/fabiojava/timebank/images/icon.png")).toExternalForm())));
            dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/org/fabiojava/timebank/styles/dialog-style.css")).toExternalForm());

            alert.showAndWait();
            log.info("Richiesta non concessa: non hai abbastanza crediti");
            return;
        }
        nuovoInserimento(Inserimento.TIPO_INSERIMENTO.RICHIESTA);
    }

    @FXML private void nuovaOfferta(){
        nuovoInserimento(Inserimento.TIPO_INSERIMENTO.OFFERTA);
    }

    private void nuovoInserimento(Inserimento.TIPO_INSERIMENTO tipoInserimento) {
        sessionManager.setDataTransferObject(tipoInserimento);
        sceneManager.switchContent(SceneManager.SceneType.INSERTION, "TimeBank - Inserimento");
    }

    @FXML private void viewOwnInsertion(ActionEvent event){
        Button source = (Button) event.getSource();
        if(source == allOfferButton) {
            sessionManager.setDataTransferObject(Inserimento.TIPO_INSERIMENTO.OFFERTA);
            sceneManager.switchContent(SceneManager.SceneType.OWN_INSERTION_LIST, "TimeBank - Le mie offerte");
        } else {
            sessionManager.setDataTransferObject(Inserimento.TIPO_INSERIMENTO.RICHIESTA);
            sceneManager.switchContent(SceneManager.SceneType.OWN_INSERTION_LIST, "TimeBank - Le mie richieste");
        }
    }
}