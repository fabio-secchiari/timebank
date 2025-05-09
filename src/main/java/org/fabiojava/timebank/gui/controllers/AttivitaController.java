package org.fabiojava.timebank.gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import lombok.extern.java.Log;
import org.fabiojava.timebank.domain.model.Attivita;
import org.fabiojava.timebank.domain.ports.repositories.AttivitaRepository;
import org.springframework.stereotype.Controller;

@Log
@Controller
public class AttivitaController {

    @FXML    private Label errorMessage;
    @FXML    private Button saveButton;
    @FXML    private TextField nomeField;
    @FXML    private TextArea descrizioneField;
    @FXML    private ComboBox<String> cmbCategoria;
    @FXML    private Button btnNuovaCategoria;
    @FXML    private TextField minOreField;
    @FXML    private TextField maxOreField;

    private final AttivitaRepository attivitaRepository;

    public AttivitaController(AttivitaRepository attivitaRepository) {
        this.attivitaRepository = attivitaRepository;
    }

    @FXML
    public void initialize() {
        attivitaRepository.findAll().forEach(attivita -> cmbCategoria.getItems().add(attivita.getCategoria()));
    }

    @FXML
    private void saveAttivita() {
        if(areDataInvalid()) { showError("Uno o più campi errati"); return; }
        attivitaRepository.save(new Attivita(
                null,
                nomeField.getText(),
                descrizioneField.getText(),
                Integer.parseInt(minOreField.getText()),
                Integer.parseInt(maxOreField.getText()),
                cmbCategoria.getSelectionModel().getSelectedItem()
            )
        );
    }

    private boolean areDataInvalid() {
        if(nomeField.getText().isEmpty()) return true;
        if(descrizioneField.getText().isEmpty()) return true;
        if(cmbCategoria.getValue() == null) return true;
        if(minOreField.getText().isEmpty()) return true;
        if(maxOreField.getText().isEmpty()) return true;

        if(!minOreField.getText().matches("[0-9]+")) return true;
        if(!maxOreField.getText().matches("[0-9]+")) return true;
        if(Integer.parseInt(minOreField.getText()) > Integer.parseInt(maxOreField.getText())) return true;

        return false;
    }

    private void showError(String content) {
        errorMessage.setVisible(true);
        errorMessage.setText(content);
        log.info("Errore: " + content);
    }

    @FXML
    private void newCategoria() {

    }
}
