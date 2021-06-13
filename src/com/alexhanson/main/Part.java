
package com.alexhanson.main;

/**
 * The basis for various types of inventoried parts.
 * @author WGU
 */

public abstract class Part {
    
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max; 
    
    /**
     * Constructor: Initializes the basic properties of all parts.
     * @param id The part's identification number.
     * @param name The part's name.
     * @param price The part's price.
     * @param stock The quantity on hand of the part.
     * @param min The minimum allowable quantity on hand.
     * @param max The maximum allowable quantity on hand.
     */
    public Part(int id, String name, double price, int stock, int min, int max) {
        
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
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
}