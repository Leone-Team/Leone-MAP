package di.uniba.leone.game;

import di.uniba.leone.gui2.MsgManager;
import di.uniba.leone.gui2.Window;
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
import java.util.Scanner;
import javax.swing.AbstractAction;
import javax.swing.Action;

/**
 *
 * @author feder
 */
public class Engine {

    Scanner scanner = new Scanner(System.in);
    private final SaveManager mrS;
    private final Game game;
    private final Parser parser;
    private final MsgManager mrMsg;
    private String commandLine = "";

    public Engine(Game game) {
        this.mrS = new SaveManager(Paths.get("").toAbsolutePath().toString(), scanner);
        this.game = game;
        game.init();
        mrMsg = game.getMrMsg();
        parser = new Parser("./leone_game/stopwords.txt");
    }

    public void execute() {
        GameTime timeGame = new GameTime();
        boolean matchLoaded = false;
        List<GameTime> players = new ArrayList<>();

        //mrS.connectToServer();
        System.out.println("");
        System.out.println(">Nuova Partita; \n>Carica Partita; \n>Esci.");
        System.out.println("?>");

        while (game.isRunning()) {
            commandLine = scanner.nextLine();
            ActionInGame action = parser.parse(commandLine, game.getItems(), game.getCommands());

            if (action == null || action.getCommand() == null) {
                System.out.println(">Non ho capito");
            } else if (action.getCommand() != null && action.getCommand().getType() == CommandType.NEW_GAME && !matchLoaded) {
                mrS.setLoadedMatch(new File(mrS.newGame()));
                matchLoaded = true;
                timeGame.start();

                System.out.println("Inizio partita");
                System.out.println(game.getWelcomeMessage());
                System.out.println("Ti trovi qui:");
                System.out.println(game.getCurrentRoom().getName());
                System.out.println("================================================");
                System.out.println(game.getCurrentRoom().getDescription());
                game.nextMove(action);
            } else if (action.getCommand() != null && action.getCommand().getType() == CommandType.LOAD && !matchLoaded) {
                mrS.recoveryFromServer();
                System.out.println("");
                mrS.setLoadedMatch(new File(mrS.loadMatch(game)));
                matchLoaded = true;
                timeGame.start();

                System.out.println("Inizio partita");
                System.out.println("Ti trovi qui:");
                System.out.println(game.getCurrentRoom().getName());
                System.out.println("================================================");
                System.out.println(game.getCurrentRoom().getDescription());
                game.nextMove(action);
            } else if (action.getCommand() != null && action.getCommand().getType() == CommandType.QUIT && matchLoaded) {
                Boolean pass = true;
                System.out.println(">Vuoi salvare?\n?>");
                do {
                    switch (scanner.nextLine().toLowerCase()) {
                        case "si" -> {
                            Saving s = new Saving(game.getObsAttached(), game.getItems(), game.getRooms(), game.getInventory(), game.getRiddles(), game.getCurrentRoom());
                            mrS.saveMatch(s);
                            mrS.backUpServer();
                            System.out.println("");
                            System.out.println(">Arrivederci.");
                        }
                        case "no" ->
                            System.out.println("Non sei Leone il cane fifone, se solo un fifone, addio!");
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
            } else if (matchLoaded) {
                game.nextMove(action);
                if (game.getCurrentRoom() == null) {
                    timeGame.stop();
                    System.out.println("La tua avventura termina qui! Complimenti!");
                    game.setRunning(false);
                    mrS.delete(mrS.getLoadedMatch());
                }
            }

            System.out.print("?>");
        }

    }

    public static void main(String[] args) {
        Game game = new LeoneGame();
        Engine engine = new Engine(game);

        engine.execute();
    }

}
