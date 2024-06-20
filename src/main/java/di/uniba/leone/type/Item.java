
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
    private boolean useable;
    private boolean turnable;
    private boolean openable; 
    private boolean open;
    
    //costruttore
    public Item(Integer id, String description, String names, Boolean peakable, Boolean useable, Boolean turnable){
        this.id = id;
        this.description = description;
        this.names.addAll(Arrays.asList(names.replaceAll(" ", "").split(",")));
        this.pickupable = peakable;
        this.useable = useable;
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

    public boolean isUseable() {
        return useable;
    }

    public void setUseable(boolean useable) {
        this.useable = useable;
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
        return "Item{" + "id=" + id + ", name=" + names + ", description=" + description + ", peakable=" + pickupable + ", useable=" + useable + ", turnable=" + turnable + '}';
    }
        // Metodo per ottenere il primo nome dal set
    public String getFirstName() {
        Iterator<String> iterator = names.iterator();
        if (iterator.hasNext()) {
            return iterator.next();
        }
        return null; 
    }

    public boolean isOpenable() {
        return openable;
    }

    public void setOpenable(boolean openeable) {
        this.openable = openeable;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }
    
    
}
