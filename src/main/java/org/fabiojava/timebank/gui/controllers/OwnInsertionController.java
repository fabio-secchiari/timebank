package org.fabiojava.timebank.gui.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import lombok.extern.java.Log;
import org.fabiojava.timebank.domain.dto.RichiestaOffertaDTO;
import org.fabiojava.timebank.domain.model.Inserimento;
import org.fabiojava.timebank.domain.services.InserimentiService;
import org.fabiojava.timebank.gui.services.SceneManager;
import org.fabiojava.timebank.gui.services.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;

import java.sql.Date;

import static org.fabiojava.timebank.gui.controllers.AllInsertionController.handleChiudi;

@Log
@Controller
public class OwnInsertionController {
    private final SessionManager sessionManager;
    private final SceneManager sceneManager;
    private final InserimentiService inserimentiService;

    @FXML private TableView<RichiestaOffertaDTO> tabellaInserimenti;
    @FXML private TableColumn<RichiestaOffertaDTO, Date> dataInizioColonna;
    @FXML private TableColumn<RichiestaOffertaDTO, Date> dataFineColonna;
    @FXML private TableColumn<RichiestaOffertaDTO, String> attivitaColonna;
    @FXML private TableColumn<RichiestaOffertaDTO, String> noteColonna;
    @FXML private TableColumn<RichiestaOffertaDTO, Void> azioniColonna;
    @FXML private Label paginaCorrente;
    @FXML private Label totaleRecord;

    private int paginaAttuale = 0;
    private int totalePagine = 0;

    @Autowired
    public OwnInsertionController(SessionManager sessionManager, SceneManager sceneManager, InserimentiService inserimentiService) {
        this.sessionManager = sessionManager;
        this.sceneManager = sceneManager;
        this.inserimentiService = inserimentiService;
    }

    @FXML
    public void initialize() {
        configuraTabellaColonne();
        caricaDati();
    }

    @FXML private void handleBack() {
        sceneManager.switchScene(SceneManager.SceneType.DASHBOARD, "TimeBank - Dashboard", true);
    }

    @FXML private void handleLogOut() {
        sessionManager.setCurrentUser(java.util.Optional.empty());
        sceneManager.switchScene(SceneManager.SceneType.LOGIN, "TimeBank - Login", false);
    }

    @FXML private void handleClose() {
        handleChiudi();
    }

    private void configuraTabellaColonne() {
        dataInizioColonna.setCellValueFactory(new PropertyValueFactory<>("dataInizio"));
        dataFineColonna.setCellValueFactory(new PropertyValueFactory<>("dataFine"));
        attivitaColonna.setCellValueFactory(new PropertyValueFactory<>("nomeAttivita"));
        noteColonna.setCellValueFactory(new PropertyValueFactory<>("note"));

        azioniColonna.setCellFactory(col -> new TableCell<>() {
            private final Button btnDettagli = new Button("✎");
            private final Button btnPrenotazioni = new Button("\uD83D\uDC41\n");
            private final HBox hbox = new HBox(5, btnDettagli, btnPrenotazioni);

            {
                btnDettagli.setOnAction(event -> {
                    RichiestaOffertaDTO dto = getTableRow().getItem();
                    if (dto != null) {
                        mostraDettagli(dto);
                    }
                });
                btnPrenotazioni.setOnAction(event -> {
                    RichiestaOffertaDTO dto = getTableRow().getItem();
                    if (dto != null) {
                        mostraPrenotazioni(dto);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : hbox);
            }
        });
    }

    private void caricaDati() {
        AllInsertionController.RichiestaCriteria criteria = AllInsertionController.RichiestaCriteria.builder()
                .pagina(paginaAttuale)
                .dimensionePagina(10)
                .build();
        if(sessionManager.getDataTransferObject() instanceof Inserimento.TIPO_INSERIMENTO tipo
                && sessionManager.getCurrentUser() != null) {
            if (tipo == Inserimento.TIPO_INSERIMENTO.RICHIESTA) {
                Page<RichiestaOffertaDTO> record = inserimentiService.filtraOwnRichieste(criteria, sessionManager.getCurrentUser().getMatricola());
                tabellaInserimenti.setItems(FXCollections.observableList(record.getContent()));
                totalePagine = record.getTotalPages();
                aggiornaPaginazione(record.getTotalElements());
            } else if (tipo == Inserimento.TIPO_INSERIMENTO.OFFERTA) {
                Page<RichiestaOffertaDTO> record = inserimentiService.filtraOwnOfferte(criteria, sessionManager.getCurrentUser().getMatricola());
                tabellaInserimenti.setItems(FXCollections.observableList(record.getContent()));
                totalePagine = record.getTotalPages();
                aggiornaPaginazione(record.getTotalElements());
            }
        }
    }

    private void mostraDettagli(RichiestaOffertaDTO dto) {
        sessionManager.setDataTransferObject(dto);
        sceneManager.switchScene(SceneManager.SceneType.INSERTION_DETAILS, "TimeBank - Dettagli inserimento", false);
    }

    private void mostraPrenotazioni(RichiestaOffertaDTO dto) {
        sessionManager.setDataTransferObject(dto);
        //sceneManager.switchScene();
    }

    @FXML private void primaPagina() {
        if (paginaAttuale != 0) {
            paginaAttuale = 0;
            caricaDati();
        }
    }

    @FXML private void paginaPrecedente() {
        if (paginaAttuale > 0) {
            paginaAttuale--;
            caricaDati();
        }
    }

    @FXML private void paginaSuccessiva() {
        if (paginaAttuale < totalePagine - 1) {
            paginaAttuale++;
            caricaDati();
        }
    }

    @FXML private void ultimaPagina() {
        if (paginaAttuale != totalePagine - 1) {
            paginaAttuale = totalePagine - 1;
            caricaDati();
        }
    }

    private void aggiornaPaginazione(long totaleElementi) {
        paginaCorrente.setText(String.format("Pagina %d di %d", paginaAttuale + 1, totalePagine));
        totaleRecord.setText(String.format("Totale: %d record", totaleElementi));
    }
}