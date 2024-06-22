/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package di.uniba.leone.game;

import di.uniba.leone.parser.ActionInGame;
import di.uniba.leone.type.Command;
import di.uniba.leone.type.Item;
import di.uniba.leone.type.Room;
import di.uniba.leone.type.Riddle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

public abstract class Game {
    
    private final HashMap<Integer, Item> items = new HashMap();
    private final HashMap<String, Room> rooms = new HashMap();
    private final List<Command> commands = new ArrayList();
    private final Set<Integer> inventory = new HashSet();
    private final HashMap<Integer, Riddle> riddles = new HashMap();
    private Room currentRoom;

    public abstract String getWelcomeMessage();
    
    public HashMap<Integer, Item> getItems() {
        return items;
    }

    public HashMap<String, Room> getRooms() {
        return rooms;
    }


    public Room getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(Room actualRoom) {
        this.currentRoom = actualRoom;
    }
    
    public abstract void init(); //inizializza il gioco
    
    public abstract void nextMove(ActionInGame act); //esegue l'azione in accordo con l'output dell'observer
    
    public List<Command> getCommands() {
        return commands;
    }

    public Set<Integer> getInventory() {
        return inventory;
    }
 
    public Item getItemByID(Integer id) {
        return items.get(id);
    }
}