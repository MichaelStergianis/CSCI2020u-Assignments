package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.CsvReadWrite;

/**
 * Created by Irfaan on 30/03/2016.
 */

//Optional settings list
    //user settings
        //set nickname
        //set major
        //set first name
        //set last name
    //print information entered to csv file and save in specific file/folder
    //have function to load default settings or create a default file if no files detected
public class Settings {


    public static void display(String title, String message) {
        //Default
        String user = "Username";
        String fname = "First Name";
        String lname = "Last Name";
        String major = "Major";

        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(200);
        window.setMinHeight(200);
        Label label = new Label();
        label.setText(message);

        GridPane editArea = new GridPane();
        editArea.setPadding(new Insets(10, 10, 10, 10));
        editArea.setVgap(10);
        editArea.setHgap(10);

        Label userLabel = new Label("Username:");
        editArea.add(userLabel, 0, 0);
        TextField userField = new TextField();
        userField.setPromptText(user);
        editArea.add(userField, 1, 0);

        Label fnameLabel = new Label("First name:");
        editArea.add(fnameLabel, 0, 1);
        TextField fnameField = new TextField();
        fnameField.setPromptText(fname);
        editArea.add(fnameField, 1, 1);

        Label lnameLabel = new Label("Last name:");
        editArea.add(lnameLabel, 0, 2);
        TextField lnameField = new TextField();
        lnameField.setPromptText(lname);
        editArea.add(lnameField, 1, 2);

        Label majorLabel = new Label("Major:");
        editArea.add(majorLabel, 0, 3);
        TextField majorField = new TextField();
        majorField.setPromptText(major);
        editArea.add(majorField, 1, 3);

        Button saveButton = new Button("Save and close");
        saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                String user = userField.getText();
                String fname = fnameField.getText();
                String lname = lnameField.getText();
                String major = majorField.getText();
                //write out to csv file
                //and overwrite old variables
                //reload csv file when opening chat client
                sample.CsvReadWrite.CsvWrite(user, fname, lname, major);
                userField.setText("");
                fnameField.setText("");
                lnameField.setText("");
                majorField.setText("");
                Alert.display("Success!", "Settings Saved!");
                window.close();
            }
        });
        Button exitButton = new Button("Exit");
        exitButton.setOnAction(e -> {
            boolean result = Exit.display("Exit without saving?");
            if (result == true){window.close();}
        });
        editArea.add(saveButton, 1, 4);
        editArea.add(exitButton, 2, 4);
        BorderPane layout = new BorderPane();
        layout.setCenter(editArea);
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }
}
