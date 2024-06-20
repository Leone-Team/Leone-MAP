/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package di.uniba.leone.type;

import java.util.HashSet;
import java.util.Set;

public class Container extends Item{
    private Set<Integer> items = new HashSet();
    private boolean locked;
    private Integer key;

    public Container(Integer id, String description, String names, Boolean peakable, Boolean useable, Boolean turnable, String items){
        super(id, description, names, peakable, useable, turnable);
        for(String item: items.split(","))
        {
            this.items.add(Integer.valueOf(item));
    }
    }

    public Set<Integer> getItems() {
        return items;
    }

    public void setItems(Set<Integer> items) {
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
        return other.getID().compareTo(this.getID()) == 0;}

    @Override
    public int hashCode() {
        return this.getID().hashCode();
    }

    @Override
    public String toString() {
        return super.toString() + ", items:" + items; // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }
    
    
    
    
}

