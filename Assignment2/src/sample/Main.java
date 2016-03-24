package sample;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
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

import javax.sound.sampled.Port;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Main extends Application {

    private BorderPane layout;
    private TableView<FileInfo> clientFolder;
    private TableView<FileInfo> serverFolder;
    private String clientPath;

    private File[] filesInDir;
    private String[] fileNameClient;


   // private ObservableList<FileInfo> clientData;
    private int count;
    private static int PORTNUMBER = 8080;
    private static String HOST_NAME = "localhost";
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;
    private String ServerFileList;
    @Override
    public void start(Stage primaryStage) throws Exception{
        ServerFileList = "";
        clientFolder = new TableView<>();
        serverFolder = new TableView<>();

        clientPath = "/home/mobile/Desktop/clientDirTest";

        File cDir = new File(clientPath);
        count = 0;

        getClientFileName(cDir);

        final ObservableList clientData = FXCollections.observableArrayList();
        final ObservableList serverData = FXCollections.observableArrayList();
        getSeverData();
        String clientCommandTokens[] = ServerFileList.split(" ");
        for(int i = 0; i<clientCommandTokens.length; i++)
        {
            serverData.add(new FileInfo(clientCommandTokens[i]));
        }
        for(int i = 0; i<fileNameClient.length; i++)
        {
            System.out.println(fileNameClient[i] + "dsjflajdfa");
            clientData.add(new FileInfo(fileNameClient[i]));
        }



        GridPane editArea = new GridPane();

        Button upload = new Button("Upload");
        upload.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                    try {

                        FileInfo fileinfo = clientFolder.getSelectionModel().getSelectedItem();
                        String selectedFilePath = clientPath + "/" + fileinfo.getFileNames();
                        File selectedFile = new File(selectedFilePath);
                        connection();
                        out.println("UPLOAD");
                        out.flush();
                        out.println(fileinfo.getFileNames());
                        out.flush();

                        serverData.add(new FileInfo(fileinfo.getFileNames()));
                        serverFolder.setItems(serverData);

                        //out.close();
                        FileReader fileReader = new FileReader(selectedFile);

                        BufferedReader bufferedReader = new BufferedReader(fileReader);
                        String line;
                        String wholeFile = "";
                        while((line = bufferedReader.readLine()) != null) {
                           wholeFile += line;
                        }
                        System.out.println(wholeFile);
                        out.println(wholeFile);
                        out.flush();
                        out.close();
                        //disconnection();
                        System.out.println("UPLOADED COMPLETE");

                    }
                    catch(IOException e)
                    {
                        e.printStackTrace();
                    }
            }
        });

        Button download = new Button("Download");
        download.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {

                    FileInfo fileinfo = serverFolder.getSelectionModel().getSelectedItem();
                    String downloadFileName = fileinfo.getFileNames();
                    //File selectedFile = new File(selectedFilePath);
                    connection();
                    out.println("DOWNLOAD");
                    out.println(downloadFileName);
                    out.flush();
                    clientData.add(new FileInfo(downloadFileName));
                    clientFolder.setItems(clientData);
                    String wholeFile = "";

                    wholeFile = in.readLine();

                    File newPath = new File(clientPath + "/" + downloadFileName);
                    if (!newPath.exists()) {
                        newPath.createNewFile();
                    }

                    FileWriter fw = new FileWriter(newPath.getAbsoluteFile());
                    BufferedWriter bw = new BufferedWriter(fw);
                    bw.write(wholeFile);
                    out.close();
                    in.close();
                    //disconnection();

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });

        Button endConnection = new Button("End");
        endConnection.setOnAction(event -> System.exit(0));

        editArea.add(upload,0,0);
        editArea.add(download,1,0);
        editArea.add(endConnection,10,0);

        clientFolder.setItems(clientData);
        clientFolder.setEditable(true);
        //Table Colume for client
        TableColumn<FileInfo,String> clientFile = null;
        clientFile = new TableColumn<>();
        clientFile.setMinWidth(300);
        clientFile.setCellValueFactory(new PropertyValueFactory<>("fileNames"));

        clientFolder.getColumns().add(clientFile);

        serverFolder.setItems(serverData);
        serverFolder.setEditable(true);
        //Table Colume for server
        TableColumn<FileInfo,String> serverFile = null;
        serverFile = new TableColumn<>();
        serverFile.setMinWidth(300);
        serverFile.setCellValueFactory(new PropertyValueFactory<>("fileNames"));

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

    public void connection()
    {
        try
        {
            clientSocket = new Socket(HOST_NAME, PORTNUMBER);
            in = new BufferedReader(new InputStreamReader (clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void disconnection()
    {
        try
        {
            clientSocket.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

    }

    public void getClientFileName (File file) {

            if (file.isDirectory()) {
                // process all of the files recursively
                filesInDir = file.listFiles();
                fileNameClient = new String[filesInDir.length];
                for (int i = 0; i < filesInDir.length; i++) {
                    getClientFileName(filesInDir[i]);
                }
            }
            else if(file.exists())
            {
                System.out.println(file.getName());
                fileNameClient[count] = file.getName();
                count++;
            }


    }



        public void getSeverData() {
            try {

                final ObservableList<FileInfo> data = FXCollections.observableArrayList();
                connection();

                out.println("DIR");

                out.flush();
                String fileNames;

                while ((fileNames = in.readLine()) != null) {
                   ServerFileList+= fileNames + " ";
                }
                serverFolder.setItems(data);
               // disconnection();


            } catch (IOException e) {
                e.printStackTrace();
            }


        }


        public static void main(String[] args) {
            launch(args);
        }

}
