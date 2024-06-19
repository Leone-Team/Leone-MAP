
package di.uniba.leone.type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class Game {
    
    private final HashMap<Integer, Item> items = new HashMap();
    private final HashMap<String, Room> rooms = new HashMap();
    private final List<Command> commands = new ArrayList();
    
    private Room actualRoom;

    public HashMap<Integer, Item> getItems() {
        return items;
    }

    public HashMap<String, Room> getRooms() {
        return rooms;
    }


    public Room getActualRoom() {
        return actualRoom;
    }

    public void setActualRoom(Room actualRoom) {
        this.actualRoom = actualRoom;
    }
    
    public abstract void init(); //inizializza il gioco
    
    //public abstract void nextMove(); //esegue l'azione in accordo con l'output dell'observer
    
    
}
