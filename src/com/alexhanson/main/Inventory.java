
package com.alexhanson.main;

import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Acts as in memory database for parts and products.
 * @author Alex Hanson
 */

public class Inventory {
    
    // Create initially empty lists for both parts and products.
    final private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    final private static ObservableList<Product> allProducts = FXCollections.observableArrayList();
    
    /**
     * @param newPart The new part to add.
     */
    public static void addPart(Part newPart) {
        allParts.add(newPart);
    }
    
    /**
     * @param newProduct The new product to add.
     */
    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }
    
    /**
     * Returns a reference to the Part held in inventory with given id.
     * @param partId The id of the Part to look for.
     * @return A reference to the first Part with given id or null.
     */
    public static Part lookupPart(int partId) {
           
        for(Part p : allParts) {
            if(p.getId() == partId)
                return p;
        }
        
        return null;
    }
    
    /**
     * Returns a reference to the Product held in inventory with given id.
     * @param productId The id of the product to look for.
     * @return A reference to the first Product with given id or null.
     */
    public static Product lookupProduct(int productId) {
       
        for(Product p : allProducts) {
            if(p.getId() == productId)
                return p;
        }
        
        return null;
    }
    
    /**
     * Returns a list of all Parts with a given name
     * @param partName The name to search for.
     * @return A list of all Parts with given name.
     */
    public static ObservableList<Part> lookupPart(String partName) {
        
        ObservableList<Part> tmp = FXCollections.observableArrayList();
        
        for(Part p : allParts) {
            if(p.getName().equalsIgnoreCase(partName)) {
                tmp.add(p);
            }
        }
        
        return tmp;
    }
    
    /**
     * Returns a list of all Products with a given name.
     * @param productName The name to search for.
     * @return A list of all Products with given name.
     */
    public static ObservableList<Product> lookupProduct(String productName) {
        
        ObservableList<Product> tmp = FXCollections.observableArrayList();
        
        for(Product p : allProducts) {
            if(p.getName().equalsIgnoreCase(productName)) {
                tmp.add(p);
            }
        }
        
        return tmp;
    }
    
    /**
     * Updates the Part reference in Inventory at given index with new Part.
     * @param index The index of the Part in Inventory to update.
     * @param selectedPart A new Part with desired updates.
     */
    public static void updatePart(int index, Part selectedPart) {
        allParts.set(index, selectedPart);
    }
    
    /**
     * Updates the Product reference in Inventory at given index with new Product.
     * @param index The index of the Product in Inventory to update.
     * @param selectedProduct A new Product with desired updates.
     */
    public static void updateProduct(int index, Product selectedProduct) {
        allProducts.set(index, selectedProduct);
    }
    
    /**
     * Deletes the first instance of Part in Inventory with the same id as given Part.
     * @param selectedPart Part instance with id matching the Part to be deleted.
     * @return True if Part was successfully deleted, false otherwise.
     */
    public static boolean deletePart(Part selectedPart) {
        
        Part tmp;
        
        for(int index = 0; index < allParts.size(); index++) {
            
            tmp = (Part) allParts.get(index);
            
            if(tmp.getId() == selectedPart.getId()) {
                allParts.remove(index);
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Deletes the first instance of Product in Inventory with the same id as given Product.
     * @param selectedProduct Product instance with id matching the Product to be deleted.
     * @return True if Product was successfully deleted, false otherwise.
     */
    public static boolean deleteProduct(Product selectedProduct) {
        
        Product tmp;
        
        for(int index = 0; index < allProducts.size(); index++) {
            
            tmp = (Product) allProducts.get(index);
            
            if(tmp.getId() == selectedProduct.getId()) {
                allProducts.remove(index);
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * @return Copy of ObersvableList of Parts in Inventory.
     */
    public static ObservableList<Part> getAllParts() {
        return FXCollections.observableList(new ArrayList<>(allParts));
    }
    
    /**
     * @return ObersvableList of all Products in Inventory.
     */
    public static ObservableList<Product> getAllProducts() {
        return FXCollections.observableList(new ArrayList<>(allProducts));
    }
    
}
