/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package di.uniba.map.type;
import di.uniba.map.leone.Leone;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author giann
 */
public class Room {
    private String name;
    private Room eastR, westR, southR, northR, upR, downR;
    private final List<Item> Items = new ArrayList<>();
    
}
