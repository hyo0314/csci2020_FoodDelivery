package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {

    private BorderPane japanLayout;

    @Override
    public void start(Stage primaryStage) throws Exception{

        //Title for the Restaurant
        GridPane titleRestaurant = new GridPane();
        titleRestaurant.setPadding(new Insets(10,10,10,10));
        titleRestaurant.setVgap(10);
        titleRestaurant.setHgap(10);

        Text titleText = new Text("Japanese HH Restaurant");
        titleText.setFill(Color.DARKRED);
        titleText.setScaleX(5);
        titleText.setScaleY(5);
        titleRestaurant.add(titleText, 40,3);


        //Button for Showing the Location, the information of the Restaurant, the ORDER NOW Button
        GridPane informationArea = new GridPane();
        informationArea.setPadding(new Insets(10,10,10,10));
        informationArea.setVgap(10);
        informationArea.setHgap(10);

        Button infoButton = new Button("INFORMATION");
        informationArea.add(infoButton,0,0);

        Button locaButton = new Button("LOCATION");
        informationArea.add(locaButton,0,1);

        Button orderButton = new Button("ORDER NOW");
        informationArea.add(orderButton,0,2);

        //Table showing the total amount


        //Menu Area: menu, salad and soup, appetizer, noodle dishes, sushi, sashimi, maki menu lists
        GridPane menuArea = new GridPane();
        menuArea.setPadding(new Insets(10,10,10,10));
        menuArea.setVgap(10);
        menuArea.setHgap(10);

        Label menuLabel = new Label("Menu");
        menuArea.add(menuLabel,0,2);

        Label saladSoupMenuLabel = new Label("Salad & Soup");
        menuArea.add(saladSoupMenuLabel,1,4);

        Label appetizerLabel = new Label("Appetizers");
        menuArea.add(appetizerLabel,1,17);

        Label noodleLabel = new Label("Noodle Dishes");
        menuArea.add(noodleLabel,1,30);

        Label sushiLabel = new Label("Sushi");
        menuArea.add(sushiLabel,1,43);

        Label sashimiLabel = new Label("Sashimi");
        menuArea.add(sashimiLabel,1,56);

        Label makiLabel = new Label("Maki");
        menuArea.add(makiLabel, 1, 69);

        japanLayout = new BorderPane();
        japanLayout.setTop(titleRestaurant);
        japanLayout.setLeft(menuArea);

        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(japanLayout, 1000,1000));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
