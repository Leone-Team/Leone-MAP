/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package di.uniba.leone.game;

import di.uniba.leone.observer.BreakObserver;
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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 *
 * @author feder
 */
public class LeoneGame extends Game implements GameObservable {

    private Set<GameObserver> observers = new HashSet();
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
        
        //istanzia la room attuale
        setCurrentRoom(getRooms().get("Camera da Letto"));
        
        //collega gli observer
        
        attach(new LookObserver());
        
        attach(new BreakObserver());
        
        attach(new InventoryObserver());
        
        attach(new MoveObserver());
        
        attach(new OpenObserver());
        
        attach(new PickUpObserver());

        attach(new TurnObserver());
        
        attach(new UseObserver());
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
        }
        if (move) {
            System.out.println(getCurrentRoom().getName());
            System.out.println("================================================");
            System.out.println(getCurrentRoom().getDescription());
        }
        
    }

    @Override
    public void attach(GameObserver o) {
        if (!observers.contains(o))
            observers.add(o);
    }

    @Override
    public void detach(GameObserver o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        for (GameObserver o : observers) {
                messages.add(o.update(this, action));
            }
    }

    @Override
    public String getWelcomeMessage() {
        return "Buongiorno Leone, la casa è impazzita, sta a te spegnerla e liberare Marilu!";
    }
    
    
    
}
