package org.fabiojava.timebank.infrastructure.adapters.database.specification;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class DeleteSpecification {
    private String fromTable;
    private List<QuerySpecification.WhereClause> whereClauses = new ArrayList<>();

    public DeleteSpecification from(String fromTable) {
        this.fromTable = fromTable;
        return this;
    }

    public DeleteSpecification where(String field, String operator, Object value) {
        whereClauses.add(new QuerySpecification.WhereClause(field, operator, value));
        return this;
    }
}
