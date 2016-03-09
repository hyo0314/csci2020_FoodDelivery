package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.io.*;
import java.util.*;

public class Main extends Application {

    private TableView<TestFile> testFileTable;
    private BorderPane layout;
    private Map<String, Integer> trainHamFreq;
    private Map<String, Integer> trainSpamFreq;
    private Map<String, Integer> fileHamCounts;
    private Map<String, Integer> fileSpamCounts;
    private Map<String, Double> fileMaps;
    private Map<String, Double> fileFreq;
    private double numOfHamFiles;
    private double numOfSpamFiles;
    private static double  SPAM_THRESHOLD = 0.001;

    public Main() {
        trainHamFreq = new TreeMap<>();
        trainSpamFreq = new TreeMap<>();
        fileHamCounts = new TreeMap<>();
        fileSpamCounts = new TreeMap<>();
        fileMaps = new TreeMap<>();
        fileFreq = new TreeMap<>();
    }

    /* *********************************************************************************************************** */
    /*                                                                                                             */
    /*                                             HAM FILE METHODS                                                */
    /*                                                                                                             */
    /* *********************************************************************************************************** */

    public void processHamFile(File file) throws IOException {
        if (file.isDirectory()) {
            // process all of the files recursively
            File[] filesInDir = file.listFiles();
            for (int i = 0; i < filesInDir.length; i++) {
                processHamFile(filesInDir[i]);
                numOfHamFiles++;
            }
        } else if (file.exists()) {
            // load all of the data, and process it into words
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()) {
                String word = scanner.next();
                if (isHamWord(word)) {
                    isInHamFile(word);
                }
                if(!scanner.hasNext()) {
                    fileHamCounts.clear();
                }
            }
        }
    }
    private void countHamWord(String word) {
        if (trainHamFreq.containsKey(word)) {
            int oldCount = trainHamFreq.get(word);
            trainHamFreq.put(word, oldCount + 1);
        } else {
            trainHamFreq.put(word, 1);
        }
    }


    private boolean isHamWord(String str){
        String pattern = "^[a-zA-Z]*$";
        if (str.matches(pattern)){
            return true;
        }
        return false;
    }
    private void isInHamFile(String str){
        if(!fileHamCounts.containsKey(str)){
            fileHamCounts.put(str, 1);
            countHamWord(str);
        }
    }

    public void printHamFreq(int minCount, File outputFile) throws FileNotFoundException {
        System.out.println("Saving word counts to " + outputFile.getAbsolutePath());
        if (!outputFile.exists() || outputFile.canWrite()) {
            PrintWriter fout = new PrintWriter(outputFile);
            double wordProb;
            Set<String> keys = trainHamFreq.keySet();
            Iterator<String> keyIterator = keys.iterator();

            while(keyIterator.hasNext()) {
                String key = keyIterator.next();
                int count = trainHamFreq.get(key);
                wordProb = count/numOfHamFiles;
                boolean isSpam = false;
                if(wordProb > SPAM_THRESHOLD)
                {
                    isSpam = true;
                }
                else {
                    isSpam = false;
                }
                if (count >= minCount) {
                    fout.println(key + ": " + count + " Probability: " + wordProb + " Is Spam: " + isSpam);
                }
            }
            fout.close();
        } else {
            System.err.println("Cannot write to output file");
        }
    }


    /* *********************************************************************************************************** */
    /*                                                                                                             */
    /*                                             SPAM FILE METHODS                                               */
    /*                                                                                                             */
    /* *********************************************************************************************************** */

    public void processSpamFile(File file) throws IOException {
        if (file.isDirectory()) {
            // process all of the files recursively
            File[] filesInDir = file.listFiles();
            for (int i = 0; i < filesInDir.length; i++) {
                processSpamFile(filesInDir[i]);
                numOfSpamFiles++;
            }
        } else if (file.exists()) {
            // load all of the data, and process it into words
            Scanner scanner = new Scanner(file);
            scanner.reset();
            while (scanner.hasNext()) {
                String word = scanner.next();
                if (isSpamWord(word)) {
                    isInSpamFile(word);
                }
                if(!scanner.hasNext()) {
                    fileHamCounts.clear();
                }
            }
        }
    }
    private void countSpamWord(String word) {
        if (trainSpamFreq.containsKey(word)) {
            int oldCount = trainSpamFreq.get(word);
            trainSpamFreq.put(word, oldCount + 1);
        } else {
            trainSpamFreq.put(word, 1);
        }
    }


    private boolean isSpamWord(String str){
        String pattern = "^[a-zA-Z]*$";
        if (str.matches(pattern)){
            return true;
        }
        return false;
    }
    private void isInSpamFile(String str){
        if(!fileSpamCounts.containsKey(str)){
            fileSpamCounts.put(str, 1);
            countSpamWord(str);
        }
    }

    public void printSpamFreq(int minCount, File outputFile) throws FileNotFoundException {
        System.out.println("Saving word counts to " + outputFile.getAbsolutePath());
        if (!outputFile.exists() || outputFile.canWrite()) {
            PrintWriter fout = new PrintWriter(outputFile);
            double wordProb;
            Set<String> keys = trainSpamFreq.keySet();
            Iterator<String> keyIterator = keys.iterator();

            while(keyIterator.hasNext()) {
                String key = keyIterator.next();
                int count = trainSpamFreq.get(key);
                wordProb = count/numOfSpamFiles;
                boolean isSpam;
                if(wordProb > SPAM_THRESHOLD){
                    isSpam = true;
                }
                else {
                    isSpam = false;
                }
                if (count >= minCount) {
                    fout.println(key + ": " + count + " Probability: " + wordProb + " Is Spam: " + isSpam);
                }
            }
            fout.close();
        } else {
            System.err.println("Cannot write to output file");
        }
    }
/* *********************************************************************************************************** */
/*                                                                                                             */
/*                                             PROBABILITY METHODS                                             */
/*                                                                                                             */
/* *********************************************************************************************************** */

    private void spamWordProb()
    {
        Set<String> spamKeys = trainSpamFreq.keySet();
        Iterator<String> spamKeyIterator = spamKeys.iterator();

        while(spamKeyIterator.hasNext())
        {
            String key = spamKeyIterator.next();

            double count = trainSpamFreq.get(key);
            double totalCount = 0;

            if(!trainHamFreq.containsKey(key))
                fileFreq.put(key, 1.0);
            else{
                totalCount = count/(count + trainHamFreq.get(key));
                fileFreq.put(key, totalCount);
            }
        }
    }

    private void fileSpam()
    {
        Set<String> spamKeys = fileFreq.keySet();
        Iterator<String> spamKeyIterator = spamKeys.iterator();
        double totalCount = 0;

        while(spamKeyIterator.hasNext())
        {
            String key = spamKeyIterator.next();

            double count = Math.log(1-(fileFreq.get(key))) - Math.log(fileFreq.get(key));
            totalCount+=count;

            fileMaps.put(key, 1/(1+Math.pow(Math.E, totalCount)));

        }

    }
    public void printFileFreq(File outputFile) throws FileNotFoundException {
        System.out.println("Saving word counts to " + outputFile.getAbsolutePath());
        if (!outputFile.exists() || outputFile.canWrite()) {
            PrintWriter fout = new PrintWriter(outputFile);
            Set<String> keys = fileFreq.keySet();
            Iterator<String> keyIterator = keys.iterator();

            while(keyIterator.hasNext()) {
                String key = keyIterator.next();
                double count = fileFreq.get(key);
                fout.println(key + ": " + count);
            }
            fout.close();
        } else {
            System.err.println("Cannot write to output file");
        }
    }


/* *********************************************************************************************************** */
/*                                                                                                             */
/*                                             MAIN METHODS                                                    */
/*                                                                                                             */
/* *********************************************************************************************************** */
    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Hello World");
        File dataDir = new File("C:\\Users\\100604449\\Desktop\\csci2020u\\assignment\\SpamDetector\\data\\train\\ham\\");
        System.out.println("File: " + dataDir);
        File outputFile = new File("C:\\Users\\100604449\\Desktop\\csci2020u\\assignment\\SpamDetector\\hamData.txt");
        try {
            processHamFile(dataDir);
            printHamFreq(1, outputFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        dataDir = new File("C:\\Users\\100604449\\Desktop\\csci2020u\\assignment\\SpamDetector\\data\\train\\spam");
        System.out.println("File: " + dataDir);
        File output2 = new File("C:\\Users\\100604449\\Desktop\\csci2020u\\assignment\\SpamDetector\\spamData.txt");
        try {
            processSpamFile(dataDir);
            printSpamFreq(1, output2);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        spamWordProb();
        fileSpam();
        File output3 = new File("C:\\Users\\100604449\\Desktop\\csci2020u\\assignment\\SpamDetector\\fileSpamData.txt");
        printFileFreq(output3);
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


    public static void main(String[] args) {
        launch(args);
    }
}












/* *********************************************************************************************************** */
/*                                                                                                             */
/*                                             DISPOSABLE METHODS                                              */
/*                                                                                                             */
/* *********************************************************************************************************** */


/*
    private void fileProb(File file) throws NullPointerException, FileNotFoundException {
        if (file.isDirectory()) {
            // process all of the files recursively
            File[] filesInDir = file.listFiles();
            for (int i = 0; i < filesInDir.length; i++) {
                fileProb(filesInDir[i]);
            }
        } else if (file.exists()) {
            // load all of the data, and process it into words
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()) {
                String word = scanner.next();
                if (isHamWord(word)) {
                    int temp = trainHamFreq.get(word);

                    double oldProb;
                    if(fileMaps.get(file.getName()) == null){
                        oldProb = 0;
                        tempProb = temp / numOfHamFiles;
                        System.out.println("temp 1 : " + tempProb);
                        fileMaps.put(file.getName(), oldProb + tempProb);
                    }else{
                        oldProb = fileMaps.get(file.getName());
                        tempProb = temp / numOfHamFiles;
                        System.out.println("temp 2 : " + tempProb);
                        fileMaps.put(file.getName(), oldProb + tempProb);
                    }
                    System.out.println();
                }
            }

        }
    }*/

/*
    private void printFile(int minCount, File outputFile) throws FileNotFoundException {
        System.out.println("Saving word counts to " + outputFile.getAbsolutePath());
        if (!outputFile.exists() || outputFile.canWrite()) {
            PrintWriter fout = new PrintWriter(outputFile);

            Set<String> keys = fileMaps.keySet();
            Iterator<String> keyIterator = keys.iterator();

            while(keyIterator.hasNext()) {
                String key = keyIterator.next();
                double count = fileMaps.get(key);

                if (count >= minCount) {
                    fout.println(key + ": " + count);
                }
            }
            fout.close();
        } else {
            System.err.println("Cannot write to output file");
        }
    }*/
