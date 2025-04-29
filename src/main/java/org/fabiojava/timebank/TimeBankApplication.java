package org.fabiojava.timebank;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import javafx.application.Application;
import javafx.stage.Stage;
import lombok.extern.java.Log;
import org.fabiojava.timebank.gui.SceneManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import java.util.logging.Level;

@EnableEncryptableProperties
@Log
@SpringBootApplication
@Configuration(proxyBeanMethods = false)
@EnableJpaRepositories(basePackages = "org.fabiojava.timebank")
@EntityScan(basePackages = "org.fabiojava.timebank")
@Component
@EnableAsync
public class TimeBankApplication extends Application {
    private ConfigurableApplicationContext springContext;
    private SceneManager sceneManager;

    @Override
    public void init() {
        springContext = SpringApplication.run(TimeBankApplication.class);
        sceneManager = springContext.getBean(SceneManager.class);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            sceneManager.setPrimaryStage(primaryStage);
            sceneManager.initializeScenes();
            sceneManager.switchScene(SceneManager.SceneType.LOGIN, "UNIMORE TimeBank Login");
        } catch (Exception e) {
            log.log(Level.SEVERE, "Errore durante l'avvio dell'applicazione", e);
        }
    }

    @Override
    public void stop() {
        springContext.close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}