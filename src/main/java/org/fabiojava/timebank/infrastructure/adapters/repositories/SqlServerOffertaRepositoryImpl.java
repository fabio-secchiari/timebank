package org.fabiojava.timebank.infrastructure.adapters.repositories;

import org.fabiojava.timebank.domain.model.Offerta;
import org.fabiojava.timebank.domain.ports.database.DatabaseConnection;
import org.fabiojava.timebank.domain.ports.repositories.OffertaRepository;
import org.fabiojava.timebank.infrastructure.adapters.mapper.OffertaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class SqlServerOffertaRepositoryImpl implements OffertaRepository {
    private final DatabaseConnection databaseConnection;

    @Autowired
    public SqlServerOffertaRepositoryImpl(DatabaseConnection dbConnection) {
        this.databaseConnection = dbConnection;
    }

    @Override
    public void save(Offerta offerta) {
        String sql = "INSERT INTO offerte (matricola_offerente, id_attivita, data_disponibilita_inizio, " +
                "data_disponibilita_fine, ore_disponibili, stato, note, data_inserimento " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        databaseConnection.executeUpdate(sql,
                offerta.getMatricolaOfferente(),
                offerta.getIdAttivita(),
                offerta.getDataInizio(),
                offerta.getDataFine(),
                offerta.getOreDisponibili(),
                offerta.getStato().name(),
                offerta.getNote(),
                offerta.getDataInserimento());
    }

    @Override
    public Optional<Offerta> findById(Long id) {
        String sql = "SELECT * FROM offerte WHERE id_offerta = ?";
        return databaseConnection.executeQuery(sql, OffertaMapper::toEntity, id)
                .stream().findFirst();
    }

    @Override
    public List<Offerta> findByUtente(String matricola) {
        String sql = "SELECT * FROM offerte WHERE matricola_offerente = ?";
        return databaseConnection.executeQuery(sql, OffertaMapper::toEntity, matricola)
                .stream().toList();
    }

    @Override
    public List<Offerta> findAll() {
        String sql = "SELECT * FROM offerte";
        return databaseConnection.executeQuery(sql, OffertaMapper::toEntity)
                .stream().toList();
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM offerte WHERE id_offerta = ?";
        databaseConnection.executeUpdate(sql, id);
    }

    @Override
    public void update(Offerta offerta) {
        String sql = "UPDATE FROM offerte SET data_disponibilita_inizio = ?, data_disponibilita_fine = ?, " +
                "ore_disponibili = ?, stato = ?, note = ? WHERE id_offerta = ?";
        databaseConnection.executeUpdate(sql,
                offerta.getDataInizio(),
                offerta.getDataFine(),
                offerta.getOreDisponibili(),
                offerta.getStato().name(),
                offerta.getNote(),
                offerta.getId());
    }
}
