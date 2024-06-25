package di.uniba.leone.observer;

import di.uniba.leone.game.Game;
import di.uniba.leone.game.GameObserver;
import di.uniba.leone.parser.ActionInGame;
import di.uniba.leone.type.CommandType;
import di.uniba.leone.type.Item;
import java.io.Serializable;

public class BreakObserver implements GameObserver, Serializable{

    private static final long serialVersionUID = 1L;
    
    @Override
    public String update(Game game, ActionInGame actioningame) {
        StringBuilder msg = new StringBuilder();

        if (actioningame.getCommand().getType() == CommandType.BREAK) {
            Item item1 = actioningame.getItem1();
            Item item2 = actioningame.getItem2();
            
            if (item1 != null && item2 != null) {
                if (item1.isBreakable() == true) {
                    if (item2.isUsable() == true) {
                        switch (item2.getFirstName()) {
                            case "cuscino" -> {
                                msg.append("E tu pensavi davvero di rompere ").append(actioningame.getItem1().getFirstName()).append(" con un cuscino? Patetico... \n");
                            }
                            case "mazza" -> {
                                if (game.getCurrentRoom().getName().contentEquals("Camera da Letto") && item1.getFirstName().contentEquals("sveglia")) {
                                    if (game.getItemByID(item1.getID()).isBroken() == false) {
                                        game.getCurrentRoom().setLocked(false);
                                        msg.append("Accipicchia che colpo! Finalmente ti sei sbarazzato di quel rumore assordante! \n");
                                        game.getItemByID(item1.getID()).setBroken(true);
                                    } else {
                                        msg.append(">Piano Rambo, hai già rotto quel che potevi! \n");
                                    }
                                } else {
                                    msg.append("Wooo, calma con quella mazza... Non vorrai mica spaccare tutto eh? \n");
                                }
                            }
                        }
                    } else {
                        msg.append("Ma non puoi usare ").append(actioningame.getItem2().getFirstName()).append(" per rompere qualcosa... \n");
                    }
                } else {
                    msg.append("Guarda che ").append(actioningame.getItem1().getFirstName()).append(" non si può rompere... \n");
                }
            } else if (item1 != null && item2 == null){
                msg.append("E con cosa, le tue mani di ricotta?. \n");
            }else{
                msg.append("Non puoi rompere nulla qui.");
            }
        }
        return msg.toString();
    }
    
    
}
