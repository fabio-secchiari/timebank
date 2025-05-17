package org.fabiojava.timebank.gui.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.Builder;
import lombok.Data;
import lombok.extern.java.Log;
import org.fabiojava.timebank.domain.dto.RichiestaOffertaDTO;
import org.fabiojava.timebank.domain.services.InserimentiService;
import org.fabiojava.timebank.gui.services.SceneManager;
import org.fabiojava.timebank.gui.services.SessionManager;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;

import java.sql.Date;

@Log
@Controller
public class AllInsertionController {
    private final InserimentiService inserimentiService;
    private final SessionManager sessionManager;
    private final SceneManager sceneManager;

    @FXML   private TableColumn<RichiestaOffertaDTO, Date> dataColonna;
    @FXML   private TableColumn<RichiestaOffertaDTO, String> tipoColonna;
    @FXML   private TableColumn<RichiestaOffertaDTO, String> statoColonna;
    @FXML   private TableColumn<RichiestaOffertaDTO, String> attivitaColonna;
    @FXML   private TableColumn<RichiestaOffertaDTO, String> noteColonna;
    @FXML   private TableColumn<RichiestaOffertaDTO, Void> azioneColonna;
    @FXML
    private TableView<RichiestaOffertaDTO> tabellaRichieste;
    @FXML
    private ComboBox<String> statoFiltro;
    @FXML
    private DatePicker dataInizioFiltro;
    @FXML
    private DatePicker dataFineFiltro;
    @FXML
    private TextField cercaFiltro;
    @FXML
    private Label paginaCorrente;
    @FXML
    private Label totaleRecord;

    private int paginaAttuale = 0;
    private int totalePagine = 0;

    @FXML private void handleIndietro() {
        sceneManager.navigateLastScene();
    }

    @FXML private void handleLogOut() {
        sessionManager.setCurrentUser(java.util.Optional.empty());
        sceneManager.navigateLoginPage();
    }

    @FXML public static void handleChiudi() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Conferma");
        alert.setHeaderText(null);
        alert.setContentText("Sei sicuro di voler chiudere TimeBank?");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                System.exit(0);
            }
        });
    }

    public void handleChiudi(ActionEvent actionEvent) {
        handleChiudi();
    }

    public static class AzioneTableCell extends TableCell<RichiestaOffertaDTO, Void> {
        private final Button visualizzaButton;

        public AzioneTableCell(String label, SceneManager sceneManager, SessionManager sessionManager) {
            visualizzaButton = new Button(label);
            visualizzaButton.setOnAction(event -> {
                if (getTableRow() != null) {
                    RichiestaOffertaDTO dto = getTableRow().getItem();
                    if (dto != null) {
                        sessionManager.setDataTransferObject(dto);
                        sceneManager.switchScene(SceneManager.SceneType.INSERTION_DETAILS, "TimeBank - Dettagli inserimento", false, false);
                    }
                }
            });
        }

        @Override
        protected void updateItem(Void item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setGraphic(null);
            } else {
                setGraphic(visualizzaButton);
            }
        }
    }


    public AllInsertionController(InserimentiService inserimentiService, SessionManager sessionManager, SceneManager sceneManager) {
        this.inserimentiService = inserimentiService;
        this.sessionManager = sessionManager;
        this.sceneManager = sceneManager;
    }

    @FXML
    public void initialize() {
        statoFiltro.getItems().addAll("Tutti", "In Attesa", "Approvata", "Rifiutata");
        configuraTabellaColonne();
        caricaDati();
    }

    private void configuraTabellaColonne() {
        // Configura il rendering delle celle e la formattazione
        dataColonna.setCellValueFactory(new PropertyValueFactory<>("dataInserimento"));
        tipoColonna.setCellValueFactory(new PropertyValueFactory<>("tipoInserimento"));
        statoColonna.setCellValueFactory(new PropertyValueFactory<>("stato"));
        attivitaColonna.setCellValueFactory(new PropertyValueFactory<>("nomeAttivita"));
        noteColonna.setCellValueFactory(new PropertyValueFactory<>("note"));
        azioneColonna.setCellFactory(col -> new AzioneTableCell("Visualizza", sceneManager, sessionManager));
    }

    @FXML
    private void applicaFiltri() {
        paginaAttuale = 0;
        caricaDati();
    }

    @FXML
    private void resetFiltri() {
        statoFiltro.setValue(null);
        dataInizioFiltro.setValue(null);
        dataFineFiltro.setValue(null);
        cercaFiltro.clear();
        applicaFiltri();
    }

    private void caricaDati() {
        RichiestaCriteria criteria = RichiestaCriteria.builder()
                .stato(statoFiltro.getValue())
                .dataInizio(dataInizioFiltro.getValue() != null ? Date.valueOf(dataInizioFiltro.getValue()) : null)
                .dataFine(dataFineFiltro.getValue() != null ? Date.valueOf(dataFineFiltro.getValue()) : null)
                .testoCerca(cercaFiltro.getText())
                .pagina(paginaAttuale)
                .dimensionePagina(10)
                .build();

        Page<RichiestaOffertaDTO> risultato = inserimentiService.filtraRichiesteOfferteRecenti(criteria, sessionManager.getCurrentUser().getMatricola());

        tabellaRichieste.setItems(FXCollections.observableList(risultato.getContent()));
        totalePagine = risultato.getTotalPages();
        aggiornaPaginazione(risultato.getTotalElements());
    }

    private void aggiornaPaginazione(long totaleElementi) {
        paginaCorrente.setText(String.format("Pagina %d di %d", paginaAttuale + 1, totalePagine));
        totaleRecord.setText(String.format("Totale: %d record", totaleElementi));
    }

    @Data
    @Builder
    public static class RichiestaCriteria {
        private String stato;
        private Date dataInizio;
        private Date dataFine;
        private String testoCerca;
        private int pagina;
        private int dimensionePagina;
    }

    @FXML
    private void primaPagina() {
        if (paginaAttuale != 0) {
            paginaAttuale = 0;
            caricaDati();
        }
    }

    @FXML
    private void paginaPrecedente() {
        if (paginaAttuale > 0) {
            paginaAttuale--;
            caricaDati();
        }
    }

    @FXML
    private void paginaSuccessiva() {
        if (paginaAttuale < totalePagine - 1) {
            paginaAttuale++;
            caricaDati();
        }
    }

    @FXML
    private void ultimaPagina() {
        if (paginaAttuale != totalePagine - 1) {
            paginaAttuale = totalePagine - 1;
            caricaDati();
        }
    }
}
