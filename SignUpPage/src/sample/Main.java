package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;

import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.apache.commons.validator.routines.EmailValidator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;

public class Main extends Application {

    //sign up Page variables.
    private BorderPane signUpLayout;
    private TextField userIDField, passwordField, firstNameField,lastNameField, emailField, phoneField, addressField;
    private Label userIdErrorLabel, passwordErrorLabel, firstNameErrorLabel,lastNameErrorLabel, emailErrorLabel, phoneErrorLabel, addressErrorLabel;
    private FileWriter dataSave;

    @Override
    public void start(Stage signUpStage) throws Exception{

        //Two Buttons 1. Sign up Button 2. Reset Button
        //when Sihn up button clicks, the information about the user will be saved and the when the reset button clicks,
        //fields will be empty.

        GridPane signUpArea = new GridPane();
        signUpArea.setPadding(new Insets(10,10,10,10));
        signUpArea.setVgap(10);
        signUpArea.setHgap(10);

        //userID section with error label message
        Label userIDLabel = new Label("User ID:");
        signUpArea.add(userIDLabel, 0, 0);
        userIDField = new TextField();
        userIDField.setPromptText("User ID");
        signUpArea.add(userIDField, 1, 0);
        userIdErrorLabel = new Label("");
        signUpArea.add(userIdErrorLabel,2,0);

        //password section with error label message
        Label passwordLabel = new Label("Password:");
        signUpArea.add(passwordLabel, 0, 1);
        passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        signUpArea.add(passwordField, 1, 1);
        passwordErrorLabel = new Label("");
        signUpArea.add(passwordErrorLabel, 2, 1);

        //firstname section with error label message
        Label firstNameLabel = new Label("First Name:");
        signUpArea.add(firstNameLabel, 0, 2);
        firstNameField = new TextField();
        firstNameField.setPromptText("First Name");
        signUpArea.add(firstNameField, 1, 2);
        firstNameErrorLabel = new Label("");
        signUpArea.add(firstNameErrorLabel, 2, 2);

        //last name section with error label message
        Label lastNameLabel = new Label("Last Name:");
        signUpArea.add(lastNameLabel,0,3);
        lastNameField = new TextField();
        lastNameField.setPromptText("Last Name");
        signUpArea.add(lastNameField,1,3);
        lastNameErrorLabel = new Label("");
        signUpArea.add(lastNameErrorLabel,2,3);

        //email section with error label message
        Label emailLabel = new Label("E-Mail:");
        signUpArea.add(emailLabel, 0, 4);
        emailField = new TextField();
        emailField.setPromptText("E-Mail");
        signUpArea.add(emailField, 1, 4);
        emailErrorLabel = new Label("");
        signUpArea.add(emailErrorLabel, 2, 4);

        //phone section with error label message
        Label phoneLabel = new Label("Phone #:");
        signUpArea.add(phoneLabel, 0, 5);
        phoneField = new TextField();
        phoneField.setPromptText("Phone #");
        signUpArea.add(phoneField, 1, 5);
        phoneErrorLabel = new Label("");
        signUpArea.add(phoneErrorLabel, 2, 5);

        //addresss section with error label message
        Label addressLabel = new Label("Address :");
        signUpArea.add(addressLabel,0,6);
        addressField = new TextField();
        signUpArea.add(addressField,1,6);
        addressField.setPromptText("Address");
        addressErrorLabel = new Label("");
        signUpArea.add(addressErrorLabel,2,6);

        //sign up button, when all inputs are correct, then the user information will be saved to the csv file with all inputs
        Button registerButton = new Button("Sign Up");
        registerButton.setPrefSize(100,60);
        registerButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //get the inputs from user
                String userID = userIDField.getText();
                String password = passwordField.getText();
                String firstName = firstNameField.getText();
                String lastName = lastNameField.getText();
                String email = emailField.getText();
                String phone = phoneField.getText();
                String address = addressField.getText();

                //error count, if there is error in each field
                int errorCount = 0;
                int userIdErrorcount = 0;

                try
                {
                    //checking the same user id, then gives the errorCount equals to 1
                    BufferedReader bf = new BufferedReader(new FileReader("Data/userInformation.csv"));
                    String line;
                    while((line = bf.readLine()) != null)
                    {
                        String[] col = line.split(",");

                        if(col[0].equals(userID))
                        {
                            userIdErrorcount = 1;
                        }
                    }

                    //email validator, learned from the lab
                    EmailValidator emailValidator = EmailValidator.getInstance();

                    //email valid boolean, phone valid boolean, first name valid, last name valid
                    boolean validEmail = emailValidator.isValid(email);
                    boolean validPhone = validatePhoneNumber(phone);
                    boolean validFirstName = validateFirstName(firstName);
                    boolean validLastName = validateLastName(lastName);

                    //if the user Id is same as the database(csv file), then gives the error message
                    //if the user puts nothing on the the Id section, then gives the error message
                    if(userIdErrorcount == 1 || userID.equals(""))
                    {
                        System.out.println(userIdErrorcount);
                        userIdErrorLabel.setText("ID already Exists or Please Enter ID");
                        errorCount++;
                    }
                    else
                        userIdErrorLabel.setText("");

                    //if the user puts nothing on address section, then gives error message
                    if(address.equals(""))
                    {
                        errorCount++;
                        addressErrorLabel.setText("Please Enter the address");
                    }
                    else
                        addressErrorLabel.setText("");

                    //if the user puts nothing on the password section, then gives error message
                    if(password.equals(""))
                    {
                        passwordErrorLabel.setText("Please enter password");
                        errorCount++;
                    }
                    else
                        passwordErrorLabel.setText("");

                    //if the email is wrong format, then gives the error message
                    if (!validEmail) {
                        emailErrorLabel.setText("Invalid E-Mail Address");
                        errorCount++;
                    } else {
                        emailErrorLabel.setText("");
                    }

                    //if the phone number is wrong format or empty, then gives the error message
                    if(!validPhone)
                    {
                        phoneErrorLabel.setText("Invalid phone Number");
                        errorCount++;
                    }
                    else
                        phoneErrorLabel.setText("");

                    //if the first name is wrong format or empty, then gives the error message
                    if(!validFirstName) {
                        firstNameErrorLabel.setText("Invalid First Name");
                        errorCount++;
                    }
                    else
                        firstNameErrorLabel.setText("");

                    //if the last name is the wrong format or empty, then gives the error message
                    if(!validLastName) {
                        lastNameErrorLabel.setText("Invalid Last Name");
                        errorCount++;
                    }
                    else
                        lastNameErrorLabel.setText("");

                    //if the program cannot find any error, then put the user information to the csv file to keep the data
                    if(errorCount == 0){
                        dataSave = new FileWriter("Data/userInformation.csv", true);
                        dataSave.append(userID);
                        dataSave.append(",");
                        dataSave.append(password);
                        dataSave.append(",");
                        dataSave.append(firstName);
                        dataSave.append(",");
                        dataSave.append(lastName);
                        dataSave.append(",");
                        dataSave.append(email);
                        dataSave.append(",");
                        dataSave.append(phone);
                        dataSave.append(",");
                        dataSave.append(address);
                        dataSave.append("\n");

                        dataSave.flush();
                        dataSave.close();

                        //pop up message to user for the congratualtion.
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Congratulation");
                        alert.setHeaderText(null);
                        alert.setContentText("Now, You have joined to FoodDelivery");

                        alert.showAndWait();

                        //after successful signup, then go to login page. hide the signupStage.
                        signUpStage.hide();
                    }
                    else
                    {

                        //if there is error, then gives the pop up message
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("ERROR");
                        alert.setHeaderText(null);
                        alert.setContentText("PLEASE TYPE AGAIN");
                        alert.showAndWait();
                    }

                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }




            }
        });
        signUpArea.add(registerButton,0,7);

        Button resetButton = new Button("Reset");
        resetButton.setPrefSize(100,60);
        resetButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                userIDField.setText("");
                passwordField.setText("");
                firstNameField.setText("");
                lastNameField.setText("");
                phoneField.setText("");
                emailField.setText("");
                addressField.setText("");
                userIdErrorLabel.setText("");
                passwordErrorLabel.setText("");
                firstNameErrorLabel.setText("");
                lastNameErrorLabel.setText("");
                emailErrorLabel.setText("");
                phoneErrorLabel.setText("");
                addressErrorLabel.setText("");
            }
        });
        signUpArea.add(resetButton,1,7);
        signUpLayout = new BorderPane();
        signUpLayout.setCenter(signUpArea);


        signUpStage.setTitle("Sign Up to Food Delivery");
        signUpStage.setScene(new Scene(signUpLayout, 550,350));
        signUpStage.show();
    }

    private static boolean validatePhoneNumber(String phoneNo) {
        //validate phone numbers of format "1234567890"
        if (phoneNo.matches("\\d{10}")) return true;
            //validating phone number with -, . or spaces
        else if(phoneNo.matches("\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}")) return true;
            //validating phone number with extension length from 3 to 5
        else if(phoneNo.matches("\\d{3}-\\d{3}-\\d{4}\\s(x|(ext))\\d{3,5}")) return true;
            //validating phone number where area code is in braces ()
        else if(phoneNo.matches("\\(\\d{3}\\)-\\d{3}-\\d{4}")) return true;
            //return false if nothing matches the input
        else return false;

    }

    // validate first name
    public static boolean validateFirstName( String firstName )
    {
        return firstName.matches( "[A-Z][a-zA-Z]*" );
    } // end method validateFirstName

    // validate last name
    public static boolean validateLastName( String lastName )
    {
        return lastName.matches( "[A-Z][a-zA-Z]*" );
    } // end method validateLastName


    public static void main(String[] args) {
        launch(args);
    }
}
