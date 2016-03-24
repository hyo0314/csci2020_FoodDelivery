package Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server{

    private Socket client;
    private ServerSocket server;
    private File serverDirectory;
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
        public synchronized void ClientConnectionHandler()  {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

                command = in.readLine();

                System.out.println(command);
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
                client.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

        }
        private synchronized void uploadFile(String filename)
        {
            try {


                System.out.println("Uploading " + filename + " from client to server...");
                focusedFile = new File(serverDirectory.getPath() + "/" + filename);

                if (!focusedFile.exists()) {
                    focusedFile.createNewFile();
                }

                String readline;
                BufferedReader fileRead = new BufferedReader(new InputStreamReader(client.getInputStream()));
                PrintWriter fout = new PrintWriter(focusedFile);

                while ((readline = fileRead.readLine()) != null) {
                    System.out.println(readline);
                    fout.println(readline);
                    fout.flush();
                }

                //fout.flush();
                fout.close();

                //client.close();
                System.out.println(filename + " successfully uploaded!");
                client.close();
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
        private synchronized void downloadFile(String filename) throws IOException
        {
            System.out.println("Downloading " + filename + " from server to client...");
            BufferedReader in = new BufferedReader(new FileReader(serverDirectory.getPath() + "/" + filename));
            PrintWriter out = new PrintWriter(client.getOutputStream());

            String line;
            String wholeFile = null;

            while((line = in.readLine()) != null)
            {
                wholeFile+=line;
                out.flush();
            }
            out.println(wholeFile);
            out.flush();

           //stop();
            client.close();
        }
    }

    public void beginSession()
    {
        serverDirectory = new File("/home/mobile/Desktop/serverDir/");
        isStopped = false;
        try
        {
            server = new ServerSocket(8080);
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
