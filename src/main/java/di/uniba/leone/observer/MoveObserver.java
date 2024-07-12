package di.uniba.leone.observer;

import di.uniba.leone.game.Game;
import di.uniba.leone.game.GameObserver;
import di.uniba.leone.parser.ActionInGame;
import java.io.Serializable;

public class MoveObserver implements GameObserver, Serializable {
    private static final long serialVersionUID = 1L;
    
    @Override
    public String update(Game game, ActionInGame actioningame) {
        String msgNoDoor = "*sbam* Si beh vista la situazione sbattere la testa al muro potrebbe essere una soluzione si...\n Ma cosa ti salta in mente? Vedi forse una porta?? \n";
        String msgClosed = "*sbam* Prima di passare per una porta, forse dovresti assicurati di poterla aprire.";
        if (null != actioningame.getCommand().getType()) {
            switch (actioningame.getCommand().getType()) {
                case NORTH -> {
                    if (game.getCurrentRoom().getNorthR() != null) {
                        if (!game.getCurrentRoom().isLocked() &&  !game.getCurrentRoom().getNorthR().isLocked()) {
                            game.setCurrentRoom(game.getCurrentRoom().getNorthR());
                        } else {
                            return msgClosed;
                        }
                    } else {
                        return msgNoDoor;
                    }
                }
                case SOUTH -> {
                    if (game.getCurrentRoom().getSouthR() != null) {
                        if (!game.getCurrentRoom().isLocked() &&  !game.getCurrentRoom().getSouthR().isLocked()) {
                            game.setCurrentRoom(game.getCurrentRoom().getSouthR());
                        } else {
                            return msgClosed;
                        }
                    } else {
                        return msgNoDoor;
                    }

                }
                case EAST -> {
                    if (game.getCurrentRoom().getEastR() != null) {
                        if (!game.getCurrentRoom().isLocked() &&  !game.getCurrentRoom().getEastR().isLocked()) {
                            game.setCurrentRoom(game.getCurrentRoom().getEastR());
                        } else {
                            return msgClosed;
                        }
                    } else {
                        return msgNoDoor;
                    }

                }
                case WEST -> {
                    if (game.getCurrentRoom().getWestR() != null) {
                        if (!game.getCurrentRoom().isLocked() &&  !game.getCurrentRoom().getWestR().isLocked()) {
                            game.setCurrentRoom(game.getCurrentRoom().getWestR());
                        } else {
                            return msgClosed;
                        }
                    } else {
                        return msgNoDoor;
                    }

                }
                
                case GO_UP ->{
                    if (game.getCurrentRoom().getUpR() != null) {
                        if (!game.getCurrentRoom().isLocked() &&  !game.getCurrentRoom().getUpR().isLocked()){
                            game.setCurrentRoom(game.getCurrentRoom().getUpR());
                        } else {
                            return msgClosed;
                        }
                    } else {
                        return msgNoDoor;
                    }
                }

                case GO_DOWN ->{
                    if (game.getCurrentRoom().getDownR()!= null) {
                        if (!game.getCurrentRoom().isLocked() &&  !game.getCurrentRoom().getDownR().isLocked()){
                            game.setCurrentRoom(game.getCurrentRoom().getDownR());
                        } else {
                            return msgClosed;
                        }
                    } else {
                        return msgNoDoor;
                    }
                }
            }
        }
        return "";
    }
}
