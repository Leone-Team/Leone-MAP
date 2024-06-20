package di.uniba.leone.type;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Item {
    
    private final Integer id;
    private String description = "";
    private final Set<String> names = new HashSet();
    private boolean pickupable;
    private boolean usable;
    private boolean turnable;
    private boolean breakable;
    private boolean turned_on;
    private boolean broken;
    
    //costruttore
    public Item(Integer id, String description, String names, Boolean peakable, Boolean usable, Boolean turnable){
        this.id = id;
        this.description = description;
        this.names.addAll(Arrays.asList(names.replaceAll(" ", "").split(",")));
        this.pickupable = peakable;
        this.usable = usable;
        this.turnable = turnable;
    }
    
    public Integer getID() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isPickupable() {
        return pickupable;
    }

    public void setPickupable(boolean pickupable) {
        this.pickupable = pickupable;
    }

    public boolean isUsable() {
        return usable;
    }

    public void setUsable(boolean usable) {
        this.usable = usable;
    }

    public boolean isTurnable() {
        return turnable;
    }

    public void setTurnable(boolean turnable) {
        this.turnable = turnable;
    }

    public Set<String> getNames() {
        return names;
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj.getClass() != getClass())
            return false;
        
        Item other = (Item) obj;
        return other.getID().compareTo(this.getID()) == 0;
    }

    @Override
    public int hashCode() {
        return this.getID().hashCode();
    }

    @Override
    public String toString() {
        return "Item{" + "id=" + id + ", name=" + names + ", description=" + description + ", peakable=" + pickupable + ", usable=" + usable + ", turnable=" + turnable + '}';
    }
        // Metodo per ottenere il primo nome dal set
    public String getFirstName() {
        Iterator<String> iterator = names.iterator();
        if (iterator.hasNext()) {
            return iterator.next();
        }
        return null; 
    }  

    public Integer getId() {
        return id;
    }

    public boolean isBreakable() {
        return breakable;
    }

    public boolean isTurned_on() {
        return turned_on;
    }

    public boolean isBroken() {
        return broken;
    }

    public void setBreakable(boolean breakable) {
        this.breakable = breakable;
    }

    public void setTurned_on(boolean turned_on) {
        this.turned_on = turned_on;
    }

    public void setBroken(boolean broken) {
        this.broken = broken;
    }
    
    
}
