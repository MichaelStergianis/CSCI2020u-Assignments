package sample;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Main extends Application {

    Stage window;
    private BorderPane layout;
    private TableView<Contact> contactList = new TableView();

    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;
        window.setTitle("Messenger");
        String[] test = null;

        /* create the menu (for the top of the user interface) */
        Menu fileMenu = new Menu("File");
        MenuItem settingsMenuItem = new MenuItem("User Settings");
        fileMenu.getItems().add(settingsMenuItem);
        settingsMenuItem.setOnAction(e -> {
            Settings.display("Settings", "Testing settings");
        });
        MenuItem exitMenuItem = new MenuItem("Exit");
        fileMenu.getItems().add(exitMenuItem);
        exitMenuItem.setAccelerator(KeyCombination.keyCombination("Ctrl+Q"));
        exitMenuItem.setOnAction(e -> {
            boolean result = Exit.display("Closing Messenger");
            //testing return value of result for exit
            //System.out.println(result);
            if (result == true){window.close();}
        });
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(fileMenu);

        //Chat Box
        GridPane functionArea = new GridPane();
        Button chatButton;
        chatButton = new Button("Chat");
        chatButton.setOnAction(e -> {
                String selected = contactList.getSelectionModel().getSelectedItem().getContactName();

            if (selected == null){
                Alert.display("Error", "No one selected to chat with.");
            }else{
                ChatBox.display("Conversation with " + selected, "Does it work?");
            }
        });
        functionArea.add(chatButton,1,1);
        functionArea.setAlignment(Pos.CENTER);

        //Table for contacts
        contactList.setItems(Contact.getContactList());
        TableColumn contactColumn;
        contactColumn = new TableColumn("Contacts");
        contactColumn.setPrefWidth(300);
        contactColumn.setMinWidth(300);
        contactColumn.setCellValueFactory(new PropertyValueFactory<>("ContactName"));
        contactList.getColumns().add(contactColumn);
        contactList.setPadding(new Insets(0, 0, 1, 0));


        //Main Contacts window
        layout = new BorderPane();
        layout.setTop(menuBar);
        layout.setCenter(contactList);
        layout.setBottom(functionArea);
        window.setMinHeight(500);
        window.setMaxWidth(302);
        Scene scene = new Scene(layout, 302, 500);
        window.setScene(scene);
//      Trying to make a default folder for settings to save into and test input/output of csvreadwrite
/*
        DirectoryChooser d = new DirectoryChooser();
        String path = null;
        File selectedDirectory =
                d.showDialog(primaryStage);
        path= selectedDirectory.getPath();*/
        test = sample.CsvReadWrite.CsvRead("C:\\Users\\Irfaan\\IdeaProjects\\ChatProgramTest\\Settings\\settings.csv");
        System.out.println(test);


        window.show();
    }


    public static void main(String[] args) {launch(args);}


}