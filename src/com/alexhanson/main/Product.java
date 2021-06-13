
package com.alexhanson.main;

import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Defines a Product inventory item.
 * @author Alex Hanson
 */
public class Product {
    
    private ObservableList<Part> associatedParts;
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;
    
    /**
     * Constructor: Initializes an new product.
     * @param id The product's id.
     * @param name The product's name.
     * @param price The product's price.
     * @param stock The quantity on hand of the product.
     * @param min The minimum allowable quantity on hand
     * @param max The maximum allowable quantity on hand.
     */
    public Product(int id, String name, double price, int stock, int min, int max) {
        
        this.id    = id;
        this.name  = name;
        this.price = price;
        this.stock = stock;
        this.min   = min;
        this.max   = max;
        this.associatedParts = FXCollections.observableArrayList();
        
    }
    
    /**
     * @return the id
     */
    
    public int getId() {
        return id;
    }
    
    /**
     * @param id the id to set
     */
    
    public void setId(int id) {
        this.id = id;
    }
    
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(double price) {
        this.price = price;
    }
    
    /**
     * @return the stock
     */
    public int getStock() {
        return stock;
    }

    /**
     * @param stock the stock to set
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * @return the min
     */
    public int getMin() {
        return min;
    }

    /**
     * @param min the min to set
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * @return the max
     */
    public int getMax() {
        return max;
    }

    /**
     * @param max the max to set
     */
    public void setMax(int max) {
        this.max = max;
    }
    
    /**
     * @param part The Part to add to the list of associated parts for this product.
     */
    public void addAssociatedPart(Part part) {
        associatedParts.add(part);
    }
    
    /**
     * @param part The Part to delete from this product's list of associated parts.
     * @return True if the part is deleted, false otherwise.
     */
    public boolean deleteAssociatedPart(Part part) {

        Part tmp;
        
        for(int index = 0; index < associatedParts.size(); index++) {
            
            tmp = (Part) associatedParts.get(index);
            
            if(part.getId() == tmp.getId()) {
                associatedParts.remove(index);
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * @return A copy of this product's list of associated parts.
     */
    public ObservableList<Part> getAllAssociated() {
        return FXCollections.observableList(new ArrayList<>(associatedParts));
    }
    
}
