package org.fabiojava.timebank.gui.controllers.dialogs;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Dialog;
import lombok.Setter;
import org.fabiojava.timebank.domain.model.Valutazione;
import org.fabiojava.timebank.gui.components.RatingControl;
import org.springframework.stereotype.Controller;

@Controller
public class ValutazioneDialogController {
    @FXML private RatingControl ratingControl;
    @FXML private TextArea commentoTextArea;
    @FXML private Label utenteLabel;

    @Setter
    private Dialog<?> dialog;
    private String matricolaValutato;

    @FXML
    public void initialize() {
        ratingControl.setRating(0); // Valore predefinito
    }

    public void setUtenteValutato(String matricola, String nomeCompleto) {
        this.matricolaValutato = matricola;
        this.utenteLabel.setText(nomeCompleto + " (Matricola: " + matricola + ")");
    }

    public Valutazione getValutazione() {
        Valutazione valutazione = new Valutazione();
        valutazione.setPunteggio(ratingControl.getRating());
        valutazione.setCommento(commentoTextArea.getText());
        return valutazione;
    }
}