package org.fabiojava.timebank.gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import lombok.extern.java.Log;
import org.fabiojava.timebank.domain.model.Attivita;
import org.fabiojava.timebank.domain.model.Offerta;
import org.fabiojava.timebank.domain.model.Richiesta;
import org.fabiojava.timebank.domain.ports.repositories.AttivitaRepository;
import org.fabiojava.timebank.domain.ports.repositories.OffertaRepository;
import org.fabiojava.timebank.domain.ports.repositories.RichiestaRepository;
import org.fabiojava.timebank.gui.services.SceneManager;
import org.fabiojava.timebank.gui.services.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.Optional;

@Log
@Controller
public class OfferRequestController {
    private final SceneManager sceneManager;
    private final SessionManager sessionManager;
    private final RichiestaRepository richiestaRepository;
    private final OffertaRepository offertaRepository;
    private final AttivitaRepository attivitaRepository;

    @FXML   private RadioButton offerRadio;
    @FXML   private RadioButton requestRadio;
    @FXML   private ToggleGroup insertType;
    @FXML   private Button submitButton;

    @FXML   private TextField hoursField;
    @FXML   private TextArea noteField;
    @FXML   private Button newAttivitaButton;

    @FXML   private ComboBox<String> attivitaField;
    @FXML   private DatePicker endDateField;
    @FXML   private DatePicker startDateField;
    @FXML   private Label errorMessage;

    @Autowired
    public OfferRequestController(SceneManager sceneManager, SessionManager sessionManager, RichiestaRepository richiestaRepository, OffertaRepository offertaRepository, AttivitaRepository attivitaRepository) {
        this.sceneManager = sceneManager;
        this.sessionManager = sessionManager;
        this.richiestaRepository = richiestaRepository;
        this.offertaRepository = offertaRepository;
        this.attivitaRepository = attivitaRepository;
    }

    public void initialize() {
        log.info("OfferRequestController inizializzato");
        attivitaRepository.findAll().forEach(attivita -> attivitaField.getItems().add(attivita.getNome()));
    }

    @FXML
    public void saveInsertion(){
        Optional<Attivita> attivita = attivitaRepository.findByNome(attivitaField.getSelectionModel().getSelectedItem());
        if(attivita.isEmpty()) {showError("Attività non trovata"); return; }
        if(insertType.getSelectedToggle() == offerRadio){
            if(areDataInvalid()) { showError("Uno o più campi errati"); return; }
            offertaRepository.save(
                    new Offerta(
                        null,
                        sessionManager.getCurrentUser().getMatricola(),
                        attivita.get().getId(),
                        startDateField.getValue(),
                        endDateField.getValue(),
                        Integer.parseInt(hoursField.getText()),
                        Offerta.StatoOfferta.DISPONIBILE,
                        noteField.getText(),
                        LocalDateTime.now()
                    )
            );
        }else{
            if(areDataInvalid()) { showError("Uno o più campi errati"); return; }
            // determino in base alla data di scadenza la priorità
            //TODO priorità richiesta
            Richiesta.PrioritaRichiesta priority = Richiesta.PrioritaRichiesta.NORMALE;
            if(endDateField.getValue().isBefore(java.time.LocalDate.now().plusDays(10))) priority = Richiesta.PrioritaRichiesta.URGENTE;
            
            richiestaRepository.save(
                    new Richiesta(
                        null,
                        sessionManager.getCurrentUser().getMatricola(),
                        attivita.get().getId(),
                        startDateField.getValue(),
                        endDateField.getValue(),
                        Integer.parseInt(hoursField.getText()),
                        Richiesta.StatoRichiesta.APERTA,
                        priority,
                        noteField.getText(),
                        LocalDateTime.now()
                    )
            );
        }
        log.info("Insertion completed");
        sceneManager.switchScene(SceneManager.SceneType.DASHBOARD, "TimeBank - Dashboard");
    }

    private boolean areDataInvalid() {
        // controllo sulla compilazione
        if(attivitaField.getValue() == null || attivitaField.getValue().isEmpty()) return true;
        if(endDateField.getValue() == null) return true;
        if(startDateField.getValue() == null) return true;
        if(hoursField.getText().isEmpty()) return true;
        if(noteField.getText().isEmpty()) return true;
        // controllo sulla validità
        if(startDateField.getValue().isAfter(endDateField.getValue())) return true;
        if(startDateField.getValue().isBefore(java.time.LocalDate.now())) return true;
        if(endDateField.getValue().isBefore(java.time.LocalDate.now())) return true;
        if(!hoursField.getText().matches("[0-9]+")) return true;
        
        return false;
    }

    private void showError(String content) {
        errorMessage.setVisible(true);
        errorMessage.setText(content);
    }

    public void addAttivita(ActionEvent actionEvent) {
        sceneManager.switchScene(SceneManager.SceneType.ATTIVITA, "TimeBank - Nuova attività");
    }
}
