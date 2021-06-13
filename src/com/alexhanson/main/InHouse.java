
package com.alexhanson.main;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Represents an in-house Part
 * @author Alex Hanson
 */
public class InHouse extends Part implements ItemSource {
    
    // The machine that produced this part.
    private int machineId;
    
    /**
     * Constructor: Initializes a new in-house part.
     * @param id The identification number of the part.
     * @param name The part's name.
     * @param price The price of the part.
     * @param stock The quantity on hand of the part.
     * @param min The minimum quantity on hand allowed.
     * @param max The maximum quantity on hand allowed.
     * @param machineId The id of the machine that produced this part.
     */
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }
    
    /**
     * Provided for the use of the PropertyValueFactory class in order to resolve conflict of reflection 
     * process between getId() from part and getMachineId() from InHouse.
     * @return The part's id as a string property.
     */
    public final StringProperty idProperty() {
        return new SimpleStringProperty(Integer.toString(getId()));
    }
    
    /**
     * @param machineId The id of the machine that created this part.
     */
    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }
    
    /**
     * @return The id of the machine that created this part.
     */
    public int getMachineId() {
        return machineId;
    }
    
    /**
     * Provides String representation of machine id for form population.
     * @return The machine id as a String.
     */
    @Override
    public String getSrc() {
        return Integer.toString(getMachineId());
    }
    
    /**
     * Provides description of the source of the part- made in house or purchased from third party.
     * Intended for use with part form configuration.
     * @return Description of part source.
     */
    @Override 
    public String getSrcDscp() {
        return "Machine ID";
    }
            
}

