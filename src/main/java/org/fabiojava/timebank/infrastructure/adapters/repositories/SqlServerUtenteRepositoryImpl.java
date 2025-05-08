package org.fabiojava.timebank.infrastructure.adapters.repositories;

import org.fabiojava.timebank.domain.ports.database.DatabaseConnection;
import org.fabiojava.timebank.domain.ports.repositories.UtenteRepository;
import org.fabiojava.timebank.domain.model.Utente;
import org.fabiojava.timebank.infrastructure.adapters.mapper.UtenteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class SqlServerUtenteRepositoryImpl implements UtenteRepository {
    private final DatabaseConnection databaseConnection;

    @Autowired
    public SqlServerUtenteRepositoryImpl(DatabaseConnection dbConnection) {
        this.databaseConnection = dbConnection;
    }

    @Override
    public void save(Utente utente) {
        String sql = "INSERT INTO utenti (matricola, nome, email, username, password, " +
                "cognome, indirizzo, telefono, data_registrazione, ore_totali) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        databaseConnection.executeUpdate(sql,
                utente.getMatricola(),
                utente.getNome(),
                utente.getEmail(),
                utente.getUsername(),
                utente.getPassword(),
                utente.getCognome(),
                utente.getIndirizzo(),
                utente.getTelefono(),
                utente.getDataRegistrazione(),
                utente.getOreTotali()
        );
    }

    @Override
    public Optional<Utente> findByMatricola(String matricola) {
        String sql = "SELECT * FROM utenti WHERE matricola = ?";
        return databaseConnection.executeQuery(sql, UtenteMapper::toEntity, matricola)
                .stream().findFirst();
    }

    @Override
    public Optional<Utente> findByUsername(String username) {
        String sql = "SELECT * FROM utenti WHERE username = ?";
        return databaseConnection.executeQuery(sql, UtenteMapper::toEntity, username)
                .stream().findFirst();
    }

    @Override
    public List<Utente> findAll() {
        String sql = "SELECT * FROM utenti";
        return databaseConnection.executeQuery(sql, UtenteMapper::toEntity)
                .stream().toList();
    }

    @Override
    public void update(Utente utente) {
        String sql = "UPDATE utenti SET nome = ?, email = ?, cognome = ? WHERE matricola = ?";
        databaseConnection.executeUpdate(sql,
                utente.getNome(),
                utente.getEmail(),
                utente.getMatricola(),
                utente.getCognome()
        );
    }

    @Override
    public void delete(String matricola) {
        String sql = "DELETE FROM utenti WHERE matricola = ?";
        databaseConnection.executeUpdate(sql, matricola);
    }
}