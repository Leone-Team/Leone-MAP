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

## SaveManager

La classe 'SaveManager' si occupa di gestire il salavataggio, caricamento e creazione di una partita, interfacciandosi con un server, qualora lo si voglia, per il backup e recupero dei dati.

I metodi principali della classe sono newGame, loadMatch e saveMatch, che permettono la gestione dei dati in locale.
Il metodo connectToServer permette di connettersi al server, attraverso operazioni di login o signin.
Se connessi sarà possibile effettare il backup delle partite in locale, o ripristinare salvataggi dal server, qualora presenti, nonchè avere accesso alla classifica globale dei giocatori.

Al player sarà possibile avere contemporaneamente salvate 3 partite, qualora non terminate.

## Server

La classe 'Server' simula il lato server del progetto. Si occupa di gestire molteplici socket, avviando un thread per ciascuno dei client connessi affinché sia possibile gestirli contemporaneamente.

Il server salva su database le informazioni riguardanti l'utente, username e password, per poi accedervi per effettuare i controlli di validità di username e correttezza password. L'accesso al database è controllato, un solo thread per volta può accedervi per effettuare le ricerche.

Il metodo run gestisce completamente le richieste dell'utente, permettendo l'accesso ai dati salvati, attraverso login, o la registrazione se non vi si è mai interfacciati.
Poi effettua operazioni di backup, ripristino, aggiunta del player alla classifica globale e accesso alla classifica.

Ad ogni player corrisponde una directory con 3 partite salvabili contemporaneamente.

## Engine

La classe 'Engine' permette di rendere riutilizzabile parte del programma, permettendo di poter sviluppare altre avventure testuali, purché abbiano stessa struttura implementativa, e poterle avviare con pochi accorgimenti.
La struttura implementativa è definita dalla classe astratta Game.

## Game

Game è una classe astratta, contenente gli attributi e i metodi necessari per la corretta costruzione ed esecuzione del gioco secondo lo schema scelto.
Tra gli attributi principali di Game abbiamo quelli responsabili della comunicazione con l'utente, mainWindow e mrMsg, che rispettivamente implementano la gui e permettono le operazioni di in/out; poi gli attributi per la gestione degli observers, obsAttached e observers; le strutture dati per contenere i componenti del gioco, items, riddles, rooms, commands, inventory; poi altri attributi secondari, necessari per controllare il flusso di esecuzione del gioco e gestire il tempo di partita.
Tra i metodi invece abbiamo nextMove, aggiorna il gioco basandosi sull'output del parser; checkRiddle, che verifica la presenza di indovinelli non risolti; init, che inizializza il gioco. L'implementazione di questi metodi è lasciata alle classi che estondono questa.

## LeoneGame

La classe 'LeoneGame' estende la classe astratta Game.
LeoneGame implementa i metodi principali di Game.
Attraverso init inizializziamo tutte le componenti principali del gioco, come Item, Riddle, Commands, Observers, etc, attraverso accessi a database contenenti le componenti.
Con nextMove riceviamo l'output del parser e aggiorniamo lo stato del gioco sulla base dell'azione eseguita, coordinando gli observer collegati.
checkRiddle invece è necessario per vedre se vi sono indovinelli nella stanza attuale da risolvere, e in tal caso scollegare gli observer che si occupano di eseguire l'azione, rendendo vano il comanod dell'utente e ricevendo una spiegazione sul perché non viene eseguito.

## Parser

Il parser ci permette di ricavare dall'input dell'utente una azione, ActionInGame, che si suddivide in comando, item1 e item2.
Il metodo parse suddivide in token l'input ricevuto, e una volta eliminato le stopwords, inizializzate con la creazine del parser e contenute in un file apposito, verifica la struttura del comando, vedendo se è un comando diretto, un azione con un oggetto o un azione con 2 oggetti. Sulla base di questa informazione costruisce l'output ActionInGame, che verrà mandato al nextMove implementato dalla classe che eredità da Game.

## Item

La classe 'Item' rappresenta gli oggetti presenti nell'avventura testuale, dotati di attributi per indicare come il giocatore può effettivamente interagire con essi. Ad esempio, pickupable indica che l'oggetto è rompibile, turnable che si può accendere e così via. È inoltre dotato di una descrizione da mostrare al giocatore e di una lista di alias, nonché nomi alternativi per riferirsi ad esso.  

## Room

La classe 'Room' rappresenta le stanze attraverso le quali il giocatore può muoversi. Sono dotate di attributi per tenere traccia delle stanze presenti nelle quattro direzioni ovest, est, sud e nord rispetto ad essa, ed eventualmente anche di una stanza ad un piano superiore o inferiore. 

È presente una lista per contenere gli oggetti presenti nella stanza con cui il giocatore può interagire.
È inoltre presente una lista di indovinelli a cui il giocatore verrà sottoposto. 

Anche qui abbiamo attributi che influenzano la maniera in cui l'utente può interagire con l'ambiente, come "lighted" e "locked" che indicano rispettivamente se la stanza è illuminata o bloccata. 

## Riddle

La classe 'Riddle' rappresenta gli indovinelli a cui il giocatore sarà sottoposto nel momento in cui si trova in una determinata stanza del gioco. 

È ovviamente dotata di una descrizione da mostrare al giocatore e di un attributo 'targetItem' che indica l'oggetto che fa attivare l'enigma. L'attributo 'counter' invece indica quante volte si è tentato di risolvere l'enigma, così come la 'blackList' indica l'elenco di comandi disattivati come conseguenza della presenza dell'enigma. 

## Command & CommandType

La classe 'Command' rappresenta i comandi impartibili dall'utente. È dotata semplicemente di un insieme di nomi, 'names', per riferirsi ad un determinato comando ed è strettamente legata alla classe enumerativa 'CommandType' in quando possiede un attributo 'type' proprio di quel tipo, necessario per identificare il comando in maniera indipendente dal nome adoperato dal giocatore.