package org.fabiojava.timebank.gui.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;

@Component
public class SpringFXMLLoader {
    private final ApplicationContext applicationContext;

    public SpringFXMLLoader(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public FXMLLoader getLoader(URL location) {
        FXMLLoader loader = new FXMLLoader(location);
        loader.setControllerFactory(applicationContext::getBean);
        return loader;
    }

}