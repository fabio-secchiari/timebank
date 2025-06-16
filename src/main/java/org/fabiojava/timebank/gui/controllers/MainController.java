package org.fabiojava.timebank.gui.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;
import org.fabiojava.timebank.gui.utils.NavigationCallback;
import org.fabiojava.timebank.gui.utils.SceneManager;
import org.fabiojava.timebank.gui.utils.SessionManager;
import org.fabiojava.timebank.gui.utils.SpringFXMLLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

@Log
@Controller
public class MainController {
    @FXML   private StackPane contentArea;
    @FXML   private MenuItem backMenuItem;

    private final SessionManager sessionManager;
    private final SpringFXMLLoader fxmlLoader;
    private final Stack<SceneElements> navigationStack = new Stack<>();
    @Getter private final Map<SceneManager.SceneType, NavigationCallback> navigationCallbacks = new HashMap<>();
    @Getter private SceneElements currentScene;
    @Setter private Stage primaryStage;

    @Autowired
    public MainController(SessionManager sessionManager, SpringFXMLLoader fxmlLoader) {
        this.sessionManager = sessionManager;
        this.fxmlLoader = fxmlLoader;
    }

    @Getter
    @AllArgsConstructor
    public static class SceneElements {
        private SceneManager.SceneType sceneType;
        private String title;
    }

    @FXML
    public void initialize() {
        backMenuItem.setDisable(true);
    }

    public void registerNavigationCallback(SceneManager.SceneType sceneType, NavigationCallback callback) {
        navigationCallbacks.put(sceneType, callback);
    }

    public void clearNavigationCallback(SceneManager.SceneType sceneType) {
        navigationCallbacks.remove(sceneType);
    }

    public void clearAllNavigationCallbacks() {
        navigationCallbacks.clear();
    }

    private int findSceneInStack(SceneManager.SceneType sceneType) {
        for (int i = 0; i < navigationStack.size(); i++) {
            if (navigationStack.get(i).getSceneType() == sceneType) {
                return i;
            }
        }
        return -1;
    }

    public void loadContent(SceneManager.SceneType sceneType, String title, boolean addToStack) {
        try {
            if (addToStack) {
                if (currentScene != null && currentScene.getSceneType() == sceneType) {
                    return;
                }
                // Se la schermata richiesta è già nello stack, rimuovi tutto fino a quella schermata
                int existingIndex = findSceneInStack(sceneType);
                if (existingIndex != -1) {
                    while (navigationStack.size() > existingIndex) {
                        navigationStack.pop();
                    }
                    addToStack = false;
                }
            }
            if (addToStack && currentScene != null) {
                navigationStack.push(currentScene);
                backMenuItem.setDisable(false);
            }
            URL location = getClass().getResource(sceneType.getFxmlPath());
            FXMLLoader loader = fxmlLoader.getLoader(location);
            Node content = loader.load();
            contentArea.getChildren().setAll(content);
            currentScene = new SceneElements(sceneType, title);
            primaryStage.setTitle(title);
        } catch (IOException e) {
            System.err.println("Errore durante il caricamento della pagina: " + sceneType.getFxmlPath());
        }
    }

    @FXML public void handleBackFromMenu() {
        SceneManager.SceneType currentScene = getCurrentScene().getSceneType();
        NavigationCallback callback = getNavigationCallbacks().get(currentScene);
        if (callback != null) {
            callback.onNavigateBack();
            clearNavigationCallback(currentScene);
        }
        handleBack();
    }

    public void handleBack() {
        if (!navigationStack.isEmpty()) {
            SceneElements previousScene = navigationStack.pop();
            loadContent(previousScene.getSceneType(), previousScene.getTitle(), false);
            backMenuItem.setDisable(navigationStack.isEmpty());
        }
    }

    @FXML
    public void handleLogout() {
        sessionManager.clearSession();
        navigationStack.clear();
        backMenuItem.setDisable(true);
        loadContent(SceneManager.SceneType.LOGIN, "TimeBank - Login", false);
    }

    @FXML
    public void handleExit() {
        Platform.exit();
    }
}