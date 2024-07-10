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

    private final Game game;
    private boolean matchLoaded;
    private final GameTime playerTime = new GameTime();
    private final GameMusic mixer = new GameMusic();
    private final SaveManager mrS;
    private final Parser parser;
    private final MsgManager mrMsg;
    private String commandLine = "";

    public Engine(Game game) {
        game.init();
        parser = new Parser("./src/main/resources/leone_game/stopwords.txt");
        this.game = game;
        this.mrMsg = game.getMrMsg();
        this.mrS = new SaveManager(Paths.get("").toAbsolutePath().toString(), mrMsg);
        ImageIcon windowIcon = new ImageIcon("./src/main/resources/img/LeoneIcona.png");
        game.getMainWindow().setIconImage(windowIcon.getImage());
    }

    public void execute() {

        Action sendMessageAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mrMsg.enterMsg();
            }
        };
        game.getMainWindow().getInputField().addActionListener(sendMessageAction);
        game.getMainWindow().getEnterBtn().addActionListener(sendMessageAction);

        mrS.connectToServer();
        if (!mrS.getUsername().contentEquals("notLogged")) {
            playerTime.setNickname(mrS.getUsername());
        }

        mrMsg.displayMsg(" ");
        mrMsg.displayMsg(">Nuova Partita; \n>Carica Partita; \n>Esci.");
        mrMsg.displayMsg(" ");

        while (game.isRunning()) {
            commandLine = mrMsg.getMsg();
            ActionInGame action = parser.parse(commandLine, game.getItems(), game.getCommands());

            if (action == null || action.getCommand() == null) {
                mrMsg.displayMsg(">Non ho capito");
            } else if (action.getCommand() != null && action.getCommand().getType() == CommandType.NEW_GAME && !matchLoaded) {
                mrS.setLoadedMatch(new File(mrS.newGame()));
                matchLoaded = true;
                playerTime.start();

                mrMsg.displayMsg("Inizio partita\n");
                mixer.startMusic();

                mrMsg.displayMsg(game.getWelcomeMessage());
                mrMsg.displayMsg("Ti trovi qui:");
                mrMsg.displayMsg(game.getCurrentRoom().getName());
                mrMsg.displayMsg("================================================");
                mrMsg.displayMsg(game.getCurrentRoom().getDescription());
                game.nextMove(action);
            } else if (action.getCommand() != null && action.getCommand().getType() == CommandType.LOAD && !matchLoaded) {
                mrS.recoveryFromServer();
                mrMsg.displayMsg("\n");
                mrS.setLoadedMatch(new File(mrS.loadMatch(game)));
                matchLoaded = true;
                playerTime.start();

                mrMsg.displayMsg(">Inizio partita\n");
                mixer.startMusic();
                mrMsg.displayMsg(game.getWelcomeMessage());
                mrMsg.displayMsg(">Ti trovi qui:");
                mrMsg.displayMsg(game.getCurrentRoom().getName());
                mrMsg.displayMsg("================================================");
                mrMsg.displayMsg(game.getCurrentRoom().getDescription());
                game.nextMove(action);
            } else if (action.getCommand() != null && action.getCommand().getType() == CommandType.QUIT && matchLoaded) {
                Boolean pass = true;
                mixer.stopMusic();
                playerTime.stop();
                mrMsg.displayMsg(">Vuoi salvare?\n?>");
                do {

                    switch (mrMsg.getMsg().toLowerCase()) {
                        case "si" -> {
                            Saving s = new Saving(game.getObsAttached(), game.getItems(), game.getRooms(), game.getInventory(), game.getRiddles(), game.getCurrentRoom(), playerTime);
                            mrS.saveMatch(s);
                            mrS.backUpServer();
                            if (!mrS.getUsername().contentEquals("notLogged")) {
                                mrS.addPlayerToGlobalRanking(playerTime);
                            }
                            mrMsg.displayMsg("\n");
                        }
                        case "no" ->
                            mrMsg.displayMsg(">Non sei Leone il cane fifone, se solo un fifone!");
                        default ->
                            pass = false;
                    }
                } while (!pass);

                if (mrS.getUsername().contentEquals("notLogged")) {
                    playerTime.showScore(mrMsg);
                } else {
                    Ranking ranking = new Ranking();
                    mrS.addPlayerToGlobalRanking(playerTime);
                    ranking = mrS.getGlobalRanking();
                    ranking.classification(mrMsg);
                }
                mrMsg.displayMsg(">Arrivederci.");
                game.setRunning(false);
                break;
            } else if (action.getCommand() != null && action.getCommand().getType() == CommandType.SAVE && matchLoaded) {
                //salvataggio
                Saving s = new Saving(game.getObsAttached(), game.getItems(), game.getRooms(), game.getInventory(), game.getRiddles(), game.getCurrentRoom(), playerTime);
                mrS.saveMatch(s);
            } else if (action.getCommand() != null && action.getCommand().getType() == CommandType.HELP && matchLoaded) {
                mrMsg.displayMsg(">I comandi possibili sono i seguenti:");
                mrMsg.displayMsg("\t salva, permette di salvare la partita;");
                mrMsg.displayMsg("\t n, s, e, w, sali, scendi, e sinonimi, permettono di muoversi tra le stanze;");
                mrMsg.displayMsg("\t inventario, inv, mostra gli oggetti raccolti;");
                mrMsg.displayMsg("\t accendi/spegni, con oggetti adatto;");
                mrMsg.displayMsg("\t prendi, permette di raccogliere un oggetto;");
                mrMsg.displayMsg("\t osserva, permette di osservare in dettaglio la stanza;");
                mrMsg.displayMsg("\t usa, permette di usare un oggetto;");
                mrMsg.displayMsg("\t rompi, permette di rompere un oggetto specificando lo strumento da usare;");
                mrMsg.displayMsg("\t apri/chiudi, con oggetti che contengono altri oggetti;");
                mrMsg.displayMsg("\t indossa, con oggetti adatti;");
                mrMsg.displayMsg("\t esci, chiude la partita.");
            } else if (game.isRunning()) {
                game.nextMove(action);
                if (game.isWin()) {
                    mixer.stopMusic();
                    playerTime.stop();
                    if (mrS.getUsername().contentEquals("notLogged")) {
                        playerTime.showScore(mrMsg);
                    } else {
                        Ranking ranking = new Ranking();
                        mrS.addPlayerToGlobalRanking(playerTime);
                        ranking = mrS.getGlobalRanking();
                        ranking.classification(mrMsg);
                    }
                    mrMsg.displayMsg(">La tua avventura termina qui! Complimenti!");
                    game.setRunning(false);
                    if (mrS.getLoadedMatch().exists()) {
                        mrS.delete(mrS.getLoadedMatch());
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
