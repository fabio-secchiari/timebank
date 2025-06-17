package org.fabiojava.timebank.gui.utils;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import lombok.Getter;
import lombok.Setter;
import org.fabiojava.timebank.domain.model.Utente;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Scope("singleton")
public class SessionManager {
    private Optional<Utente> currentUser = Optional.empty();
    private final ObjectProperty<Utente> currentUserProperty = new SimpleObjectProperty<>();
    @Getter @Setter private Object dataTransferObject = null;

    public void clearSession() {
        this.currentUser = Optional.empty();
    }

    public void setCurrentUser(Optional<Utente> user) {
        this.currentUser = user;
        this.currentUserProperty.set(user.orElse(null));
    }

    public ObjectProperty<Utente> currentUserProperty() {
        return currentUserProperty;
    }

    public Utente getCurrentUser() throws IllegalStateException {
        return currentUser.orElse(null);
    }

    public boolean isUserLoggedIn() {
        return currentUser.isPresent();
    }
}