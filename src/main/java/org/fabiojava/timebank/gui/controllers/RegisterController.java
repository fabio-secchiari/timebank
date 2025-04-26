package org.fabiojava.timebank.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Controller;

@Controller
public class RegisterController {
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



    public void handleRegister(){}
    public void handleCancel(){}
}
