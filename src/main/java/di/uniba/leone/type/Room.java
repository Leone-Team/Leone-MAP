/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package di.uniba.leone.type;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author giann
 */
/**
 * Classe rappresentativa delle varie stanza del gioco.
 * @author giann
 */
public class Room {
    private String name;
    private Room eastR, westR, southR, northR, upR, downR;
    private List<Integer> Items = new ArrayList<>();
    private boolean lighted;
    private boolean locked;
    private String description;

    public Room() {
    }

    public Room(String name, Room eastR, Room westR, Room southR, Room northR, Room upR, Room downR, boolean lighted, boolean locked, String description) {
        this.name = name;
        this.eastR = eastR;
        this.westR = westR;
        this.southR = southR;
        this.northR = northR;
        this.upR = upR;
        this.downR = downR;
        this.lighted = lighted;
        this.description = description;
    }
    
    public Room(String name, boolean lighted, boolean locked, String description){
        this.name = name;
        this.lighted = lighted;
        this.locked = locked;
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEastR(Room eastR) {
        this.eastR = eastR;
    }

    public void setWestR(Room westR) {
        this.westR = westR;
    }

    public void setSouthR(Room southR) {
        this.southR = southR;
    }

    public void setNorthR(Room northR) {
        this.northR = northR;
    }

    public void setUpR(Room upR) {
        this.upR = upR;
    }

    public void setDownR(Room downR) {
        this.downR = downR;
    }

    public void setLighted(boolean lighted) {
        this.lighted = lighted;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public Room getEastR() {
        return eastR;
    }

    public Room getWestR() {
        return westR;
    }

    public Room getSouthR() {
        return southR;
    }

    public Room getNorthR() {
        return northR;
    }

    public Room getUpR() {
        return upR;
    }

    public Room getDownR() {
        return downR;
    }

    public List<Integer> getItems() {
        return Items;
    }

    public boolean isLighted() {
        return lighted;
    }

    public String getDescription() {
        return description;
    }
    
    public void setItems(List<Integer> items){
        this.Items.addAll(items);
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    } 
}
