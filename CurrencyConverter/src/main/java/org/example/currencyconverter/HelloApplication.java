package org.example.currencyconverter;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class HelloApplication extends Application {
    @Override
    public void start(Stage mainStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
        mainStage.setTitle("Currency Converter App");
        mainStage.setScene(new Scene(root, 540, 760));
        mainStage.setResizable(false);

        // End program upon user exit
        mainStage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });

        mainStage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}