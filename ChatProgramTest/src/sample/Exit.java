package sample;

/**
 * Created by Irfaan on 30/03/2016.
 */

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Exit {
    //Return variable
    static boolean answer;

    public static boolean display(String title) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(300);
        Label label = new Label();
        label.setText("Confirm Exit");

        //Decision buttons
        Button yesButton = new Button("Yes");
        Button noButton = new Button("No");

        //Exit condition to true
        yesButton.setOnAction(e -> {
            answer = true;
            window.close();
        });
        //Exit condition to false
        noButton.setOnAction(e -> {
            answer = false;
            window.close();
        });

        VBox layout = new VBox(10);

        //Adding buttons to wintow
        layout.getChildren().addAll(label, yesButton, noButton);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();

        //Return decision
        return answer;
    }

}