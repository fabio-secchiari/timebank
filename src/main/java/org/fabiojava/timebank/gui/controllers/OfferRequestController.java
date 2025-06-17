package org.fabiojava.timebank.gui.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;
import org.fabiojava.timebank.domain.dto.CategoriaDTO;
import org.fabiojava.timebank.domain.dto.RichiestaOffertaDTO;
import org.fabiojava.timebank.domain.model.*;
import org.fabiojava.timebank.domain.ports.repositories.AttivitaRepository;
import org.fabiojava.timebank.domain.ports.repositories.OffertaRepository;
import org.fabiojava.timebank.domain.ports.repositories.PrenotazioneRepository;
import org.fabiojava.timebank.domain.ports.repositories.RichiestaRepository;
import org.fabiojava.timebank.domain.services.UtenteService;
import org.fabiojava.timebank.gui.utils.SceneManager;
import org.fabiojava.timebank.gui.utils.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
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
    private final PrenotazioneRepository prenotazioneRepository;

    private RichiestaOffertaDTO richiestaOffertaDTO;

    @FXML   private RadioButton offerRadio;
    @FXML   private RadioButton requestRadio;
    @FXML   private ToggleGroup insertType;

    @FXML   private TextField hoursField;
    @FXML   private TextArea noteField;
    @FXML   private Button newAttivitaButton;

    @FXML   private ComboBox<AttivitaItem> attivitaField;
    @FXML   private DatePicker endDateField;
    @FXML   private DatePicker startDateField;
    @FXML   private Label errorMessage;

    @Setter
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
    public OfferRequestController(SceneManager sceneManager, SessionManager sessionManager, RichiestaRepository richiestaRepository, OffertaRepository offertaRepository, AttivitaRepository attivitaRepository, UtenteService utenteService, PrenotazioneRepository prenotazioneRepository) {
        this.sceneManager = sceneManager;
        this.sessionManager = sessionManager;
        this.richiestaRepository = richiestaRepository;
        this.offertaRepository = offertaRepository;
        this.attivitaRepository = attivitaRepository;
        this.utenteService = utenteService;
        this.prenotazioneRepository = prenotazioneRepository;
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

        if(sessionManager.getDataTransferObject() instanceof HashMap<?, ?> map){
            for (Object key : map.keySet()) {
                Object value = map.get(key);
                if(key instanceof Richiesta richiesta){
                    if(value instanceof RichiestaOffertaDTO dto){
                        this.richiestaOffertaDTO = dto;
                        populateFields(true, richiesta);
                    }
                }else if(key instanceof Offerta offerta){
                    if(value instanceof RichiestaOffertaDTO dto){
                        this.richiestaOffertaDTO = dto;
                        populateFields(true, offerta);
                    }
                }
            }
        }
        sessionManager.setDataTransferObject(null);

        startDateField.setShowWeekNumbers(false);
        endDateField.setShowWeekNumbers(false);
    }

    private void populateFields(boolean disabled, Inserimento inserimento) {
        if(richiestaOffertaDTO != null) {
            for(AttivitaItem item : attivitaField.getItems()){
                if(item.getNome().equals("   - " + richiestaOffertaDTO.getNomeAttivita())
                    && item.getCategoria().equals(richiestaOffertaDTO.getCategoria())){
                    attivitaField.getSelectionModel().select(item);
                    break;
                }
            }
            if (Objects.equals(richiestaOffertaDTO.getTipoInserimento(), Inserimento.TIPO_INSERIMENTO.RICHIESTA.name())) {
                // sto inserendo un'offerta
                insertType.selectToggle(offerRadio);
                Offerta offerta = (Offerta) inserimento;
                startDateField.setValue(offerta.getDataDisponibilitaInizio().toLocalDate());
                endDateField.setValue(offerta.getDataDisponibilitaFine().toLocalDate());
                hoursField.setText(String.valueOf(offerta.getOreDisponibili()));
            } else {
                // sto inserendo una richiesta
                insertType.selectToggle(requestRadio);
                Richiesta richiesta = (Richiesta) inserimento;
                startDateField.setValue(richiesta.getDataRichiestaInizio().toLocalDate());
                endDateField.setValue(richiesta.getDataRichiestaFine().toLocalDate());
                hoursField.setText(String.valueOf(richiesta.getOreRichieste()));
            }
            offerRadio.setDisable(disabled);
            requestRadio.setDisable(disabled);
            newAttivitaButton.setDisable(disabled);
            attivitaField.setDisable(disabled);
        }
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
            return selected.getNome().substring(5);
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
        if(attivita.isEmpty()) { showError("Attività non trovata"); return; }
        if(insertType.getSelectedToggle() == offerRadio){
            if(areDataInvalid()) { showError("Uno o più campi errati"); return; }
            Offerta offerta = new Offerta(
                    null,
                    sessionManager.getCurrentUser().getMatricola(),
                    attivita.get().getIdAttivita(),
                    Date.valueOf(startDateField.getValue()),
                    Date.valueOf(endDateField.getValue()),
                    Integer.parseInt(hoursField.getText()),
                    Offerta.StatoOfferta.DISPONIBILE,
                    noteField.getText(),
                    Timestamp.valueOf(LocalDateTime.now())
            );
            Long id_offerta = offertaRepository.save(offerta);
            offerta.setIdOfferta(id_offerta);
            prenotazioneRepository.save(
                new Prenotazione(
                    null,
                    offerta.getIdOfferta(),
                    richiestaOffertaDTO.getId(),
                    Timestamp.valueOf(LocalDateTime.now()),
                    offerta.getOreDisponibili(),
                    Prenotazione.StatoPrenotazione.IN_CORSO,
                    null,
                    offerta.getNote()
                )
            );
        } else {
            if(areDataInvalid()) { showError("Uno o più campi errati"); return; }
            // determino in base alla data di scadenza la priorità
            //TODO priorità richiesta
            Richiesta.PrioritaRichiesta priority = Richiesta.PrioritaRichiesta.NORMALE;
            if(endDateField.getValue().isBefore(java.time.LocalDate.now().plusDays(10))) priority = Richiesta.PrioritaRichiesta.URGENTE;
            Richiesta richiesta = new Richiesta(
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
            );
            Long id_richiesta = richiestaRepository.save(richiesta);
            richiesta.setIdRichiesta(id_richiesta);
            prenotazioneRepository.save(
                    new Prenotazione(
                            null,
                            richiesta.getIdRichiesta(),
                            richiestaOffertaDTO.getId(),
                            Timestamp.valueOf(LocalDateTime.now()),
                            richiesta.getOreRichieste(),
                            Prenotazione.StatoPrenotazione.IN_CORSO,
                            null,
                            richiesta.getNote()
                    )
            );
        }
        log.info("Insertion completed");
        if(insertType.getSelectedToggle() == offerRadio) {
            utenteService.aggiungiOreUtente(sessionManager.getCurrentUser().getMatricola(), 3);
        }else{
            utenteService.sottraiOreUtente(sessionManager.getCurrentUser().getMatricola(), 1);
        }
        sceneManager.navigateLastScene();
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
        sceneManager.switchContent(SceneManager.SceneType.ATTIVITA, "TimeBank - Nuova attività");
    }

    public static void confirmCancel(Class<?> obj, SceneManager sceneManager) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Conferma annullamento inserimento");
        alert.setGraphic(null);
        alert.setContentText("Sei sicuro di voler annullare?");
        alert.setHeaderText(null);

        DialogPane dialogPane = alert.getDialogPane();
        ((Stage) dialogPane.getScene().getWindow()).getIcons().add(new Image(Objects.requireNonNull(Objects.requireNonNull(obj.getResource("/org/fabiojava/timebank/images/icon.png")).toExternalForm())));
        dialogPane.getStylesheets().add(Objects.requireNonNull(obj.getResource("/org/fabiojava/timebank/styles/light-theme.css")).toExternalForm());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK){
            sceneManager.navigateLastScene();
        }
    }

    @FXML private void onCancelHandle() {
        confirmCancel(OfferRequestController.class, sceneManager);
    }
}
