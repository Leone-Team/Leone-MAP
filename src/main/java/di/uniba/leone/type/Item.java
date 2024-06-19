
package di.uniba.leone.type;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Item {
    
    private final Integer id;
    private String description = "";
    private final Set<String> names = new HashSet();
    private boolean peakable;
    private boolean useable;
    private boolean turnable;
    
    //costruttore
    public Item(Integer id, String description, String names, Boolean peakable, Boolean useable, Boolean turnable){
        this.id = id;
        this.description = description;
        this.names.addAll(Arrays.asList(names.replaceAll(" ", "").split(",")));
        this.peakable = peakable;
        this.useable = useable;
        this.turnable = turnable;
    }
    
    public Integer getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isPeakable() {
        return peakable;
    }

    public void setPeakable(boolean peakable) {
        this.peakable = peakable;
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
        return other.getId().compareTo(this.getId()) == 0;
    }

    @Override
    public int hashCode() {
        return this.getId().hashCode();
    }

    @Override
    public String toString() {
        return "Item{" + "id=" + id + ", name=" + names + ", description=" + description + ", peakable=" + peakable + ", useable=" + useable + ", turnable=" + turnable + '}';
    }
    
    
    
}
