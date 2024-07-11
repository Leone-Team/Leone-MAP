package di.uniba.leone.game;

import di.uniba.leone.gui.MsgManager;
import di.uniba.leone.parser.ActionInGame;
import di.uniba.leone.parser.Parser;
import di.uniba.leone.save.SaveManager;
import di.uniba.leone.save.Saving;
import di.uniba.leone.type.CommandType;
import java.awt.event.ActionEvent;

import java.io.File;
import java.nio.file.Paths;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;

/**
 *
 * @author feder
 */
public class Engine {

    private final Game GAME;
    private boolean matchLoaded;
    private final GameTime PLAYERTIME = new GameTime();
    private final GameMusic MIXER = new GameMusic();
    private final SaveManager MRS;
    private final Parser PARSER;
    private final MsgManager MRMSG;
    private String commandLine = "";

    public Engine(Game game) {
        game.init();
        PARSER = new Parser("./src/main/resources/leone_game/stopwords.txt");
        this.GAME = game;
        this.MRMSG = game.getMrMsg();
        this.MRS = new SaveManager(Paths.get("").toAbsolutePath().toString(), MRMSG);
        ImageIcon windowIcon = new ImageIcon("./src/main/resources/img/LeoneIcona.png");
        game.getMainWindow().setIconImage(windowIcon.getImage());
    }

    public void execute() {

        Action sendMessageAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MRMSG.enterMsg();
            }
        };
        GAME.getMainWindow().getInputField().addActionListener(sendMessageAction);
        GAME.getMainWindow().getEnterBtn().addActionListener(sendMessageAction);

        MRS.connectToServer();
        if (!MRS.getUsername().contentEquals("notLogged")) {
            PLAYERTIME.setNickname(MRS.getUsername());
        }

        MRMSG.displayMsg(" ");
        MRMSG.displayMsg(">Nuova Partita; \n>Carica Partita; \n>Esci.");
        MRMSG.displayMsg(" ");

        while (GAME.isRunning()) {
            commandLine = MRMSG.getMsg();
            ActionInGame action = PARSER.parse(commandLine, GAME.getItems(), GAME.getCommands());

            if (action == null || action.getCommand() == null) {
                MRMSG.displayMsg(">Non ho capito");
            } else if (action.getCommand() != null && action.getCommand().getType() == CommandType.NEW_GAME && !matchLoaded) {
                MRS.setLoadedMatch(new File(MRS.newGame()));
                matchLoaded = true;
                PLAYERTIME.start();

                MRMSG.displayMsg("Inizio partita\n");
                MIXER.startMusic();

                MRMSG.displayMsg(GAME.getWelcomeMessage());
                MRMSG.displayMsg("Ti trovi qui:");
                MRMSG.displayMsg(GAME.getCurrentRoom().getName());
                MRMSG.displayMsg("================================================");
                MRMSG.displayMsg(GAME.getCurrentRoom().getDescription());
                GAME.nextMove(action);
            } else if (action.getCommand() != null && action.getCommand().getType() == CommandType.LOAD && !matchLoaded) {
                MRS.recoveryFromServer();
                MRMSG.displayMsg("\n");
                MRS.setLoadedMatch(new File(MRS.loadMatch(GAME)));
                matchLoaded = true;
                PLAYERTIME.start();

                MRMSG.displayMsg(">Inizio partita\n");
                MIXER.startMusic();
                MRMSG.displayMsg(GAME.getWelcomeMessage());
                MRMSG.displayMsg(">Ti trovi qui:");
                MRMSG.displayMsg(GAME.getCurrentRoom().getName());
                MRMSG.displayMsg("================================================");
                MRMSG.displayMsg(GAME.getCurrentRoom().getDescription());
                GAME.nextMove(action);
            } else if (action.getCommand() != null && action.getCommand().getType() == CommandType.QUIT) {
                Boolean pass = true;
                MIXER.stopMusic();
                PLAYERTIME.stop();
                MRMSG.displayMsg(">Vuoi salvare?\n?>");
                do {

                    switch (MRMSG.getMsg().toLowerCase()) {
                        case "si" -> {
                            Saving s = new Saving(GAME.getObsAttached(), GAME.getItems(), GAME.getRooms(), GAME.getInventory(), GAME.getRiddles(), GAME.getCurrentRoom(), PLAYERTIME);
                            MRS.saveMatch(s);
                            MRS.backUpServer();
                            if (!MRS.getUsername().contentEquals("notLogged")) {
                                MRS.addPlayerToGlobalRanking(PLAYERTIME);
                            }
                            MRMSG.displayMsg("\n");
                        }
                        case "no" ->
                            MRMSG.displayMsg(">Non sei Leone il cane fifone, sei solo un fifone!");
                        default ->
                            pass = false;
                    }
                } while (!pass);

                if (MRS.getUsername().contentEquals("notLogged")) {
                    PLAYERTIME.showScore(MRMSG);
                } else {
                    Ranking ranking = new Ranking();
                    MRS.addPlayerToGlobalRanking(PLAYERTIME);
                    ranking = MRS.getGlobalRanking();
                    ranking.classification(MRMSG);
                }
                MRMSG.displayMsg(">Arrivederci.");
                GAME.setRunning(false);
                break;
            } else if (action.getCommand() != null && action.getCommand().getType() == CommandType.SAVE && matchLoaded) {
                //salvataggio
                Saving s = new Saving(GAME.getObsAttached(), GAME.getItems(), GAME.getRooms(), GAME.getInventory(), GAME.getRiddles(), GAME.getCurrentRoom(), PLAYERTIME);
                MRS.saveMatch(s);
            } else if (action.getCommand() != null && action.getCommand().getType() == CommandType.HELP && matchLoaded) {
                MRMSG.displayMsg(">I comandi possibili sono i seguenti:");
                MRMSG.displayMsg("-salva, permette di salvare la partita;");
                MRMSG.displayMsg("-n, s, e, w, sali, scendi, e sinonimi, permettono di muoversi tra le stanze;");
                MRMSG.displayMsg("-inventario, inv, mostra gli oggetti raccolti;");
                MRMSG.displayMsg("-accendi/spegni, con oggetti adatto;");
                MRMSG.displayMsg("-prendi, permette di raccogliere un oggetto;");
                MRMSG.displayMsg("-osserva, permette di osservare in dettaglio la stanza;");
                MRMSG.displayMsg("-usa, permette di usare un oggetto;");
                MRMSG.displayMsg("-rompi, permette di rompere un oggetto specificando lo strumento da usare;");
                MRMSG.displayMsg("-apri/chiudi, con oggetti che contengono altri oggetti;");
                MRMSG.displayMsg("-indossa, con oggetti adatti;");
                MRMSG.displayMsg("-esci, chiude la partita.");
            } else if (GAME.isRunning()) {
                GAME.nextMove(action);
                if (GAME.isWin()) {
                    PLAYERTIME.setWin(true);
                    MIXER.stopMusic();
                    PLAYERTIME.stop();
                    MRS.connectToServer();
                    if (MRS.getUsername().contentEquals("notLogged")) {
                        PLAYERTIME.showScore(MRMSG);
                    } else {
                        Ranking ranking = new Ranking();
                        MRS.addPlayerToGlobalRanking(PLAYERTIME);
                        ranking = MRS.getGlobalRanking();
                        ranking.classification(MRMSG);
                    }
                    MRMSG.displayMsg(">La tua avventura termina qui! Complimenti!");
                    GAME.setRunning(false);
                    if (MRS.getLoadedMatch().exists()) {
                        MRS.delete(MRS.getLoadedMatch());
                    }
                }
            }

        }

    }

    public static void main(String[] args) {
        Game game = new LeoneGame();
        Engine engine = new Engine(game);

        engine.execute();
    }

}
