package di.uniba.leone.game;

import di.uniba.leone.type.CommandType;
import java.util.Set;

/**
 *
 * @author feder
 */
public interface GameObservable {
    
    /**
     *
     * @param o
     */
    public void detach(GameObserver o);
    
    /**
     *
     * @param o
     */
    public void attach(GameObserver o);
    
    /**
     *
     */
    public void notifyObservers();
}
