package sample;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Server  {

    private Socket client;
    private ServerSocket server;
    private File serverDirectory;
    private Thread thread;
    private boolean isStopped;
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
        private void ClientConnectionHandler()
        {
            String command;

            Scanner scanner = new Scanner(System.in);
            command = scanner.next();

            if(command.equals("DIR"))
                sendDirectory();
            else if(command.equals("UPLOAD"))
                uploadFile(focusedFile);
            else if(command.equals("DOWNLOAD"))
                downloadFile(focusedFile);
            else
            {
                System.out.println("Command not registered.");
                stop();
            }

        }
        private void sendDirectory()
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
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            stop();
        }
        private void uploadFile(File file)
        {
            System.out.println("Uploading file from client to server...");

            Socket socket = null;
            InputStream in = null;

            try {
                socket = server.accept();
            } catch (IOException e) {
                System.out.println("Cannot accept client connection.");
            }

            try{
                assert socket != null;
                in = socket.getInputStream();
            } catch(IOException e) {
                System.out.println("Cannot get socket input stream.");
            }

            stop();
        }
        private void downloadFile(File file)
        {
            System.out.println("Downloading file from server to client...");

            Socket socket = null;
            OutputStream out = null;

            try {
                socket = server.accept();
            } catch (IOException e) {
                System.out.println("Cannot accept client connection.");
            }

            try{
                out = new FileOutputStream(focusedFile);
            } catch (FileNotFoundException e) {
                System.out.println("Cannot find file.");
            }

            stop();
        }
    }

    public void beginSession()
    {
        serverDirectory = new File("/home/mobile/Desktop/serverDir");
        isStopped = false;
        try
        {
            server = new ServerSocket(12345);
            while(!isStopped)
            {
                System.out.println("Listening for sockets");
                client = server.accept();
                System.out.println("Connected with : " + client);


                thread = new Thread(new ClientConnectionHandler());
                thread.start();

            }
        }
        catch (IOException e)
        {
            System.out.println("Cannot set up server on this port number.");
        }
    }


    public static void main(String[] args) throws IOException {

        new Server().beginSession();
    }


}
