/**
 * Created by Irfaan on 30/03/2016.
 */

package Masquerade.Client.UI;

import Masquerade.Client.Chat;
import javafx.scene.input.KeyCombination;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

import javax.swing.*;

public class ChatWindow implements Runnable{
    Chat chat;
    TextArea textArea;

    public ChatWindow(Chat chat){
        this.chat = chat;
        this.textArea = chat.getTextArea();
    }
    public void display() {
        Stage chatWindowStage = new Stage();
        chatWindowStage.setTitle("Speaking to " + chat.getRecipient());


        MenuBar menuBar = new MenuBar();

        Menu fileMenu = new Menu("File");
        MenuItem exitMenuItem = new MenuItem("Exit");
        fileMenu.getItems().add(exitMenuItem);
        exitMenuItem.setAccelerator(KeyCombination.keyCombination("Ctrl+Q"));
        exitMenuItem.setOnAction(e -> {
            boolean result = ConfirmBox.display("Closing Chat", "Do you accept?");
            //testing return value of result for exit
            //System.out.println(result);
            if (result == true){
                chat.disconnect();
                chatWindowStage.close();
            }
        });

        menuBar.getMenus().add(fileMenu);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));

        grid.add(textArea, 0, 0);

        TextField messageField = new TextField();
        grid.add(messageField, 0, 1);

        Button sendButton;
        sendButton = new Button("Send");
        sendButton.setOnAction(e -> {
            chat.sendMessage(messageField.getText());
            messageField.clear();
        });
        grid.add(sendButton, 1, 1);

        BorderPane layout = new BorderPane();
        layout.setTop(menuBar);

        layout.setCenter(grid);
        Scene scene = new Scene(layout, 800, 550);

        chatWindowStage.setScene(scene);
        chatWindowStage.show();
    }

    @Override
    public void run(){
        display();
    }

}
