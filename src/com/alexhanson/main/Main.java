
package com.alexhanson.main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Entry point of application, launches the app's HomePage.
 * @author Alex Hanson
 */
public class Main extends Application {
    
    /**
     * Program entry.
     * @param args Command line options
     */
    public static void main(String [] args) {
        
        launch(args);
    }
    
    /**
     * Starts the applications GUI interface.
     * @param primaryStage The app's primary Stage.
     */
    @Override
    public void start(Stage primaryStage) {
        
        primaryStage.setScene(new Scene(new HomePage(primaryStage)));
        primaryStage.show();
    }
  
    
}
