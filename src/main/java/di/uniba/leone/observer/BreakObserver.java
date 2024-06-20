
package di.uniba.leone.observer;

import di.uniba.leone.game.Game;
import di.uniba.leone.game.GameObserver;
import di.uniba.leone.parser.ActionInGame;
import di.uniba.leone.type.CommandType;
import di.uniba.leone.type.Item;

    public class BreakObserver implements GameObserver {

    @Override
    public String update(Game game, ActionInGame actioningame) {
        StringBuilder msg = new StringBuilder();
        
        if (actioningame.getCommand().getType() == CommandType.BREAK) {
            Item item1 = actioningame.getItem1();
            Item item2 = actioningame.getItem2(); 
            if (item1 != null && item2 != null) {
                if(item1.isBreakable()==true) {
                    if(item2.isUsable() == true) {
                switch (item2.getFirstName()) {
                    case "cuscino": {
                           msg.append("E tu pensavi davvero di rompere ").append(actioningame.getItem1().getFirstName()).append(" con un cuscino? Patetico... \n");
                        break;
                    }
                    case "mazza": {
                        if(game.getCurrentRoom().getName() == "Camera da Letto" && item1.getFirstName() == "sveglia") {
                            if(game.getItemByID(item1.getID()).isBroken() == false) {
                            game.getCurrentRoom().setLocked(false);
                            msg.append("Accipicchia che colpo! Finalmente ti sei sbarazzato di quel rumore assordante! \n");
                            game.getItemByID(item1.getID()).setBroken(true);
                        } else {
                            msg.append("Che astuzia, così adesso non sei più costretto a sopportare Rick Astley per non sentire la sveglia... Ben fatto! \n");
                            } 
                        } else {
                            msg.append("Wooo, calma con quella mazza... Non vorrai mica spaccare tutto eh? \n");
                        }
                        break;
                    }
                }
               } else {
                        msg.append("Ma non puoi usare ").append(actioningame.getItem2().getFirstName()).append(" per rompere qualcosa... \n");
                    }
              } else {
                    msg.append("Guarda che ").append(actioningame.getItem1().getFirstName()).append(" non si può rompere... \n");
                }
            } else {
                msg.append("Non puoi rompere nulla qui. \n");
            }
        }
        return msg.toString();
    }
}
