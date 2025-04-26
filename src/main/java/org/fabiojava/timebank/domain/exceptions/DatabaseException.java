package org.fabiojava.timebank.domain.exceptions;

public class DatabaseException extends RuntimeException {

    public DatabaseException(String message) {
        super(message);
    }

    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public DatabaseException(Throwable cause) {
        super("Errore di accesso al database", cause);
    }

    public static DatabaseException connectionError(Throwable cause) {
        return new DatabaseException("Impossibile connettersi al database", cause);
    }

    public static DatabaseException queryError(String query, Throwable cause) {
        return new DatabaseException("Errore nell'esecuzione della query: " + query, cause);
    }

    public static DatabaseException dataError(String entity, String operation, Throwable cause) {
        return new DatabaseException(
                String.format("Errore durante %s dell'entità %s", operation, entity),
                cause
        );
    }
}
