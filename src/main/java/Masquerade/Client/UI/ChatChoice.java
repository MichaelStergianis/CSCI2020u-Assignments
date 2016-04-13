package Masquerade.Client.UI;

import Masquerade.Client.Chat;
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
    private Chat chat;


    public ChatChoice(Chat chat){
        this.chat = chat;
    }


    public void display() {
        Platform.setImplicitExit(false);
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("New Chat");
        window.setMinWidth(400);
        window.setMinHeight(200);
        Label label = new Label();
        label.setText("Do you wish to chat with " + chat.getRecipient() + "?");

        Button yesButton = new Button("Yes");
        Button noButton = new Button("No");

        yesButton.setOnAction(e -> {
            chat.setApproved(true);
            window.close();
        });
        noButton.setOnAction(e -> {
            chat.setApproved(false);
            window.close();
        });

        VBox layout = new VBox(10);

        layout.getChildren().addAll(label, yesButton, noButton);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.show();
    }

    @Override
    public void run(){
        display();
    }
}
