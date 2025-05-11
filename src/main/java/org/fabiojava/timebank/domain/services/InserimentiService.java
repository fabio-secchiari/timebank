package org.fabiojava.timebank.domain.services;

import org.fabiojava.timebank.domain.dto.RichiestaOffertaDTO;
import org.fabiojava.timebank.domain.ports.database.QueryPort;
import org.fabiojava.timebank.infrastructure.adapters.database.specification.QuerySpecification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InserimentiService {
    private final QueryPort queryPort;

    public InserimentiService(QueryPort queryPort) {
        this.queryPort = queryPort;
    }

    public List<RichiestaOffertaDTO> trovaRichiesteOfferteRecenti(String matricolaUtente) {
        QuerySpecification spec = new QuerySpecification();

        spec.addSelect("'OFFERTA' as tipo_inserimento")
                .addSelect("o.id_offerta as id")
                .addSelect("o.data_inserimento")
                .addSelect("o.data_disponibilita_inizio as data_inizio")
                .addSelect("o.data_disponibilita_fine as data_fine")
                .addSelect("o.matricola_offerente as matricola_utente")
                .addSelect("o.stato")
                .addSelect("NULL as priorita")
                .addSelect("o.ore_disponibili")
                .addSelect("o.note")
                .addSelect("a.id_attivita")
                .addSelect("a.nome_attivita")
                .addSelect("a.categoria")
            .from("offerte o")
                .join("attivita a", "o.id_attivita = a.id_attivita", QuerySpecification.JoinClause.JoinType.INNER)
            .where("o.matricola_offerente", "!=", matricolaUtente)
                .where("o.stato", "=", "DISPONIBILE");

        spec.union();

        spec.addSelect("'RICHIESTA' as tipo_inserimento")
                .addSelect("r.id_richiesta as id")
                .addSelect("r.data_inserimento")
                .addSelect("r.data_richiesta_inizio as data_inizio")
                .addSelect("r.data_richiesta_fine as data_fine")
                .addSelect("r.matricola_richiedente as matricola_utente")
                .addSelect("r.stato")
                .addSelect("r.priorita")
                .addSelect("r.ore_richieste as ore_disponibili")
                .addSelect("r.note")
                .addSelect("a.id_attivita")
                .addSelect("a.nome_attivita")
                .addSelect("a.categoria")
            .from("richieste r")
                .join("attivita a", "r.id_attivita = a.id_attivita", QuerySpecification.JoinClause.JoinType.INNER)
            .where("r.matricola_richiedente", "!=", matricolaUtente)
                .where("r.stato", "=", "APERTA");

        spec.orderBy("data_inserimento", false)
            .limit(20);

        return queryPort.execute(spec, RichiestaOffertaDTO.class);
    }

}
