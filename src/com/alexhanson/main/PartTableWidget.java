
package com.alexhanson.main;

import java.util.function.Predicate;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Display widget for all parts inventory items.
 * Provides search, deletion, and access to Add and Modify forms.
 * @author Alex Hanson
 */
public class PartTableWidget extends VBox {
    
    // Widget UI components requiring handles
    final private TableView<Part> table;
    final private TextField search;
    final private Button add;
    final private Button modify;
    final private Button delete;
    
    final private Stage appStage;
    private FilteredList<Part> data;
    
    /**
     * Constructor: Initializes table display with parts inventory
     * @param appStage Reference to application stage for form redirection.
     * @param data The parts to display.
     */
    public PartTableWidget(Stage appStage, ObservableList<Part> data) {
        
        super();
        this.appStage = appStage;
        this.data = new FilteredList<>(data);
        
        table = new TableView<>(this.data);
        search = new TextField();
        add = new Button("Add");
        modify = new Button("Modify");
        delete = new Button("Delete");
        
        buildHeader();
        searchBarConfig();
        tableConfig();
        btnConfig();
        
        setMinWidth(650);
        
        getStyleClass().add("border-box");
        getStylesheets().add("file:css/tablewidget.css");
    }
    
    // Provides lay out of the title and search bar.
    private void buildHeader() {
        
        Label n = new Label("Parts");
        n.setMinWidth(100);
        n.getStyleClass().add("table-title");
        HBox tableHeader = new HBox(275, n, search);
        tableHeader.setAlignment(Pos.BOTTOM_RIGHT);
        getChildren().add(tableHeader);
    }
    
    // Configures search bar functionality.
    private void searchBarConfig() {
        
        // Dynamically update search results based on user input.
        search.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    
                    data.setPredicate(createPredicate(newValue));
                    
                    table.getSelectionModel().selectFirst();

                    if(newValue.isEmpty()) 
                        table.getSelectionModel().clearSelection();

                    enableDisableBtns();
                }); 
        
        // Centers palceholder text. Left justifies input on focus.
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
        search.setMinWidth(180);
        search.setAlignment(Pos.CENTER);
    }
    
    // Configures table display.
    @SuppressWarnings("unchecked")
    private void tableConfig() {
        
        TableColumn<Part, String> id = new TableColumn<>("Part ID");
        TableColumn<Part, String> n = new TableColumn<>("Part Name");
        TableColumn<Part, String> stock = new TableColumn<>("Inventory Level");
        TableColumn<Part, String> cost =   new TableColumn<>("Price/Cost per Unit");
        
        id.setPrefWidth(90);
        n.setPrefWidth(120);
        stock.setPrefWidth(130);
        cost.setPrefWidth(150);
        
        id.getStyleClass().add("table-col-label");
        n.getStyleClass().add("table-col-label");
        stock.getStyleClass().add("table-col-label");
        cost.getStyleClass().add("table-col-label");
        
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        n.setCellValueFactory(new PropertyValueFactory<>("name"));
        stock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        cost.setCellValueFactory(new PropertyValueFactory<>("price"));
        
        table.getColumns().addAll(id, n, stock, cost);
        
        // Mouse event that allows user to deselect a row by clicking again.
        table.setRowFactory(tv -> {
            final TableRow<Part> r = new TableRow<>();
            
            r.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
                int i = r.getIndex();
                
                if(tv.getSelectionModel().isSelected(i)) {
                    
                    tv.getSelectionModel().clearSelection();
                    e.consume();
                    
                }else {
                    tv.getSelectionModel().select(i);
                }
                
                enableDisableBtns();
            });
            
            return r;
        });
        
        table.setPrefHeight(250);
        HBox tableContainer = new HBox(table);
        tableContainer.getStyleClass().add("table-container");
        tableContainer.setAlignment(Pos.CENTER_RIGHT);
        getChildren().add(tableContainer);
    }
   
    // If there is not a current item that the user has either clicked on or seareched
    // for, disable add and delete buttons.
    private void enableDisableBtns() {
        
        if(table.getSelectionModel().getSelectedItem() != null) {
            modify.setDisable(false);
            delete.setDisable(false);
        } else {
            modify.setDisable(true);
            delete.setDisable(true);
        }  
    }
    
    // Configure the add, modify, and delete button actions and layout.
    private void btnConfig() {
  
        add.setOnAction(e -> { 
            appStage.setScene(new Scene(new PartForm(appStage, -1)));
        });
        
        modify.setOnAction(e -> { 
            
            Part tmp = table.getSelectionModel().getSelectedItem();
            
            if(tmp != null) {
                appStage.setScene(new Scene(new PartForm(appStage, data.getSource().indexOf(tmp))));
            }
        });
        
        delete.setOnAction(e -> {
            
            Part tmp = table.getSelectionModel().getSelectedItem();

            if(tmp != null) {
                
                Alert c = new Alert(Alert.AlertType.CONFIRMATION);
                c.setHeaderText("DELETE");
                c.setContentText("Are you sure you want to delete part?");
                c.showAndWait().ifPresent(response -> {
                                    if (response == ButtonType.OK) {
                                        if(Inventory.deletePart(tmp)) {
                                            data = new FilteredList<>(Inventory.getAllParts());
                                            table.setItems(data);
                                            // force a firing of change event to update output
                                            // while leaving original search text;
                                            String deleteSearch = search.getText();
                                            search.setText("");
                                            search.fireEvent(new KeyEvent(KeyEvent.KEY_TYPED, deleteSearch, deleteSearch, KeyCode.ALPHANUMERIC, false, false, false, false));
                                        }
                                    }
                                });
            }
        });
            
        HBox btnBox = new HBox(40, new HBox(10, add, modify), delete);
        btnBox.getStyleClass().add("table-btns");
        btnBox.setAlignment(Pos.CENTER_RIGHT); 
        
        delete.setDisable(true);
        modify.setDisable(true);
        
        this.getChildren().add(btnBox);
    }
    
    // Provides the test for addition to search results.
    private boolean searchPart(Part part, String text) {
        return( Integer.toString(part.getId()).startsWith(text) ||
                part.getName().toLowerCase().startsWith(text.toLowerCase()));
    }
    
    // Defines the criteria by which parts are added to the filtered results
    // of the user search. Uses searchPart to determine addition of an item.
    // Provides default true value for an empty list.
    private Predicate<Part> createPredicate(String text){
        return part -> {
            if(text == null || text.isEmpty())
                return true;
            
            return searchPart(part, text);
        };
    }
}
