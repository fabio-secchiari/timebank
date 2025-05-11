package org.fabiojava.timebank.gui.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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

import java.util.List;

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

    @FXML    private TableColumn<?, ?> recordDateColumn;
    @FXML    private TableColumn<?, ?> recordUserColumn;
    @FXML    private TableColumn<?, ?> recordTitleColumn;
    @FXML    private TableColumn<?, ?> recordTypeColumn;

    @FXML    private TableView<?> latestRecordsTable;
    @FXML    private Button newOfferButton;
    @FXML    private ListView<String> userOffersList;
    @FXML    private Button newRequestButton;
    @FXML    private ListView<String> userRequestsList;

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
        if(sessionManager.getCurrentUser() != null){
            creditBar.setProgress(map(0, 30, 0, 1, sessionManager.getCurrentUser().getOreTotali()));
            creditLabel.setText(String.format("(%d)", sessionManager.getCurrentUser().getOreTotali()));
            initRichieste();
            initOfferte();
            initHotInsertion();
            initValutazioni();
        }
    }

    private void initValutazioni() {

    }

    private void initHotInsertion() {
        List<RichiestaOffertaDTO> inserimentiRecenti = inserimentiService.trovaRichiesteOfferteRecenti(sessionManager.getCurrentUser().getMatricola());
        inserimentiRecenti.forEach(inserimento -> log.info("HOT" + inserimento.toString()));
    }

    private void initRichieste() {
        List<Richiesta> arrRichiesta = richiestaRepository.findByUtente(
                sessionManager.getCurrentUser().getMatricola());
        log.info(arrRichiesta.toString());
        ObservableList<String> userRequestsList = FXCollections.observableArrayList();
        for(Richiesta richiesta : arrRichiesta){
            attivitaRepository.findById(richiesta.getIdAttivita()).ifPresent(attivita ->
                    userRequestsList.add(richiesta.getDataInserimento().toLocalDateTime().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " - " +
                            attivita.getNomeAttivita()));
        }
        this.userRequestsList.setItems(userRequestsList);
    }

    private void initOfferte() {
        List<Offerta> arrOfferta = offertaRepository.findByUtente(
                sessionManager.getCurrentUser().getMatricola());
        log.info(arrOfferta.toString());
        ObservableList<String> userOffersList = FXCollections.observableArrayList();
        for(Offerta offerta : arrOfferta){
            attivitaRepository.findById(offerta.getIdAttivita()).ifPresent(attivita ->
                    userOffersList.add(offerta.getDataInserimento().toLocalDateTime().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " - " +
                            attivita.getNomeAttivita()));
        }
        this.userOffersList.setItems(userOffersList);
    }

    @FXML
    public void handleLogout() {
        log.info("Logout");
        sessionManager.clearSession();
        sceneManager.switchScene(SceneManager.SceneType.LOGIN, "TimeBank - Login", false);
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
        sceneManager.switchScene(SceneManager.SceneType.INSERTION, "TimeBank - Inserimento", false);
    }

    public void handleExit() {
        System.exit(0);
    }
}