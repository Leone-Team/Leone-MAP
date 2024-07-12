# Progetto Leone - MAP

## INDICE
- [Introduzione](#introduzione)
- [Diagramma delle Classi](#diagramma-delle-classi)
- [Specifiche Algebriche](#specifiche-algebriche)
- [Applicazione Argomenti del Corso](#applicazione-argomenti-del-corso)

## INTRODUZIONE

### Team

Il team che ha lavorato sul progetto è composto da:
- Federisco Troisi
- Domenico Tinelli
- Anna Maffione

### Descrizione

Questo progetto, realizzato in occasione dell'esame di Metodi Avanzati di Programmazione, è un'avventura testuale basata sul personaggio iconico della serie animata Leone il Cane Fifone.

### Trama

Giustino ha acquistato una casa super moderna dotata di intelligenza artificiale. Tuttavia, Giustino perde il controllo su di essa, finendo per essere espulso dalla casa e lasciando Leone e Marilù in preda alla casa imbizzarrita. 
Sarà compito di Leone risolvere i vari enigmi a cui sarà sottoposto per disattivare una volta per tutte la casa e mettere fine a questo disastro. 

## DIAGRAMMA DELLE CLASSI

![](./img/Diagramma_Classi_MAP.jpg)

## DESCRIZIONE CLASSI

### SaveManager

La classe 'SaveManager' si occupa di gestire il salavataggio, caricamento e creazione di una partita, interfacciandosi con un server, qualora lo si voglia, per il backup e recupero dei dati.

I metodi principali della classe sono newGame, loadMatch e saveMatch, che permettono la gestione dei dati in locale.
Il metodo connectToServer permette di connettersi al server, attraverso operazioni di login o signin.
Se connessi sarà possibile effettare il backup delle partite in locale, o ripristinare salvataggi dal server, qualora presenti, nonchè avere accesso alla classifica globale dei giocatori.

Al player sarà possibile avere contemporaneamente salvate 3 partite, qualora non terminate.

### Server

La classe 'Server' simula il lato server del progetto. Si occupa di gestire molteplici socket, avviando un thread per ciascuno dei client connessi affinché sia possibile gestirli contemporaneamente.

Il server salva su database le informazioni riguardanti l'utente, username e password, per poi accedervi per effettuare i controlli di validità di username e correttezza password. L'accesso al database è controllato, un solo thread per volta può accedervi per effettuare le ricerche.

Il metodo run gestisce completamente le richieste dell'utente, permettendo l'accesso ai dati salvati, attraverso login, o la registrazione se non vi si è mai interfacciati.
Poi effettua operazioni di backup, ripristino, aggiunta del player alla classifica globale e accesso alla classifica.

Ad ogni player corrisponde una directory con 3 partite salvabili contemporaneamente.

