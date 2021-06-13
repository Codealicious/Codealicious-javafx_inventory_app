
package com.alexhanson.main;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Provides the basis for all forms used in this application.
 * @author Alex Hanson
 */
public abstract class InventoryForm extends VBox {
        
        // App stage and scene for form to return to
        final private Stage appStage;
    
        // Form field container
        final private GridPane formFields;
        
        /** 
         * Index where error condition styles are set and removed on
         * form TextFields getStyleClass().add() invocations.
         */
        final protected int ERROR_STATE_STYLES = 1;
        /** Default width for form TextFields. */
        final protected int DEFAULT_FORM_FIELD_WIDTH = 130;
        /** Default width for form TextField Labels. */
        final protected int DEFAULT_FORM_LABEL_WIDTH = 90;
    
        // Form input
        final private TextField id;
        final private TextField name;
        final private TextField price;
        final private TextField stock;
        final private TextField min;
        final private TextField max;
        
        /** Reference to optional error label for inv form field. */
        protected Label stockErr;
        /** Reference to optional error label for min > max error condition. */
        protected Label minMaxErr;

        // Form Buttons
        final private Button save;
        final private Button cxl;
        
        /**
         * Constructor: Initializes GUI components common to all forms in this application.
         * @param appStage The applications main stage.
         */
        public InventoryForm(Stage appStage) {
            
            super(15);
            
            this.appStage = appStage;
            
            formFields = new GridPane();
            id = new TextField();
            name = new TextField();
            price = new TextField();
            stock = new TextField();
            min = new TextField();
            max = new TextField();
            
            save = new Button("Save");
            cxl = new Button("Cancel");
            
            getStylesheets().add("file:css/inventory_form.css");
            
        }
        
        /**
         * Intended to be overridden in concrete subclass, defines the layout for the header of the form.
         * @param index The index of an item with which to populate the form with initial values.
         * Intended for use with modify or update forms. Provide a value of -1 for add forms.
         * @return The layout container of the form's header.
         */
        protected abstract Pane buildHeader(int index);
        
        /**
         * Provides default layout of form fields common to all forms in this application.
         * @return  The GridPane container for essential form fields.
         */
        protected GridPane layoutForm() {

            Label idLabel = new Label("ID");
            idLabel.setPrefWidth(DEFAULT_FORM_LABEL_WIDTH );
            id.setPrefWidth(DEFAULT_FORM_FIELD_WIDTH);
            id.setDisable(true);
            id.setText("Auto Gen- Disabled");
            HBox idBox = new HBox(10, idLabel, id);
            idBox.setAlignment(Pos.CENTER_LEFT);
            
            Label nameLabel = new Label("Name");
            nameLabel.setPrefWidth(DEFAULT_FORM_LABEL_WIDTH );
            name.setPrefWidth(DEFAULT_FORM_FIELD_WIDTH);
            HBox nameBox = new HBox(10, nameLabel, name);
            nameBox.setAlignment(Pos.CENTER_LEFT);
            
            Label stockLabel = new Label("Inv");
            stockLabel.setPrefWidth(DEFAULT_FORM_LABEL_WIDTH );
            stock.setPrefWidth(DEFAULT_FORM_FIELD_WIDTH);
            HBox stockBox = new HBox(10, stockLabel, stock);
            stockBox.setAlignment(Pos.CENTER_LEFT);
            
            Label priceLabel = new Label("Price/Cost");
            priceLabel.setPrefWidth(DEFAULT_FORM_LABEL_WIDTH );
            price.setPrefWidth(DEFAULT_FORM_FIELD_WIDTH);
            HBox priceBox = new HBox(10, priceLabel, price);
            priceBox.setAlignment(Pos.CENTER_LEFT);
            
            Label maxLabel = new Label("Max");
            maxLabel.setPrefWidth(DEFAULT_FORM_LABEL_WIDTH );  
            max.setPrefWidth(DEFAULT_FORM_FIELD_WIDTH);
            HBox maxBox = new HBox(10, maxLabel, max);                     
            maxBox.setAlignment(Pos.CENTER_LEFT);
            
            Label minLabel = new Label("Min");
            minLabel.setPrefWidth(40);
            min.setPrefWidth(DEFAULT_FORM_FIELD_WIDTH);
            HBox minBox = new HBox( minLabel, min);
            minBox.setAlignment(Pos.CENTER_LEFT);
            minBox.getStyleClass().add("min-wrapper");
            
            addFormField(idBox, 0, 0);
            addFormField(nameBox, 0, 1);
            addFormField(stockBox, 0, 2);
            addFormField(priceBox, 0, 3);
            addFormField(maxBox, 0, 4);
            addFormField(minBox, 1, 4);

            formFields.setHgap(5);
            formFields.setVgap(8);
            
            return formFields;
        }
        
        /**
         * Provides a way for subclasses to extend the form's fields.
         * @param field Layout wrapper for a new form field.
         * @param col Column to place the new form field in.
         * @param row Row to place the new form field in.
         */
        protected void addFormField(Pane field, int col, int row) {
            formFields.add(field, col, row);
        }
        
        /**
         * Validates the form's fields.
         * Checks that numeric input can be converted to appropriate types, that no
         * numbers are negative, that max > min inventory levels, the items stock is 
         * within max and min inventory range, and that the name field is not empty.
         * @return True if all conditions are met, false otherwise.
         */
        protected boolean formValidation() {
 
            formFields.getChildren().remove(minMaxErr);
            formFields.getChildren().remove(stockErr);
            
            name.getStyleClass().set(ERROR_STATE_STYLES, "");
            price.getStyleClass().set(ERROR_STATE_STYLES, "");
            stock.getStyleClass().set(ERROR_STATE_STYLES, "");
            max.getStyleClass().set(ERROR_STATE_STYLES, "");
            min.getStyleClass().set(ERROR_STATE_STYLES, "");
            
            boolean valid = true;
            
            try {
                getName();
            }catch(IllegalArgumentException e) {
                name.getStyleClass().set(ERROR_STATE_STYLES, "invalid-input");
                valid = false;
            }
            
            try {
                getPrice();
            } catch (IllegalArgumentException e) {    
                price.getStyleClass().set(ERROR_STATE_STYLES, "invalid-input");
                valid = false;
            }
            
            try {
                getStock();
            } catch (IllegalArgumentException e) {
                stock.getStyleClass().set(ERROR_STATE_STYLES, "invalid-input");
                valid = false;
            }
            
            try {
                getMax();
            } catch (IllegalArgumentException e) {
                max.getStyleClass().set(ERROR_STATE_STYLES, "invalid-input");
                valid = false;
            }
            
            try {
                getMin();
            } catch (IllegalArgumentException e) {
                min.getStyleClass().set(ERROR_STATE_STYLES, "invalid-input");
                valid = false;
            }
            
            // The following range checks involve min, max, and stock.
            // If any of these inputs are in an invalid state, exceptions will be thrown again.
            // This time, we just ignore them, only doing these further range checks
            // when all three inputs are in valid states.
            
            try {
            
                if(getMin() > getMax()) {

                    minMaxErr = new Label("Minimum inventory amount less the maximum");
                    minMaxErr.getStyleClass().addAll("input-error-label", "err-text-display");
                    min.getStyleClass().set(ERROR_STATE_STYLES, "invalid-input");
                    max.getStyleClass().set(ERROR_STATE_STYLES, "invalid-input");

                    formFields.add(minMaxErr, 1, 5);

                    valid = false;

                }else if(getStock() > getMax() || getStock() < getMin()){

                    stock.getStyleClass().set(ERROR_STATE_STYLES, "invalid-input");
                    stockErr = new Label("Inventory outside range");
                    stockErr.getStyleClass().addAll("input-error-label", "err-text-display");

                    formFields.add(stockErr, 1, 2);

                    valid = false;
                }
            }catch(Exception e) {
                // Do nothing...
            }

            return valid;
        }
 
        /**
         * Provides access to form's save Button to subclasses for layout and action configuration,
         * @return Reference to form's save Button.
         */
        final protected Button getSaveBtn() {
            return save;
        }
        
        /**
         * Provides access to form's cancel Button to subclasses for layout and action configuration.
         * @return Reference to form's cancel Button.
         */
        final protected Button getCancelBtn() {
            return cxl;
        }
        
        /**
         * Provides access to application Stage to subclasses for redirection.
         * @return Reference to application stage for redirection.
         */
        final protected Stage getStage() {
            return appStage;
        }
        
        /********* Form values extraction utility methods. *********/
        
        /**
         * @return The item's id.
         * @throws NumberFormatException Thrown if id value cannot be converted to an integer.
         * @throws IllegalArgumentException Thrown if id value is negative.
         */
        final protected int getItemId() throws NumberFormatException,
                                           IllegalArgumentException
        {
            return getIntValue(id);
        }
        
        /**
         * @return The item's name.
         * @throws IllegalArgumentException Thrown if the name field is blank.
         */
        final protected String getName() throws IllegalArgumentException {
            
            if(name.getText().isEmpty())
                throw new IllegalArgumentException("Name cannont be blank");
            
            return name.getText();
        }
        
        /**
         * @return The price of the item.
         * @throws NumberFormatException Thrown if price value cannot be converted to a double.
         * @throws IllegalArgumentException Thrown if price is negative.
         */
        final protected double getPrice()  throws NumberFormatException,
                                        IllegalArgumentException 
        {
            double d = Double.parseDouble(price.getText());
            
            if(d < 0)
                throw new IllegalArgumentException("negative number");
            
            return d;
        }
        
        /**
         * @return The on hand quantity of the item.
         * @throws NumberFormatException Thrown if the inventory value cannot be converted to an integer.
         * @throws IllegalArgumentException Thrown if the inventory value is negative.
         */
        final protected int getStock() throws NumberFormatException,
                                        IllegalArgumentException  
        {
            return getIntValue(stock);
        }
        
        /**
         * @return The minimum on hand quantity of the item.
         * @throws NumberFormatException Thrown if the minimum on hand quantity value cannot be converted to an integer.
         * @throws IllegalArgumentException Thrown if the minimum on hand quantity value is negative.
         */
        final protected int getMin() throws NumberFormatException,
                                        IllegalArgumentException 
        {
            return getIntValue(min);
        }
        
        /**
         * @return The maximum on hand quantify of the item.
         * @throws NumberFormatException Thrown if the maximum on hand quantity value cannot be converted to an integer.
         * @throws IllegalArgumentException Thrown if the maximum on hand quantity value is negative.
         */
        final protected int getMax() throws NumberFormatException,
                                        IllegalArgumentException 
        {
            return getIntValue(max);
        }
        
        /**
         * Form validation and Getter utility method.
         * @param tf TextField reference from which to extract integer data.
         * @return Value of form field as an integer.
         * @throws NumberFormatException Thrown if form field value cannot be converted to an integer.
         * @throws IllegalArgumentException  Thrown if the form field value is negative.
         */
        final protected int getIntValue(TextField tf) throws NumberFormatException,
                                                        IllegalArgumentException
        {
            int i = Integer.parseInt(tf.getText());
            
            if(i < 0)
                throw new IllegalArgumentException("negative number");
            
            return i;
        }
        
        /********* Form population utility methods. *********/
        
        /**
         * @param i The item's id.
         */
        final protected void setId(int i) {
            id.setText(Integer.toString(i));
        }
        
        /**
         * @param n The name of the item.
         */
        final protected void setName(String n) {
            name.setText(n);
        }
        
        /**
         * @param d The item's price.
         */
        final protected void setPrice(double d) {
            price.setText(Double.toString(d));
        }
        
        /**
         * @param i The quantity on hand.
         */
        final protected void setStock(int i) {
            stock.setText(Integer.toString(i));
        }
        
        /**
         * @param i The maximum allowable quantity on hand.
         */
        final protected void setMax(int i) {
            max.setText(Integer.toString(i));
        }
        
        /**
         * @param i The minimum allowable quantity on hand.
         */
        final protected void setMin(int i) {
            min.setText(Integer.toString(i));
        }
        
        /******** Subclass form configuration methods ********/
        
        /**
         * Populate fields of an update or modify form.
         * @param index The index of the item in its Inventory list.
         */
        protected abstract void populateForm(int index);
        
        /**
         * Layout and configure actions of form buttons.
         * @return The layout container of the forms buttons.
         */
        protected abstract Pane configBtns();
        
        /**
         * Layout and configure the actions of components used to close the form.
         */
        protected abstract void closeForm();
}
