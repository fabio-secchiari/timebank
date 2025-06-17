# Gestionale Banca del Tempo

## 📌 Descrizione del Progetto

Questo progetto è un gestionale per una **banca del tempo**, sviluppato in Java utilizzando **JavaFX** per l'interfaccia grafica e **Spring Boot** per la logica backend.  
L'applicazione consente agli utenti di registrarsi, autenticarsi e interagire con richieste e offerte di tempo in modo semplice e sicuro.  
Ogni utente può inserire, prenotare, filtrare e valutare attività all'interno di un'interfaccia moderna e responsive.

## ✨ Caratteristiche Principali

- **🔐 Autenticazione e Sicurezza**
  - Login sicuro con hashing SHA-1 e salting potenziato
  - Conferma email asincrona
  - Protezione delle configurazioni con **Jasypt**
  - Gestione sessione tramite singleton dedicato

- **📋 Gestione Utenti e Attività**
  - Registrazione con verifica unicità
  - Inserimento, modifica e visualizzazione di **richieste** e **offerte**
  - Filtro avanzato per categoria, disponibilità, tipo e data

- **📆 Prenotazioni e Valutazioni**
  - Prenotazione intelligente con controlli su date e stato
  - Storico delle prenotazioni, gestione accettazioni/rifiuti
  - Dialog di valutazione con sistema a **stelle** e storico valutazioni

- **🎨 Interfaccia Utente**
  - Navigazione dinamica con gestione **stack delle viste**
  - UI personalizzata via CSS (supporto Scene Builder)
  - Barra strumenti centralizzata con callback e icone
  - Modal e alert personalizzati per una UX moderna

- **🧠 Architettura e Tecnologie**
  - Backend: Spring Boot + Maven
  - Frontend: JavaFX
  - Query dinamiche con supporto a UNION, paginazione e filtri
  - Mappatura automatica dei resultset senza ORM pesanti

## 🔧 Tecnologie Utilizzate

- Java 17+
- Spring Boot
- JavaFX (FXML + CSS)
- Maven
- Jasypt
- Scene Builder
- Database relazionale SQL

## ▶️ Avvio del Progetto

1. Clona la repository:

   ```bash
   git clone <URL_REPOSITORY>
   
2. Configura il file `application.properties` con le credenziali del tuo database.

3. Costruisci il progetto:

   ```bash
   mvn clean install

4. Avvia l'app Spring Boot:

   ```bash
   mvn spring-boot:run

5. Esegui l'interfaccia JavaFX dal modulo dedicato.

## 🚧 Stato del Progetto

- ✅ Funzionalità principali completate  
- ✅ UI responsive e rifinita graficamente  
- ✅ Sistema completo di gestione utenti, prenotazioni e valutazioni  
- 🔜 Struttura pronta per estensioni future (es. caricamento file, chat integrata)

## 🤝 Contributi

Il progetto è aperto a miglioramenti e contributi.  
Segnalazioni di bug e proposte di nuove funzionalità sono benvenute tramite **Issue** o **Pull Request**.
