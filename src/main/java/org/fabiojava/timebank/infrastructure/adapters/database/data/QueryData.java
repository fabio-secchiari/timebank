package org.fabiojava.timebank.infrastructure.adapters.database.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class QueryData {
   private final String query;
   private final Object[] parameters;
}
