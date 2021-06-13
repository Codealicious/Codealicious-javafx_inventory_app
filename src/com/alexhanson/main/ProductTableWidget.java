
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
 * Display widget for all products inventory items.
 * Provides search, deletion, and access to Add and Modify forms.
 * @author Alex Hanson
 */
public class ProductTableWidget extends VBox {
    
    // Widget UI components requiring handles
    final private TableView<Product> table;
    final private TextField search;
    final private Button add;
    final private Button modify;
    final private Button delete;
    
    final private Stage appStage;
    private FilteredList<Product> data;
    
    /**
     * Constructor: Initializes table display with products inventory.
     * @param appStage Reference to application's Stage for redirection.
     * @param data The products to display.
     */
    public ProductTableWidget(Stage appStage, ObservableList<Product> data) {
        
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
    
    // Provides layout for widget title and search bar.
    private void buildHeader() {
        
        Label n = new Label("Products");
        n.setMinWidth(100);
        n.getStyleClass().add("table-title");
        HBox tableHeader = new HBox(275, n, search);
        tableHeader.setAlignment(Pos.BOTTOM_RIGHT);
        getChildren().add(tableHeader);
    }
    
    // Configures search bar functionality.
    private void searchBarConfig() {
        
        search.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    
                    data.setPredicate(createPredicate(newValue));

                    table.getSelectionModel().selectFirst();

                    if(newValue.isEmpty()) 
                        table.getSelectionModel().clearSelection();

                    enableDisableBtns();
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
        
        search.setPromptText("Search by Product ID or Name");
        search.setMinWidth(180);
        search.setAlignment(Pos.CENTER);
    }
    
    @SuppressWarnings("unchecked")
    private void tableConfig() {
        
        TableColumn<Product, String> id = new TableColumn<>("Product ID");
        TableColumn<Product, String> n = new TableColumn<>("Product Name");
        TableColumn<Product, String> stock = new TableColumn<>("Inventory Level");
        TableColumn<Product, String> cost = new TableColumn<>("Price/Cost per Unit");
        
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
        
        table.setRowFactory( tv -> {
            final TableRow<Product> r = new TableRow<>();

            r.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
                int i = r.getIndex();
                
                if(i >= 0 && i < tv.getItems().size() && tv.getSelectionModel().isSelected(i)) {
                    
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
    
    private void enableDisableBtns() {

        if(table.getSelectionModel().getSelectedItem() != null) {
            modify.setDisable(false);
            delete.setDisable(false);
        } else {
            modify.setDisable(true);
            delete.setDisable(true);
        }  
    }
    
    private void btnConfig() {
        
        add.setOnAction(e -> { 
            appStage.setScene(new Scene(new ProductForm(appStage, -1)));
        });
        
        modify.setOnAction(e -> { 
            
            Product tmp = table.getSelectionModel().getSelectedItem();
                    
            if(tmp != null)
                appStage.setScene(new Scene(new ProductForm(appStage, data.getSource().indexOf(tmp))));
        });
        
        delete.setOnAction(e -> {
            
            Product tmp = table.getSelectionModel().getSelectedItem();
            
            if(tmp != null) {
                
                Alert c = new Alert(Alert.AlertType.CONFIRMATION);
                c.setHeaderText("DELETE");
                c.setContentText("Are you sure you want to delete product?");
                c.showAndWait().ifPresent(response -> {
                                    if (response == ButtonType.OK) {
                                        
                                        if(tmp.getAllAssociated().isEmpty()){
                                            
                                            if(Inventory.deleteProduct(tmp)) {
                                                data = new FilteredList<>(Inventory.getAllProducts());
                                                table.setItems(data);
                                                // force a firing of change event to update output
                                                // while leaving original search text;
                                                String deleteSearch = search.getText();
                                                search.setText("");
                                                search.fireEvent(new KeyEvent(KeyEvent.KEY_TYPED, deleteSearch, deleteSearch, KeyCode.ALPHANUMERIC, false, false, false, false));
                                            }
                                        }else {
                                            Alert w = new Alert(Alert.AlertType.WARNING);
                                            w.setHeaderText("WARNING!");
                                            w.setContentText("Cannont delete product with associated parts!");
                                            w.show();
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
    
    private boolean searchPart(Product prod, String text) {
        return( Integer.toString(prod.getId()).startsWith(text) ||
                prod.getName().toLowerCase().startsWith(text.toLowerCase()));
    }
    
    private Predicate<Product> createPredicate(String text){
        return prod -> {
            if(text == null || text.isEmpty())
                return true;
            
            return searchPart(prod, text);
        };
    }
}
