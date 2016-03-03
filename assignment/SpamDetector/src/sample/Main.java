package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class Main extends Application {

    private TableView<TestFile> testFileTable;
    private BorderPane layout;
    private FileInputStream input = null;
    private FileOutputStream output = null;


    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Spam Detector 3000");

        testFileTable = new TableView<>();
        //testFileTable.setItems();
        testFileTable.setEditable(true);

        TableColumn<TestFile,String> fileColumn = null;
        fileColumn = new TableColumn<>("File");
        fileColumn.setMinWidth(300);
        fileColumn.setCellValueFactory(new PropertyValueFactory<>("filename"));

        TableColumn<TestFile, Double> spamProbColumn = null;
        spamProbColumn = new TableColumn<>("Spam Probability");
        spamProbColumn.setMinWidth(200);
        spamProbColumn.setCellValueFactory(new PropertyValueFactory<>("spamProbability"));

        TableColumn<TestFile, String> actualClassColumn = null;
        actualClassColumn = new TableColumn<>("Actual Class");
        actualClassColumn.setMinWidth(200);
        actualClassColumn.setCellValueFactory(new PropertyValueFactory<>("actualClass"));

        testFileTable.getColumns().add(fileColumn);
        testFileTable.getColumns().add(actualClassColumn);
        testFileTable.getColumns().add(spamProbColumn);

        layout = new BorderPane();
        layout.setCenter(testFileTable);


        Scene scene = new Scene(layout, 700, 700);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
