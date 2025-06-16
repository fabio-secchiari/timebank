package org.fabiojava.timebank.gui.controllers.dialogs;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import lombok.Setter;
import org.fabiojava.timebank.domain.model.Utente;
import org.fabiojava.timebank.domain.model.Valutazione;
import org.fabiojava.timebank.gui.components.RatingControl;
import org.springframework.stereotype.Controller;

import java.util.List;

import static org.fabiojava.timebank.gui.controllers.DashboardController.configuraRatingHistory;

@Controller
public class UtenteDetailsDialogController {
    @FXML private Label nomeCompletoLabel;
    @FXML private Label matricolaLabel;
    @FXML private Label emailLabel;
    @FXML private Label telefonoLabel;
    @FXML private Label creditiLabel;
    @FXML private HBox starContainer;

    @FXML private TableView<Valutazione> valutazioniTable;
    @FXML private TableColumn<Valutazione, String> dataColumn;
    @FXML private TableColumn<Valutazione, Integer> punteggioColumn;
    @FXML private TableColumn<Valutazione, String> commentoColumn;
    @FXML private TableColumn<Valutazione, String> tipoValutatoreColumn;

    @Setter
    private Dialog<ButtonType> dialog;

    @FXML
    public void initialize() {
        configuraRatingHistory(tipoValutatoreColumn, punteggioColumn, commentoColumn, dataColumn);
        Platform.runLater(() -> {
            Button closeButton = (Button) dialog.getDialogPane()
                    .lookupButton(ButtonType.CLOSE);
            closeButton.setText("Chiudi");
        });
    }

    public void setData(Utente utente, double mediaValutazioni, List<Valutazione> valutazioni) {
        nomeCompletoLabel.setText(utente.getNome() + " " + utente.getCognome());
        matricolaLabel.setText(utente.getMatricola());
        emailLabel.setText(utente.getEmail());
        telefonoLabel.setText(utente.getTelefono());
        creditiLabel.setText(String.valueOf(utente.getOreTotali()));

        RatingControl mediaRatingControl = new RatingControl();
        mediaRatingControl.setMouseTransparent(true);
        mediaRatingControl.setFocusTraversable(false);
        mediaRatingControl.setRating((int) Math.round(mediaValutazioni));

        starContainer.getChildren().clear();
        starContainer.getChildren().add(mediaRatingControl);

        valutazioniTable.setItems(FXCollections.observableArrayList(valutazioni));
    }
}