package org.fabiojava.timebank.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import lombok.extern.java.Log;
import org.fabiojava.timebank.domain.services.UtenteService;
import org.fabiojava.timebank.gui.SceneManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.logging.Level;

@Controller
@Log
public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Button exitButton;

    @FXML
    private Button registerButton;

    @FXML
    private Button helpButton;

    @FXML
    private Label errorLabel;

    @Autowired
    private UtenteService utenteService;

    @Autowired
    private SceneManager sceneManager;

    @FXML
    public void initialize() {
        loginButton.setDisable(true);

        // listener per verificare che i campi non siano vuoti
        usernameField.textProperty().addListener((observable, oldValue, newValue) ->
                checkFields());
        passwordField.textProperty().addListener((observable, oldValue, newValue) ->
                checkFields());
    }

    private void checkFields() {
        boolean fieldsEmpty = usernameField.getText().trim().isEmpty() ||
                passwordField.getText().trim().isEmpty();
        loginButton.setDisable(fieldsEmpty);
    }

    @FXML
    public void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        try {
            boolean loginSuccessful = validateLogin(username, password);

            if (loginSuccessful) {
                sceneManager.switchScene(SceneManager.SceneType.DASHBOARD, "TimeBank - Dashboard");
            } else {
                showError("Credenziali non valide",
                        "Username o password non corretti.");
                passwordField.clear();
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "Errore durante il login", e);
            showError("Errore di Sistema",
                    "Si è verificato un errore durante il login. Riprova più tardi.");
        }
    }

    private boolean validateLogin(String username, String password) {
        return utenteService.verificaCredenziali(username, password);
    }

    private void showError(String header, String content) {
        errorLabel.setText(header + "\n" + content);
    }

    @FXML
    public void handleExit(){
        System.exit(0);
    }

    @FXML
    public void handleSignUp(){
        sceneManager.switchScene(SceneManager.SceneType.SIGNUP, "TimeBank - Registrazione");
    }

    @FXML
    public void handlePswRecover(){}

}