package Masquerade;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Created by michael on 14/03/16.
 */
public class Main extends Application{

    @Override public void start(Stage primaryStage){
        Stage configureStage = new Stage();
        ConfigureUI configureUI = new ConfigureUI();
        configureUI.start(configureStage);
    }

    public static void main(String[] args){
        launch(args);
    }
}
