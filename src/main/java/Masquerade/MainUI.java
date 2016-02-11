package Masquerade;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainUI extends Application{
    @Override public void start(Stage primaryStage) {
        primaryStage.setTitle("Masquerade");

        Menu fileMenu = new Menu("File");


        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(fileMenu);

        BorderPane layout = new BorderPane();
        layout.setTop(menuBar);

        Scene scene = new Scene(layout, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args){
        launch(args);
    }
}