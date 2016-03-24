package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Main extends Application {

    private BorderPane layout;
    private TableView<FileInfo> clientFolder;
    private TableView<FileInfo> serverFolder;
    private String clientPath;
    private File[] filesInDir;
    private String[] fileInStringClient;
   // private ObservableList<FileInfo> data;
    private int count;

    @Override
    public void start(Stage primaryStage) throws Exception{
        File cDir = new File("/home/mobile/Desktop/clientDirTest");
        count = 0;

        getClientFileName(cDir);

        final ObservableList data = FXCollections.observableArrayList();

        for(int i = 0; i<fileInStringClient.length; i++)
        {
            System.out.println(fileInStringClient[i]);
            data.add(new FileInfo(fileInStringClient[i]));
        }

        GridPane editArea = new GridPane();

        Button upload = new Button("Upload");
        upload.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            }
        });

        Button download = new Button("Download");
        download.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            }
        });

        Button endConnection = new Button("End");
        endConnection.setOnAction(event -> System.exit(0));

        editArea.add(upload,0,0);
        editArea.add(download,1,0);
        editArea.add(endConnection,10,0);

        clientFolder = new TableView<>();

        clientFolder.setItems(data);
        clientFolder.setEditable(true);
        //Table Colume for client
        TableColumn<FileInfo,String> clientFile = null;
        clientFile = new TableColumn<>();
        clientFile.setMinWidth(300);
        clientFile.setCellValueFactory(new PropertyValueFactory<>("fileNames"));

        clientFolder.getColumns().add(clientFile);

        serverFolder = new TableView<>();
        serverFolder.setItems(data);
        serverFolder.setEditable(true);
        //Table Colume for server
        TableColumn<FileInfo,String> serverFile = null;
        serverFile = new TableColumn<>();
        serverFile.setMinWidth(300);
        serverFile.setCellValueFactory(new PropertyValueFactory<>("server"));

       serverFolder.getColumns().add(serverFile);


        primaryStage.setTitle("File Sharer v 1.0");

        layout = new BorderPane();
        layout.setLeft(clientFolder);
        layout.setRight(serverFolder);
        layout.setTop(editArea);

        Scene scene = new Scene(layout, 650, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void getClientFileName (File file) {

            if (file.isDirectory()) {
                // process all of the files recursively
                filesInDir = file.listFiles();
                fileInStringClient = new String[filesInDir.length];
                for (int i = 0; i < filesInDir.length; i++) {
                    getClientFileName(filesInDir[i]);
                }
            }
            else if(file.exists())
            {
                System.out.println(file.getName());
                fileInStringClient[count] = file.getName();
                count++;
            }


    }



    public static void main(String[] args) {
        launch(args);
    }
}
