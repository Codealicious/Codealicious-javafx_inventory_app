
package com.alexhanson.main;

import javafx.stage.Stage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;


/**
 * The main page of the app.
 * It contains: the title, display tables for
 * parts and products, and an exit button to quit the application.
 * 
 * @author Alex Hanson
 */
public class HomePage extends VBox {
    
    /**
     * Constructor: Initializes the HomePage app component with a reference to the app's primary stage
     * and adds the necessary CSS StyleSheets.
     * @param appStage A reference to the application's primary stage. This reference
     * is used to display all subsequent scenes the HomePage may create i.e. inventory forms.
     */
    public HomePage(Stage appStage) {
        
        super(20);
        buildHomeScene(appStage);
        getStylesheets().add("file:css/homepage.css");
    }
    
    // Configure the layout of the HomePage and configure the exit button.
    private void buildHomeScene(Stage appStage) {
        
        Label homepageHeader = new Label("Inventory Management System");
        homepageHeader.getStyleClass().add("homepage-title");
        getChildren().add(homepageHeader);
       
        HBox tablesContainer = new HBox(50, new PartTableWidget(appStage, Inventory.getAllParts()),
                                            new ProductTableWidget(appStage, Inventory.getAllProducts()));
        
        tablesContainer.getStyleClass().add("table-view-container");
        
        getChildren().add(tablesContainer);
        
        Button exitBtn = new Button("Exit");
        
        exitBtn.setOnAction(e -> {
            Alert c = new Alert(Alert.AlertType.CONFIRMATION);
            c.setHeaderText("EXIT");
            c.setContentText("Are you sure you want to exit?");
            c.showAndWait().ifPresent(response -> {
                                if (response == ButtonType.OK) {
                                    appStage.close();
                                }
                            });
        });
                
        HBox exitBox = new HBox(exitBtn);
        exitBox.setAlignment(Pos.CENTER_RIGHT);
        exitBox.getStyleClass().add("exit-btn");
        
        getChildren().add(exitBox);
        
        setPadding(new Insets(30));
    }    
    
}