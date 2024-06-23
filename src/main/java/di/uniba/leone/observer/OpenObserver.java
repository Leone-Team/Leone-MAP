/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package di.uniba.leone.observer;

import di.uniba.leone.game.Game;
import di.uniba.leone.game.GameObserver;
import di.uniba.leone.parser.ActionInGame;
import di.uniba.leone.type.CommandType;
import di.uniba.leone.type.Container;
import di.uniba.leone.type.Item;
import java.util.HashSet;
import java.util.Set;

public class OpenObserver implements GameObserver {

 
    
    @Override
    public String update(Game game, ActionInGame actioningame) {
        StringBuilder msg = new StringBuilder();
        if (actioningame.getCommand().getType() == CommandType.OPEN) {
            if (actioningame.getItem1() == null) {
                msg.append("Non puoi aprire nulla.");
            } else {
                Item item = actioningame.getItem1();
                if (item instanceof Container) {
                    Container container = (Container) item;
                    msg.append("Hai aperto: ").append(container.getFirstName());
                    if (!container.getItems().isEmpty()) {
                        msg.append(". ").append(container.getFirstName()).append(" contiene:");
                        for (Integer itemId : container.getItems()) {
                            Item next = game.getItemByID(itemId);
                            msg.append(" ").append(next.getFirstName());
                        }
                    } else {
                        msg.append(". ").append(container.getFirstName()).append(" è vuoto.");
                    }
                    msg.append("\n");
                } else {
                    msg.append("Non puoi aprire questo oggetto.");
                }
            }
        }
        return msg.toString();
    }
}
