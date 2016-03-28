package Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by mobile on 28/03/16.
 */
public class FileServer {

        private Socket client;
        private ServerSocket server;
        private static File serverDirectory;
        private Thread thread;
        private boolean isStopped;
        private String filename;
        private String command;
        private File focusedFile;

        public class ClientConnectionHandler implements Runnable
        {
            @Override
            public void run()
            {
                   ClientConnectionHandler();
            }
            public void stop()
            {
                try
                {
                    server.close();
                    isStopped = false;
                    System.out.println("Server is now closed.");
                    System.exit(0);
                }
                catch (IOException e)
                {
                    System.out.println(e.getMessage());
                }
            }

            //getting the information from the client. when the client makes the action, this wll help client to do their work.
            public synchronized void ClientConnectionHandler()  {
                try {
                    BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

                    command = in.readLine();

                    if (command.equals("DIR")) {
                        sendDirectory();
                    }
                    else if (command.equals("UPLOAD")) {
                        filename = in.readLine();
                        uploadFile(filename);
                    }
                    else if (command.equals("DOWNLOAD")) {
                        filename = in.readLine();
                        downloadFile(filename);
                    }
                    else {
                        System.out.println("Command not registered.");
                        stop();
                    }
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }
            }

            //sendDirectiory method is when client opens the firesharing system and we give the file list to the window
            //that client opened.
            private synchronized void sendDirectory()
            {
                System.out.println("Sending directory contents...");
                ArrayList<File> directoryFiles = new ArrayList<>();
                File[] list = serverDirectory.listFiles();

                try
                {
                    PrintWriter fileOut = new PrintWriter(client.getOutputStream());

                    for(File output : list)
                    {
                        System.out.println("File name : " + output.getName());
                        fileOut.println(output.getName());
                        fileOut.flush();
                        directoryFiles.add(output);
                    }

                    //we disconnect client from the server.
                    client.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }

            }

            //uploadFile method is when client clicks the upload button, then from the client/Main.java will send the file names and contents
            //of the selected file by client
            private synchronized void uploadFile(String filename) throws IOException
            {
                System.out.println("Uploading " + filename + " from client to server...");
                focusedFile = new File(serverDirectory.getPath() + "/" + filename);

                if (!focusedFile.exists()) {
                    focusedFile.createNewFile();
                }

                String contents;
                BufferedReader fileRead = new BufferedReader(new InputStreamReader(client.getInputStream()));

                FileOutputStream fos = new FileOutputStream(focusedFile);
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

                //this will copy the contents
                while ((contents = fileRead.readLine()) != null) {
                    bw.write(contents);
                    bw.newLine();
                }

                System.out.println(filename + " successfully uploaded!");
                bw.close();
                fileRead.close();

                client.close();
            }

            //DownloadFile method is when client clicks the download button then server will give the file name and file contents to the Main.java
            private synchronized void downloadFile(String filename) throws IOException
            {
                System.out.println("Downloading " + filename + " from server to client...");
                BufferedReader in = new BufferedReader(new FileReader(serverDirectory.getPath() + "/" + filename));
                PrintWriter out = new PrintWriter(client.getOutputStream());

                String line;

                while((line = in.readLine()) != null)
                {
                    out.println(line);
                    out.flush();
                }

                client.close();
                out.close();
                in.close();
            }
        }

        //beginSession is to begin the server
        public void beginSession()
        {
            //set the server sharing folder
            serverDirectory = new File("/home/mobile/Desktop/FoodDelivery/csci2020_FoodDelivery/Assignment2/serverDir");
            isStopped = false;
            try
            {
                //when client connects to the server.
                server = new ServerSocket(8080);
                while(!isStopped)
                {
                    //prints out the information about the client that connectected to the server
                    System.out.println("Listening for sockets");
                    client = server.accept();
                    System.out.println("Connected with : " + client);

                    thread = new Thread(new FileServer.ClientConnectionHandler());
                    thread.start();
                }
            }
            catch (IOException e)
            {
                System.out.println("Cannot set up server on this port number.");
            }
        }


    public static void main(String[] args) {
        new FileServer().beginSession();
    }
}
