/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package di.uniba.leone.type;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author giann
 */
/**
 * Classe rappresentativa delle varie stanza del gioco.
 *
 * @author
 */
public class Room implements Serializable {

    private static final long serialVersionUID = 1L;
    private String name;
    private Room eastR, westR, southR, northR, upR, downR;
    private List<Integer> items = new ArrayList();
    private boolean lighted;
    private boolean locked;
    private String description;
    private List<Integer> riddles = new ArrayList();

    /**
     *
     */
    public Room() {
    }

    /**
     *
     * @param name
     * @param eastR
     * @param westR
     * @param southR
     * @param northR
     * @param upR
     * @param downR
     * @param lighted
     * @param locked
     * @param description
     */
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

    /**
     *
     * @param name
     * @param lighted
     * @param locked
     * @param description
     */
    public Room(String name, boolean lighted, boolean locked, String description) {
        this.name = name;
        this.lighted = lighted;
        this.locked = locked;
        this.description = description;
    }

    
    /** 
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    
    /** 
     * @param eastR
     */
    public void setEastR(Room eastR) {
        this.eastR = eastR;
    }

    
    /** 
     * @param westR
     */
    public void setWestR(Room westR) {
        this.westR = westR;
    }

    /**
     *
     * @param southR
     */
    public void setSouthR(Room southR) {
        this.southR = southR;
    }

    /**
     *
     * @param northR
     */
    public void setNorthR(Room northR) {
        this.northR = northR;
    }

    /**
     *
     * @param upR
     */
    public void setUpR(Room upR) {
        this.upR = upR;
    }

    /**
     *
     * @param downR
     */
    public void setDownR(Room downR) {
        this.downR = downR;
    }

    /**
     *
     * @param lighted
     */
    public void setLighted(boolean lighted) {
        this.lighted = lighted;
    }

    /**
     *
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return
     */
    public Room getEastR() {
        return eastR;
    }

    /**
     *
     * @return
     */
    public Room getWestR() {
        return westR;
    }

    /**
     *
     * @return
     */
    public Room getSouthR() {
        return southR;
    }

    /**
     *
     * @return
     */
    public Room getNorthR() {
        return northR;
    }

    /**
     *
     * @return
     */
    public Room getUpR() {
        return upR;
    }

    /**
     *
     * @return
     */
    public Room getDownR() {
        return downR;
    }

    /**
     *
     * @return
     */
    public List<Integer> getItems() {
        return items;
    }

    /**
     *
     * @return
     */
    public boolean isLighted() {
        return lighted;
    }

    /**
     *
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param items
     */
    public void setItems(List<Integer> items) {
        this.items.addAll(items);
    }

    /**
     *
     * @return
     */
    public boolean isLocked() {
        return locked;
    }

    /**
     *
     * @param locked
     */
    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    /**
     *
     * @return
     */
    public List<Integer> getRiddles() {
        return riddles;
    }

    /**
     *
     * @param idRiddles
     */
    public void SetRiddles(List<Integer> idRiddles) {
        this.riddles.addAll(idRiddles);
    }

    /**
     *
     * @param riddle
     */
    public void addRiddle(Integer riddle) {
        this.riddles.add(riddle);
    }
}
