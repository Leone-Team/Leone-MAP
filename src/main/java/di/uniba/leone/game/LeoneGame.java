/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package di.uniba.leone.game;

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
import java.util.Properties;
import java.util.Set;

/**
 *
 * @author feder
 */
public class LeoneGame extends Game {

    @Override
    public void init() {
        Properties dbprop = new Properties();
        dbprop.setProperty("user", "Leone");
        dbprop.setProperty("password", "1234");

        //inizializza oggetti
        try (Connection conn = DriverManager.getConnection("jdbc:h2:./leone_game/dbs/item", dbprop)) {
            PreparedStatement pstm = conn.prepareStatement("SELECT id, names, desc, peakable, useable, turnable, container, items_con FROM item");
            pstm.executeUpdate();
            ResultSet rs = pstm.executeQuery();

            Item item;
            while (rs.next()) {
                if (rs.getBoolean("container") == true) {
                    item = new Item(rs.getInt("id"), rs.getString("desc"), rs.getString("names"), rs.getBoolean("peakable"), rs.getBoolean("useable"), rs.getBoolean("turnable"));
                } else {
                    item = new Container(rs.getInt("id"), rs.getString("desc"), rs.getString("names"), rs.getBoolean("peakable"), rs.getBoolean("useable"), rs.getBoolean("turnable"), rs.getString("items_con"));
                }
                getItems().put(item.getID(), item);
            }
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        //inizializza stanze
        try (Connection conn = DriverManager.getConnection("jdbc:h2./leone_game/dbs/rooms", dbprop)) {
            PreparedStatement pstm = conn.prepareStatement("SELECT  name, id_items, lighted, locked, desc FROM rooms");
            ResultSet rs = pstm.executeQuery();

            Room room;
            ArrayList<Item> items_stored = new ArrayList();
            while (rs.next()) {
                room = new Room(rs.getString("name"), rs.getBoolean("lighted"), rs.getBoolean("locked"), rs.getString("desc"));
                String items = rs.getString("id_items");
                for (String id_item : items.split(",")) {
                    items_stored.add(getItems().get(Integer.valueOf(id_item)));
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
            
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        //inizializza comandi
        
        try(Connection conn = DriverManager.getConnection("jdbc:h2./leone_game/dbs/commands", dbprop)){
            PreparedStatement pstm = conn.prepareStatement("SELECT  type, names FROM rooms");
            ResultSet rs = pstm.executeQuery();
            
            Command command;
            Set<String> names = new HashSet();
            while(rs.next()){
                try{
                    names.addAll(Arrays.asList(rs.getString("names").replace(" ", "").split(",")));
                    command = new Command(CommandType.valueOf(rs.getString("type")), names);
                    names.clear();
                }catch(IllegalArgumentException ex){
                    System.out.println("Parola non valida:"+rs.getString("type"));
                }
            }
            
        }catch(SQLException ex){
            System.out.println(ex.getMessage());
        }

        //istanzia la room attuale
        setCurrentRoom(getRooms().get("Camera da letto"));
    }
}
