package org.fabiojava.timebank.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.fabiojava.timebank.domain.dto.EmailDto;
import org.fabiojava.timebank.domain.model.Utente;
import org.fabiojava.timebank.domain.ports.repositories.UtenteRepository;
import org.fabiojava.timebank.domain.services.EmailService;
import org.fabiojava.timebank.domain.services.UtenteService;
import org.fabiojava.timebank.gui.services.SceneManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Optional;
import java.util.Random;

@Controller
public class RegisterController {
    private final EmailService emailService;
    private final UtenteService utenteService;
    private final SceneManager sceneManager;
    private final UtenteRepository utenteRepository;

    @Autowired
    public RegisterController(EmailService emailService, UtenteService utenteService, SceneManager sceneManager, UtenteRepository utenteRepository) {
        this.emailService = emailService;
        this.utenteService = utenteService;
        this.sceneManager = sceneManager;
        this.utenteRepository = utenteRepository;
    }

    @FXML
    public Button registerButton;

    @FXML
    public Button cancelButton;

    @FXML
    public PasswordField passwordField;

    @FXML
    public TextField usernameField;

    @FXML
    public TextField emailField;

    @FXML
    public TextField nomeField;

    @FXML
    public TextField cognomeField;

    @FXML
    public TextField matricolaField;

    @FXML
    public TextField telefonoField;

    @FXML
    public TextField indirizzoField;

    @FXML
    public Label errorMessage;

    @FXML
    public TextField codeField;

    private String randomCode;

    private enum RegisterState {
        PASSWORD_TROPPO_CORTA,
        EMAIL_NON_VALIDA,
        MATRICOLA_NON_VALIDA,
        TELEFONO_NON_VALIDO,
        INDIRIZZO_NON_VALIDO,
        REGISTRAZIONE_AVVENUTA_CON_SUCCESSO,
        MATRICOLA_GIA_ESISTENTE,
        UNO_O_PIU_CAMPI_VUOTI,
        EMAIL_GIA_ESISTENTE, INSERISCI_QUI_IL_CODICE_DI_VERIFICA_RICEVUTO_VIA_EMAIL
    }

    private RegisterState validateFields(){
        // controlli se sono valorizzati
        if (usernameField.getText().trim().isEmpty() ||
                nomeField.getText().trim().isEmpty() ||
                cognomeField.getText().trim().isEmpty() ||
                matricolaField.getText().trim().isEmpty() ||
                indirizzoField.getText().trim().isEmpty() ||
                telefonoField.getText().trim().isEmpty() ||
                emailField.getText().trim().isEmpty() ||
                passwordField.getText().trim().isEmpty()
        )   return RegisterState.UNO_O_PIU_CAMPI_VUOTI;
        // password di almeno 8 caratteri
        if (passwordField.getText().length() < 8) return RegisterState.PASSWORD_TROPPO_CORTA;
        // email con un formato valido (deve finire con unimore.it)
        if (!emailField.getText().matches("^.+@?+unimore\\.it$")) return RegisterState.EMAIL_NON_VALIDA;
        // matricola di esattamente 6 numeri
        if (!matricolaField.getText().matches("^\\d{6}$")) return RegisterState.MATRICOLA_NON_VALIDA;
        // telefono cellulare valido
        if(!telefonoField.getText().matches("^\\d{10}$")) return RegisterState.TELEFONO_NON_VALIDO;
        return RegisterState.REGISTRAZIONE_AVVENUTA_CON_SUCCESSO;
    }

    public void handleRegister(){
        EmailDto emailDto = new EmailDto();

        emailDto.setToList(emailField.getText());
        emailDto.setSubject("Registrazione TimeBank");

        randomCode = String.format("%06d", new Random().nextInt(999999));
        emailDto.setBody("Grazie per esserti registrato a TimeBank!\n\nPer completare la registrazione, inserisci il seguente codice di verifica:\n\n" + randomCode + "\n\nSe non hai richiesto tu questa registrazione, puoi ignorare questa email.");

        RegisterState state = validateFields();
        if(state == RegisterState.REGISTRAZIONE_AVVENUTA_CON_SUCCESSO) {
            Optional<Utente> utente = utenteRepository.findByMatricola(matricolaField.getText());
            if(utente.isPresent()){
                showError(RegisterState.MATRICOLA_GIA_ESISTENTE);
                return;
            }
            emailService.sendSimpleMessage(emailDto);
            showEmailConfirm();
        } else {
            showError(state);
        }
    }

    public void showEmailConfirm() {
        showError(RegisterState.INSERISCI_QUI_IL_CODICE_DI_VERIFICA_RICEVUTO_VIA_EMAIL);
        codeField.setVisible(true);
        registerButton.setDisable(true);
        codeField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals(randomCode)) {
                String username = usernameField.getText().trim();
                String password = passwordField.getText().trim();
                String nome = nomeField.getText().trim();
                String cognome = cognomeField.getText().trim();
                String matricola = matricolaField.getText().trim();
                String telefono = telefonoField.getText().trim();
                String indirizzo = indirizzoField.getText().trim();
                String email = emailField.getText().trim();
                try {
                    utenteService.registraUtente(matricola, username, password, nome, cognome, email, indirizzo, telefono);
                } catch (Exception e) {
                    showError(RegisterState.EMAIL_GIA_ESISTENTE);
                    resetScene(false);
                    return;
                }
                showError(RegisterState.REGISTRAZIONE_AVVENUTA_CON_SUCCESSO);
                try {
                    Thread.sleep(2000);
                    resetScene(true);
                    sceneManager.switchScene(SceneManager.SceneType.LOGIN, "TimeBank - Login", false);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void showError(RegisterState state) {
        errorMessage.setText(state.name().replace("_", " "));
    }

    private void resetScene(boolean clearAll){
        if(clearAll){
            usernameField.clear();
            nomeField.clear();
            cognomeField.clear();
            matricolaField.clear();
            telefonoField.clear();
            indirizzoField.clear();
            passwordField.clear();
            emailField.clear();
            errorMessage.setText("");
        }
        registerButton.setDisable(false);
        codeField.setVisible(false);
        codeField.clear();
    }

    public void handleCancel(){
        resetScene(true);
        sceneManager.switchScene(SceneManager.SceneType.LOGIN, "TimeBank - Login", false);
    }
}
