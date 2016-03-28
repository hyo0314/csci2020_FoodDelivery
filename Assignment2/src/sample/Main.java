package sample;


import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.io.*;
import java.net.Socket;

public class Main extends Application {

    private BorderPane layout;

    private TableView<FileInfo> clientFolder;
    private TableView<FileInfo> serverFolder;

    private String clientPath;

    private File[] filesInDir;
    private String[] fileNameClient;

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

        //I dont' know how to put this as command line arguments, I just used my own directory for the Client
        clientPath = "/home/mobile/Desktop/FoodDelivery/csci2020_FoodDelivery/Assignment2/clientDirTest";

        File cDir = new File(clientPath);

        //function to get the file name in the client folder
        getClientFileName(cDir);

        final ObservableList clientData = FXCollections.observableArrayList();
        final ObservableList serverData = FXCollections.observableArrayList();

        //function to get the file name for the server/sharing file names.
        getSeverData();

        //this first loop is to split the file names by space because in getServeData, we get the all the server folder file
        //names together and saparted by the space and put the server file names into table(right side)
        String clientCommandTokens[] = ServerFileList.split(" ");
        for(int i = 0; i<clientCommandTokens.length; i++)
        {
            serverData.add(new FileInfo(clientCommandTokens[i]));
        }

        //second for loop is to put the files names in client directory/folder into client tabble.
        for(int i = 0; i<fileNameClient.length; i++)
        {
            clientData.add(new FileInfo(fileNameClient[i]));
        }

        GridPane editArea = new GridPane();

        //upload button
        Button upload = new Button("Upload");
        upload.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                    try {

                        //When the user clicks the file names in the Client Table and we use FileInfo class to get file name of clicked one.
                        FileInfo fileinfo = clientFolder.getSelectionModel().getSelectedItem();

                        //selectedfilePath is to set the path for selected file
                        String selectedFilePath = clientPath + "/" + fileinfo.getFileNames();
                        File selectedFile = new File(selectedFilePath);

                        //once we clikc the upload button, connect to the server.
                        connection();
                        out.println("UPLOAD");
                        out.flush();
                        out.println(fileinfo.getFileNames());
                        out.flush();

                        //add the selected item from client to server table
                        serverData.add(new FileInfo(fileinfo.getFileNames()));
                        serverFolder.setItems(serverData);

                        //copying the contents of selected file
                        FileReader fileReader = new FileReader(selectedFile);

                        BufferedReader bufferedReader = new BufferedReader(fileReader);
                        String line;

                        //sending to Server system, line by line.
                        while((line = bufferedReader.readLine()) != null) {
                            out.println(line);
                            out.flush();
                        }

                        out.close();

                        System.out.println("UPLOADED COMPLETE");

                        //highlighted file will be clear
                        serverFolder.getSelectionModel().clearSelection();
                        clientFolder.getSelectionModel().clearSelection();

                        //The disconnection will happen in the Server class.

                    }
                    catch(IOException e)
                    {
                        e.printStackTrace();
                    }
            }
        });

        //dowload button
        Button download = new Button("Download");
        download.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    //get file name of selected in Sever table.
                    FileInfo fileinfo = serverFolder.getSelectionModel().getSelectedItem();
                    String downloadFileName = fileinfo.getFileNames();

                    //connect to the server
                    connection();

                    //give "DOWNLOAD" to Server as parameter and the file name to server as well
                    out.println("DOWNLOAD");
                    out.println(downloadFileName);
                    out.flush();


                    String temp;

                    File newPath = new File(clientPath + "/" + downloadFileName);

                    //if the file already exist in client table, we dont copy the contents and file.
                    if(!newPath.exists())
                    {
                        //put file name into client table
                        clientData.add(new FileInfo(downloadFileName));
                        clientFolder.setItems(clientData);

                        //copy the content
                        FileOutputStream fos = new FileOutputStream(newPath);
                        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
                        while((temp = in.readLine()) != null)
                        {
                            //send to lines to Server
                            bw.write(temp);
                            bw.newLine();
                        }

                        bw.close();

                    }
                    else
                    {
                        System.out.println("FILE EXISTS ALREADY");
                    }

                    out.close();
                    in.close();

                    //disconnection also happens in fileSever.java

                    serverFolder.getSelectionModel().clearSelection();
                    clientFolder.getSelectionModel().clearSelection();

                    System.out.println("DOWNLOAD COMPLETE");

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });

        //add new button
        Button endConnection = new Button("End");
        endConnection.setOnAction(event -> disconnection());

        //button position
        editArea.add(upload,0,0);
        editArea.add(download,1,0);
        editArea.add(endConnection,10,0);

        //set client items to table
        clientFolder.setItems(clientData);
        clientFolder.setEditable(true);

        //Table Colume for client
        TableColumn<FileInfo,String> clientFile = null;
        clientFile = new TableColumn<>("CLIENT FOLDER");
        clientFile.setMinWidth(300);
        clientFile.setCellValueFactory(new PropertyValueFactory<>("fileNames"));

        clientFolder.getColumns().add(clientFile);


        //Table for Server
        serverFolder.setItems(serverData);
        serverFolder.setEditable(true);
        //Table Colume for server
        TableColumn<FileInfo,String> serverFile = null;
        serverFile = new TableColumn<>("SHARING FOLDER(SERVER)");
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

    //connection method is to connect to the server with given host_name and portNumber
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

    //disconnection method is to disconnect from the server and close the window.
    public void disconnection()
    {
        try
        {
            clientSocket.close();
            System.exit(0);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

    }

    //getClientFileName method is to get client files and names of them from given directroy.
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
                fileNameClient[count] = file.getName();
                count++;
            }
    }

    //getServerData method is to get the file names/files from sharing folder, which is in server.
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

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
            launch(args);
        }

}
