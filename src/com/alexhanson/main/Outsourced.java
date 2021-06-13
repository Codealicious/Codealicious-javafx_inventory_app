
package com.alexhanson.main;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Represents an out-sourced Part.
 * @author Alex Hanson
 */
public class Outsourced extends Part implements ItemSource {
    
    // The source of this part.
    private String companyName;
    
    /**
     * Constructor: Initializes a new out-sourced part.
     * @param id The identification number of the part.
     * @param name The name of the part.
     * @param price The price of the part.
     * @param stock The quantity on hand of the part.
     * @param min The minimum allowable quantity on hand.
     * @param max The maximum allowable quantity on hand.
     * @param companyName The company from which this part was purchased.
     */
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }
    
    /**
     * Provided for the use of the PropertyValueFactory class.
     * @return The part's id as a string property.
     */
    public final StringProperty idProperty() {
        return new SimpleStringProperty(Integer.toString(getId()));
    }
    
    /**
     * @param companyName The company from which this product was purchased.
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    
    /**
     * @return The name of the company from which this product was purchased.
     */
    public String getCompanyName() {
        return companyName;
    }
    
    /**
     * Same as getCompanyName, provided to comply with ItemSource interface.
     * @return Same as getCompanyName.
     */
    @Override
    public String getSrc() {
        return getCompanyName();
    }
    
    /**
     * Provides description of the source of the part- made in house or purchased from third party.
     * Intended for use with part form configuration.
     * @return Description of part source.
     */
    @Override 
    public String getSrcDscp() {
        return "Company Name";
    }
}
