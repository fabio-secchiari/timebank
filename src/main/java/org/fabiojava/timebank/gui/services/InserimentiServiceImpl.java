package org.fabiojava.timebank.gui.services;

import org.fabiojava.timebank.domain.dto.CountDTO;
import org.fabiojava.timebank.domain.dto.RichiestaOffertaDTO;
import org.fabiojava.timebank.domain.model.Offerta;
import org.fabiojava.timebank.domain.model.Richiesta;
import org.fabiojava.timebank.domain.ports.database.QueryPort;
import org.fabiojava.timebank.domain.services.InserimentiService;
import org.fabiojava.timebank.gui.controllers.AllInsertionController;
import org.fabiojava.timebank.infrastructure.adapters.database.specification.QuerySpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InserimentiServiceImpl implements InserimentiService {
    private static final int LIMIT_DEFAULT = 20;
    private final QueryPort queryPort;

    public InserimentiServiceImpl(QueryPort queryPort) {
        this.queryPort = queryPort;
    }

    public List<RichiestaOffertaDTO> trovaRichiesteOfferteRecenti(String matricolaUtente) {
        QuerySpecification spec = buildBaseQuerySpecification(matricolaUtente);
        spec.orderBy("data_inserimento", false)
                .limit(LIMIT_DEFAULT);
        return queryPort.execute(spec, RichiestaOffertaDTO.class);
    }
    
    private void addFilters(QuerySpecification spec, AllInsertionController.RichiestaCriteria criteria) {
        if (spec.isUnion()) {
            // Applica i filtri sulla query unificata
            if (criteria.getStato() != null) {
                spec.whereOnUnion("stato", "=", criteria.getStato());
            }
            if (criteria.getDataInizio() != null) {
                spec.whereOnUnion("data_inizio", ">=", criteria.getDataInizio());
            }
            if (criteria.getDataFine() != null) {
                spec.whereOnUnion("data_fine", "<=", criteria.getDataFine());
            }
            if (criteria.getTestoCerca() != null && !criteria.getTestoCerca().isEmpty()) {
                spec.whereOnUnion("note", "LIKE", "%" + criteria.getTestoCerca() + "%");
            }
        } else {
            for(QuerySpecification.SelectQuery query : spec.getQueries()){
                if (criteria.getStato() != null) {
                    query.getWhereClauses().add(new QuerySpecification.WhereClause("stato", "=", criteria.getStato()));
                }
                if (criteria.getDataInizio() != null) {
                    query.getWhereClauses().add(new QuerySpecification.WhereClause("data_inizio", ">=", criteria.getDataInizio()));
                }
                if (criteria.getDataFine() != null) {
                    query.getWhereClauses().add(new QuerySpecification.WhereClause("data_fine", "<=", criteria.getDataFine()));
                }
                if (criteria.getTestoCerca() != null && !criteria.getTestoCerca().isEmpty()) {
                    query.getWhereClauses().add(new QuerySpecification.WhereClause("note", "LIKE", "%" + criteria.getTestoCerca() + "%"));
                }
            }
        }
    }

    public Page<RichiestaOffertaDTO> cercaRichieste(AllInsertionController.RichiestaCriteria criteria, String matricolaUtente) {
        QuerySpecification countSpec = buildBaseQuerySpecification(matricolaUtente);
        addFilters(countSpec, criteria);
        countSpec.addSelectOnUnion("COUNT(*) as total");
        CountDTO totalElements = queryPort.executeSingle(countSpec, CountDTO.class)
                .orElse(new CountDTO(0));

        QuerySpecification dataSpec = buildBaseQuerySpecification(matricolaUtente);
        addFilters(dataSpec, criteria);
        dataSpec.orderBy("data_inserimento", false)
                .offset(criteria.getPagina() * criteria.getDimensionePagina())
                .limit(criteria.getDimensionePagina());

        List<RichiestaOffertaDTO> content = queryPort.execute(dataSpec, RichiestaOffertaDTO.class);

        return new PageImpl<>(
                content,
                PageRequest.of(criteria.getPagina(), criteria.getDimensionePagina()),
                totalElements.getTotal()
        );

    }

    private QuerySpecification buildBaseQuerySpecification(String matricolaUtente) {
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
                .join("attivita a", "o.id_attivita = a.id_attivita",
                        QuerySpecification.JoinClause.JoinType.INNER)
            .where("o.matricola_offerente", "!=", matricolaUtente)
                .where("o.stato", "=", Offerta.StatoOfferta.DISPONIBILE.name());

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
                .join("attivita a", "r.id_attivita = a.id_attivita",
                        QuerySpecification.JoinClause.JoinType.INNER)
            .where("r.matricola_richiedente", "!=", matricolaUtente)
                .where("r.stato", "=", Richiesta.StatoRichiesta.APERTA.name());

        return spec;
    }
}
