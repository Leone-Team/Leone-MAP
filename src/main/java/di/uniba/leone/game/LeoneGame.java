/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package di.uniba.leone.game;

import di.uniba.leone.gui.MsgManager;
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
import di.uniba.leone.gui.Window;
import di.uniba.leone.observer.wearObserver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author feder
 */
public class LeoneGame extends Game implements GameObservable {

    private final List<String> messages = new ArrayList<>();
    private ActionInGame action;

    /**
     *
     */
    @Override
    public void init() {
        setDbProperties("Leone", "1234");

        //inizializza oggetti
        try (Connection conn = DriverManager.getConnection("jdbc:h2:./src/main/resources/leone_game/dbs/item", getDbprop())) {
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
        getItems().get(17).setTurnable(false);

        //inizializza stanze
        try (Connection conn = DriverManager.getConnection("jdbc:h2:./src/main/resources/leone_game/dbs/rooms", getDbprop())) {
            PreparedStatement pstm = conn.prepareStatement("SELECT  name, id_items, lighted, locked, desc FROM rooms");
            ResultSet rs = pstm.executeQuery();

            Room room;
            ArrayList<Integer> items_stored = new ArrayList();
            while (rs.next()) {
                room = new Room(rs.getString("name"), rs.getBoolean("lighted"), rs.getBoolean("locked"), rs.getString("desc"));
                String[] items = rs.getString("id_items").replace(" ", "").split(",");

                for (String id_item : items) {
                    if (id_item.compareTo("") != 0) {
                        items_stored.add(Integer.valueOf(id_item));
                    }
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
        try (Connection conn = DriverManager.getConnection("jdbc:h2:./src/main/resources/leone_game/dbs/commands", getDbprop())) {

            PreparedStatement pstm = conn.prepareStatement("SELECT  type, names FROM commands");
            ResultSet rs = pstm.executeQuery();

            Set<String> names = new HashSet();
            while (rs.next()) {
                try {
                    names.addAll(Arrays.asList(rs.getString("names").toLowerCase().replace(" ", "").split(",")));
                    getCommands().add(new Command(CommandType.valueOf(rs.getString("type")), names));
                    names.clear();
                } catch (IllegalArgumentException ex) {
                    System.out.println("Parola non valida:" + rs.getString("type"));
                }
            }
            pstm.close();
            rs.close();
            conn.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        //istanziare gli indovinelli
        try (Connection conn = DriverManager.getConnection("jdbc:h2:./src/main/resources/leone_game/dbs/riddles", getDbprop())) {

            PreparedStatement pstm = conn.prepareStatement("SELECT  * FROM riddles");
            ResultSet rs = pstm.executeQuery();

            Set<CommandType> blacklist;
            while (rs.next()) {
                try {
                    blacklist = new HashSet();
                    for (String type : rs.getString("blackList").replace(" ", "").split(",")) {
                        if (!type.isEmpty()) {
                            blacklist.add(CommandType.valueOf(type));
                        }
                    }
                    if (rs.getBoolean("itemRiddle")) {
                        getRiddles().put(rs.getInt("id"), new ItemRiddle(rs.getInt("id"), blacklist, rs.getString("desc"), rs.getBoolean("solved"), rs.getInt("targetItem"), rs.getInt("tool")));
                    } else {
                        getRiddles().put(rs.getInt("id"), new QuestionRiddle(rs.getInt("id"), blacklist, rs.getString("desc"), rs.getBoolean("solved"), rs.getInt("targetItem"), rs.getString("question"), rs.getString("answer"), rs.getString("deathMsg")));

                    }
                    getRooms().get(rs.getString("room")).addRiddle(rs.getInt("id"));
                } catch (IllegalArgumentException ex) {
                    System.out.println(ex.getMessage());
                }
            }
            pstm.close();
            rs.close();
            conn.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        //collega gli observer
        GameObserver obs;

        obs = new LookObserver();
        attach(obs);
        getObservers().put(obs, new HashSet(Arrays.asList(CommandType.LOOK)));

        obs = new BreakObserver();
        attach(obs);
        getObservers().put(obs, new HashSet(Arrays.asList(CommandType.BREAK)));

        obs = new InventoryObserver();
        attach(obs);
        getObservers().put(obs, new HashSet(Arrays.asList(CommandType.INVENTORY)));

        obs = new MoveObserver();
        attach(obs);
        getObservers().put(obs, new HashSet(Arrays.asList(CommandType.EAST, CommandType.NORTH, CommandType.SOUTH, CommandType.WEST, CommandType.GO_DOWN, CommandType.GO_UP)));

        obs = new OpenObserver();
        attach(obs);
        getObservers().put(obs, new HashSet(Arrays.asList(CommandType.OPEN)));

        obs = new PickUpObserver();
        attach(obs);
        getObservers().put(obs, new HashSet(Arrays.asList(CommandType.PICK_UP)));

        obs = new TurnObserver();
        attach(obs);
        getObservers().put(obs, new HashSet(Arrays.asList(CommandType.TURN_ON, CommandType.TURN_OFF, CommandType.WEAR)));

        obs = new UseObserver();
        attach(obs);
        getObservers().put(obs, new HashSet(Arrays.asList(CommandType.USE)));

        obs = new DropObserver();
        attach(obs);
        getObservers().put(obs, new HashSet(Arrays.asList(CommandType.DROP)));

        obs = new wearObserver();
        attach(obs);
        getObservers().put(obs, new HashSet(Arrays.asList(CommandType.WEAR)));
        
        //istanzia la room attuale
        setCurrentRoom(getRooms().get("Camera da Letto"));
        setRunning(true);

        //istanzia la gui
        setMainWindow(new Window("Leone Il cane fifone"));
        setMrMsg(new MsgManager(getMainWindow().getDisplay(), getMainWindow().getInputField()));
    }

    
    /** 
     * @param act
     */
    @Override
    public void nextMove(ActionInGame act) {
        action = act;
        messages.clear();

        if (getCurrentRoom() != null) {
            Room cr = getCurrentRoom();
            notifyObservers();
            boolean move = !cr.equals(getCurrentRoom()) && getCurrentRoom() != null;
            if (!messages.isEmpty()) {
                for (String m : messages) {
                    if (m.length() > 0) {
                        getMrMsg().displayMsg(m);
                    }
                }
            } else {
                getMrMsg().displayMsg(">Comando non valido");
            }

            //gestione possibile indovinello ItemRiddle
            if (move) {
                getMrMsg().displayMsg(getCurrentRoom().getName());
                getMrMsg().displayMsg("================================================");
                getMrMsg().displayMsg(getCurrentRoom().getDescription());
            }

            checkRiddles();
        }
    }

    /**
     *
     */
    @Override
    public void checkRiddles() {
        Set<GameObserver> obsToDetach = new HashSet<>();
        for (Integer id_riddle : getCurrentRoom().getRiddles()) {
            if (getRiddles().get(id_riddle) instanceof ItemRiddle itemRiddle) {
                itemRiddle.resolved(getItems());
            }
            if (!getRiddles().get(id_riddle).isSolved()) {
                getMrMsg().displayMsg(getRiddles().get(id_riddle).getDescription());
            }
            for (GameObserver o : getObservers().keySet()) {
                boolean isInBlackList = getObservers().get(o).stream().anyMatch(getRiddles().get(id_riddle).getBlackList()::contains);
                boolean isSolved = getRiddles().get(id_riddle).isSolved();
                if (isInBlackList && !isSolved) {
                    obsToDetach.add(o);
                } else if (isInBlackList && isSolved) {
                    obsToDetach.remove(o);
                }

            }
        }

        for (GameObserver o : getObservers().keySet()) {
            if (obsToDetach.contains(o)) {
                detach(o);
            } else {
                attach(o);
            }
        }
    }

    
    /** 
     * @param o
     */
    @Override
    public void attach(GameObserver o) {
        
        if (!getObsAttached().contains(o)) {
            getObsAttached().add(o);
        }
    }

    
    /** 
     * @param o
     */
    @Override
    public void detach(GameObserver o) {
        getObsAttached().remove(o);
    }

    /**
     *
     */
    @Override
    public void notifyObservers() {
        for (GameObserver o : getObsAttached()) {
            messages.add(o.update(this, action));
        }
    }

    /**
     *
     * @return
     */
    @Override
    public String getWelcomeMessage() {
        String msg = """
                     >Buongiorno Leone, la casa \u00e8 impazzita, sta a te spegnerla e liberare Marilu!>Muoviti con n, s, e, w, u, d (nord, sud, est, ovest, sali, scendi);
                     >Osserva la stanza con : osserva;
                     >Per altri aiuti inserisci:help.
                     """;
        return msg;
    }

}
