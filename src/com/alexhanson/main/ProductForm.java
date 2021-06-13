
package com.alexhanson.main;

import java.util.function.Predicate;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Defines the form layout to add or modify a product and its associated parts.
 * @author Alex Hanson
 */
public class ProductForm extends InventoryForm {
    
    final private TableView<Part> availableParts;
    final private FilteredList<Part> partSearch;
    final private TableView<Part> assocParts;
    final private TextField search;
    final private Button removePart;
    final private Button addPart;
    
    /**
     * Constructor: Initializes the layout and controls of a product form.
     * @param appStage Reference to application Stage for redirection.
     * @param index Index of a product item to populate update or modify form, -1 otherwise.
     */
    public ProductForm(Stage appStage, int index) {
        
        super(appStage);
 
        partSearch = new FilteredList<>(Inventory.getAllParts());
        availableParts = new TableView<> (partSearch);
        
        assocParts = index >= 0 ? new TableView<>(Inventory.getAllProducts().get(index).getAllAssociated()) :
                                  new TableView<> ();
        
        search = new TextField();
        removePart = new Button("Remove Associated Part");
        addPart = new Button("Add");
        
        VBox input = new VBox(20);
        input.setPrefWidth(600);
        input.getStyleClass().add("product-input");

        input.getChildren().add(buildHeader(index));
        input.getChildren().add(layoutForm());
        
        HBox partFormContainer = new HBox(40, input, buildPartsPane(index));
        partFormContainer.getStyleClass().add("border-box");
        
        configSearchBar();
        configTables(availableParts);
        configTables(assocParts);
        
        populateForm(index);
        
        getChildren().add(partFormContainer);
        
        this.setPadding(new Insets(20));
        getStylesheets().add("file:css/product_form.css");
    }
    
    /**
     * Builds the header of the form fields area.
     * @param index The index of a product being updated or modified, -1 otherwise.
     * @return The layout container of the header.
     */
    @Override
    protected Pane buildHeader(int index) {
        
        Label formTitle = new Label((index >= 0 ? "Modify" : "Add") + " Product");
        formTitle.getStyleClass().add("form-title");
        formTitle.setPrefWidth(200);
        
        HBox header = new HBox(formTitle);
        header.setAlignment(Pos.CENTER_LEFT);
        
        return header;
    }
    
    /**
     * Defines user input area of form.
     * @return The layout container of the form fields area.
     */
    @Override
    protected GridPane layoutForm() {
        
        GridPane gp = super.layoutForm();
        gp.getStyleClass().add("form-field-container");
        
        return gp;
    }
    
    // Builds the GUI component holding the tables allowing user to 
    // associated parts with the product from table of all available parts.
    private Pane buildPartsPane(int index) {
        
        VBox pane = new VBox(15);
        
        pane.getChildren().add(search);
        pane.getChildren().add(availableParts);
        
        HBox addPartWrap = new HBox(addPart);
        addPartWrap.getStyleClass().add("add-part-btn-wrapper");
        addPartWrap.setAlignment(Pos.CENTER_RIGHT);
        
        pane.getChildren().add(addPartWrap);
        pane.getChildren().add(assocParts);
        pane.getChildren().add(configBtns(index));
        
        pane.setPadding(new Insets(20));
        pane.setAlignment(Pos.CENTER_RIGHT);

        return pane;
    }
    
    // Create a search bar for the available parts table.
    private void configSearchBar() {
        
        search.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    
                    partSearch.setPredicate(createPredicate(newValue));
                   
                    availableParts.getSelectionModel().selectFirst();
                    
                    if(newValue.isEmpty()) 
                        availableParts.getSelectionModel().clearSelection();

                    enableDisableBtns(availableParts);
                }); 
        
        search.focusedProperty().addListener((observable, oldVal, newVal) -> {
            
            if(newVal) {
                search.setAlignment(Pos.CENTER_LEFT);
            } else if(!search.getText().isEmpty()) {
                search.setAlignment(Pos.CENTER_LEFT);
            } else {
                search.setAlignment(Pos.CENTER);
            }
        });
        
        search.setPromptText("Search by Part ID or Name");
        search.setMaxWidth(180);
        search.setAlignment(Pos.CENTER);
    }
    
    /******** Helper methods for available parts table search bar ********/
    
    private Predicate<Part> createPredicate(String text){
        return part -> {
            if(text == null || text.isEmpty())
                return true;
            
            return searchPart(part, text);
        };
    }
    
    private boolean searchPart(Part part, String text) {
        return( Integer.toString(part.getId()).startsWith(text) ||
                part.getName().toLowerCase().startsWith(text.toLowerCase()));
    }
    
    // Configures the available and associated parts tables.
    @SuppressWarnings("unchecked")
    private void configTables(TableView<Part> table) {
        
        TableColumn<Part, String> id = new TableColumn<>("Part ID");
        TableColumn<Part, String> n = new TableColumn<>("Part Name");
        TableColumn<Part, String> stock = new TableColumn<>("Inventory Level");
        TableColumn<Part, String> cost = new TableColumn<>("Price/Cost per Unit");
        
        id.setMinWidth(80);
        n.setMinWidth(100);
        stock.setMinWidth(140);
        cost.setMinWidth(160);
        
        id.getStyleClass().add("table-col-label");
        n.getStyleClass().add("table-col-label");
        stock.getStyleClass().add("table-col-label");
        cost.getStyleClass().add("table-col-label");
        
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        n.setCellValueFactory(new PropertyValueFactory<>("name"));
        stock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        cost.setCellValueFactory(new PropertyValueFactory<>("price"));
        
        table.getColumns().addAll(id, n, stock, cost);
        
        table.setRowFactory(tv -> {
            final TableRow<Part> r = new TableRow<>();
            
            r.addEventFilter(MouseEvent.MOUSE_PRESSED,  e -> {
                int i = r.getIndex();
                
                if(i >= 0 && i < tv.getItems().size() && tv.getSelectionModel().isSelected(i)) {
                    
                    tv.getSelectionModel().clearSelection();
                    e.consume();
                    
                }else {
                    tv.getSelectionModel().select(i);
                }
                
                enableDisableBtns(tv);
            });
            
            return r;
        });
        
        table.setPrefSize(480, 200);
    }
    
    // If an available part is not selected Add Button is disabled.
    // If an associated part is not selected Remove Part Button is disable.
    // Otherwise these buttons are enabled.
    private void enableDisableBtns(TableView<Part> tv) {
        
        if(tv == availableParts) {
            if(availableParts.getSelectionModel().getSelectedItem() != null) 
                addPart.setDisable(false);
            else 
                addPart.setDisable(true);
        } else {
            if(assocParts.getSelectionModel().getSelectedItem() != null) 
                removePart.setDisable(false);
            else 
                removePart.setDisable(true);
        }
        
    }
    
    /**
     * Populates the fields of an update or modify form with the product determined by index.
     * @param index The index of a product to update or modify, -1 otherwise.
     */
    @Override
    protected void populateForm (int index) {
        
        Product prod = index >= 0 ? Inventory.getAllProducts().get(index) : null;
    
        if(prod != null) {
            setId(prod.getId());
            setName(prod.getName());
            setPrice(prod.getPrice());
            setStock(prod.getStock());
            setMax(prod.getMax());
            setMin(prod.getMin());
            
            assocParts.setItems(prod.getAllAssociated());
                        
        }
    }

    /**
     * Configures layout and action of form's buttons.
     * @return The layout container for the form buttons.
     */
    @Override
    protected Pane configBtns() {

        getSaveBtn().setOnAction(e -> {
            
                if(formValidation()) {
                
                    int id = Inventory.getAllProducts().size();

                    Inventory.addProduct(new Product(++id, getName(), getPrice(), getStock(), getMin(), getMax()));
                    
                    if(assocParts.getItems().size() > 0) {
                        
                        for(Part part : assocParts.getItems()) {
                            Inventory.lookupProduct(id).addAssociatedPart(part);
                        }
                    }

                    closeForm();
                }
            });
        
        // add part to assoc parts
        addPart.setOnAction(e -> { 
            
            Part part = availableParts.getSelectionModel().getSelectedItem();
            
            for(Part p : assocParts.getItems()) {
                if(part != null && part.getId() == p.getId()) {
                    part = null;
                }
            }
            
            if(part != null)
                assocParts.getItems().add(part);
            
            availableParts.getSelectionModel().clearSelection();
            enableDisableBtns(availableParts);
        });
        
        // remove part logic
        removePart.setOnAction(e -> {
            
            Part part = assocParts.getSelectionModel().getSelectedItem();
            
            if(part != null) {
                
                Alert c = new Alert(Alert.AlertType.CONFIRMATION);
                c.setHeaderText("REMOVE PART");
                c.setContentText("Are you sure you want to remove part?");
                c.showAndWait().ifPresent(response -> {
                                    if (response == ButtonType.OK) {
                                        assocParts.getItems().remove(part);
                                        assocParts.getSelectionModel().clearSelection();
                                        enableDisableBtns(assocParts);
                                    }
                                });
            }
            
            
        });
        
        getCancelBtn().setOnAction(e -> {
            closeForm();
        });
        
        addPart.setDisable(true);
        removePart.setDisable(true);
        
        HBox btnBox = new HBox(50, getSaveBtn(), getCancelBtn());
        btnBox.setAlignment(Pos.CENTER_RIGHT);
        
        VBox btnContainer = new VBox(15, removePart, btnBox);
        btnContainer.setAlignment(Pos.CENTER_RIGHT);
        btnContainer.getStyleClass().add("product-form-btn-wrapper");
        
        return btnContainer;         
    }
    
    // Provides default layout and configuration for all buttons through
    // conFigBtns(). Configures Save button to update product determined by index.
    // If index is -1, provides default save button configuration through 
    // configBtns();
    private Pane configBtns(int index) {
        
        VBox btnBox = (VBox) configBtns();
 
        if(index >= 0) {

            getSaveBtn().setOnAction(e -> {

                if(formValidation()) {
                    
                    Inventory.updateProduct(index, new Product(getItemId(), getName(), getPrice(), getStock(), getMin(), getMax()));
                    
                    Product prod = Inventory.getAllProducts().get(index);
                    ObservableList<Part> tmp = prod.getAllAssociated();
                    
                    for(Part part : tmp) {
                        prod.deleteAssociatedPart(part);
                    }
                    
                    for(Part part : assocParts.getItems()) {
                        prod.addAssociatedPart(part);
                    }
                    
                    closeForm();
                }
            });
           
        }
        
        return btnBox;
    }
    
    /**
     * Defines action of closing the form- HomePage redirection. 
     */
    @Override
    protected void closeForm() {
        getStage().setScene(new Scene(new HomePage(getStage())));
    }
}

