/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package di.uniba.leone.type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Container extends Item{
    private List<String> items = new LinkedList();
    private boolean locked;
    private Integer key;
    
    public Container(String container){
        super(container.split("#")[0]);
        List<String> tokens = new ArrayList();
        tokens.addAll(Arrays.asList(container.split("#")[1].split("/")));
        
        this.items.addAll(Arrays.asList(tokens.get(0).split(",")));
        locked = tokens.get(1).compareToIgnoreCase("true") == 0;
        key = Integer.valueOf(tokens.get(2));
        
    }

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public boolean useKey(Integer key)
    {
        if(key.compareTo(this.key) == 0)
            this.locked = !this.locked;
        
        //restituisce se la chiave è corretta e ha funzionato
        return key.compareTo(this.key) == 0;
    }
    @Override
    public boolean equals(Object obj) {
        if(obj.getClass() != getClass())
            return false;
        
        Container other = (Container) obj;
        return other.getId().compareTo(this.getId()) == 0;}

    @Override
    public int hashCode() {
        return this.getId().hashCode();
    }

    @Override
    public String toString() {
        return super.toString() + ", items:" + items; // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }
    
    
    
    
}

