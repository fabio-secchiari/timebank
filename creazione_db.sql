-- Creazione della tabella UTENTI
/*CREATE TABLE UTENTI (
    matricola VARCHAR(10) NOT NULL,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL,  -- Using VARCHAR(255) for hashed passwords
    nome VARCHAR(50) NOT NULL,
    cognome VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL,
    indirizzo VARCHAR(200),
    cod_dipartimento VARCHAR(10),
    CONSTRAINT PK_UTENTI PRIMARY KEY (matricola),
    CONSTRAINT AK_UTENTI_USERNAME UNIQUE (username),  -- Alternate Key per username
    CONSTRAINT AK_UTENTI_EMAIL UNIQUE (email),        -- Alternate Key per email
    CONSTRAINT AK_UTENTI_NOMINATIVO UNIQUE (nome, cognome, cod_dipartimento), -- AK per combinazione nome-cognome-dipartimento
    /*CONSTRAINT FK_DIPARTIMENTO FOREIGN KEY (cod_dipartimento) 
        REFERENCES DIPARTIMENTI(cod_dipartimento)*/
);

-- Creazione degli indici per migliorare le performance delle ricerche
CREATE INDEX IDX_UTENTI_USERNAME ON UTENTI(username);
CREATE INDEX IDX_UTENTI_EMAIL ON UTENTI(email);
CREATE INDEX IDX_UTENTI_COGNOME ON UTENTI(cognome, nome);*/

-- Creazione della tabella DIPARTIMENTI
/*CREATE TABLE DIPARTIMENTI (
    cod_dipartimento VARCHAR(10) NOT NULL,
    nome_dipartimento VARCHAR(100) NOT NULL,
    descrizione VARCHAR(500),
    sede VARCHAR(200) NOT NULL,
    telefono VARCHAR(20),
    email_dipartimento VARCHAR(100) NOT NULL,
    direttore VARCHAR(100) NOT NULL,
    CONSTRAINT PK_DIPARTIMENTI PRIMARY KEY (cod_dipartimento),
    CONSTRAINT AK_DIPARTIMENTI_NOME UNIQUE (nome_dipartimento),
    CONSTRAINT AK_DIPARTIMENTI_EMAIL UNIQUE (email_dipartimento),
    CONSTRAINT CHK_EMAIL_FORMATO CHECK (email_dipartimento LIKE '%@%.%'),
    CONSTRAINT CHK_CODICE_FORMATO CHECK (cod_dipartimento LIKE 'DIP%')
);

-- Creazione degli indici per migliorare le performance delle ricerche
CREATE INDEX IDX_DIPARTIMENTI_NOME ON DIPARTIMENTI(nome_dipartimento);
CREATE INDEX IDX_DIPARTIMENTI_SEDE ON DIPARTIMENTI(sede);*/

/*-- Aggiunta check constraint per il formato email
ALTER TABLE UTENTI
ADD CONSTRAINT CHK_UTENTI_EMAIL_FORMATO 
CHECK (email LIKE '%@%.%');

-- Aggiunta check constraint per la lunghezza minima username
ALTER TABLE UTENTI
ADD CONSTRAINT CHK_UTENTI_USERNAME_LENGTH 
CHECK (LEN(username) >= 5);

-- Aggiunta check constraint per la lunghezza minima password
ALTER TABLE UTENTI
ADD CONSTRAINT CHK_UTENTI_PASSWORD_LENGTH 
CHECK (LEN(password) >= 8);

-- Aggiunta check constraint per il formato matricola (esempio: assumendo che inizi con 'MAT' seguito da numeri)
ALTER TABLE UTENTI
ADD CONSTRAINT CHK_UTENTI_MATRICOLA_FORMATO 
CHECK (matricola LIKE 'MAT%' AND LEN(matricola) = 8);

-- Aggiunta check constraint per assicurare che nome e cognome non contengano numeri
ALTER TABLE UTENTI
ADD CONSTRAINT CHK_UTENTI_NOME 
CHECK (nome NOT LIKE '%[0-9]%');

ALTER TABLE UTENTI
ADD CONSTRAINT CHK_UTENTI_COGNOME 
CHECK (cognome NOT LIKE '%[0-9]%');

-- Aggiunta check constraint per assicurare che l'email sia del dominio unimore
ALTER TABLE UTENTI
ADD CONSTRAINT CHK_UTENTI_EMAIL_DOMAIN 
CHECK (email LIKE '%@%unimore.it');*/

/*-- Tabella delle categorie di attivit�
CREATE TABLE CATEGORIE_ATTIVITA (
    id_categoria INT IDENTITY(1,1) NOT NULL,
    nome_categoria VARCHAR(50) NOT NULL,
    descrizione VARCHAR(200),
    CONSTRAINT PK_CATEGORIE PRIMARY KEY (id_categoria),
    CONSTRAINT AK_CATEGORIA_NOME UNIQUE (nome_categoria)
);

-- Tabella delle attivit� specifiche
CREATE TABLE ATTIVITA (
    id_attivita INT IDENTITY(1,1) NOT NULL,
    nome_attivita VARCHAR(100) NOT NULL,
    descrizione VARCHAR(500),
    id_categoria INT NOT NULL,
    durata_minima_ore INT NOT NULL,
    durata_massima_ore INT,
    CONSTRAINT PK_ATTIVITA PRIMARY KEY (id_attivita),
    CONSTRAINT FK_ATTIVITA_CATEGORIA FOREIGN KEY (id_categoria) 
        REFERENCES CATEGORIE_ATTIVITA(id_categoria),
    CONSTRAINT AK_ATTIVITA_NOME UNIQUE (nome_attivita),
    CONSTRAINT CHK_ATTIVITA_DURATA CHECK (durata_minima_ore > 0 
        AND (durata_massima_ore IS NULL OR durata_massima_ore >= durata_minima_ore))
);

-- Tabella delle offerte di disponibilit�
CREATE TABLE OFFERTE (
    id_offerta INT IDENTITY(1,1) NOT NULL,
    matricola_offerente VARCHAR(10) NOT NULL,
    id_attivita INT NOT NULL,
    data_disponibilita_inizio DATE NOT NULL,
    data_disponibilita_fine DATE,
    ore_disponibili INT NOT NULL,
    stato VARCHAR(20) NOT NULL DEFAULT 'DISPONIBILE',
    note VARCHAR(500),
    data_inserimento DATETIME DEFAULT GETDATE(),
    CONSTRAINT PK_OFFERTE PRIMARY KEY (id_offerta),
    CONSTRAINT FK_OFFERTE_UTENTE FOREIGN KEY (matricola_offerente) 
        REFERENCES UTENTI(matricola),
    CONSTRAINT FK_OFFERTE_ATTIVITA FOREIGN KEY (id_attivita) 
        REFERENCES ATTIVITA(id_attivita),
    CONSTRAINT CHK_OFFERTE_STATO CHECK (stato IN ('DISPONIBILE', 'PRENOTATA', 'COMPLETATA', 'CANCELLATA')),
    CONSTRAINT CHK_OFFERTE_DATE CHECK (data_disponibilita_fine IS NULL 
        OR data_disponibilita_fine >= data_disponibilita_inizio),
    CONSTRAINT CHK_OFFERTE_ORE CHECK (ore_disponibili > 0)
);

-- Tabella delle richieste di aiuto
CREATE TABLE RICHIESTE (
    id_richiesta INT IDENTITY(1,1) NOT NULL,
    matricola_richiedente VARCHAR(10) NOT NULL,
    id_attivita INT NOT NULL,
    data_richiesta_inizio DATE NOT NULL,
    data_richiesta_fine DATE,
    ore_richieste INT NOT NULL,
    stato VARCHAR(20) NOT NULL DEFAULT 'APERTA',
    priorita VARCHAR(10) NOT NULL DEFAULT 'NORMALE',
    note VARCHAR(500),
    data_inserimento DATETIME DEFAULT GETDATE(),
    CONSTRAINT PK_RICHIESTE PRIMARY KEY (id_richiesta),
    CONSTRAINT FK_RICHIESTE_UTENTE FOREIGN KEY (matricola_richiedente) 
        REFERENCES UTENTI(matricola),
    CONSTRAINT FK_RICHIESTE_ATTIVITA FOREIGN KEY (id_attivita) 
        REFERENCES ATTIVITA(id_attivita),
    CONSTRAINT CHK_RICHIESTE_STATO CHECK (stato IN ('APERTA', 'ASSEGNATA', 'COMPLETATA', 'CANCELLATA')),
    CONSTRAINT CHK_RICHIESTE_PRIORITA CHECK (priorita IN ('BASSA', 'NORMALE', 'ALTA', 'URGENTE')),
    CONSTRAINT CHK_RICHIESTE_DATE CHECK (data_richiesta_fine IS NULL 
        OR data_richiesta_fine >= data_richiesta_inizio),
    CONSTRAINT CHK_RICHIESTE_ORE CHECK (ore_richieste > 0)
);

-- Tabella degli accoppiamenti (matching) tra offerte e richieste
CREATE TABLE MATCHING (
    id_matching INT IDENTITY(1,1) NOT NULL,
    id_offerta INT NOT NULL,
    id_richiesta INT NOT NULL,
    data_matching DATETIME DEFAULT GETDATE(),
    ore_concordate INT NOT NULL,
    stato VARCHAR(20) NOT NULL DEFAULT 'PROGRAMMATO',
    data_esecuzione DATE,
    valutazione_richiedente INT,
    valutazione_offerente INT,
    note_feedback VARCHAR(500),
    CONSTRAINT PK_MATCHING PRIMARY KEY (id_matching),
    CONSTRAINT FK_MATCHING_OFFERTA FOREIGN KEY (id_offerta) 
        REFERENCES OFFERTE(id_offerta),
    CONSTRAINT FK_MATCHING_RICHIESTA FOREIGN KEY (id_richiesta) 
        REFERENCES RICHIESTE(id_richiesta),
    CONSTRAINT CHK_MATCHING_STATO CHECK (stato IN ('PROGRAMMATO', 'IN_CORSO', 'COMPLETATO', 'CANCELLATO')),
    CONSTRAINT CHK_MATCHING_ORE CHECK (ore_concordate > 0),
    CONSTRAINT CHK_MATCHING_VALUTAZIONE CHECK (
        (valutazione_richiedente IS NULL OR valutazione_richiedente BETWEEN 1 AND 5) AND
        (valutazione_offerente IS NULL OR valutazione_offerente BETWEEN 1 AND 5)
    )
);*/

/*
 CREATE TABLE valutazioni (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    punteggio INT NOT NULL,
    commento NVARCHAR(MAX),
    data_valutazione DATETIME DEFAULT GETDATE(),
    tipo_valutatore VARCHAR(20) NOT NULL,
    id_prenotazione BIGINT NOT NULL,
    CONSTRAINT check_punteggio CHECK (punteggio >= 1 AND punteggio <= 5),
    CONSTRAINT check_tipo_valutatore CHECK (tipo_valutatore IN ('RICHIEDENTE', 'OFFERENTE'))
);
 */

/*
 -- Crea un nuovo login
CREATE LOGIN user_tb
WITH PASSWORD = 'q5L+[W9{03|v',
DEFAULT_DATABASE = TimeBank,
CHECK_EXPIRATION = OFF,
CHECK_POLICY = ON;

-- Passa al database specifico
USE TimeBank;

-- Crea l'utente nel database e associalo al login
CREATE USER user_tb FOR LOGIN user_tb;

-- Assegna i permessi necessari
EXEC sp_addrolemember 'db_datareader', 'user_tb';
EXEC sp_addrolemember 'db_datawriter', 'user_tb';
 */