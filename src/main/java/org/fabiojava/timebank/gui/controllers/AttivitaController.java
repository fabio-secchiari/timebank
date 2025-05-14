package org.fabiojava.timebank.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import lombok.extern.java.Log;
import org.fabiojava.timebank.domain.dto.CategoriaDTO;
import org.fabiojava.timebank.domain.model.Attivita;
import org.fabiojava.timebank.domain.ports.repositories.AttivitaRepository;
import org.fabiojava.timebank.gui.services.SceneManager;
import org.fabiojava.timebank.gui.services.SessionManager;
import org.springframework.stereotype.Controller;

import java.util.Optional;

@Log
@Controller
public class AttivitaController {

    @FXML    private Label errorMessage;
    @FXML    private TextField nomeField;
    @FXML    private TextArea descrizioneField;
    @FXML    private ComboBox<String> cmbCategoria;
    @FXML    private TextField minOreField;
    @FXML    private TextField maxOreField;

    private final SceneManager sceneManager;
    private final AttivitaRepository attivitaRepository;
    private final SessionManager sessionManager;

    public AttivitaController(SceneManager sceneManager, AttivitaRepository attivitaRepository, SessionManager sessionManager) {
        this.sceneManager = sceneManager;
        this.attivitaRepository = attivitaRepository;
        this.sessionManager = sessionManager;
    }

    @FXML
    public void initialize() {
        attivitaRepository.findAllCategorie().stream().map(CategoriaDTO::getCategoria).forEach(cmbCategoria.getItems()::add);
    }

    @FXML
    private void saveAttivita() {
        if(areDataInvalid()) { showError("Uno o più campi errati"); return; }
        Attivita attivita = new Attivita(
                null,
                nomeField.getText(),
                descrizioneField.getText(),
                Integer.parseInt(minOreField.getText()),
                Integer.parseInt(maxOreField.getText()),
                cmbCategoria.getSelectionModel().getSelectedItem()
        );
        attivitaRepository.save(attivita);

        log.info("Attività salvata correttamente");
        sessionManager.setDataTransferObject(attivita);
        sceneManager.navigateLastScene();
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
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Nuova categoria");
        dialog.setContentText("Nome categoria:");
        dialog.graphicProperty().set(null);
        dialog.setHeaderText(null);

        dialog.showAndWait().ifPresent(result -> {
            if (!result.trim().isEmpty()) {
                cmbCategoria.getItems().add(result);
                cmbCategoria.getSelectionModel().select(result);
            }else{
                showError("Nome categoria non può essere vuoto");
            }
        });
    }

    @FXML private void onCancelHandle() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Conferma annullamento inserimento");
        alert.setGraphic(null);
        alert.setHeaderText("Sei sicuro di voler annullare?");
        alert.setContentText(null);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK){
            sceneManager.navigateLastScene();
        }
    }

    @FXML private void onLogOutHandle() {
        log.info("Logout");
        sessionManager.clearSession();
        sceneManager.navigateLoginPage();
    }

    @FXML private void onExitHandle() {
        System.exit(0);
    }
}