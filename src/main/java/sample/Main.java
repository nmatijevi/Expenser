package main.java.sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.xml.crypto.Data;

public class Main extends Application {

    private static Stage mainStage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        mainStage = primaryStage;
        Parent root =
                FXMLLoader.load(getClass().getClassLoader().getResource(
                        "login.fxml"));
        mainStage.setTitle("Expenser");
        mainStage.setScene(new Scene(root, 800, 550));
        mainStage.show();
    }
    public static void setMainStage(Stage newStage) {
        mainStage = newStage;

    }

    public static Stage getMainStage() {
        return mainStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
