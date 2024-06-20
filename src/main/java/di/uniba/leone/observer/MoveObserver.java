package di.uniba.leone.observer;

import di.uniba.leone.game.Game;
import di.uniba.leone.game.GameObserver;
import di.uniba.leone.parser.ActionInGame;
import di.uniba.leone.type.CommandType;

public class MoveObserver implements GameObserver {

    @Override
    public String update(Game game, ActionInGame actioningame) {
        if (actioningame.getCommand().getType() == CommandType.NORTH) {
            if (game.getCurrentRoom().getNorthR() != null) {
                game.setCurrentRoom(game.getCurrentRoom().getNorthR());
            } else {
                return "Si beh vista la situazione sbattere la testa al muro potrebbe essere una soluzione si...\n Ma cosa ti salta in mente? Vedi forse una porta?? \n";
            }
        } else if (actioningame.getCommand().getType() == CommandType.SOUTH) {
            if (game.getCurrentRoom().getSouthR() != null) {
                game.setCurrentRoom(game.getCurrentRoom().getSouthR());
            } else {
                return "Si beh vista la situazione sbattere la testa al muro potrebbe essere una soluzione si...\n Ma cosa ti salta in mente? Vedi forse una porta?? \n";
            }
        } else if (actioningame.getCommand().getType() == CommandType.EAST) {
            if (game.getCurrentRoom().getEastR() != null) {
                game.setCurrentRoom(game.getCurrentRoom().getEastR());
            } else {
                return "Si beh vista la situazione sbattere la testa al muro potrebbe essere una soluzione si...\n Ma cosa ti salta in mente? Vedi forse una porta?? \n";
            }
        } else if (actioningame.getCommand().getType() == CommandType.WEST) {
            if (game.getCurrentRoom().getWestR() != null) {
                game.setCurrentRoom(game.getCurrentRoom().getWestR());
            } else {
                return "Si beh vista la situazione sbattere la testa al muro potrebbe essere una soluzione si...\n Ma cosa ti salta in mente? Vedi forse una porta?? \n";
            }
        }
        return "";
    }
}

