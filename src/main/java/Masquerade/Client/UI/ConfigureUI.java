package Masquerade.Client.UI;

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

public class ConfigureUI extends Application{
    @Override public void start(Stage primaryStage) {
        primaryStage.setTitle("Masquerade");

        Menu fileMenu = new Menu("File");

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(fileMenu);
        // Create exit option
        MenuItem exitMenu = new MenuItem();
        exitMenu.setText("Exit");
        exitMenu.setOnAction(actionEvent -> {
            System.exit(0);
        });
        fileMenu.getItems().add(exitMenu);


        BorderPane layout = new BorderPane();

        // Create a grid for configuration
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(10);
        grid.setHgap(10);

        Label promptLabel = new Label("Configuration");
        grid.add(promptLabel, 0, 0);

        Label userLabel = new Label("Username");
        grid.add(userLabel, 0, 1);
        final PasswordField userField = new PasswordField();
        userField.setPromptText("Username");
        grid.add(userField, 1, 1);

        Label passLabel = new Label("Password");
        grid.add(passLabel, 0, 2);
        final PasswordField passField = new PasswordField();
        passField.setPromptText("************");
        grid.add(passField, 1, 2);

        Label passLabel2 = new Label("Re-enter Password");
        grid.add(passLabel2, 0, 3);
        final PasswordField passField2 = new PasswordField();
        passField2.setPromptText("************");
        grid.add(passField2, 1, 3);

        Label errorLabel = new Label();
        errorLabel.setPrefWidth(175);
        grid.add(errorLabel, 0, 4);

        Button regButton = new Button("Submit");
        regButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
            }
        });
        grid.add(regButton, 0, 5);

        layout.setTop(menuBar);
        layout.setCenter(grid);

        Scene scene = new Scene(layout, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();

    }


    public static void main(String[] args){
        launch(args);
    }
}