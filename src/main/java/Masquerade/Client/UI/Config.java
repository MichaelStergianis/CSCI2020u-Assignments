package Masquerade.Client.UI;

import Masquerade.Client.User;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

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
public class Config {
    Stage window;
    String [] ret;

    public Config(Stage window){
        this.window = window;
        this.ret = new String[4];
    }

    public String[] display() {
        //Default
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Configuration");
        window.setMinWidth(350);
        window.setMinHeight(250);

        GridPane editArea = new GridPane();
        editArea.setPadding(new Insets(10, 10, 10, 10));
        editArea.setVgap(10);
        editArea.setHgap(10);

        Label userLabel = new Label("Username:");
        editArea.add(userLabel, 0, 0);

        TextField userField = new TextField();
        userField.setPromptText("username");
        editArea.add(userField, 1, 0);

        Label portLabel = new Label("Listen on Port (above 1024):");
        editArea.add(portLabel, 0, 1);

        TextField portField = new TextField();
        portField.setPromptText("0");
        editArea.add(portField, 1, 1);

        Label serverLabel = new Label("Server");
        editArea.add(serverLabel, 0, 2);

        TextField serverField = new TextField();
        serverField.setPromptText("127.0.0.1");
        editArea.add(serverField, 1, 2);

        Label serverPortLabel = new Label("Server Port:");
        editArea.add(serverPortLabel, 0, 3);

        TextField serverPortField = new TextField();
        serverPortField.setPromptText("0");
        editArea.add(serverPortField, 1, 3);

        Button exitButton = new Button("Submit");
        exitButton.setOnAction(e -> {
            if (ConfirmBox.display("Are you Certain?", "Are username and port correct?")){
                ret[0] = userField.getText();
                ret[2] = serverField.getText();
                String tempPort = portField.getText();
                String tempServerPort = serverPortField.getText();
                if (!tempPort.matches("[[[0-9]{4,6}][0]]")) {
                    Alert.display("Invalid Port", "Defaulting to OS selected port");
                    tempPort = "0";
                }
//                if (!tempServerPort.matches("[[[0-9]{4,6}][0]]")) {
//                    Alert.display("Invalid Port", "Defaulting to OS selected port");
//                    tempServerPort = "0";
//                }
                ret[1] = tempPort;
                ret[3] = tempServerPort;
                window.close();
            }
        });
//        editArea.add(saveButton, 1, 4);
        editArea.add(exitButton, 1, 4);
        BorderPane layout = new BorderPane();
        layout.setCenter(editArea);
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
        return ret;
    }
}
