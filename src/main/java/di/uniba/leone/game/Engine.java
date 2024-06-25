/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package di.uniba.leone.game;

import di.uniba.leone.parser.ActionInGame;
import di.uniba.leone.parser.Parser;
import di.uniba.leone.type.CommandType;
import java.util.Scanner;

/**
 *
 * @author feder
 */
public class Engine {

    private final Game game;
    private final Parser parser;

    public Engine(Game game) {
        this.game = game;
        game.init();
        parser = new Parser("./leone_game/stopwords.txt");
    }

    public void execute() {
        System.out.println("Inizio partita");
        System.out.println(game.getWelcomeMessage());
        System.out.println("Ti trovi qui:");
        System.out.println(game.getCurrentRoom().getName());
        System.out.println(game.getCurrentRoom().getDescription());
        game.checkRiddles();
        System.out.print("?> ");
        Scanner scanner = new Scanner(System.in);
        
        while (game.isRunning()) {
            String commandline = scanner.nextLine();
            ActionInGame action = parser.parse(commandline, game.getItems(), game.getCommands());
            if (action == null || action.getCommand() == null) {
                System.out.println(">Non ho capito.");
            } else if (action.getCommand() != null && action.getCommand().getType() == CommandType.QUIT) {
                System.out.println("Non sei Leone il cane fifone, se solo un fifone, addio!");
                game.setRunning(false);
                break;
            } else {
                game.nextMove(action);
                if (game.getCurrentRoom() == null) {
                    System.out.println("La tua avventura termina qui! Complimenti!");
                    game.setRunning(false);
                }
            }
            System.out.print("?> ");
        }

    }

    public static void main(String[] args) {
        Game game = new LeoneGame();
        Engine engine = new Engine(game);

        engine.execute();
    }

}
