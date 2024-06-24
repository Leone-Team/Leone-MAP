/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package di.uniba.leone.game;

import di.uniba.leone.observer.BreakObserver;
import di.uniba.leone.observer.DropObserver;
import di.uniba.leone.observer.InventoryObserver;
import di.uniba.leone.observer.LookObserver;
import di.uniba.leone.observer.MoveObserver;
import di.uniba.leone.observer.OpenObserver;
import di.uniba.leone.observer.PickUpObserver;
import di.uniba.leone.observer.TurnObserver;
import di.uniba.leone.observer.UseObserver;
import di.uniba.leone.parser.ActionInGame;
import di.uniba.leone.type.Item;
import di.uniba.leone.type.Container;
import di.uniba.leone.type.Room;
import di.uniba.leone.type.Command;
import di.uniba.leone.type.CommandType;
import di.uniba.leone.type.ItemRiddle;
import di.uniba.leone.type.QuestionRiddle;
import di.uniba.leone.type.Riddle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 *
 * @author feder
 */
public class LeoneGame extends Game implements GameObservable {

    private Set<GameObserver> obsAttached = new HashSet();
    private Map<GameObserver, Set<CommandType>> observers = new HashMap();
    private final List<String> messages = new ArrayList<>();
    private ActionInGame action;
    
    @Override
    public void init() {
        Properties dbprop = new Properties();
        dbprop.setProperty("user", "Leone");
        dbprop.setProperty("password", "1234");

        //inizializza oggetti
        try (Connection conn = DriverManager.getConnection("jdbc:h2:./leone_game/dbs/item", dbprop)) {
            PreparedStatement pstm = conn.prepareStatement("SELECT id, names, desc, peakable, useable, turnable, breakable, openable, broken, turned_on, container, items_con FROM item");
            pstm.executeQuery();
            ResultSet rs = pstm.executeQuery();

            Item item;
            while (rs.next()) {
                if (rs.getBoolean("container") == true) {
                    item = new Container(rs.getInt("id"), rs.getString("desc"), rs.getString("names").replace(" ", ""), rs.getBoolean("peakable"), rs.getBoolean("useable"), rs.getBoolean("turnable"), rs.getBoolean("breakable"), rs.getBoolean("openable"), rs.getBoolean("broken"), rs.getBoolean("turned_on"), rs.getString("items_con").replace(" ", ""));
                } else {
                    item = new Item(rs.getInt("id"), rs.getString("desc"), rs.getString("names"), rs.getBoolean("peakable"), rs.getBoolean("useable"), rs.getBoolean("turnable"), rs.getBoolean("breakable"), rs.getBoolean("broken"), rs.getBoolean("turned_on"));
                }
                getItems().put(item.getID(), item);
            }
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        //inizializza stanze
        try (Connection conn = DriverManager.getConnection("jdbc:h2:./leone_game/dbs/rooms", dbprop)) {
            PreparedStatement pstm = conn.prepareStatement("SELECT  name, id_items, lighted, locked, desc FROM rooms");
            ResultSet rs = pstm.executeQuery();

            Room room;
            ArrayList<Integer> items_stored = new ArrayList();
            while (rs.next()) {
                room = new Room(rs.getString("name"), rs.getBoolean("lighted"), rs.getBoolean("locked"), rs.getString("desc"));
                String[] items = rs.getString("id_items").replace(" ", "").split(",");
                
                for (String id_item : items) {
                    if(id_item.compareTo("") != 0)
                        items_stored.add(Integer.valueOf(id_item));
                }
                room.setItems(items_stored);
                items_stored.clear();

                getRooms().put(room.getName(), room);
            }
            pstm.close();
            rs.close();
            
            pstm = conn.prepareStatement("SELECT  name, east, west, north, south, up, down FROM rooms");
            rs = pstm.executeQuery();
            
            String name;
            while (rs.next()) {
                name = rs.getString("name");
                getRooms().get(name).setNorthR(getRooms().get(rs.getString("north")));
                getRooms().get(name).setEastR(getRooms().get(rs.getString("east")));
                getRooms().get(name).setWestR(getRooms().get(rs.getString("west")));
                getRooms().get(name).setSouthR(getRooms().get(rs.getString("south")));
                getRooms().get(name).setUpR(getRooms().get(rs.getString("up")));
                getRooms().get(name).setDownR(getRooms().get(rs.getString("down")));
            }
            pstm.close();
            rs.close();
            conn.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        
        //inizializza comandi
        
        try(Connection conn = DriverManager.getConnection("jdbc:h2:./leone_game/dbs/commands", dbprop)){
            
            PreparedStatement pstm = conn.prepareStatement("SELECT  type, names FROM commands");
            ResultSet rs = pstm.executeQuery();
            
            Set<String> names = new HashSet();
            while(rs.next()){
                try{
                    names.addAll(Arrays.asList(rs.getString("names").toLowerCase().replace(" ", "").split(",")));
                    getCommands().add(new Command(CommandType.valueOf(rs.getString("type")), names));
                    names.clear();
                }catch(IllegalArgumentException ex){
                    System.out.println("Parola non valida:"+rs.getString("type"));
                }
            }
            pstm.close();
            rs.close();
            conn.close();
        }catch(SQLException ex){
            System.out.println(ex.getMessage());
        }

        //istanziare gli indovinelli
        try(Connection conn = DriverManager.getConnection("jdbc:h2:./leone_game/dbs/riddles", dbprop)){
            
            PreparedStatement pstm = conn.prepareStatement("SELECT  * FROM riddles");
            ResultSet rs = pstm.executeQuery();
            
            Set<CommandType> blacklist;
            while(rs.next()){
                try{
                    blacklist = new HashSet();
                    for(String type:rs.getString("blackList").replace(" ", "").split(",")){
                        if(!type.isEmpty())
                            blacklist.add(CommandType.valueOf(type));
                    }
                    if (rs.getBoolean("itemRiddle")){
                        getRiddles().put(rs.getInt("id"), new ItemRiddle(rs.getInt("id"), blacklist, rs.getString("desc"), rs.getBoolean("solved"), rs.getInt("targetItem"), rs.getInt("tool")));
                    }else{
                        getRiddles().put(rs.getInt("id"), new QuestionRiddle(rs.getInt("id"), blacklist, rs.getString("desc"), rs.getBoolean("solved"), rs.getInt("targetItem"), rs.getString("question"), rs.getString("answer")));  
                    }
                    getRooms().get(rs.getString("room")).addRiddle(rs.getInt("id"));
                }catch(IllegalArgumentException ex){
                    System.out.println(ex.getMessage());
                }
            }
            pstm.close();
            rs.close();
            conn.close();
        }catch(SQLException ex){
            System.out.println(ex.getMessage());
        }
        
        //collega gli observer
        GameObserver obs;
        
        obs = new LookObserver();
        attach(obs);
        this.observers.put(obs, new HashSet(Arrays.asList(CommandType.LOOK)));
        
        obs = new BreakObserver();
        attach(obs);
        this.observers.put(obs, new HashSet(Arrays.asList(CommandType.BREAK)));
        
        obs = new InventoryObserver();
        attach(obs);
        this.observers.put(obs, new HashSet(Arrays.asList(CommandType.INVENTORY)));
        
        obs = new MoveObserver();
        attach(obs);
        this.observers.put(obs, new HashSet(Arrays.asList(CommandType.EAST, CommandType.NORTH, CommandType.SOUTH, CommandType.WEST, CommandType.GO_DOWN, CommandType.GO_UP)));
        
        obs = new OpenObserver();
        attach(obs);
        this.observers.put(obs, new HashSet(Arrays.asList(CommandType.OPEN)));
        
        obs = new PickUpObserver();
        attach(obs);
        this.observers.put(obs, new HashSet(Arrays.asList(CommandType.PICK_UP)));
        
        obs = new TurnObserver();
        attach(obs);
        this.observers.put(obs, new HashSet(Arrays.asList(CommandType.TURN_ON, CommandType.TURN_OFF, CommandType.WEAR)));
        
        obs = new UseObserver();
        attach(obs);
        this.observers.put(obs, new HashSet(Arrays.asList(CommandType.USE)));
        
        obs = new DropObserver();
        attach(obs);
        this.observers.put(obs, new HashSet(Arrays.asList(CommandType.DROP)));
        //istanzia la room attuale
        setCurrentRoom(getRooms().get("Camera da Letto"));
    }
    
    @Override
    public void nextMove(ActionInGame act) {
        action = act;
        messages.clear();
        
        Room cr = getCurrentRoom();
        
        notifyObservers();
        boolean move = !cr.equals(getCurrentRoom()) && getCurrentRoom() != null;
        if (!messages.isEmpty()) {
            for (String m : messages) {
                if (m.length() > 0) {
                    System.out.println(m);
                }
            }
        }else{
            System.out.println(">Comando non valido");
        }

        //gestione possibile indovinello ItemRiddle
        if (move) {
            System.out.println(getCurrentRoom().getName());
            System.out.println("================================================");
            System.out.println(getCurrentRoom().getDescription());
        }
        checkRiddles();
    }

    @Override
    public void checkRiddles() {
        for(Integer id_riddle:getCurrentRoom().getRiddles()){
            if(getRiddles().get(id_riddle) instanceof ItemRiddle itemRiddle)
                    itemRiddle.resolved(getItems());
            if(!getRiddles().get(id_riddle).isSolved())
                System.out.println(getRiddles().get(id_riddle).getDescription());
            for(GameObserver o: observers.keySet()){
                if(observers.get(o).stream().anyMatch(getRiddles().get(id_riddle).getBlackList() :: contains) && !getRiddles().get(id_riddle).isSolved())
                    detach(o);
                else
                    attach(o);
            
            }
        }
    }

    @Override
    public void attach(GameObserver o) {
        if (!obsAttached.contains(o))
            obsAttached.add(o);
    }

    @Override
    public void detach(GameObserver o) {
        obsAttached.remove(o);
    }

    @Override
    public void notifyObservers() {
        for (GameObserver o : obsAttached) {
                messages.add(o.update(this, action));
            }
    }

    @Override
    public String getWelcomeMessage() {
        return "Buongiorno Leone, la casa è impazzita, sta a te spegnerla e liberare Marilu!";
    }
    
    
    
}
