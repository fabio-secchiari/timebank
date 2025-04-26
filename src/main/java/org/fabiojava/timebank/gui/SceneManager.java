package org.fabiojava.timebank.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;
import org.fabiojava.timebank.gui.utils.SpringFXMLLoader;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Component
@Log
public class SceneManager {

    @Getter
    public enum SceneType {
        LOGIN("/org/fabiojava/timebank/fxml/login.fxml"),
        DASHBOARD("/org/fabiojava/timebank/fxml/dashboard.fxml"),
        SIGNUP("/org/fabiojava/timebank/fxml/register.fxml");

        private final String fxmlPath;

        SceneType(String fxmlPath) {
            this.fxmlPath = fxmlPath;
        }

    }

    private final Map<SceneType, Scene> scenes = new HashMap<>();
    @Setter
    private Stage primaryStage;
    private final SpringFXMLLoader springFXMLLoader;

    public SceneManager(SpringFXMLLoader springFXMLLoader) {
        this.springFXMLLoader = springFXMLLoader;
    }

    public void initializeScenes() {
        for (SceneType sceneType : SceneType.values()) {
            loadScene(sceneType);
        }
    }

    private void loadScene(SceneType sceneType) {
        try {
            URL location = getClass().getResource(sceneType.getFxmlPath());
            FXMLLoader loader = springFXMLLoader.getLoader(location);

            Parent root = loader.load();
            Scene scene = new Scene(root);
            scenes.put(sceneType, scene);

        } catch (IOException e) {
            log.severe("Errore nel caricamento della scena " + sceneType + ": " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void switchScene(SceneType sceneType, String title) {
        Scene scene = scenes.get(sceneType);
        if (scene == null) {
            loadScene(sceneType);
            scene = scenes.get(sceneType);
        }

        if (primaryStage != null) {
            primaryStage.setTitle(title);
            primaryStage.setResizable(true);
            primaryStage.centerOnScreen();
            primaryStage.setScene(scene);
            primaryStage.show();
            primaryStage.toFront();
            primaryStage.requestFocus();
            primaryStage.sizeToScene();
        } else {
            log.severe("primaryStage non inizializzato");
            throw new IllegalStateException("primaryStage non inizializzato");
        }
    }
}