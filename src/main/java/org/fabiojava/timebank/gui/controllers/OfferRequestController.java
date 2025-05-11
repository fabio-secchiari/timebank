package org.fabiojava.timebank.gui.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import lombok.Getter;
import lombok.extern.java.Log;
import org.fabiojava.timebank.domain.dto.CategoriaDTO;
import org.fabiojava.timebank.domain.model.Attivita;
import org.fabiojava.timebank.domain.model.Inserimento;
import org.fabiojava.timebank.domain.model.Offerta;
import org.fabiojava.timebank.domain.model.Richiesta;
import org.fabiojava.timebank.domain.ports.repositories.AttivitaRepository;
import org.fabiojava.timebank.domain.ports.repositories.OffertaRepository;
import org.fabiojava.timebank.domain.ports.repositories.RichiestaRepository;
import org.fabiojava.timebank.domain.services.UtenteService;
import org.fabiojava.timebank.gui.services.SceneManager;
import org.fabiojava.timebank.gui.services.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Log
@Controller
public class OfferRequestController {
    private final SceneManager sceneManager;
    private final SessionManager sessionManager;
    private final RichiestaRepository richiestaRepository;
    private final OffertaRepository offertaRepository;
    private final AttivitaRepository attivitaRepository;
    private final UtenteService utenteService;

    @FXML   private RadioButton offerRadio;
    @FXML   private RadioButton requestRadio;
    @FXML   private ToggleGroup insertType;
    @FXML   private Button submitButton;

    @FXML   private TextField hoursField;
    @FXML   private TextArea noteField;
    @FXML   private Button newAttivitaButton;

    @FXML   private ComboBox<AttivitaItem> attivitaField;
    @FXML   private DatePicker endDateField;
    @FXML   private DatePicker startDateField;
    @FXML   private Label errorMessage;

    @Getter
    private static class AttivitaItem {
        private String nome;
        private boolean isCategoria;
        private String categoria;

        public AttivitaItem(String nome, boolean isCategoria, String categoria) {
            this.nome = nome;
            this.isCategoria = isCategoria;
            this.categoria = categoria;
        }
    }

    @Autowired
    public OfferRequestController(SceneManager sceneManager, SessionManager sessionManager, RichiestaRepository richiestaRepository, OffertaRepository offertaRepository, AttivitaRepository attivitaRepository, UtenteService utenteService) {
        this.sceneManager = sceneManager;
        this.sessionManager = sessionManager;
        this.richiestaRepository = richiestaRepository;
        this.offertaRepository = offertaRepository;
        this.attivitaRepository = attivitaRepository;
        this.utenteService = utenteService;
    }

    public void initialize() {
        log.info("OfferRequestController inizializzato");
        if(sessionManager.getDataTransferObject() instanceof AttivitaItem attivitaItem){
            attivitaField.getSelectionModel().select(attivitaItem);
        }else if(sessionManager.getDataTransferObject() instanceof Inserimento.TIPO_INSERIMENTO){
            if(sessionManager.getDataTransferObject() == Inserimento.TIPO_INSERIMENTO.RICHIESTA){
                insertType.selectToggle(requestRadio);
            }else{
                insertType.selectToggle(offerRadio);
            }
        }
        sessionManager.setDataTransferObject(null);

        attivitaField.setButtonCell(createComboBoxCell());
        attivitaField.setCellFactory(listView -> createComboBoxCell());
        List<String> categorie = attivitaRepository.findAllCategorie().stream().map(CategoriaDTO::getCategoria).toList();
        List<Attivita> attivita = attivitaRepository.findAll();
        for (String categoria : categorie) {
            attivitaField.getItems().add(new AttivitaItem(categoria, true, categoria));
            attivita.stream()
                .filter(a -> a.getCategoria().equals(categoria))
                .forEach(a -> attivitaField.getItems().add(
                        new AttivitaItem("   - " + a.getNomeAttivita(), false, categoria)
                ));
        }

        attivitaField.setOnShowing(e -> {
            AttivitaItem selected = attivitaField.getSelectionModel().getSelectedItem();
            if (selected != null && !selected.isCategoria()) {
                Platform.runLater(() -> attivitaField.getSelectionModel().select(selected));
            }
        });

    }

    private ListCell<AttivitaItem> createComboBoxCell() {
        return new ListCell<>() {
            @Override
            protected void updateItem(AttivitaItem item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null); setDisable(false);
                } else {
                    setText(item.getNome());
                    if (item.isCategoria()) {
                        setStyle("-fx-text-fill: gray; -fx-font-weight: bold;");
                        setDisable(true);  // non selezionabile
                    } else {
                        setStyle(""); setDisable(false);
                    }
                }
            }
        };
    }

    private String getSelectedAttivita() {
        AttivitaItem selected = attivitaField.getValue();
        if (selected != null && !selected.isCategoria()) {
            return selected.getNome().substring(4);
        }
        return null;
    }

    @FXML
    public void saveInsertion(){
        if(insertType.getSelectedToggle() == requestRadio) {
            if (sessionManager.getCurrentUser().getOreTotali() < 3) {
                showError("Non hai abbastanza crediti per effettuare questa richiesta");
                log.info("Richiesta non concessa: non hai abbastanza crediti");
                return;
            }
        }
        Optional<Attivita> attivita = attivitaRepository.findByNome(getSelectedAttivita());
        if(attivita.isEmpty()) {showError("Attività non trovata"); return; }
        if(insertType.getSelectedToggle() == offerRadio){
            if(areDataInvalid()) { showError("Uno o più campi errati"); return; }
            offertaRepository.save(
                    new Offerta(
                        null,
                        sessionManager.getCurrentUser().getMatricola(),
                        attivita.get().getIdAttivita(),
                        Date.valueOf(startDateField.getValue()),
                        Date.valueOf(endDateField.getValue()),
                        Integer.parseInt(hoursField.getText()),
                        Offerta.StatoOfferta.DISPONIBILE,
                        noteField.getText(),
                        Timestamp.valueOf(LocalDateTime.now())
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
                        attivita.get().getIdAttivita(),
                        Date.valueOf(startDateField.getValue()),
                        Date.valueOf(endDateField.getValue()),
                        Integer.parseInt(hoursField.getText()),
                        Richiesta.StatoRichiesta.APERTA,
                        noteField.getText(),
                        Timestamp.valueOf(LocalDateTime.now()),
                        priority
                    )
            );
        }
        log.info("Insertion completed");
        if(insertType.getSelectedToggle() == offerRadio) {
            utenteService.aggiungiOreUtente(sessionManager.getCurrentUser().getMatricola(), 3);
        }else{
            utenteService.sottraiOreUtente(sessionManager.getCurrentUser().getMatricola(), 1);
        }
        sceneManager.switchScene(SceneManager.SceneType.DASHBOARD, "TimeBank - Dashboard", true);
    }

    private boolean areDataInvalid() {
        // controllo sulla compilazione
        if(attivitaField.getValue() == null || Objects.requireNonNull(getSelectedAttivita()).isEmpty()) return true;
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

    public void addAttivita() {
        sceneManager.switchScene(SceneManager.SceneType.ATTIVITA, "TimeBank - Nuova attività", false);
    }

    @FXML private void onCancelHandle() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Conferma annullamento inserimento");
        alert.setGraphic(null);
        alert.setContentText("Sei sicuro di voler annullare?");
        alert.setHeaderText(null);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK){
            sceneManager.switchScene(SceneManager.SceneType.DASHBOARD, "TimeBank - Dashboard", true);
        }
    }

    @FXML private void onLogOutHandle() {
        log.info("Logout");
        sessionManager.clearSession();
        sceneManager.switchScene(SceneManager.SceneType.LOGIN, "TimeBank - Login", false);
    }

    @FXML private void onExitHandle() {
        System.exit(0);
    }
}
