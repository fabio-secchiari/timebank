package org.fabiojava.timebank.infrastructure.adapters.repositories;

import org.fabiojava.timebank.domain.model.Attivita;
import org.fabiojava.timebank.domain.ports.database.DatabaseConnection;
import org.fabiojava.timebank.domain.ports.repositories.AttivitaRepository;
import org.fabiojava.timebank.infrastructure.adapters.mapper.AttivitaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class SqlServerAttivitaRepositoryImpl implements AttivitaRepository {
    private final DatabaseConnection databaseConnection;

    @Autowired
    public SqlServerAttivitaRepositoryImpl(DatabaseConnection dbConnection) {
        this.databaseConnection = dbConnection;
    }

    @Override
    public void save(Attivita attivita) {
        String sql = "INSERT INTO attivita (nome_attivita, descrizione, categoria, durata_minima_ore, durata_massima_ore)" +
                "VALUES (?, ?, ?, ?, ?)";
        databaseConnection.executeUpdate(sql,
                attivita.getNome(),
                attivita.getDescrizione(),
                attivita.getCategoria(),
                attivita.getDurataMinOre(),
                attivita.getDurataMaxOre()
        );
    }

    @Override
    public Optional<Attivita> findById(Long id) {
        String sql = "SELECT * FROM attivita WHERE id = ?";
        return databaseConnection.executeQuery(sql, AttivitaMapper::toEntity, id)
                .stream().findFirst();
    }

    @Override
    public Optional<Attivita> findByNome(String nome) {
        String sql = "SELECT * FROM attivita WHERE nome_attivita = ?";
        return databaseConnection.executeQuery(sql, AttivitaMapper::toEntity, nome)
                .stream().findFirst();
    }

    @Override
    public List<Attivita> findAll() {
        String sql = "SELECT * FROM attivita";
        return databaseConnection.executeQuery(sql, AttivitaMapper::toEntity)
                .stream().toList();
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM attivita WHERE id = ?";
        databaseConnection.executeUpdate(sql, id);
    }

    @Override
    public void update(Attivita attivita) {
        String sql = "UPDATE attivita SET nome_attivita = ?, descrizione = ?, durata_minima_ore = ?, durata_massima_ore = ? WHERE id = ?";
        databaseConnection.executeUpdate(sql,
                attivita.getNome(),
                attivita.getDescrizione(),
                attivita.getDurataMinOre(),
                attivita.getDurataMaxOre(),
                attivita.getId()
        );
    }
}
