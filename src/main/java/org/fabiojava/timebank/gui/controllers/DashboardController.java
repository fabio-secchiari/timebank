package org.fabiojava.timebank.gui.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import lombok.extern.java.Log;
import org.fabiojava.timebank.domain.dto.RichiestaOffertaDTO;
import org.fabiojava.timebank.domain.model.Inserimento;
import org.fabiojava.timebank.domain.model.Offerta;
import org.fabiojava.timebank.domain.model.Richiesta;
import org.fabiojava.timebank.domain.ports.repositories.*;
import org.fabiojava.timebank.domain.services.InserimentiService;
import org.fabiojava.timebank.gui.services.SceneManager;
import org.fabiojava.timebank.gui.services.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.sql.Date;
import java.util.List;

import static org.fabiojava.timebank.gui.services.SceneManager.SceneType.DASHBOARD;

@Log
@Controller
public class DashboardController {
    private final SceneManager sceneManager;
    private final OffertaRepository offertaRepository;
    private final RichiestaRepository richiestaRepository;
    private final ValutazioneRepository valutazioneRepository;
    private final PrenotazioneRepository prenotazioneRepository;
    private final AttivitaRepository attivitaRepository;
    private final SessionManager sessionManager;
    private final InserimentiService inserimentiService;

    @FXML    private ListView<?> todoList;
    @FXML    private Label creditLabel;
    @FXML    private ProgressIndicator creditBar;

    @FXML    private TableView<?> ratingsTable;
    @FXML    private TableColumn<?, ?> ratingTypeColumn;
    @FXML    private TableColumn<?, ?> ratingValueColumn;
    @FXML    private TableColumn<?, ?> ratingCommentColumn;
    @FXML    private TableColumn<?, ?> ratingDateColumn;

    @FXML    private TableView<RichiestaOffertaDTO> hotRecordsTable;
    @FXML    private TableColumn<RichiestaOffertaDTO, String> hotTipoColonna;
    @FXML    private TableColumn<RichiestaOffertaDTO, String> hotAttivitaColonna;
    @FXML    private TableColumn<RichiestaOffertaDTO, Date> hotDataInizioColonna;
    @FXML    private TableColumn<RichiestaOffertaDTO, Date> hotDataFineColonna;
    @FXML    private TableColumn<RichiestaOffertaDTO, Void> hotAzioneColonna;

    @FXML    private ListView<Inserimento> userOffersList;
    @FXML    private ListView<Inserimento> userRequestsList;

    @FXML    private Button allRequestButton;
    @FXML    private Button allOfferButton;

    @Autowired
    public DashboardController(SceneManager sceneManager, OffertaRepository offertaRepository,
                               RichiestaRepository richiestaRepository, ValutazioneRepository valutazioneRepository,
                               PrenotazioneRepository prenotazioneRepository, SessionManager sessionManager,
                               AttivitaRepository attivitaRepository, InserimentiService inserimentiService) {
        this.sceneManager = sceneManager;
        this.offertaRepository = offertaRepository;
        this.richiestaRepository = richiestaRepository;
        this.valutazioneRepository = valutazioneRepository;
        this.prenotazioneRepository = prenotazioneRepository;
        this.sessionManager = sessionManager;
        this.attivitaRepository = attivitaRepository;
        this.inserimentiService = inserimentiService;
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
            creditLabel.setText(String.format("(%d)", sessionManager.getCurrentUser().getOreTotali()));
            initRichieste();
            initOfferte();
            initHotInsertion();
            initValutazioni();
        }
    }

    private void configuraTabellaColonne() {
        hotTipoColonna.setCellValueFactory(new PropertyValueFactory<>("tipoInserimento"));
        hotAttivitaColonna.setCellValueFactory(new PropertyValueFactory<>("nomeAttivita"));
        hotDataInizioColonna.setCellValueFactory(new PropertyValueFactory<>("dataInizio"));
        hotDataFineColonna.setCellValueFactory(new PropertyValueFactory<>("dataFine"));
        hotAzioneColonna.setCellFactory(col -> new AllInsertionController.AzioneTableCell("Dettagli", sceneManager, sessionManager, DASHBOARD));
        userRequestsList.setCellFactory(lv -> new AzioneListViewCell());
        userOffersList.setCellFactory(lv -> new AzioneListViewCell());
    }

    private void initValutazioni() {

    }

    private void initHotInsertion() {
        List<RichiestaOffertaDTO> inserimentiRecenti = inserimentiService.trovaRichiesteOfferteRecenti(sessionManager.getCurrentUser().getMatricola());
        hotRecordsTable.setItems(FXCollections.observableList(inserimentiRecenti));
    }

    @FXML
    private void viewHotInsertion(){
        sceneManager.switchScene(SceneManager.SceneType.HOT_INSERTION_LIST, "TimeBank - Inserimenti recenti", false, false);
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
                    String text = inserimento.getDataInserimento().toLocalDateTime()
                            .format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")) +
                            " - " + attivita.getNomeAttivita();
                    if (inserimento instanceof Richiesta richiesta) {
                        text += " [" + richiesta.getStato() + "]";
                    } else if (inserimento instanceof Offerta offerta) {
                        text += " [" + offerta.getStato() + "]";
                    }
                    Label label = new Label(text);
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

    @FXML
    public void handleLogout() {
        log.info("Logout");
        sessionManager.clearSession();
        sceneManager.navigateLoginPage();
    }

    @FXML private void nuovaRichiesta(){
        if (sessionManager.getCurrentUser().getOreTotali() < 3) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Richiesta non concessa");
            alert.setHeaderText(null);
            alert.setContentText("Non hai abbastanza crediti per effettuare una richiesta");
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
        sceneManager.switchScene(SceneManager.SceneType.INSERTION, "TimeBank - Inserimento", false, false);
    }

    public void handleExit() {
        System.exit(0);
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Errore");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML private void viewOwnInsertion(ActionEvent event){
        Button source = (Button) event.getSource();
        if(source == allOfferButton) {
            sessionManager.setDataTransferObject(Inserimento.TIPO_INSERIMENTO.OFFERTA);
            sceneManager.switchScene(SceneManager.SceneType.OWN_INSERTION_LIST, "TimeBank - Le mie offerte", false, false);
        } else {
            sessionManager.setDataTransferObject(Inserimento.TIPO_INSERIMENTO.RICHIESTA);
            sceneManager.switchScene(SceneManager.SceneType.OWN_INSERTION_LIST, "TimeBank - Le mie richieste", false, false);
        }
    }
}