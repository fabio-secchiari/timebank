package org.fabiojava.timebank.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lombok.extern.java.Log;
import org.fabiojava.timebank.domain.dto.RichiestaOffertaDTO;
import org.fabiojava.timebank.domain.model.Inserimento;
import org.fabiojava.timebank.domain.model.Offerta;
import org.fabiojava.timebank.domain.model.Richiesta;
import org.fabiojava.timebank.domain.ports.repositories.OffertaRepository;
import org.fabiojava.timebank.domain.ports.repositories.RichiestaRepository;
import org.fabiojava.timebank.gui.utils.SceneManager;
import org.fabiojava.timebank.gui.utils.SessionManager;
import org.springframework.stereotype.Controller;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Objects;
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
    @FXML    private Button btnPrenotazioni;

    public void initialize(){
        dataInizio.setShowWeekNumbers(false);
        dataFine.setShowWeekNumbers(false);
        if(sessionManager.getDataTransferObject() != null && sessionManager.getDataTransferObject() instanceof RichiestaOffertaDTO dto) {
            this.richiestaOffertaDTO = dto;
            sessionManager.setDataTransferObject(null);
        }
        sceneManager.registerNavigationCallback(SceneManager.SceneType.INSERTION_DETAILS,
                () -> sessionManager.setDataTransferObject(Inserimento.TIPO_INSERIMENTO.valueOf(richiestaOffertaDTO.getTipoInserimento())));
        populateFields();
        if(!Objects.equals(richiestaOffertaDTO.getMatricolaUtente(), sessionManager.getCurrentUser().getMatricola())) {
            btnModifica.setDisable(true);
            btnElimina.setDisable(true);
            note.setEditable(false);
            oreDisponibili.setEditable(false);
            dataInizio.getEditor().setEditable(false);
            dataFine.getEditor().setEditable(false);
            btnPrenotazioni.setText("Prenota");
            if(richiestaOffertaDTO.getStato().equals(Offerta.StatoOfferta.PRENOTATA.name()) ||
                richiestaOffertaDTO.getStato().equals(Richiesta.StatoRichiesta.ASSEGNATA.name())){
                btnPrenotazioni.setDisable(true);
            }
        }
    }

    private void populateFields() {
        tipoInserimento.setText(richiestaOffertaDTO.getTipoInserimento());
        dataInizio.setValue(richiestaOffertaDTO.getDataInizio().toLocalDate());
        dataFine.setValue(richiestaOffertaDTO.getDataFine().toLocalDate());
        oreDisponibili.setText(String.valueOf(richiestaOffertaDTO.getOreDisponibili()));
        nomeAttivita.setText(richiestaOffertaDTO.getNomeAttivita());
        stato.setText(richiestaOffertaDTO.getStato());
        note.setText(richiestaOffertaDTO.getNote());
        utente.setText(richiestaOffertaDTO.getMatricolaUtente());
        dataInserimento.setText(richiestaOffertaDTO.getDataInserimento().toString());
    }

    public DettaglioInserimentoController(SessionManager sessionManager, SceneManager sceneManager, RichiestaRepository richiestaRepository, OffertaRepository offertaRepository) {
        this.sessionManager = sessionManager;
        this.sceneManager = sceneManager;
        this.richiestaRepository = richiestaRepository;
        this.offertaRepository = offertaRepository;
    }

    @FXML private void visualizzaPrenotazioni() {
        if(btnPrenotazioni.getText().equals("Prenota")) { // vuol dire che non è mio
            if(richiestaOffertaDTO.getTipoInserimento().equals("RICHIESTA")) {
                Offerta offerta = new Offerta();
                offerta.setIdAttivita(richiestaOffertaDTO.getIdAttivita());
                offerta.setOreDisponibili(richiestaOffertaDTO.getOreDisponibili());
                offerta.setDataDisponibilitaInizio(richiestaOffertaDTO.getDataInizio());
                offerta.setDataDisponibilitaFine(richiestaOffertaDTO.getDataFine());
                offerta.setMatricolaOfferente(sessionManager.getCurrentUser().getMatricola());
                offerta.setStato(Offerta.StatoOfferta.DISPONIBILE);
                offerta.setNote("");
                offerta.setDataInserimento(Timestamp.valueOf(LocalDateTime.now()));
                HashMap<Offerta, RichiestaOffertaDTO> map = new HashMap<>();
                map.put(offerta, richiestaOffertaDTO);
                sessionManager.setDataTransferObject(map);
            }
            else {
                Richiesta richiesta = new Richiesta();
                richiesta.setIdAttivita(richiestaOffertaDTO.getIdAttivita());
                richiesta.setOreRichieste(richiestaOffertaDTO.getOreDisponibili());
                richiesta.setDataRichiestaInizio(richiestaOffertaDTO.getDataInizio());
                richiesta.setDataRichiestaFine(richiestaOffertaDTO.getDataFine());
                richiesta.setMatricolaRichiedente(sessionManager.getCurrentUser().getMatricola());
                richiesta.setStato(Richiesta.StatoRichiesta.APERTA);
                richiesta.setNote("");
                richiesta.setDataInserimento(Timestamp.valueOf(LocalDateTime.now()));
                richiesta.setPriorita(Richiesta.PrioritaRichiesta.NORMALE);
                HashMap<Richiesta, RichiestaOffertaDTO> map = new HashMap<>();
                map.put(richiesta, richiestaOffertaDTO);
                sessionManager.setDataTransferObject(map);
            }
            sceneManager.switchContent(SceneManager.SceneType.INSERTION, "TimeBank - Inserimento prenotazione");
        } else {
            sessionManager.setDataTransferObject(richiestaOffertaDTO);
            sceneManager.switchContent(SceneManager.SceneType.PRENOTAZIONI_LIST, "TimeBank - Lista prenotazioni");
        }
    }

    @FXML private void modificaInserimento() {
        richiestaOffertaDTO.setDataInizio(Date.valueOf(dataInizio.getValue()));
        richiestaOffertaDTO.setDataFine(Date.valueOf(dataFine.getValue()));
        try {
            richiestaOffertaDTO.setOreDisponibili(Integer.valueOf(oreDisponibili.getText()));
        } catch (NumberFormatException e) {
            errorMessage.setVisible(true);
            errorMessage.setText("Ore disponibili non valido");
            return;
        }
        richiestaOffertaDTO.setNote(note.getText());
        if (richiestaOffertaDTO.getTipoInserimento().equals("RICHIESTA")) {
            richiestaRepository.update(richiestaOffertaDTO.toRichiesta());
        } else {
            offertaRepository.update(richiestaOffertaDTO.toOfferta());
        }
        errorMessage.setVisible(true);
        errorMessage.setText("Inserimento modificato con successo");
        log.info("Inserimento modificato con successo");
    }

    @FXML private void eliminaInserimento() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Conferma eliminazione inserimento");
        alert.setHeaderText("Sei sicuro di voler eliminare l'inserimento?");
        alert.setContentText(null);

        DialogPane dialogPane = alert.getDialogPane();
        ((Stage) dialogPane.getScene().getWindow()).getIcons().add(new Image(Objects.requireNonNull(Objects.requireNonNull(getClass().getResource("/org/fabiojava/timebank/images/icon.png")).toExternalForm())));
        dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/org/fabiojava/timebank/styles/light-theme.css")).toExternalForm());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            log.info("Eliminazione inserimento effettuata con successo");
            if (richiestaOffertaDTO.getTipoInserimento().equals("RICHIESTA")) {
                richiestaRepository.delete(richiestaOffertaDTO.getId());
            } else {
                offertaRepository.delete(richiestaOffertaDTO.getId());
            }
            sessionManager.setDataTransferObject(Inserimento.TIPO_INSERIMENTO.valueOf(richiestaOffertaDTO.getTipoInserimento()));
            sceneManager.navigateLastScene();
        }
    }
}
