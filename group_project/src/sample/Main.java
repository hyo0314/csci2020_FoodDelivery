package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Main extends Application {


    private BorderPane layout;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Food Delivery App");

        /*Image background = new Image("background-food.jpg");

        ImageView BGImage = new ImageView();
        BGImage.setImage(background);
        BGImage.setFitWidth(200);
        BGImage.setSmooth(true);

        Group imageRoot = new Group();
        //Scene scene1 = new Scene(imageRoot);
        HBox hbox = new HBox();
        hbox.getChildren().add(BGImage);
        imageRoot.getChildren().add(hbox);*/


        layout = new BorderPane();

        GridPane editArea = new GridPane();
        editArea.setPadding(new Insets(10, 10, 10, 10));
        editArea.setVgap(10);
        editArea.setHgap(10);

        Label nameLabel = new Label("Enter Restaurant Name:");
        editArea.add(nameLabel, 25, 40);
        TextField nameField = new TextField();
        nameField.setPromptText("restaurant name here");
        editArea.add(nameField, 25, 41);


        Button addButton = new Button("Go");
        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                String restaurantName = nameField.getText();


                nameField.setText("");

            }
        });
        editArea.add(addButton, 25, 43);

        layout.setCenter(editArea);
        Scene scene = new Scene(layout, 700, 700);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
