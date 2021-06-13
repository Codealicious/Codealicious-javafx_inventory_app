
package com.alexhanson.main;

/**
 * Interface that makes sure a Part's source field can be populated in update or modify
 * forms as a text value regardless of actual storage type.
 * @author Alex Hanson
 */
public interface ItemSource {
    
    /**
     * Provides the Parts source value as a String.
     * @return The Parts source.
     */
    String getSrc();
    
    /**
     * Provides meta data about the Parts source for labeling.
     * @return Description of the Part's source value.
     */
    String getSrcDscp();
}
