package org.fabiojava.timebank.infrastructure.adapters.repositories;

import org.fabiojava.timebank.domain.model.Valutazione;
import org.fabiojava.timebank.domain.ports.database.DatabaseConnection;
import org.fabiojava.timebank.domain.ports.repositories.ValutazioneRepository;
import org.fabiojava.timebank.infrastructure.adapters.mapper.ValutazioneMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class SqlServerValutazioneRepositoryImpl implements ValutazioneRepository {
    private final DatabaseConnection databaseConnection;

    @Autowired
    public SqlServerValutazioneRepositoryImpl(DatabaseConnection dbConnection) {
        this.databaseConnection = dbConnection;
    }

    @Override
    public void save(Valutazione valutazione) {
        String sql = "INSERT INTO valutazioni (punteggio, commento, data_valutazione, tipo_valutatore, id_prenotazione) " +
                "VALUES (?, ?, ?, ?, ?)";

        databaseConnection.executeUpdate(sql,
                valutazione.getPunteggio(),
                valutazione.getCommento(),
                valutazione.getDataValutazione(),
                valutazione.getTipoValutatore().name(),
                valutazione.getIdPrenotazione()
        );
    }

    @Override
    public Optional<Valutazione> findById(Long id) {
        String sql = "SELECT * FROM valutazioni WHERE id = ?";
        return databaseConnection.executeQuery(sql, ValutazioneMapper::toEntity, id)
                .stream().findFirst();
    }

    @Override
    public List<Valutazione> findByUtente(String username) {
        String sql = "SELECT * FROM valutazioni WHERE id_utente = ?";
        return databaseConnection.executeQuery(sql, ValutazioneMapper::toEntity, username)
                .stream().toList();
    }

    @Override
    public List<Valutazione> findAll() {
        String sql = "SELECT * FROM valutazioni";
        return databaseConnection.executeQuery(sql, ValutazioneMapper::toEntity)
                .stream().toList();
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM valutazioni WHERE id = ?";
        databaseConnection.executeUpdate(sql, id);
    }

    // non aggiorno tipo valutatore e id_prenotazione
    @Override
    public void update(Valutazione valutazione) {
        String sql = "UPDATE valutazioni SET punteggio = ?, commento = ?, data_valutazione = ?" +
                "WHERE id = ?";
        databaseConnection.executeUpdate(sql,
                valutazione.getPunteggio(),
                valutazione.getCommento(),
                valutazione.getDataValutazione(),
                valutazione.getId()
        );
    }
}
