package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

public class Main extends Application {
    private DecimalFormat df = new DecimalFormat("#.00000000000");
    private TableView<TestFile> testFileTable;
    private BorderPane layout;
    private File dataDir;

    private Map<String,Integer> wordCountsTotalHam;
    private Map<String,Integer> wordCountsTotalSpam;
    private Map<String,Integer> tempCounts;
    private Map<String,Integer> hamCounts;
    private Map<String,Integer> spamCounts;
    private Map<String,Double> probWS;
    private Map<String,Double> probWH;
    private Map<String,Double> probSW;
    private Map<String,Double> hamTest;

    private double totalNumHam;
    private double totalNumSpam;

    @Override
    public void start(Stage primaryStage) throws Exception{
        tempCounts = new TreeMap<>();
        wordCountsTotalSpam = new TreeMap<>();
        spamCounts = new TreeMap<>();
        wordCountsTotalHam = new TreeMap<>();
        hamCounts = new TreeMap<>();
        probWS = new TreeMap<>();
        probWH = new TreeMap<>();
        probSW = new TreeMap<>();
        hamTest = new TreeMap<>();
        totalNumHam = 0;
        totalNumSpam = 0;

        try {
            dataDir = new File("/home/mobile/Desktop/FoodDelivery/csci2020_FoodDelivery/assignments/data/train/ham/");
            processFile(dataDir);
            dataDir = new File("/home/mobile/Desktop/FoodDelivery/csci2020_FoodDelivery/assignments/data/train/ham2/");
            processFile(dataDir);

            dataDir = new File("/home/mobile/Desktop/FoodDelivery/csci2020_FoodDelivery/assignments/data/train/ham/");
            processFileForHamOne(dataDir);
            dataDir =new File("/home/mobile/Desktop/FoodDelivery/csci2020_FoodDelivery/assignments/data/train/ham2/");
            processFileForHamOne(dataDir);

            dataDir = new File("/home/mobile/Desktop/FoodDelivery/csci2020_FoodDelivery/assignments/data/train/spam/");
            processFileTotalSpam(dataDir);

            processSpam(dataDir);

            prWS();
            prWH();
            prSW();

            File hamFile = new File("/home/mobile/Desktop/FoodDelivery/csci2020_FoodDelivery/assignments/data/test/spam/");
            testingHam(hamFile);
            finalProbHam();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

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

    public void finalProbHam()
    {
        Set<String> keys = hamTest.keySet();
        Iterator<String> keyIterator = keys.iterator();

        while(keyIterator.hasNext()) {
            String key = keyIterator.next();
            double count = hamTest.get(key);
            //System.out.println(count);
            double finalProb = 1/(1+Math.pow(Math.E, count));

            System.out.println(df.format(finalProb));
            if(hamTest.containsKey(key))
            {
                hamTest.put(key, finalProb);
            }
        }

    }

    //Each File probability in Ham folder
    public void calculateHam(String str, File file)
    {
        if(probSW.containsKey(str))
        {
            double n = Math.log(1-probSW.get(str)) - Math.log(probSW.get(str));

            //System.out.println(df.format(n));
            if (hamTest.containsKey(file.getName())) {

                double temp = hamTest.get(file.getName());
                hamTest.put(file.getName(), temp + n);
            } else {

                hamTest.put(file.getName(), n);
            }
        }


    }

    public void testingHam(File file)throws IOException {

        if (file.isDirectory()) {
            // process all of the files recursively
            File[] filesInDir = file.listFiles();
            for (int i = 0; i < filesInDir.length; i++) {

                testingHam(filesInDir[i]);


            }
        } else if (file.exists()) {
            Scanner scanner = new Scanner(file);

            while(scanner.hasNext())
            {
                String word = scanner.next();


                if(isWord(word))
                {
                    isInHam(word,file);
                }
            }

            if(!scanner.hasNext())
                tempCounts.clear();

        }
    }

    public void isInHam(String str,File file)
    {
        if(!tempCounts.containsKey(str))
        {
            tempCounts.put(str,1);
            calculateHam(str,file);
        }
    }

    //probability of Spam files (Train)
    public void prWS()
    {
        Set<String> keySpam = spamCounts.keySet();
        Iterator<String> keyIteratorSpam = keySpam.iterator();

        while(keyIteratorSpam.hasNext()) {

            String key = keyIteratorSpam.next();
            double count = spamCounts.get(key);

            probWS.put(key, count/totalNumSpam);
        }

        //System.out.println(probWS.toString());
    }

    //probability of Ham files (Train)
    public void prWH()
    {
        //System.out.println(totalNumHam);
       // System.out.println(hamCounts.toString());
        Set<String> keyHam = hamCounts.keySet();
        Iterator<String> keyIteratorSpam = keyHam.iterator();

        while(keyIteratorSpam.hasNext()) {

            String key = keyIteratorSpam.next();
            double count = hamCounts.get(key);

            probWH.put(key, count/totalNumHam);
        }

        //System.out.println(probWH.toString());
    }

    //probability of total spam
    public void prSW()
    {
        Set<String> keySpam = probWS.keySet();
        Iterator<String> keyIteratorSpam = keySpam.iterator();

        while(keyIteratorSpam.hasNext()) {

            String key = keyIteratorSpam.next();
            double count = probWS.get(key);
            double totalCount = 0.0;
            if(!probWH.containsKey(key)) {

                probSW.put(key, 1.0);
            }
            else
            {
                totalCount= count/(count + probWH.get(key));
                probSW.put(key,totalCount);
            }

        }


       /* Set<String> keyHam = probWH.keySet();
        Iterator<String> keyIteratorHam = keyHam.iterator();

        while(keyIteratorHam.hasNext())
        {
            String key = keyIteratorHam.next();
            if(!probSW.containsKey(key))
                probSW.put(key,(double)0);
        }*/

        /*Set<String> b = probSW.keySet();
        Iterator<String> c = b.iterator();

        while(c.hasNext())
        {
            String key = c.next();
            double count = probSW.get(key);

            System.out.println(key + " " + count);
        }*/
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

            while (scanner.hasNext()) {
                String word = scanner.next();

                if(isWord(word) )
                {
                    isFile(word);
                }


            }
            if(!scanner.hasNext())
                tempCounts.clear();

        }
    }

    public void isFile(String str)
    {
        if(!tempCounts.containsKey(str)) {
            tempCounts.put(str, 1);
            countFileHamOne(str);
        }
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

                   isFileSpam(word);
                }
                if(!scanner.hasNext())
                {
                    tempCounts.clear();
                }

            }

        }
    }

    public void isFileSpam(String str)
    {
       if(!tempCounts.containsKey(str))
       {
           tempCounts.put(str,1);
           countFileSpam(str);
       }
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


    public static void main(String[] args) {
        launch(args);

    }
}
