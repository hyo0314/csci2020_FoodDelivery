package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import java.io.*;

import java.text.DecimalFormat;
import java.util.*;
import javafx.scene.control.Label;

public class Main extends Application {

    private DecimalFormat df = new DecimalFormat("0.00000");
    private TableView<TestFile> testFileTable;
    private BorderPane layout;

    //Initializing the tree maps
    private Map<String,Integer> tempCounts;
    private Map<String,Integer> hamCounts;
    private Map<String,Integer> spamCounts;
    private Map<String,Double> probWS;
    private Map<String,Double> probWH;
    private Map<String,Double> probSW;
    public Map<String,Double> finalTest;

    //initializing the numbers that we need to count for total number of files in spam and ham in train and test folder
    private double totalNumHam;
    private double totalNumSpam;
    private double totalNumTest;

    //countSpamGuess is the counting both mails of spam and not spam.
    private double countSpamGuess;

    //correctGuess is the counting the mails if it is correct(ham and spam) meaing if the mail is the ham, and the mail
    // is in the ham folder, then we count. If the mail is spam, and the mail is in the spam folder, then we count.
    private double correctGuess;

    //correctGuessSpam is the counting the "only spam" mails that only appear in the spam folder in test.
    private double correctGuessSpam;

    @Override
    public void start(Stage primaryStage) throws Exception{

        tempCounts = new TreeMap<>();

        spamCounts = new TreeMap<>();

        hamCounts = new TreeMap<>();
        probWS = new TreeMap<>();
        probWH = new TreeMap<>();
        probSW = new TreeMap<>();
        finalTest = new TreeMap<>();

        //set those total as zero
        totalNumHam = 0;
        totalNumSpam = 0;
        totalNumTest = 0;

        //set these count as zero
        countSpamGuess = 0;
        correctGuess = 0;
        correctGuessSpam = 0;

        //ask user to search the directory
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File("."));

        try {

            //First directory is the "Ham" in Train folder
            File mainDirectory = directoryChooser.showDialog(primaryStage);
            File trainHam = mainDirectory;


            processFileForHamOne(trainHam);

            //ask user to search the "Ham2" in Train folder
            mainDirectory = directoryChooser.showDialog(primaryStage);
            File trainHam2 = mainDirectory;

            processFileForHamOne(trainHam2);

            //ask user to search the "Spam" in the Train folder
            mainDirectory = directoryChooser.showDialog(primaryStage);
            File trainSpam = mainDirectory;

            processSpam(trainSpam);

            //calculating the probability of the training folder for the spam and ham
            prWS();
            prWH();

            //calculating the probability of the spam word
            prSW();

            //ask user to search the "Ham" in Test folder
            mainDirectory = directoryChooser.showDialog(primaryStage);
            File hamFile = mainDirectory;

            //ask user to search the "Spam" in Test Folder
            mainDirectory = directoryChooser.showDialog(primaryStage);
            File spamFile = mainDirectory;

            //get the probability of the Spam Mail in Ham folder in Test folder
            testingFinalProb(hamFile);
            finalProb();

            //initializing the data as the observablelist to put these value
            final ObservableList<TestFile> data = FXCollections.observableArrayList();

            Set<String> finalVal = finalTest.keySet();
            Iterator<String> finalIteratorHam = finalVal.iterator();

            //passing the value to TestFile to put the values in the Table for the Ham file probability of spam
            while(finalIteratorHam.hasNext()) {

                String key = finalIteratorHam.next();

                //count is the probability of each file
                double count = finalTest.get(key);

                //if the probabilitay is less than 0.001 we think the files are the Ham.
                // correctGuess is if we detect the mail right, then we add up
                if(count <0.001)
                    correctGuess++;

                //tried to put as 5 decimal places for the probability
                data.add(new TestFile(key,(double)Math.round(count * 100000d)/100000d,"Ham"));
            }

            //then we have to clear the finaTest treemap to work on the files in Spam folder in Test
            finalTest.clear();

            testingFinalProb(spamFile);
            finalProb();

            Set<String> finalSpam = finalTest.keySet();
            Iterator<String> finalIter = finalSpam.iterator();

            //passing the value to TestFile to put the values in the Table for the Spam file probability of spam
            while(finalIter.hasNext()) {

                String key = finalIter.next();
                //count is the probability of each file
                double count = finalTest.get(key);

                //if the probabilitay is bigger than 0.5 we think the files are the Spam.
                //CorrectGuessSpam is all the files in the spam folder must be spams. So, if my guess is right, then we add up to CorrectGuessSpam.
                //CorrectGuess is if we detect spam mails correctly in spam folder
                if(count > 0.5) {
                    correctGuessSpam++;
                    correctGuess++;
                }

                data.add(new TestFile(key,(double)Math.round(count * 100000d)/100000d,"Spam"));
            }

            //title
            primaryStage.setTitle("Spam Detector 3000");

            //setting up table.
            testFileTable = new TableView<TestFile>();
            testFileTable.setItems(data);
            testFileTable.setEditable(true);

            //accuracy = number of correct guesses in Spam and Ham / total number of files in the test folder(both in Spam and Ham);
            //precision = number of correct guesses(only spam folder) / number of spam guesses;
            double acc = correctGuess/totalNumTest;
            double pre = correctGuessSpam/countSpamGuess;

            //Table Colume for file name
            TableColumn<TestFile,String> fileColumn = null;
            fileColumn = new TableColumn<>("File");
            fileColumn.setMinWidth(300);
            fileColumn.setCellValueFactory(new PropertyValueFactory<TestFile,String>("filename"));

            //Table Column for the spam probability
            TableColumn<TestFile, String> spamProbColumn = null;
            spamProbColumn = new TableColumn<>("Spam Probability");
            spamProbColumn.setMinWidth(200);
            spamProbColumn.setCellValueFactory(new PropertyValueFactory<TestFile,String>("spamProbability"));

            //Table column for the Actual Class
            TableColumn<TestFile, String> actualClassColumn = null;
            actualClassColumn = new TableColumn<>("Actual Class");
            actualClassColumn.setMinWidth(200);
            actualClassColumn.setCellValueFactory(new PropertyValueFactory<TestFile,String>("actualClass"));

            //show the value to Table
            testFileTable.getColumns().add(fileColumn);
            testFileTable.getColumns().add(actualClassColumn);
            testFileTable.getColumns().add(spamProbColumn);

            //The below Table, for the Accuracy and Precision
            GridPane resultArea = new GridPane();
            resultArea.setPadding(new Insets(10, 10, 10, 10));
            resultArea.setVgap(10);
            resultArea.setHgap(10);

            //This is Where the Accuracy and the Precision
            Label accurL = new Label("Accuracy :");
            resultArea.add(accurL, 0, 0);
            TextField accField = new TextField();

            accField.setEditable(false);
            accField.setText(df.format(acc));
            resultArea.add(accField, 1, 0);

            Label preL = new Label("Precision:");
            resultArea.add(preL, 0, 1);

            TextField preField = new TextField();
            preField.setEditable(false);
            preField.setText(df.format(pre));
            resultArea.add(preField, 1, 1);

            //showing the final table
            layout = new BorderPane();
            layout.setCenter(testFileTable);
            layout.setBottom(resultArea);

            Scene scene = new Scene(layout, 700, 700);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //FinalProb method is to calculating the pr(S|F) for files of Spam and Ham in Test Folder.
    public void finalProb()
    {
        Set<String> keys = finalTest.keySet();
        Iterator<String> keyIterator = keys.iterator();

        while(keyIterator.hasNext()) {
            String key = keyIterator.next();
            double count = finalTest.get(key);

            //we get the values from summation equation that Randy gave.
            //we use the equation of 1/(1+e^count); to get the pr(S|F)
            double finalProb = 1/(1+ (Math.pow(Math.E, count)));

            //if statements to count the CountSpamGuess.
            //CountSpamGuess is we count the spam mails in both spam and ham files in test folder
            // when the final probability is higher than 0.6, even though all ham files are the ham and all spam files spam.
            if(finalProb > 0.6)
                countSpamGuess++;

            //put the values in the finalTest TreeMap.
            if(finalTest.containsKey(key))
            {
                finalTest.put(key, finalProb);
            }
        }
    }

    //Calculating the summation eqaution that Randy gave with ln for each file in ham and spam in test folder
    public void calculateSummation(String str, File file)
    {
        //str is word from single file, and check word is in the pr(S|W) tree map, which is probSW.
        if(probSW.containsKey(str))
        {
            //equation given from Randy.
            double n = Math.log(1.0-probSW.get(str)) - Math.log(probSW.get(str));

            //if the file name is already exist in treemap of FinalTest, then we add up "n" with old value.
            //if not, make new key as file name and give n value.
            if (finalTest.containsKey(file.getName())) {

                double temp = finalTest.get(file.getName());
                finalTest.put(file.getName(), temp + n);
            } else {

                finalTest.put(file.getName(), n);
            }
        }
    }

    //This TestingFinalProb method is to search every word in a file, in Spam and Ham folder, in Test folder.
    public void testingFinalProb(File file)throws IOException {

        if (file.isDirectory()) {
            // process all of the files recursively
            File[] filesInDir = file.listFiles();
            for (int i = 0; i < filesInDir.length; i++) {
                totalNumTest++;
                testingFinalProb(filesInDir[i]);


            }
        } else if (file.exists()) {
            Scanner scanner = new Scanner(file);

            while(scanner.hasNext())
            {
                String word = scanner.next();

                //if the scanned word is real word which has no special letter,
                // then we make the word as lowercase then we check that the word already found before in a same file.
                if(isWord(word))
                {
                    word = word.toLowerCase();
                    isInTestFile(word,file);
                }
            }

            //after the scanning word in a file finished, then we make temp Treemap empty to use again next file.
            if(!scanner.hasNext())
                tempCounts.clear();

        }
    }

    //isInTestFile method is to check whether the word already found in a file or not.
    public void isInTestFile(String str,File file)
    {
        //if the scanned word from the a file is not in the temp TreeMap, tempCounts, then we put the word in treeMap with the value of 1.
        if(!tempCounts.containsKey(str)){
            tempCounts.put(str,1);
            //after putting the value in tree map, we call the calculate summation to calculate the probability.
            calculateSummation(str,file);
        }
    }

    //probability of Spam files (in Train folder)  = Pr(W|S)
    public void prWS()    {
        //after we calcuate how many files has "certain word" in Spam Folder in Train, then we have to calculate the probability of that word
        //by the given equation
        //pr(W|S) = number of spam files containing Word divided by the total number of spam files. IN TRAIN FOLDER
        Set<String> keySpam = spamCounts.keySet();
        Iterator<String> keyIteratorSpam = keySpam.iterator();

        while(keyIteratorSpam.hasNext()) {
            String key = keyIteratorSpam.next();
            double count = spamCounts.get(key);

            //put value in probWS for PR(W|S)
            probWS.put(key, count/totalNumSpam);
        }
    }

    //probability of Ham files (in Train folder) = Pr(W|H)
    public void prWH()    {
        //after we calcuate how many files has "certain word" in Ham folder in Train, then we have to calculate the probability of that word
        //by the given equation
        //pr(W|H) = number of Ham files containing Word divided by the total number of Ham files. IN TRAIN FOLDER
        Set<String> keyHam = hamCounts.keySet();
        Iterator<String> keyIteratorSpam = keyHam.iterator();

        while(keyIteratorSpam.hasNext()) {

            String key = keyIteratorSpam.next();
            double count = hamCounts.get(key);

            //put value in probWH for PR(W|H)
            probWH.put(key, count/totalNumHam);
        }
    }

    //probability of the file is spam, given the word. PR(S|W)
    public void prSW()
    {
        //we call the each word's probability from PR(S|W) because those words are mostly spam.
        Set<String> keySpam = probWS.keySet();
        Iterator<String> keyIteratorSpam = keySpam.iterator();

        while(keyIteratorSpam.hasNext()) {
            String key = keyIteratorSpam.next();
            double count = probWS.get(key);
            double totalCount = 0.0;

            //if the word only exist in SPAM, then the pr(S|W) should be one.
            if(!probWH.containsKey(key)) {

                probSW.put(key, 1.0);
            }
            else
            {
                //if the word exists in both spam and ham, then we use the equation that is given.
                totalCount= count/(count + probWH.get(key));
                probSW.put(key,totalCount);
            }

            //I ignore the case when the word only exist in Ham, not in Spam because It gives the zero probability.
        }
    }

    //processFileForHamOne is the method of getting word from the ham folder in Train.
    //number of files containing word.
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

            //get every word from the file
            while (scanner.hasNext()) {
                String word = scanner.next();

                //if the word is real word, not special word with special letter.(ex. #@$@#$@ajdlkfajfd)
                if(isWord(word))
                {
                    //change the word to lowercase
                    word = word.toLowerCase();
                    //checking the word already has been searched before in same file
                    isFile(word);
                }
            }

            //clearing the temp tree map to use this tree map for next file.
            if(!scanner.hasNext())
                tempCounts.clear();

        }
    }

    public void isFile(String str)
    {
        //if the word cannot find in the temp tree map, this means that the word has not been searched before. meaning it is new word.
        //then we make word as new tree map list in tempCounts tree map.
        if(!tempCounts.containsKey(str)) {
            tempCounts.put(str, 1);

            //pass the word to the CountFileHamOne
            countFileHamOne(str);
        }
    }

    //countFileHamOne is the method that counts the number of the files containing that word.
    public void countFileHamOne(String str)
    {
        //if the word already exists in the treemap, then we add up the value by one with the old value.
        if (hamCounts.containsKey(str)) {
            int oldCount = hamCounts.get(str);
            hamCounts.put(str, oldCount + 1);
        } else {
            //if the word is new, then make new key with new value.
            hamCounts.put(str, 1);
        }
    }

    //SAME LOGIC AS THE HAM in TRAINING
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

            while (scanner.hasNext()) {
                String word = scanner.next();

                if(isWord(word) )
                {
                    word = word.toLowerCase();
                    isFileSpam(word);
                }

            }
            if(!scanner.hasNext())
            {
                tempCounts.clear();
            }

        }
    }

    //SAME LOGIC AS THE HAM in TRAINING
    public void isFileSpam(String str)
    {
        if(!tempCounts.containsKey(str))
        {
            tempCounts.put(str,1);
            countFileSpam(str);
        }
    }

    //SAME LOGIC AS THE HAM in TRAINING
    public void countFileSpam(String str)
    {
        if (spamCounts.containsKey(str)) {

            int oldCount = spamCounts.get(str);
            spamCounts.put(str, oldCount + 1);
        } else {

            spamCounts.put(str, 1);
        }
    }

    //checking the word is real word that has no special letter or space.
    private boolean isWord(String str){
        String pattern = "^[a-zA-Z]*$";
        if (str.matches(pattern)){
            return true;
        }
        return false;
    }

    public static void main(String[] args) {launch(args); }
}
