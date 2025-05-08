package org.fabiojava.timebank.gui.services;

import lombok.Setter;
import org.fabiojava.timebank.domain.model.Utente;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Setter
@Scope("singleton")
public class SessionManager {
    private Optional<Utente> currentUser = Optional.empty();

    public void clearSession() {
        this.currentUser = Optional.empty();
    }

    public Utente getCurrentUser() throws IllegalStateException {
        return currentUser.orElse(null);
    }

    public boolean isUserLoggedIn() {
        return currentUser.isPresent();
    }
}