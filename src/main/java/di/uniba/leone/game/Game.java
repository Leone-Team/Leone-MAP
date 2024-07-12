/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package di.uniba.leone.game;

import di.uniba.leone.gui.MsgManager;
import di.uniba.leone.parser.ActionInGame;
import di.uniba.leone.type.Command;
import di.uniba.leone.type.CommandType;
import di.uniba.leone.type.Item;
import di.uniba.leone.type.Room;
import di.uniba.leone.type.Riddle;
import di.uniba.leone.gui.Window;
import di.uniba.leone.observer.BreakObserver;
import di.uniba.leone.observer.DropObserver;
import di.uniba.leone.observer.InventoryObserver;
import di.uniba.leone.observer.LookObserver;
import di.uniba.leone.observer.MoveObserver;
import di.uniba.leone.observer.OpenObserver;
import di.uniba.leone.observer.PickUpObserver;
import di.uniba.leone.observer.TurnObserver;
import di.uniba.leone.observer.UseObserver;
import di.uniba.leone.observer.wearObserver;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;

public abstract class Game {

    private Window mainWindow;
    private MsgManager mrMsg;
    private Set<GameObserver> obsAttached = new HashSet();
    private Map<GameObserver, Set<CommandType>> observers = new HashMap();
    private Properties dbprop = new Properties();
    private HashMap<Integer, Item> items = new HashMap();
    private HashMap<String, Room> rooms = new HashMap();
    private HashMap<Integer, Riddle> riddles = new HashMap();
    private List<Command> commands = new ArrayList();
    private Set<Integer> inventory = new HashSet();
    private Room currentRoom;
    private GameTime stopWatch = new GameTime();
    private boolean running;
    private boolean win;

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

    public MsgManager getMrMsg() {
        return mrMsg;
    }

    public Window getMainWindow() {
        return mainWindow;
    }

    public GameTime getStopWatch() {
        return stopWatch;
    }

    public boolean isWin() {
        return win;
    }

    public Properties getDbprop() {
        return dbprop;
    }

    public Set<GameObserver> getObsAttached() {
        return obsAttached;
    }

    public Map<GameObserver, Set<CommandType>> getObservers() {
        return observers;
    }

    public void setWin(boolean win) {
        this.win = win;
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

    public void setObsAttached(HashMap<GameObserver, Boolean> obsStatus) {
        Set<GameObserver> obsAttached = new HashSet<>();
        for(GameObserver o:obsStatus.keySet()){
            if(obsStatus.get(o)){
                obsAttached.add(o);
            }
        }
        this.obsAttached = obsAttached;
    }

    public void setItems(HashMap<Integer, Item> items) {
        this.items = items;
    }

    public void setRooms(HashMap<String, Room> rooms) {
        this.rooms = rooms;
    }

    public void setInventory(Set<Integer> inventory) {
        this.inventory = inventory;
    }

    public void setRiddles(HashMap<Integer, Riddle> riddles) {
        this.riddles = riddles;
    }

    public void setMrMsg(MsgManager mrMsg) {
        this.mrMsg = mrMsg;
    }

    public void setMainWindow(Window mainwWindow) {
        this.mainWindow = mainwWindow;
    }

    public void setStopWatch(GameTime stopWatch) {
        this.stopWatch = stopWatch;
    }

    public void setObservers(Set<GameObserver> obs) {
        HashMap<GameObserver, Set<CommandType>> observers = new HashMap();
        for (GameObserver o : obs) {
            if (o instanceof LookObserver) {
                observers.put(o, new HashSet(Arrays.asList(CommandType.LOOK)));
            }

            if (o instanceof BreakObserver) {
                observers.put(o, new HashSet(Arrays.asList(CommandType.BREAK)));
            }

            if (o instanceof InventoryObserver) {
                observers.put(o, new HashSet(Arrays.asList(CommandType.INVENTORY)));
            }

            if (o instanceof MoveObserver) {
                observers.put(o, new HashSet(Arrays.asList(CommandType.EAST, CommandType.NORTH, CommandType.SOUTH, CommandType.WEST, CommandType.GO_DOWN, CommandType.GO_UP)));
            }

            if (o instanceof OpenObserver) {
                observers.put(o, new HashSet(Arrays.asList(CommandType.OPEN)));
            }

            if (o instanceof PickUpObserver) {
                observers.put(o, new HashSet(Arrays.asList(CommandType.PICK_UP)));
            }

            if (o instanceof TurnObserver) {
                observers.put(o, new HashSet(Arrays.asList(CommandType.TURN_ON, CommandType.TURN_OFF, CommandType.WEAR)));
            }

            if (o instanceof UseObserver) {
                observers.put(o, new HashSet(Arrays.asList(CommandType.USE)));
            }

            if (o instanceof DropObserver) {
                observers.put(o, new HashSet(Arrays.asList(CommandType.DROP)));
            }

            if (o instanceof wearObserver) {
                observers.put(o, new HashSet(Arrays.asList(CommandType.WEAR)));
            }
        }
        this.observers = observers;
    }
}
