package Masquerade.Client.UI;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Created by michael on 08/04/16.
 */
public class ChatChoice implements Runnable{
    Stage window;
    boolean answer;
    private String userName;


    public ChatChoice(String userName){
        this.userName = userName;
    }


    public boolean display() {
        Platform.setImplicitExit(false);
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("New Chat");
        window.setMinWidth(800);
        window.setMinHeight(600);
        Label label = new Label();
        label.setText("Do you wish to chat with " + userName + "?");

        Button yesButton = new Button("Yes");
        Button noButton = new Button("No");

        yesButton.setOnAction(e -> {
            answer = true;
            window.close();
        });
        noButton.setOnAction(e -> {
            answer = false;
            window.close();
        });

        VBox layout = new VBox(10);

        layout.getChildren().addAll(label, yesButton, noButton);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.show();
        return answer;
    }

    @Override
    public void run(){
        display();
    }
}
