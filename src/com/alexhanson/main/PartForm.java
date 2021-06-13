
package com.alexhanson.main;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Creates the layout for Add and Modify part forms, and the ability to save into
 * or update parts in Inventory.
 * @author Alex Hanson
 */
public class PartForm extends InventoryForm {
    
    // Handels to part form GUI components.
    final private RadioButton inHouse;
    final private RadioButton outSourced;
    final private Label srcLabel;
    final private TextField src;

    /**
     * Initializes part form and underlying Inventory form components.
     * Uses helper methods to initialize and layout GUI controls.
     * @param appStage Application stage for redirection.
     * @param index Expects a valid index if modifying or updating a part, otherwise -1.
     */
    public PartForm(Stage appStage, int index) {
       
        super(appStage);
        
        this.setPrefSize(600, 450);
        
        inHouse = new RadioButton("In-House");
        outSourced = new RadioButton("Outsourced");
        srcLabel = new Label();
        src = new TextField();
        
        src.setPrefWidth(DEFAULT_FORM_FIELD_WIDTH);

        getChildren().add(buildHeader(index));
        getChildren().add(layoutForm());
        populateForm(index);
        configToggle(index);
        addFormField(configBtns(index), 1, 6); 

        getStylesheets().add("file:css/part_form.css");
        
        setPadding(new Insets(30));
    }
    
    /**
     * Builds the form header.
     * @param index The index for a part being updated or modified, otherwise -1.
     * @return The layout container for the part form header.
     */
    @Override
    protected Pane buildHeader(int index) {
        
        Label formTitle = new Label((index >= 0 ? "Modify" : "Add") + " Part");
        formTitle.getStyleClass().add("form-title");
        formTitle.setPrefWidth(100);
        HBox toggleBox = new HBox(80, inHouse, outSourced);
        toggleBox.setAlignment(Pos.CENTER_LEFT);
        
        HBox header = new HBox(20, formTitle , toggleBox);
        header.setAlignment(Pos.CENTER_LEFT);
        
        return header;
    }
    
    /**
     * Creates form field layout.
     * @return Reference to the GridPane containing the main form fields.
     */
    @Override
    protected GridPane layoutForm() {
        
        GridPane gp = super.layoutForm();
        
        srcLabel.setPrefWidth(90);
        
        HBox srcWrapper = new HBox(10, srcLabel, src);
        srcWrapper.setAlignment(Pos.CENTER_LEFT);
        
        addFormField(srcWrapper, 0, 5);
        
        gp.getStyleClass().add("form-field-container");
        
        return gp;
    }
    
    /**
     * Populates form fields with corresponding info from the part at index.
     * @param index The index of the part in Inventory.
     */
    @Override
    protected void populateForm (int index) {
        
        Part part  = index >= 0 ? Inventory.getAllParts().get(index) : null;
        
        if(part != null) {

            setId(part.getId());
            setName(part.getName());
            setPrice(part.getPrice());
            setStock(part.getStock());
            setMax(part.getMax());
            setMin(part.getMin());
        
            src.setText( ((ItemSource)part).getSrc() );
        }
    }
    
    /**
     * Validates the form field contents.
     * See InventoryForm class for more details.
     * @return True if all form fields have valid values, false otherwise.
     */
    @Override
    protected boolean formValidation() {
        
        boolean valid = super.formValidation();
        
        src.getStyleClass().set(ERROR_STATE_STYLES, "");
                    
        if(inHouse.isSelected())
            try {
                Integer.parseInt(src.getText());
            } catch(NumberFormatException e) {
                src.getStyleClass().set(ERROR_STATE_STYLES, "invalid-input");
                valid = false;
            }
        
        return valid;  
    }
    
    // Provides default configuration of radio buttons
    // see configToggle(int index).
    private void configToggle() {
        
        ToggleGroup sourceGroup = new ToggleGroup();
        inHouse.setToggleGroup(sourceGroup);
        outSourced.setToggleGroup(sourceGroup);
        inHouse.setSelected(true);
        srcLabel.setText("Machine ID");
        
        inHouse.setOnAction(event -> {
            srcLabel.setText("Machine ID");
        });

        outSourced.setOnAction(event -> {
            srcLabel.setText("Company Name");
        });
    }
    
    // Helper method to configure the actions of the 
    // radio buttons in the part form header. If index is < 0
    // method provides default configuration through configToggle().
    private void configToggle(int index) {
        
        configToggle();
        
        if(index >= 0) {
            if( ((ItemSource) Inventory.getAllParts().get(index)).getSrcDscp().equals("Company Name") ) {
                outSourced.setSelected(true);
                srcLabel.setText("Company Name");
            } else {
                inHouse.setSelected(true);
                srcLabel.setText("Machine ID");
            }
        }
    }
    
    /**
     * Provides default action and layout for the forms Buttons.
     * @return The layout container for part form buttons.
     */
    @Override
    protected Pane configBtns() {

        getSaveBtn().setOnAction(e -> {
            
                if(formValidation()) {
                
                    int id = Inventory.getAllParts().size();

                    if(inHouse.isSelected()) {
                        Inventory.addPart(new InHouse(++id, getName(), getPrice(), getStock(), getMin(), getMax(), getSrc(0)));
                    }
                    else {
                        Inventory.addPart(new Outsourced(++id, getName(), getPrice(), getStock(), getMin(), getMax(), getSrc("")));
                    }

                    closeForm();
                }
            });
        
        getCancelBtn().setOnAction(e -> {
            closeForm();
        });
        
        HBox btnBox = new HBox(15, getSaveBtn(), getCancelBtn());
        btnBox.getStyleClass().add("part-form-btn-wrapper");
        
        return btnBox; 
    }

    // Helper method. Configures Save Button to update part indicated by index.
    // If index < 0, this method provides default Save action through configBtns()
    private Pane configBtns(int index) {
        
        HBox btnBox = (HBox) configBtns();
        
        if(index >= 0) {
 
            getSaveBtn().setOnAction(e -> {

                if(formValidation()) {

                    if(inHouse.isSelected())
                        Inventory.updatePart(index,new InHouse(getItemId(), getName(), getPrice(), getStock(), getMin(), getMax(), getSrc(0)));
                    else
                        Inventory.updatePart(index,new Outsourced(getItemId(), getName(), getPrice(), getStock(), getMin(), getMax(), getSrc("")));

                    closeForm();
                }
            });
        }
        
        return btnBox;
    }
    
    /**
     * Returns to the home page by placing a new instance of HomePage on the app stage.
     */
    @Override
    protected void closeForm() {
        getStage().setScene(new Scene(new HomePage(getStage())));
    }
    
    /******** Helper methods to grab correct source type for the Part ********/
    
    // int parameter for overloading.
    private int getSrc(int i) {
        return Integer.parseInt(src.getText());
    }
    
    // String parameter for overloading.
    private String getSrc(String s) {
        return src.getText();
    }
}
