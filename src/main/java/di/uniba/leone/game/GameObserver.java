/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package di.uniba.leone.game;

import di.uniba.leone.parser.ActionInGame;
import java.io.Serializable;

/**
 *
 * @author giann
 */
public interface GameObserver {
    /**
     *
     * @param game
     * @param actioningame
     * @return
     */
    public String update(Game game, ActionInGame actioningame);

}
