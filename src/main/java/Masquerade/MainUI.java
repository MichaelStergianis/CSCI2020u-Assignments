package Masquerade;

import javafx.application.Application;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class MainUI extends Application{
    @Override public void start(Stage primaryStage) {
        Keys keys = new Keys();
        primaryStage.setTitle("Masquerade");

        Menu fileMenu = new Menu("File");

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(fileMenu);

        BorderPane layout = new BorderPane();

        // Create a grid for configuration
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(10);
        grid.setHgap(10);

        Label passLabel = new Label("Password");
        grid.add(passLabel, 0, 0);
        final PasswordField passField = new PasswordField();
        passField.setPromptText("************");
        grid.add(passField, 1, 0);

        Label passLabel2 = new Label("Re-enter Password");
        grid.add(passLabel2, 0, 1);
        final PasswordField passField2 = new PasswordField();
        passField2.setPromptText("************");
        grid.add(passField2, 1, 1);

        Label errorLabel = new Label("                        ");
        grid.add(errorLabel, 0, 2);

        Button regButton = new Button("Submit");
        regButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                keys.checkPassword(passField.getText(), passField2.getText(), errorLabel);
            }
        });
        grid.add(regButton, 0, 3);

        layout.setTop(menuBar);
        layout.setCenter(grid);

        Scene scene = new Scene(layout, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

    }


    public static void main(String[] args){
        launch(args);
    }
}