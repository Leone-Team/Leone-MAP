/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package di.uniba.leone.game;

/**
 *
 * @author feder
 */
public interface GameObservable {
    
    public void detach(GameObserver o);
    
    public void attach(GameObserver o);
    
    public void notifyObservers();
}
