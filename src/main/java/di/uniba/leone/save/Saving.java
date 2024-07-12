/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package di.uniba.leone.save;

import di.uniba.leone.game.GameObserver;
import di.uniba.leone.game.GameTime;
import di.uniba.leone.type.Item;
import di.uniba.leone.type.Riddle;
import di.uniba.leone.type.Room;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author feder
 */
public class Saving implements Serializable{
    private static final long serialVersionUID = 1L;
    
    private HashMap<GameObserver, Boolean> obsStatus = new HashMap();
    private HashMap<Integer, Item> items = new HashMap();
    private HashMap<String, Room> rooms = new HashMap();
    private Set<Integer> inventory = new HashSet();
    private HashMap<Integer, Riddle> riddles = new HashMap();
    private Room currentRoom = new Room();
    private GameTime playerTime;
    
    public Saving(Set<GameObserver> observers,Set<GameObserver> obsAttached, HashMap<Integer, Item> items, HashMap<String, Room> rooms, Set<Integer> inventory, HashMap<Integer, Riddle> riddles, Room currentRoom, GameTime playerTime){
        
        for(GameObserver o:observers){
            if(obsAttached.contains(o)){
                obsStatus.put(o, Boolean.TRUE);
            }else{
                obsStatus.put(o, Boolean.FALSE);
            }
        }
        this.items.putAll(items);
        this.rooms.putAll(rooms);
        this.inventory.addAll(inventory);
        this.riddles.putAll(riddles);
        this.currentRoom = currentRoom;
        this.playerTime = playerTime;
    }

    public Saving(){
    }

    public HashMap<GameObserver, Boolean> getObsStatus() {
        return obsStatus;
    }

    public HashMap<Integer, Item> getItems() {
        return items;
    }

    public HashMap<String, Room> getRooms() {
        return rooms;
    }

    public Set<Integer> getInventory() {
        return inventory;
    }

    public HashMap<Integer, Riddle> getRiddles() {
        return riddles;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public GameTime getPlayerTime() {
        return playerTime;
    }
    
    
    
    
}
