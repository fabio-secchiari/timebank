package org.fabiojava.timebank.gui.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import lombok.extern.java.Log;
import org.fabiojava.timebank.domain.model.Offerta;
import org.fabiojava.timebank.domain.model.Richiesta;
import org.fabiojava.timebank.domain.ports.repositories.*;
import org.fabiojava.timebank.gui.services.SceneManager;
import org.fabiojava.timebank.gui.services.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

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

    @FXML
    public ListView<?> todoList;

    @FXML
    private Label creditLabel;
    @FXML
    private ProgressIndicator creditBar;

    @FXML
    private TableView<?> ratingsTable;
    @FXML
    private TableColumn<?, ?> ratingTypeColumn;
    @FXML
    private TableColumn<?, ?> ratingValueColumn;
    @FXML
    private TableColumn<?, ?> ratingCommentColumn;
    @FXML
    private TableColumn<?, ?> ratingDateColumn;

    @FXML
    private TableColumn<?, ?> recordDateColumn;
    @FXML
    private TableColumn<?, ?> recordUserColumn;
    @FXML
    private TableColumn<?, ?> recordTitleColumn;
    @FXML
    private TableColumn<?, ?> recordTypeColumn;

    @FXML
    private TableView<?> latestRecordsTable;
    @FXML
    private Button newOfferButton;
    @FXML
    private ListView<String> userOffersList;
    @FXML
    private Button newRequestButton;
    @FXML
    private ListView<String> userRequestsList;

    @Autowired
    public DashboardController(SceneManager sceneManager, OffertaRepository offertaRepository,
                               RichiestaRepository richiestaRepository, ValutazioneRepository valutazioneRepository,
                               PrenotazioneRepository prenotazioneRepository, SessionManager sessionManager,
                               AttivitaRepository attivitaRepository) {
        this.sceneManager = sceneManager;
        this.offertaRepository = offertaRepository;
        this.richiestaRepository = richiestaRepository;
        this.valutazioneRepository = valutazioneRepository;
        this.prenotazioneRepository = prenotazioneRepository;
        this.sessionManager = sessionManager;
        this.attivitaRepository = attivitaRepository;
    }

    public void initialize() {
        log.info("DashboardController initialized");
        if(sessionManager.getCurrentUser() != null){
            List<Richiesta> arrRichiesta = richiestaRepository.findByUtente(
                    sessionManager.getCurrentUser().getUsername());
            ObservableList<String> userRequestsList = FXCollections.observableArrayList();
            for(Richiesta richiesta : arrRichiesta){
                attivitaRepository.findById(richiesta.getIdAttivita()).ifPresent(attivita -> {
                    userRequestsList.add(richiesta.getDataInserimento().toString() + " - " +
                                        attivita.getNome());
                });
            }
            this.userRequestsList.setItems(userRequestsList);
            List<Offerta> arrOfferta = offertaRepository.findByUtente(
                    sessionManager.getCurrentUser().getUsername());
            ObservableList<String> userOffersList = FXCollections.observableArrayList();
            for(Offerta offerta : arrOfferta){
                attivitaRepository.findById(offerta.getIdAttivita()).ifPresent(attivita -> {
                    userOffersList.add(offerta.getDataInserimento().toString() + " - " +
                                        attivita.getNome());
                });
            }
            this.userOffersList.setItems(userOffersList);
        }
    }

    @FXML
    public void handleLogout() {
        log.info("Logout");
        sessionManager.setCurrentUser(Optional.empty());
        sceneManager.switchScene(SceneManager.SceneType.LOGIN, "TimeBank - Login");
    }

    @FXML
    private void nuovoInserimento() {
        log.info("Nuova richiesta");
        sceneManager.switchScene(SceneManager.SceneType.INSERTION, "TimeBank - Inserimento");
    }

    public void handleExit() {
        System.exit(0);
    }
}