package org.fabiojava.timebank.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import lombok.extern.java.Log;
import org.fabiojava.timebank.domain.dto.RichiestaOffertaDTO;
import org.fabiojava.timebank.domain.ports.repositories.OffertaRepository;
import org.fabiojava.timebank.domain.ports.repositories.RichiestaRepository;
import org.fabiojava.timebank.gui.services.SceneManager;
import org.fabiojava.timebank.gui.services.SessionManager;
import org.springframework.stereotype.Controller;

import java.util.Optional;

@Log
@Controller
public class DettaglioInserimentoController {
    private final SessionManager sessionManager;
    private final SceneManager sceneManager;
    private RichiestaOffertaDTO richiestaOffertaDTO;
    private final RichiestaRepository richiestaRepository;
    private final OffertaRepository offertaRepository;

    @FXML    private Label tipoInserimento;
    @FXML    private DatePicker dataInizio;
    @FXML    private DatePicker dataFine;
    @FXML    private TextField oreDisponibili;
    @FXML    private Label nomeAttivita;
    @FXML    private Label stato;
    @FXML    private TextArea note;
    @FXML    private Label utente;
    @FXML    private Label dataInserimento;
    @FXML    private Label errorMessage;
    @FXML    private Button btnModifica;
    @FXML    private Button btnElimina;

    public void initialize(){
        if(sessionManager.getDataTransferObject() != null && sessionManager.getDataTransferObject() instanceof RichiestaOffertaDTO dto) {
            this.richiestaOffertaDTO = dto;
            sessionManager.setDataTransferObject(null);
        }
        populateFields();
    }

    private void populateFields() {
        tipoInserimento.setText(richiestaOffertaDTO.getTipoInserimento());
        dataInizio.setValue(richiestaOffertaDTO.getDataInizio().toLocalDate());
        dataFine.setValue(richiestaOffertaDTO.getDataFine().toLocalDate());
        oreDisponibili.setText(String.valueOf(richiestaOffertaDTO.getOreDisponibili()));
        nomeAttivita.setText(richiestaOffertaDTO.getNomeAttivita());
        stato.setText(richiestaOffertaDTO.getStato());
        note.setText(richiestaOffertaDTO.getNote());
        utente.setText(sessionManager.getCurrentUser().getEmail());
        dataInserimento.setText(richiestaOffertaDTO.getDataInserimento().toString());
    }

    public DettaglioInserimentoController(SessionManager sessionManager, SceneManager sceneManager, RichiestaRepository richiestaRepository, OffertaRepository offertaRepository) {
        this.sessionManager = sessionManager;
        this.sceneManager = sceneManager;
        this.richiestaRepository = richiestaRepository;
        this.offertaRepository = offertaRepository;
    }

    @FXML private void handleIndietro() {
        sceneManager.switchScene(SceneManager.SceneType.DASHBOARD, "TimeBank - Dashboard", true);
    }

    @FXML private void handleLogOut() {
        sessionManager.setCurrentUser(java.util.Optional.empty());
        sceneManager.switchScene(SceneManager.SceneType.LOGIN, "TimeBank - Login", false);
    }

    @FXML private void handleChiudi() {
        AllInsertionController.handleChiudi();
    }

    @FXML private void visualizzaPrenotazioni() {
        // TODO
    }

    @FXML private void modificaInserimento() {
        if(richiestaOffertaDTO.getTipoInserimento().equals("RICHIESTA")) {
            richiestaRepository.update(richiestaOffertaDTO.toRichiesta());
        }else{
            offertaRepository.update(richiestaOffertaDTO.toOfferta());
        }
    }

    @FXML private void eliminaInserimento() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Conferma eliminazione inserimento");
        alert.setHeaderText("Sei sicuro di voler eliminare l'inserimento?");
        alert.setContentText(null);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK){
            log.info("Eliminazione inserimento effettuata con successo");
            if(richiestaOffertaDTO.getTipoInserimento().equals("RICHIESTA")) {
                richiestaRepository.delete(richiestaOffertaDTO.getId());
            }else{
                offertaRepository.delete(richiestaOffertaDTO.getId());
            }
            sceneManager.switchScene(SceneManager.SceneType.DASHBOARD, "TimeBank - Dashboard", true);
        }
    }
}
