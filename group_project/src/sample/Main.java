package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Main extends Application {


    private BorderPane layout;

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Food Delivery App");


        layout = new BorderPane();

        GridPane textArea = new GridPane();
        textArea.setPadding(new Insets(10, 10, 10, 10));
        textArea.setVgap(10);
        textArea.setHgap(10);

        Label nameLabel = new Label("\t\tEnter Restaurant Name:");
        textArea.add(nameLabel, 25, 5);
        TextField nameField = new TextField();
        nameField.setPromptText("restaurant name here");
        textArea.add(nameField, 25, 6);

        ImageView buttonImage = new ImageView(new Image(Main.class.getResourceAsStream("go.jpg")));
        buttonImage.setFitWidth(200);
        buttonImage.setFitHeight(100);
        Button addButton = new Button("", buttonImage);
        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                String restaurantName = nameField.getText();


                nameField.setText("");
                System.out.println(restaurantName);
                primaryStage.hide();

                BorderPane layout2 =new BorderPane();
                Stage nextStage = new Stage();
                nextStage.setTitle("ByeWorld");
                nextStage.setScene(new Scene(layout2,500,500));
                nextStage.show();
            }
        });
        textArea.add(addButton, 25, 8);

        GridPane imageArea = new GridPane();
        ImageView image = new ImageView(new Image(Main.class.getResourceAsStream("background-food.jpg")));
        image.setFitWidth(750);
        image.setFitHeight(300);
        imageArea.add(image, 0, 0, 1, 2);


        layout.setTop(imageArea);
        layout.setCenter(textArea);
        Scene scene = new Scene(layout, 750, 700);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
