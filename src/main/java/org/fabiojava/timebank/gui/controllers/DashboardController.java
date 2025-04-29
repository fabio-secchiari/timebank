package org.fabiojava.timebank.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.springframework.stereotype.Controller;

@Controller
public class DashboardController {
    @FXML
    public ListView<?> todoList;

    @FXML
    private Label creditLabel;
    @FXML
    private ProgressIndicator creditBar;

    @FXML
    private TableView<?> ratingsTable;
    @FXML
    private TableColumn<?, ?> ratingTypeColumn;
    @FXML
    private TableColumn<?, ?> ratingValueColumn;
    @FXML
    private TableColumn<?, ?> ratingCommentColumn;
    @FXML
    private TableColumn<?, ?> ratingDateColumn;

    @FXML
    private TableColumn<?, ?> recordDateColumn;
    @FXML
    private TableColumn<?, ?> recordUserColumn;
    @FXML
    private TableColumn<?, ?> recordTitleColumn;
    @FXML
    private TableColumn<?, ?> recordTypeColumn;

    @FXML
    private TableView<?> latestRecordsTable;
    @FXML
    private Button newOfferButton;
    @FXML
    private ListView<?> userOffersList;
    @FXML
    private Button newRequestButton;
    @FXML
    private ListView<?> userRequestsList;

    //TODO BOTTONI PER LOGOUT E TERMINARE APPLICAZIONE


}
