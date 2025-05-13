INSERT INTO attivita (nome_attivita, descrizione, durata_minima_ore, durata_massima_ore, categoria)
VALUES
-- Istruzione e Formazione
('Ripetizioni Matematica', 'Lezioni private di matematica per studenti', 1, 4, 'Istruzione'),
('Lezioni di Inglese', 'Conversazione e grammatica inglese', 1, 3, 'Istruzione'),
('Supporto Informatico Base', 'Aiuto nell uso del computer e internet', 1, 2, 'Istruzione'),
('Lezioni di Italiano', 'Grammatica e letteratura italiana', 1, 3, 'Istruzione'),
('Lezioni di Musica', 'Lezioni base di strumenti musicali', 1, 2, 'Istruzione'),
-- Assistenza Domestica
('Pulizie Domestiche', 'Pulizia e riordino della casa', 2, 4, 'Assistenza Domestica'),
('Preparazione Pasti', 'Cucina e preparazione pasti', 1, 3, 'Assistenza Domestica'),
('Giardinaggio Base', 'Manutenzione base del giardino', 2, 4, 'Assistenza Domestica'),
('Piccole Riparazioni', 'Riparazioni domestiche semplici', 1, 3, 'Assistenza Domestica'),
('Organizzazione Casa', 'Aiuto nell organizzazione degli spazi', 2, 4, 'Assistenza Domestica'),
-- Assistenza Personale
('Accompagnamento Spesa', 'Supporto per la spesa settimanale', 1, 2, 'Assistenza Personale'),
('Compagnia Anziani', 'Compagnia e supporto per anziani', 1, 4, 'Assistenza Personale'),
('Dog Sitting', 'Custodia e passeggiata cani', 1, 2, 'Assistenza Personale'),
('Cat Sitting', 'Custodia e cura gatti', 1, 2, 'Assistenza Personale'),
('Accompagnamento Visite', 'Accompagnamento per visite mediche', 1, 3, 'Assistenza Personale'),
-- Servizi Professionali
('Consulenza Informatica', 'Supporto tecnico informatico avanzato', 1, 3, 'Servizi Professionali'),
('Traduzioni', 'Traduzioni semplici di documenti', 1, 4, 'Servizi Professionali'),
('Supporto Amministrativo', 'Aiuto con pratiche burocratiche', 1, 2, 'Servizi Professionali'),
('Fotografia Base', 'Servizio fotografico semplice', 1, 3, 'Servizi Professionali'),
('Grafica Base', 'Creazione semplici grafiche', 1, 3, 'Servizi Professionali'),
-- Benessere e Salute
('Yoga Base', 'Lezioni base di yoga', 1, 2, 'Benessere'),
('Ginnastica Dolce', 'Esercizi di ginnastica leggera', 1, 2, 'Benessere'),
('Meditazione', 'Introduzione alla meditazione', 1, 2, 'Benessere'),
('Supporto Emotivo', 'Ascolto e supporto emotivo', 1, 2, 'Benessere'),
('Passeggiate Assistite', 'Accompagnamento per passeggiate', 1, 2, 'Benessere'),
-- Cultura e Tempo Libero
('Club del Libro', 'Organizzazione gruppo lettura', 1, 2, 'Cultura'),
('Visite Culturali', 'Accompagnamento musei e mostre', 2, 4, 'Cultura'),
('Cineforum', 'Organizzazione visioni film', 2, 3, 'Cultura'),
('Giochi da Tavolo', 'Organizzazione sessioni di gioco', 1, 3, 'Cultura'),
('Laboratorio Creativo', N'Attività manuali creative', 2, 4, 'Cultura'),
-- Tecnologia
('Supporto Smartphone', 'Aiuto uso smartphone e app', 1, 2, 'Tecnologia'),
('Social Media Base', 'Introduzione ai social media', 1, 2, 'Tecnologia'),
('Email e Internet', 'Supporto uso email e navigazione', 1, 2, 'Tecnologia'),
('Videochiamate', 'Supporto per videochiamate', 1, 2, 'Tecnologia'),
('Sicurezza Online', 'Consigli per sicurezza internet', 1, 2, 'Tecnologia'),
-- Bambini
('Baby Sitting', 'Custodia bambini', 2, 4, 'Bambini'),
('Aiuto Compiti', 'Supporto compiti scolastici', 1, 2, 'Bambini'),
(N'Attività Ricreative', N'Giochi e attività per bambini', 2, 3, 'Bambini'),
('Letture Animate', 'Lettura storie per bambini', 1, 2, 'Bambini'),
('Arte per Bambini', 'Laboratorio artistico bambini', 1, 3, 'Bambini'),
-- Sport
('Sport Individuali', N'Supporto e allenamento per attività sportive individuali', 1, 3, 'Sport'),
('Sport di Squadra', N'Partecipazione e organizzazione attività sportive di gruppo', 1, 3, 'Sport'),
(N'Attività all Aperto', N'Accompagnamento per attività sportive all aria aperta', 1, 3, 'Sport'),
('Fitness Base', 'Supporto per esercizi di base e mantenimento forma fisica', 1, 2, 'Sport'),
('Sport Acquatici', N'Accompagnamento per attività in acqua e supporto base', 1, 2, 'Sport');



INSERT INTO utenti (matricola, username, password, nome, cognome, email, indirizzo, telefono, data_registrazione, ore_totali)
VALUES
    ('000001', 'mario.rossi', '', 'Mario', 'Rossi', 'mario.rossi@studenti.unimore.it', 'Via Roma 1, Modena', '3331234567', CURRENT_TIMESTAMP, 10),
    ('000002', 'laura.bianchi', '', 'Laura', 'Bianchi', 'laura.bianchi@studenti.unimore.it', 'Via Emilia 25, Bologna', '3339876543', CURRENT_TIMESTAMP, 5),
    ('000003', 'giuseppe.verdi', '', 'Giuseppe', 'Verdi', 'giuseppe.verdi@studenti.unimore.it', 'Corso Italia 15, Reggio Emilia', '3351122334', CURRENT_TIMESTAMP, 8),
    ('000004', 'anna.neri', '', 'Anna', 'Neri', 'anna.neri@studenti.unimore.it', 'Via Garibaldi 7, Modena', '3387654321', CURRENT_TIMESTAMP, 3);



INSERT INTO offerte (matricola_offerente, id_attivita, data_disponibilita_inizio, data_disponibilita_fine, ore_disponibili, stato, note, data_inserimento)
VALUES
-- Offerte da 000001
('000001', 3, '2025-04-01', '2025-05-31', 20, 'DISPONIBILE', 'Lezioni pomeridiane disponibili', CURRENT_TIMESTAMP),
('000001', 43, '2025-04-01', '2025-06-30', 24, 'DISPONIBILE', 'Allenamenti personalizzati', CURRENT_TIMESTAMP),
('000001', 5, '2025-04-15', '2025-05-15', 15, 'DISPONIBILE', 'Supporto informatico base', CURRENT_TIMESTAMP),
('000001', 23, '2025-04-01', '2025-05-31', 24, 'DISPONIBILE', 'Sessioni yoga principianti', CURRENT_TIMESTAMP),
('000001', 33, '2025-04-01', '2025-05-31', 25, 'DISPONIBILE', 'Assistenza dispositivi Apple', CURRENT_TIMESTAMP),
('000001', 8, '2025-04-01', '2025-04-30', 16, 'DISPONIBILE', 'Pulizie domestiche', CURRENT_TIMESTAMP),

-- Offerte da 000002
('000002', 44, '2025-04-15', '2025-07-15', 30, 'DISPONIBILE', 'Organizzazione sport di squadra', CURRENT_TIMESTAMP),
('000002', 4, '2025-04-01', '2025-06-30', 30, 'DISPONIBILE', 'Conversazione inglese', CURRENT_TIMESTAMP),
('000002', 29, '2025-04-15', '2025-06-15', 24, 'DISPONIBILE', 'Visite guidate musei', CURRENT_TIMESTAMP),
('000002', 13, '2025-04-01', '2025-05-31', 20, 'DISPONIBILE', 'Accompagnamento con auto', CURRENT_TIMESTAMP),
('000002', 38, '2025-04-01', '2025-07-31', 30, 'DISPONIBILE', 'Babysitting esperienza pluriennale', CURRENT_TIMESTAMP),
('000002', 9, '2025-04-15', '2025-05-15', 12, 'DISPONIBILE', 'Cucina tradizionale', CURRENT_TIMESTAMP),

-- Offerte da 000003
('000003', 45, '2025-04-01', '2025-05-31', 20, 'DISPONIBILE', 'Guide escursioni natura', CURRENT_TIMESTAMP),
('000003', 19, '2025-04-15', '2025-05-15', 20, 'DISPONIBILE', 'Traduzioni italiano-inglese', CURRENT_TIMESTAMP),
('000003', 24, '2025-04-15', '2025-06-15', 20, 'DISPONIBILE', 'Ginnastica dolce anziani', CURRENT_TIMESTAMP),
('000003', 10, '2025-04-01', '2025-06-30', 24, 'DISPONIBILE', 'Giardinaggio e potature', CURRENT_TIMESTAMP),
('000003', 34, '2025-04-15', '2025-06-15', 20, 'DISPONIBILE', 'Gestione social media', CURRENT_TIMESTAMP),
('000003', 14, '2025-04-15', '2025-07-15', 30, 'DISPONIBILE', 'Assistenza anziani', CURRENT_TIMESTAMP),

-- Offerte da 000004 e 330592
('000004', 46, '2025-04-01', '2025-06-30', 25, 'DISPONIBILE', 'Personal training base', CURRENT_TIMESTAMP),
('000004', 30, '2025-04-01', '2025-07-31', 20, 'DISPONIBILE', 'Cineforum e discussioni', CURRENT_TIMESTAMP),
('000004', 15, '2025-04-01', '2025-04-30', 15, 'DISPONIBILE', 'Dog sitting con esperienza', CURRENT_TIMESTAMP),
('330592', 47, '2025-04-15', '2025-07-15', 20, 'DISPONIBILE', 'Lezioni nuoto base', CURRENT_TIMESTAMP),
('330592', 39, '2025-04-15', '2025-06-15', 25, 'DISPONIBILE', 'Aiuto compiti elementari', CURRENT_TIMESTAMP),
('330592', 5, '2025-04-15', '2025-05-15', 15, 'DISPONIBILE', 'Supporto uso PC base', CURRENT_TIMESTAMP);




INSERT INTO richieste (matricola_richiedente, id_attivita, data_richiesta_inizio, data_richiesta_fine, ore_richieste, stato, priorita, note, data_inserimento)
VALUES
-- Richieste da 000001
('000001', 44, '2025-04-15', '2025-05-15', 12, 'APERTA', 'NORMALE', 'Cerco gruppo calcetto', CURRENT_TIMESTAMP),
('000001', 4, '2025-04-01', '2025-04-30', 10, 'APERTA', 'ALTA', 'Preparazione colloquio lavoro inglese', CURRENT_TIMESTAMP),
('000001', 13, '2025-04-10', '2025-05-10', 8, 'APERTA', 'NORMALE', 'Accompagnamento visite mediche', CURRENT_TIMESTAMP),
('000001', 39, '2025-04-15', '2025-05-15', 10, 'APERTA', 'URGENTE', 'Aiuto compiti matematica figlio', CURRENT_TIMESTAMP),
('000001', 9, '2025-04-01', '2025-04-30', 6, 'APERTA', 'BASSA', 'Imparare ricette base', CURRENT_TIMESTAMP),
('000001', 33, '2025-04-05', '2025-04-30', 4, 'APERTA', 'NORMALE', 'Configurazione nuovo smartphone', CURRENT_TIMESTAMP),

-- Richieste da 000002
('000002', 45, '2025-04-01', '2025-04-30', 6, 'APERTA', 'NORMALE', 'Escursioni weekend', CURRENT_TIMESTAMP),
('000002', 18, '2025-04-05', '2025-04-30', 6, 'APERTA', 'ALTA', 'Riparazione PC urgente', CURRENT_TIMESTAMP),
('000002', 23, '2025-04-01', '2025-05-31', 8, 'APERTA', 'BASSA', 'Lezioni yoga base', CURRENT_TIMESTAMP),
('000002', 10, '2025-04-01', '2025-04-30', 4, 'APERTA', 'NORMALE', 'Sistemazione giardino', CURRENT_TIMESTAMP),
('000002', 29, '2025-04-10', '2025-04-30', 6, 'APERTA', 'BASSA', 'Visite musei con guida', CURRENT_TIMESTAMP),
('000002', 14, '2025-04-15', '2025-05-15', 20, 'APERTA', 'ALTA', 'Assistenza madre anziana', CURRENT_TIMESTAMP),

-- Richieste da 000003
('000003', 46, '2025-04-10', '2025-05-10', 10, 'APERTA', 'NORMALE', 'Inizio programma fitness', CURRENT_TIMESTAMP),
('000003', 30, '2025-04-15', '2025-05-15', 9, 'APERTA', 'BASSA', 'Partecipazione gruppo cineforum', CURRENT_TIMESTAMP),
('000003', 15, '2025-04-01', '2025-04-15', 5, 'APERTA', 'ALTA', 'Dog sitting per weekend', CURRENT_TIMESTAMP),
('000003', 38, '2025-04-10', '2025-05-10', 15, 'APERTA', 'URGENTE', 'Babysitter pomeridiana', CURRENT_TIMESTAMP),
('000003', 5, '2025-04-01', '2025-04-15', 3, 'APERTA', 'NORMALE', 'Installazione nuovo PC', CURRENT_TIMESTAMP),
('000003', 19, '2025-04-15', '2025-04-30', 10, 'APERTA', 'ALTA', 'Traduzione documenti urgenti', CURRENT_TIMESTAMP),

-- Richieste da 000004 e 330592
('000004', 47, '2025-04-15', '2025-05-15', 8, 'APERTA', 'NORMALE', 'Corso nuoto principianti', CURRENT_TIMESTAMP),
('000004', 24, '2025-04-15', '2025-05-15', 6, 'APERTA', 'ALTA', 'Ginnastica riabilitativa post incidente', CURRENT_TIMESTAMP),
('000004', 8, '2025-04-05', '2025-04-30', 12, 'APERTA', 'NORMALE', 'Pulizia casa completa', CURRENT_TIMESTAMP),
('330592', 43, '2025-04-05', '2025-05-05', 8, 'APERTA', 'NORMALE', 'Cerco partner allenamento', CURRENT_TIMESTAMP),
('330592', 34, '2025-04-15', '2025-05-15', 6, 'APERTA', 'BASSA', 'Aiuto gestione Instagram', CURRENT_TIMESTAMP),
('330592', 7, '2025-04-10', '2025-05-10', 10, 'APERTA', 'ALTA', N'Ripetizioni matematica università', CURRENT_TIMESTAMP);