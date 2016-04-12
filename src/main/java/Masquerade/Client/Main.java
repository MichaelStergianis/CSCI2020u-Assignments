package Masquerade.Client;

import Masquerade.Client.UI.*;
import Masquerade.Client.UI.Alert;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Scanner;

public class Main extends Application {

    Stage window;
    private BorderPane layout;
    private User user;

    @Override
    public void start(Stage primaryStage) {
        Config config = new Config(new Stage());
        String[] userInfo = config.display();

        user = new User(userInfo[0], new Integer(userInfo[1]));
        ChatListener chatListener = new ChatListener(user);
        Thread chatListenerThread = new Thread(chatListener);
        chatListenerThread.start();
        System.out.println(user.getUserName() + " " + user.getListenSocket().getLocalPort());
        System.out.println(userInfo[2] + " " + userInfo[3]);
        user.connectToServer(userInfo[2], new Integer(userInfo[3]));

        window = primaryStage;
        window.setTitle("Masquerade");

        /* create the menu (for the top of the user interface) */
        Menu fileMenu = new Menu("File");
        MenuItem exitMenuItem = new MenuItem("Exit");
        fileMenu.getItems().add(exitMenuItem);
        exitMenuItem.setAccelerator(KeyCombination.keyCombination("Ctrl+Q"));
        exitMenuItem.setOnAction(e -> {
            boolean result = ConfirmBox.display("Closing Messenger", "Do you accept?");
            //testing return value of result for exit
            //System.out.println(result);
            if (result == true){
                try {
                    user.logout();
                } catch (Exception e1){
                    Alert.display("Not logged in", e1.getMessage());
                }
                window.close();
                return;
            }
        });
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(fileMenu);

        // Grid pane configuration
        GridPane functionArea = new GridPane();
        functionArea.setPadding(new Insets(10, 10, 10, 10));
        functionArea.setHgap(10);
        functionArea.setVgap(10);

        // username selection
        Label userLabel = new Label("Username:");
        functionArea.add(userLabel, 1, 0);

        TextField userField = new TextField();
        userField.setPromptText("linus");
        functionArea.add(userField, 1, 1);

        //Chat Box
        Button chatButton;
        chatButton = new Button("Chat");
        chatButton.setOnAction(e -> {
            try {
                Chat chat = user.startChat(userField.getText());

            } catch (Exception e1){
                Alert.display("Not logged in", e1.getMessage());
            }
        });
        functionArea.add(chatButton,1,2);
        functionArea.setAlignment(Pos.CENTER);

        //Table for contacts
//        contactList.setItems(Contact.getContactList());
//        TableColumn contactColumn;
//        contactColumn = new TableColumn("Contacts");
//        contactColumn.setPrefWidth(300);
//        contactColumn.setMinWidth(300);
//        contactColumn.setCellValueFactory(new PropertyValueFactory<>("ContactName"));
//        contactList.getColumns().add(contactColumn);
//        contactList.setPadding(new Insets(0, 0, 1, 0));

        //Main Contacts window
        layout = new BorderPane();
        layout.setTop(menuBar);
        layout.setCenter(functionArea);
        window.setMinHeight(200);
        window.setMaxWidth(300);
        Scene scene = new Scene(layout, 300, 200);
        window.setScene(scene);
        window.show();
    }


    public static void main(String[] args) {
        launch(args);
//        User user = new User(args[0]);
//        System.out.println("ListenPort: " + user.getListenSocket().getLocalPort());
//        user.connectToServer("127.0.0.1", 8020);
//        try {
//            if (args[1].equals("y")) {
//                user.startChat(args[2]);
//            }
//            System.out.print("Ready to logout? [y/n]: ");
//            Scanner s = new Scanner(System.in);
//            String answer = "n";
//            while (!answer.equalsIgnoreCase("y")){
//                answer = s.next();
//                if (answer.equalsIgnoreCase("y")) {
//                    user.logout();
//                }
//            }
//        } catch (Exception e){
//            e.printStackTrace();
//        }
    }

    public static boolean displayChatChoice(Chat chat){
        ChatChoice chatChoice = new ChatChoice(chat.getRecipient());
        Platform.runLater(chatChoice);
        return chatChoice.display();
    }

    public static void createChatWindow(String[] request){
        Chat chat = new Chat();
        Platform.runLater(chat);
        ChatWindow chatWindow = new ChatWindow(chat);
        Platform.runLater(chatWindow);
    }

    public static void handleIncomingChat(Chat chat){

    }


}