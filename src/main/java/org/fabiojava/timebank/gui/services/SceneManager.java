package org.fabiojava.timebank.gui.services;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;
import org.fabiojava.timebank.gui.utils.SpringFXMLLoader;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.util.*;

@Service
@Scope("singleton")
@Log
public class SceneManager {
    private final ResourceLoader resourceLoader;

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

    @Getter
    @Setter
    @AllArgsConstructor
    public static class SceneElements {
        private SceneType sceneType;
        private String title;
        private boolean fullScreen;
    }

    private final Map<SceneType, Scene> scenes = new HashMap<>();
    @Setter
    private Stage primaryStage;
    private final Stack<SceneElements> sceneElementsList = new Stack<>();
    private SceneElements currentSceneElements = null;
    private final SpringFXMLLoader springFXMLLoader;

    public SceneManager(SpringFXMLLoader springFXMLLoader, ResourceLoader resourceLoader) {
        this.springFXMLLoader = springFXMLLoader;
        this.resourceLoader = resourceLoader;
    }

    private void loadScene(SceneType sceneType) {
        try {
            URL location = getClass().getResource(sceneType.getFxmlPath());
            FXMLLoader loader = springFXMLLoader.getLoader(location);

            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.setUserData(sceneType);
            scenes.put(sceneType, scene);

        } catch (IOException e) {
            log.severe("Errore nel caricamento della scena " + sceneType + ": " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void unloadScene(SceneType sceneType) {
        Scene scene = scenes.get(sceneType);
        if (scene != null) {
            primaryStage.hide();
            primaryStage.setScene(null);
            scenes.remove(sceneType);
        } else {
            log.warning("SceneType " + sceneType + " non presente");
        }
    }

    public void navigateLastScene(){
        if(sceneElementsList.isEmpty()) return;
        SceneElements oldScene = sceneElementsList.pop();
        switchScene(oldScene.getSceneType(), oldScene.getTitle(), oldScene.isFullScreen(), true);
    }

    public void navigateLoginPage(){
        sceneElementsList.clear();
        switchScene(SceneType.LOGIN, "UNIMORE TimeBank Login", false, false);
    }

    public void switchScene(SceneType sceneType, String title, boolean fullScreen, boolean backward) {
        if(currentSceneElements != null && !backward) sceneElementsList.push(currentSceneElements);
        currentSceneElements = new SceneElements(sceneType, title, fullScreen);
        // faccio l'unload della scena perchè così la volta successiva venga richiamato tutto
        unloadScene(primaryStage.getScene() == null ? null : (SceneType) primaryStage.getScene().getUserData());
        Scene scene = scenes.get(sceneType);
        if (scene == null) {
            loadScene(sceneType);
            scene = scenes.get(sceneType);
        }

        if (primaryStage != null) {
            primaryStage.hide(); // necessario affinchè venga chiamato il listener sull'onShow

            primaryStage.setTitle(title);
            primaryStage.setResizable(true);
            primaryStage.setScene(scene);
            primaryStage.toFront();
            primaryStage.requestFocus();

            Scene finalScene = scene;
            primaryStage.setOnShown(windowEvent -> {
                if(fullScreen) primaryStage.sizeToScene();
                double width = finalScene.getWidth();
                double height = finalScene.getHeight();
                Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
                primaryStage.setX((screenBounds.getWidth() - width) / 2);
                primaryStage.setY((screenBounds.getHeight() - height) / 2);
            });

            if (fullScreen) {
                Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
                finalScene.getWindow().setWidth(screenBounds.getWidth() * 0.9);
                finalScene.getWindow().setHeight(screenBounds.getHeight() * 0.9);
                Stage stage = (Stage) finalScene.getWindow();
                stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
                stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
            }

            primaryStage.show();
        } else {
            log.severe("primaryStage non inizializzato");
            throw new IllegalStateException("primaryStage non inizializzato");
        }
    }
}