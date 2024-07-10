package di.uniba.leone.game;

import di.uniba.leone.gui.MsgManager;
import di.uniba.leone.gui.Window;
import di.uniba.leone.parser.ActionInGame;
import di.uniba.leone.parser.Parser;
import di.uniba.leone.save.SaveManager;
import di.uniba.leone.save.Saving;
import di.uniba.leone.type.CommandType;
import java.awt.event.ActionEvent;
import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;

/**
 *
 * @author feder
 */
public class Engine {

    private final SaveManager mrS;
    private final Game game;
    private final Parser parser;
    private final MsgManager mrMsg;
    private String commandLine = "";

    public Engine(Game game) {
        game.init();
        parser = new Parser("./leone_game/stopwords.txt");
        this.game = game;
        this.mrMsg = game.getMrMsg();
        this.mrS = new SaveManager(Paths.get("").toAbsolutePath().toString(), mrMsg);

        // Imposta l'icona della finestra
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

        GameTime timeGame = new GameTime();
        boolean matchLoaded = false;
        List<GameTime> players = new ArrayList<>();

        mrS.connectToServer();
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
                timeGame.start();

                mrMsg.displayMsg("Inizio partita");
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
                timeGame.start();

                mrMsg.displayMsg(">Inizio partita");
                mrMsg.displayMsg(">Muoviti con n, s, e, w, u, d (nord, sud, est, ovest, sali, scendi);");
                mrMsg.displayMsg(">Osserva la stanza con : osserva;");
                mrMsg.displayMsg(">Per altri aiuti inserisci:help.");
                mrMsg.displayMsg(" ");
                mrMsg.displayMsg(">Ti trovi qui:");
                mrMsg.displayMsg(game.getCurrentRoom().getName());
                mrMsg.displayMsg("================================================");
                mrMsg.displayMsg(game.getCurrentRoom().getDescription());
                game.nextMove(action);
            } else if (action.getCommand() != null && action.getCommand().getType() == CommandType.QUIT && matchLoaded) {
                Boolean pass = true;
                mrMsg.displayMsg(">Vuoi salvare?\n?>");
                do {
                    switch (mrMsg.getMsg().toLowerCase()) {
                        case "si" -> {
                            Saving s = new Saving(game.getObsAttached(), game.getItems(), game.getRooms(), game.getInventory(), game.getRiddles(), game.getCurrentRoom());
                            mrS.saveMatch(s);
                            mrS.backUpServer();
                            mrMsg.displayMsg("\n");
                            mrMsg.displayMsg(">Arrivederci.");
                        }
                        case "no" ->
                            mrMsg.displayMsg(">Non sei Leone il cane fifone, se solo un fifone, addio!");
                        default ->
                            pass = false;
                    }
                } while (!pass);
                game.setRunning(false);
                break;
            } else if (action.getCommand() != null && action.getCommand().getType() == CommandType.SAVE && matchLoaded) {
                //salvataggio
                Saving s = new Saving(game.getObsAttached(), game.getItems(), game.getRooms(), game.getInventory(), game.getRiddles(), game.getCurrentRoom());
                mrS.saveMatch(s);
            } else if (action.getCommand() != null && action.getCommand().getType() == CommandType.HELP && matchLoaded)   {
                mrMsg.displayMsg(">I comandi possibili sono i seguenti:");
                    mrMsg.displayMsg("\t salva, permette di salvare la partita");
                    mrMsg.displayMsg("\t n, s, e, w, sali, scendi, e sinonimi, permettono di muoversi tra le stanze;");
                    mrMsg.displayMsg("\t inventario, inv, mostra gli oggetti raccolti");
                    mrMsg.displayMsg("\t accendi/spegni, con oggetti adatto");
                    mrMsg.displayMsg("\t prendi, permette di raccogliere un oggetto");
                    mrMsg.displayMsg("\t osserva, permette di osservare in dettaglio la stanza");
                    mrMsg.displayMsg("\t usa, permette di usare un oggetto");
                    mrMsg.displayMsg("\t rompi, permette di rompere un oggetto specificando lo strumento da usare");
                    mrMsg.displayMsg("\t apri/chiudi, con oggetti che contengono altri oggetti");
                    mrMsg.displayMsg("\t indossa, con oggetti adatti");
            } else if (matchLoaded) {
                game.nextMove(action);
                if (game.getCurrentRoom() == null) {
                    timeGame.stop();
                    mrMsg.displayMsg(">La tua avventura termina qui! Complimenti!");
                    game.setRunning(false);
                    mrS.delete(mrS.getLoadedMatch());
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
