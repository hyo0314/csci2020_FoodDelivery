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
import java.io.*;
import java.util.*;


public class Main extends Application {

    private TableView<TestFile> testFileTable;
    private BorderPane layout;
    private FileInputStream input = null;
    private FileOutputStream output = null;
    private Map<String,Integer> wordCountsTotalHam;
    private Map<String,Integer> wordCountsTotalSpam;
    private File dataDir;
    private Map<String,Integer> hamCounts;

    private Map<String,Integer> spamCounts;
    private int totalNumHam;
    private int totalNumSpam;
    private Map<String,Double> probWS;
    private Map<String,Double> probWH;
    private Map<String,Double> probSW;


    @Override
    public void start(Stage primaryStage) throws Exception{

        totalNumHam = 0;
        totalNumSpam = 0;

        wordCountsTotalHam = new TreeMap<>();
        dataDir = new File("C:\\Users\\100604449\\Documents\\GitHub\\csci2020_FoodDelivery\\assignments\\data\\train\\ham");
        File hamDir = new File("C:\\Users\\100604449\\Documents\\GitHub\\csci2020_FoodDelivery\\assignments\\hamword.txt");

        try {

            processFile(dataDir);
            printFreq(2, hamDir);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }



        //System.out.println(totalNumHam);
        hamCounts = new TreeMap<>();
        try {
            dataDir = new File("C:\\Users\\100604449\\Documents\\GitHub\\csci2020_FoodDelivery\\assignments\\data\\train\\ham");
            processFileForHamOne(dataDir);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(totalNumHam);

        wordCountsTotalSpam = new TreeMap<>();
        dataDir = new File("C:\\Users\\100604449\\Documents\\GitHub\\csci2020_FoodDelivery\\assignments\\data\\train\\spam");

        try {
            processFileTotalSpam(dataDir);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        spamCounts = new TreeMap<>();
        try {
            processSpam(dataDir);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("------------------------------------------------");
        System.out.println(totalNumSpam);
        Set<String> keySpam = spamCounts.keySet();
        Iterator<String> keyIteratorSpam = keySpam.iterator();

        while(keyIteratorSpam.hasNext()) {

            String key = keyIteratorSpam.next();
            int count = spamCounts.get(key);
            System.out.println(key + " " + count);
        }


        //Probability
        probWS = new TreeMap<>();
        probWH = new TreeMap<>();
        probSW = new TreeMap<>();
        prWS();
        prWH();


        System.out.println(probWS.toString());
        System.out.println(probWH.toString());


        //Codes for Table
        primaryStage.setTitle("Spam Detector 3000");

        testFileTable = new TableView<>();

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

    //probability of Spam files (Train)
    public void prWS()
    {
        Set<String> keySpam = spamCounts.keySet();
        Iterator<String> keyIteratorSpam = keySpam.iterator();

        while(keyIteratorSpam.hasNext()) {

            String key = keyIteratorSpam.next();
            int count = spamCounts.get(key);

            probWS.put(key, (double)count/totalNumSpam);


        }
    }

    //probability of Ham files (Train)
    public void prWH()
    {
        Set<String> keySpam = hamCounts.keySet();
        Iterator<String> keyIteratorSpam = keySpam.iterator();

        while(keyIteratorSpam.hasNext()) {

            String key = keyIteratorSpam.next();
            int count = hamCounts.get(key);

            probWH.put(key, (double)count/totalNumHam);
        }
    }

    public void prSW()
    {
        
    }


    public void processFileForHamOne(File file) throws IOException {

        if (file.isDirectory()) {
            // process all of the files recursively
            File[] filesInDir = file.listFiles();
            for (int i = 0; i < filesInDir.length; i++) {
                totalNumHam++;
                processFileForHamOne(filesInDir[i]);
            }
        } else if (file.exists()) {
            Scanner scanner = new Scanner(file);

           String eachFile = "";

            while (scanner.hasNext()) {
                String word = scanner.next();

                if(isWord(word) )
                {

                   if(isFile(word) && eachFile.indexOf(word)<0)
                   {
                       eachFile += word + " ";

                       countFileHamOne(word);
                   }
                }
            }
        }
    }

    public boolean isFile(String str)
    {
        Set<String> keys = wordCountsTotalHam.keySet();
        Iterator<String> keyIterator = keys.iterator();

        while(keyIterator.hasNext()) {
            String key = keyIterator.next();
            if(str.equals(key))
                return true;
        }

        return false;
    }

    public void countFileHamOne(String str)
    {
        if (hamCounts.containsKey(str)) {

            int oldCount = hamCounts.get(str);
            hamCounts.put(str, oldCount + 1);
        } else {

            hamCounts.put(str, 1);
        }
    }
    public void processSpam(File file) throws IOException {

        if (file.isDirectory()) {
            // process all of the files recursively
            File[] filesInDir = file.listFiles();
            for (int i = 0; i < filesInDir.length; i++) {
                totalNumSpam++;
                processSpam(filesInDir[i]);


            }
        } else if (file.exists()) {
            Scanner scanner = new Scanner(file);

            String eachFile = "";


            while (scanner.hasNext()) {
                String word = scanner.next();

                if(isWord(word) )
                {

                    if(isFileSpam(word) && eachFile.indexOf(word)<0)
                    {
                        eachFile += word + " ";

                        countFileSpam(word);

                    }
                }

            }

        }
    }

    public boolean isFileSpam(String str)
    {
        Set<String> keys = wordCountsTotalSpam.keySet();
        Iterator<String> keyIterator = keys.iterator();

        while(keyIterator.hasNext()) {
            String key = keyIterator.next();
            // System.out.println(key);
            if(str.equals(key))
                return true;
        }

        return false;
    }



    public void countFileSpam(String str)
    {
        if (spamCounts.containsKey(str)) {

            int oldCount = spamCounts.get(str);
            spamCounts.put(str, oldCount + 1);
        } else {

            spamCounts.put(str, 1);
        }
    }


    //word count methods
    //process all files
    public void processFile(File file) throws IOException {

        if (file.isDirectory()) {
            // process all of the files recursively
            File[] filesInDir = file.listFiles();
            for (int i = 0; i < filesInDir.length; i++) {

                processFile(filesInDir[i]);


            }
        } else if (file.exists()) {
            // load all of the data, and process it into words
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()) {
                String word = scanner.next();
                if (isWord(word)) {
                    countWord(word);
                }
            }
        }
    }

    //counting word
    private void countWord(String word) {

        if (wordCountsTotalHam.containsKey(word)) {
            int oldCount = wordCountsTotalHam.get(word);
            wordCountsTotalHam.put(word, oldCount + 1);
        } else {

            wordCountsTotalHam.put(word, 1);
        }

    }

    //word count methods
    //process all files
    public void processFileTotalSpam(File file) throws IOException {

        if (file.isDirectory()) {
            // process all of the files recursively
            File[] filesInDir = file.listFiles();
            for (int i = 0; i < filesInDir.length; i++) {
                processFileTotalSpam(filesInDir[i]);
            }
        } else if (file.exists()) {
            // load all of the data, and process it into words
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()) {
                String word = scanner.next();
                if (isWord(word)) {
                    countWordSpam(word);
                }
            }
        }
    }

    //counting word
    private void countWordSpam(String word) {

        if (wordCountsTotalSpam.containsKey(word)) {
            int oldCount = wordCountsTotalSpam.get(word);
            wordCountsTotalSpam.put(word, oldCount + 1);
        } else {

            wordCountsTotalSpam.put(word, 1);
        }
    }

    //checking
    private boolean isWord(String str){
        String pattern = "^[a-zA-Z]*$";
        if (str.matches(pattern)){
            return true;
        }
        return false;
    }
    public void printFreq(int minCount, File outputFile) throws FileNotFoundException {
        System.out.println("Saving word counts to " + outputFile.getAbsolutePath());
        if (!outputFile.exists() || outputFile.canWrite()) {
            PrintWriter fout = new PrintWriter(outputFile);

            Set<String> keys = probWH.keySet();
            Iterator<String> keyIterator = keys.iterator();


            while(keyIterator.hasNext()) {
                String key = keyIterator.next();
                double count = probWH.get(key);
                if (count >= minCount) {
                    fout.println(key + ": " + count);
                }
            }
            fout.close();
        } else {
            System.err.println("Cannot write to output file");
        }
    }



    public static void main(String[] args) {
        launch(args);

    }
}
