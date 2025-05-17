package org.fabiojava.timebank.gui.controllers.dialogs;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import lombok.Setter;
import org.fabiojava.timebank.domain.model.Utente;
import org.fabiojava.timebank.domain.model.Valutazione;
import org.springframework.stereotype.Controller;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

@Controller
public class UtenteDetailsDialogController {
    @FXML private Label nomeCompletoLabel;
    @FXML private Label matricolaLabel;
    @FXML private Label emailLabel;
    @FXML private Label telefonoLabel;
    @FXML private Label creditiLabel;
    @FXML private HBox starContainer;

    @FXML private TableView<Valutazione> valutazioniTable;
    @FXML private TableColumn<Valutazione, Timestamp> dataColumn;
    @FXML private TableColumn<Valutazione, Integer> punteggioColumn;
    @FXML private TableColumn<Valutazione, String> commentoColumn;
    @FXML private TableColumn<Valutazione, Valutazione.TipoValutatore> tipoValutatoreColumn;

    @Setter
    private Dialog<ButtonType> dialog;

    @FXML
    public void initialize() {
        configuraTabellaColonne();
        Platform.runLater(() -> {
            Button closeButton = (Button) dialog.getDialogPane()
                    .lookupButton(ButtonType.CLOSE);
            closeButton.setText("Chiudi");
        });
    }

    private void configuraTabellaColonne() {
        dataColumn.setCellValueFactory(new PropertyValueFactory<>("dataValutazione"));
        punteggioColumn.setCellValueFactory(new PropertyValueFactory<>("punteggio"));
        commentoColumn.setCellValueFactory(new PropertyValueFactory<>("commento"));
        tipoValutatoreColumn.setCellValueFactory(new PropertyValueFactory<>("tipoValutatore"));

        // Formattatore per la data
        dataColumn.setCellFactory(column -> new TableCell<>() {
            private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

            @Override
            protected void updateItem(Timestamp item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(sdf.format(item));
                }
            }
        });

        // Formattatore per il punteggio (stelle)
        punteggioColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText("★".repeat(item) + "☆".repeat(5 - item));
                }
            }
        });
    }

    public void setData(Utente utente, double mediaValutazioni, List<Valutazione> valutazioni) {
        nomeCompletoLabel.setText(utente.getNome() + " " + utente.getCognome());
        matricolaLabel.setText(utente.getMatricola());
        emailLabel.setText(utente.getEmail());
        telefonoLabel.setText(utente.getTelefono());
        creditiLabel.setText(String.valueOf(utente.getOreTotali()));

        // TODO Aggiunge le stelle per la valutazione media
        /*starContainer.getChildren().clear();
        for (int i = 1; i <= 5; i++) {
            ImageView star = new ImageView(new Image(
                    getClass().getResourceAsStream("/images/star.png")));
            star.setFitHeight(20);
            star.setFitWidth(20);
            star.getStyleClass().add(i <= mediaValutazioni ? "star-filled" : "star-empty");
            starContainer.getChildren().add(star);
        }*/

        // Popola la tabella
        valutazioniTable.setItems(FXCollections.observableArrayList(valutazioni));
    }
}