package org.fabiojava.timebank.gui.utils;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.extern.java.Log;
import org.fabiojava.timebank.gui.controllers.MainController;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;

@Service
@Scope("singleton")
@Log
public class SceneManager {
    private final MainController mainController;

    @Getter
    public enum SceneType {
        LOGIN("/org/fabiojava/timebank/fxml/login.fxml"),
        DASHBOARD("/org/fabiojava/timebank/fxml/dashboard.fxml"),
        SIGNUP("/org/fabiojava/timebank/fxml/register.fxml"),
        INSERTION("/org/fabiojava/timebank/fxml/offer-request.fxml"),
        ATTIVITA("/org/fabiojava/timebank/fxml/attivita.fxml"),
        HOT_INSERTION_LIST("/org/fabiojava/timebank/fxml/hot-insertion.fxml"),
        OWN_INSERTION_LIST("/org/fabiojava/timebank/fxml/own-insertion.fxml"),
        INSERTION_DETAILS("/org/fabiojava/timebank/fxml/insertion-detail.fxml"),
        PRENOTAZIONI_LIST("/org/fabiojava/timebank/fxml/prenotazioni-list.fxml"),;

        private final String fxmlPath;

        SceneType(String fxmlPath) {
            this.fxmlPath = fxmlPath;
        }

    }

    private Stage primaryStage;
    private final SpringFXMLLoader springFXMLLoader;

    public SceneManager(SpringFXMLLoader springFXMLLoader, MainController mainController) {
        this.springFXMLLoader = springFXMLLoader;
        this.mainController = mainController;
    }

    public void registerNavigationCallback(SceneType sceneType, NavigationCallback callback) {
        mainController.registerNavigationCallback(sceneType, callback);
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.mainController.setPrimaryStage(primaryStage);
    }

    public void navigateLastScene() {
        mainController.handleBackFromMenu();
    }

    public void switchContent(SceneType sceneType, String title) {
        mainController.loadContent(sceneType, title, true);
    }

    public void switchContent(SceneType sceneType) {
        String title = switch (sceneType) {
            case LOGIN -> "TimeBank - Login";
            case DASHBOARD -> "TimeBank - Dashboard";
            case SIGNUP -> "TimeBank - Registrazione";
            case INSERTION -> "TimeBank - Nuovo Inserimento";
            case ATTIVITA -> "TimeBank - Gestione Attività";
            case HOT_INSERTION_LIST -> "TimeBank - Inserimenti Recenti";
            case OWN_INSERTION_LIST -> "TimeBank - I Miei Inserimenti";
            case INSERTION_DETAILS -> "TimeBank - Dettagli Inserimento";
            case PRENOTAZIONI_LIST -> "TimeBank - Prenotazioni";
        };
        switchContent(sceneType, title);
    }

    public void initMainScene() {
        try {
            URL location = getClass().getResource("/org/fabiojava/timebank/fxml/main.fxml");
            FXMLLoader loader = springFXMLLoader.getLoader(location);
            Scene scene = new Scene(loader.load());
            primaryStage.setScene(scene);

            // rendo la finestra centrata e full screen
            Screen screen = Screen.getPrimary();
            Rectangle2D bounds = screen.getVisualBounds();
            primaryStage.setX(bounds.getMinX());
            primaryStage.setY(bounds.getMinY());
            primaryStage.setWidth(bounds.getWidth());
            primaryStage.setHeight(bounds.getHeight());
            primaryStage.setMaximized(true);

            primaryStage.show();
            switchContent(SceneType.LOGIN);
        } catch (IOException e) {
            System.err.println("Errore durante l'inizializzazione della pagina principale: " + e.getMessage());
        }
    }

    public void navigateLoginPage(){
        mainController.handleLogout();
    }
}