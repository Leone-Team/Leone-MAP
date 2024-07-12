/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package di.uniba.leone.game;

import di.uniba.leone.type.CommandType;
import java.util.Set;

/**
 *
 * @author feder
 */
public interface GameObservable {
    
    public void detach(GameObserver o);
    
    public void attach(GameObserver o);
    
    public void notifyObservers();
}
