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
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Properties;

public abstract class Game {
    
    private Properties dbprop = new Properties(); 
    private HashMap<Integer, Item> items = new HashMap();
    private HashMap<String, Room> rooms = new HashMap();
    private List<Command> commands = new ArrayList();
    private Set<Integer> inventory = new HashSet();
    private HashMap<Integer, Riddle> riddles = new HashMap();
    private Room currentRoom;
    private boolean running;
    
    private File game;
    
    public abstract String getWelcomeMessage();
    
    public abstract void init(); //inizializza il gioco
    
    public abstract void nextMove(ActionInGame act); //esegue l'azione in accordo con l'output dell'observer
    
    public abstract void checkRiddles();//verifica la presenza di indovinelli nella stanza, avviandoli
    
    public List<Command> getCommands() {
        return commands;
    }

    public Set<Integer> getInventory() {
        return inventory;
    }
 
    public Item getItemByID(Integer id) {
        return items.get(id);
    }

    public HashMap<Integer, Riddle> getRiddles() {
        return riddles;
    }
    
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

    public void setRunning(boolean running) {
        this.running = running;
    }

    public boolean isRunning() {
        return running;
    }
    
    public void setDbProperties(String user, String password) {
        dbprop.setProperty("user", user);
        dbprop.setProperty("password", password);
    }

    public Properties getDbprop() {
        return dbprop;
    }
}