package sample;

import com.sun.xml.internal.messaging.saaj.packaging.mime.MessagingException;
import com.sun.xml.internal.ws.api.message.Message;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.apache.commons.validator.routines.EmailValidator;
import sun.plugin2.message.transport.Transport;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;

public class Main extends Application {

    //login page variables
    private BorderPane loginLayout;
    private TextField typeID, typePassword;
    private Label typeIDError,typePasswordError;

    //sign up Page variables.
    private BorderPane signUpLayout;
    private TextField userIDField, passwordField, firstNameField,lastNameField, emailField, phoneField, addressField;
    private Label userIdErrorLabel, passwordErrorLabel, firstNameErrorLabel,lastNameErrorLabel, emailErrorLabel, phoneErrorLabel, addressErrorLabel;
    private FileWriter dataSave;
    private Stage signUpStage;

    //forgot USERID STAGE variable
    private Stage forgotID;
    private BorderPane forgotIDLayout;
    private TextField typeEmailField;

    //forgot Password Stage variable
    private Stage forgotPW;
    private BorderPane forgotPWLayout;
    private TextField typedIDField;

    @Override
    public void start(Stage loginPage) throws Exception{
        //sign up stage
        signUpStage = new Stage();

        //forgot userid stage
        forgotID = new Stage();

        //forgot pw stage
        forgotPW = new Stage();

        //login area
        GridPane loginArea = new GridPane();
        loginArea.setPadding(new Insets(10,10,10,10));
        loginArea.setVgap(10);
        loginArea.setHgap(10);

        //userID section with error label message
        Label typeUserIDLabel = new Label("User ID:");
        typeUserIDLabel.setStyle("-fx-text-fill: #FFFFFF");
        loginArea.add(typeUserIDLabel, 0, 0);
        typeID = new TextField();
        typeID.setPromptText("Enter the ID");
        loginArea.add(typeID, 1, 0);
        typeIDError = new Label("");
        loginArea.add(typeIDError,2,0);

        //password section with error label message
        Label typePasswordLabel = new Label("Password:");
        typePasswordLabel.setStyle("-fx-text-fill: #FFFFFF");
        loginArea.add(typePasswordLabel, 0, 1);
        typePassword = new PasswordField();
        typePassword.setPromptText("Enter the Password");
        loginArea.add(typePassword, 1, 1);
        typePasswordError = new Label("");
        loginArea.add(typePasswordError, 2, 1);

        //After you put the ID and password, the button do the action such as checking id and pw.
        Button loginButton = new Button("Login");
        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String userId = typeID.getText();
                String userPw = typePassword.getText();
                try
                {
                    //if the id and pw meet out database, the go to Searching page, if not, type again.
                    boolean checkLoginInfo = loginValidate(userId,userPw);
                    if(!checkLoginInfo)
                    {
                        typeIDError.setText("ID Doesn't Exist");
                        typePasswordError.setText("Or Password Doesn't Match");

                        //pop up message to user for the error message.
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("ERROR");
                        alert.setHeaderText(null);
                        alert.setContentText("PLEASE TYPE AGAIN");

                        alert.showAndWait();
                    }
                    else
                    {
                        typeIDError.setText("");
                        typePasswordError.setText("");
                        //pop up message to user for the congratualtion.
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Congratulation");
                        alert.setHeaderText(null);
                        alert.setContentText("Now, You have successfully log into the FoodDelivery");

                        alert.showAndWait();

                        //after successful login, then the login page will hide and the searching Page will pop up
                        loginPage.hide();
                    }
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }

            }
        });

        loginArea.add(loginButton,0,2);

        //sign up button is to go to the sign up page.
        Button signUpButton = new Button("Sign Up");
        signUpButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                    loginPage.hide();


                    //Two Buttons 1. Sign up Button 2. Reset Button
                    //when sign up button clicks, the information about the user will be saved and the when the reset button clicks,
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
                    userIdErrorLabel.setStyle("-fx-text-fill: #FF0000");
                    signUpArea.add(userIdErrorLabel,2,0);

                    //password section with error label message
                    Label passwordLabel = new Label("Password:");
                    signUpArea.add(passwordLabel, 0, 1);
                    passwordField = new PasswordField();
                    passwordField.setPromptText("Password");
                    signUpArea.add(passwordField, 1, 1);
                    passwordErrorLabel = new Label("");
                    passwordErrorLabel.setStyle("-fx-text-fill: #FF0000");
                    signUpArea.add(passwordErrorLabel, 2, 1);

                    //firstname section with error label message
                    Label firstNameLabel = new Label("First Name:");
                    signUpArea.add(firstNameLabel, 0, 2);
                    firstNameField = new TextField();
                    firstNameField.setPromptText("First Name");
                    signUpArea.add(firstNameField, 1, 2);
                    firstNameErrorLabel = new Label("");
                    firstNameErrorLabel.setStyle("-fx-text-fill: #FF0000");
                    signUpArea.add(firstNameErrorLabel, 2, 2);

                    //last name section with error label message
                    Label lastNameLabel = new Label("Last Name:");
                    signUpArea.add(lastNameLabel,0,3);
                    lastNameField = new TextField();
                    lastNameField.setPromptText("Last Name");
                    signUpArea.add(lastNameField,1,3);
                    lastNameErrorLabel = new Label("");
                    lastNameErrorLabel.setStyle("-fx-text-fill: #FF0000");
                    signUpArea.add(lastNameErrorLabel,2,3);

                    //email section with error label message
                    Label emailLabel = new Label("E-Mail:");
                    signUpArea.add(emailLabel, 0, 4);
                    emailField = new TextField();
                    emailField.setPromptText("E-Mail");
                    signUpArea.add(emailField, 1, 4);
                    emailErrorLabel = new Label("");
                    emailErrorLabel.setStyle("-fx-text-fill: #FF0000");
                    signUpArea.add(emailErrorLabel, 2, 4);

                    //phone section with error label message
                    Label phoneLabel = new Label("Phone #:");
                    signUpArea.add(phoneLabel, 0, 5);
                    phoneField = new TextField();
                    phoneField.setPromptText("Phone #");
                    signUpArea.add(phoneField, 1, 5);
                    phoneErrorLabel = new Label("");
                    phoneErrorLabel.setStyle("-fx-text-fill: #FF0000");
                    signUpArea.add(phoneErrorLabel, 2, 5);

                    //addresss section with error label message
                    Label addressLabel = new Label("Address :");
                    signUpArea.add(addressLabel,0,6);
                    addressField = new TextField();
                    signUpArea.add(addressField,1,6);
                    addressField.setPromptText("Address");
                    addressErrorLabel = new Label("");
                    addressErrorLabel.setStyle("-fx-text-fill: #FF0000");
                    signUpArea.add(addressErrorLabel,2,6);

                    //sign up button, when all inputs are correct, then the user information will be saved to the csv file with all inputs
                    Button registerButton = new Button("Register");
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
                            int userIDRepeatError = 0;
                            int userEmailRepeatError = 0;


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
                                        userIDRepeatError = 1;
                                    }

                                    if(col[4].equals(email))
                                    {
                                        userEmailRepeatError = 1;
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
                                if(userIDRepeatError == 1 || userID.equals(""))
                                {
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
                                if (!validEmail || userEmailRepeatError == 1) {
                                    emailErrorLabel.setText("Invalid E-Mail Address Or Email already Exists");
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
                                    firstNameErrorLabel.setText("Invalid Input or First Letter has to be Capital");
                                    errorCount++;
                                }
                                else
                                    firstNameErrorLabel.setText("");

                                //if the last name is the wrong format or empty, then gives the error message
                                if(!validLastName) {
                                    lastNameErrorLabel.setText("Invalid Input or First Letter has to be Capital");
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
                                    signUpStage.close();
                                    loginPage.show();
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
                    signUpLayout.setStyle("-fx-background-color: #FFFFFF;");

                    signUpStage.setTitle("Register to FoodDelivery");
                    signUpStage.setScene(new Scene(signUpLayout, 650,350));
                    signUpStage.show();

                }
        });
        loginArea.add(signUpButton,1,2);

        //forgotIDButton in the Login Page
        //this forgotIDButton click, the forgotID page will pop up
        Button forgotIDButton = new Button("Forgot ID?");
        forgotIDButton.setPrefSize(100,30);
        forgotIDButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                GridPane forgotIDArea = new GridPane();
                forgotIDArea.setPadding(new Insets(10,10,10,10));
                forgotIDArea.setVgap(10);
                forgotIDArea.setHgap(10);

                //label and textfield for seaching the ID by Registered email
                Label typeEmailLabel = new Label("Type Email : ");
                typeEmailLabel.setStyle("-fx-text-fill: #FFFFFF");
                forgotIDArea.add(typeEmailLabel, 0,0);
                typeEmailField = new TextField();
                typeEmailField.setPromptText("Type your registered Email");
                forgotIDArea.add(typeEmailField,1,0);

                //the button ShowID is if the typed email is in our database, then send the pop up message with ID
                Button showID = new Button("Show ID");
                forgotIDArea.add(showID,2,0);
                showID.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        String typedEmail = typeEmailField.getText();
                        String matchedID = "";

                        try {

                            BufferedReader bf = new BufferedReader(new FileReader("Data/userInformation.csv"));
                            String line;
                            while ((line = bf.readLine()) != null) {
                                String[] col = line.split(",");

                                if (col[4].equals(typedEmail)) {
                                    matchedID = col[0];
                                }
                            }


                            if(matchedID.equals(""))
                            {
                                //if user typed email is not in the database,
                                //pop up message to user with error message
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("The Email IS NOT in Our Data");
                                alert.setHeaderText(null);
                                alert.setContentText("Please Type Email Again");

                                alert.showAndWait();
                                forgotID.close();

                            }
                            else {
                                //if the user typed emaile is in the databse,
                                //pop up message to user with their ID.
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("WE FIND YOUR ID");
                                alert.setHeaderText(null);
                                alert.setContentText("Your ID is " + matchedID);

                                alert.showAndWait();
                                forgotID.close();
                            }
                        }
                        catch(Exception e)
                        {
                            e.printStackTrace();
                        }

                    }
                });

                forgotIDLayout = new BorderPane();
                forgotIDLayout.setCenter(forgotIDArea);
                forgotIDLayout.setStyle("-fx-background-color: #000000;");

                forgotID.setTitle("Find Your ID");
                forgotID.setScene(new Scene(forgotIDLayout, 500,60));
                forgotID.show();
            }
        });
        loginArea.add(forgotIDButton,0,3);

        Button forgotPWButton = new Button("Forgot Password?");
        loginArea.add(forgotPWButton,1,3);
        forgotPWButton.setPrefSize(150,30);
        forgotPWButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                GridPane forgotPWArea = new GridPane();
                forgotPWArea.setPadding(new Insets(10,10,10,10));
                forgotPWArea.setVgap(10);
                forgotPWArea.setHgap(10);

                //label and textfield for seaching the password by Registered ID
                Label typedUserID = new Label("Type Your USER ID : ");
                typedUserID.setStyle("-fx-text-fill: #FFFFFF");
                forgotPWArea.add(typedUserID,0,0);
                typedIDField = new TextField();
                typedIDField.setText("");
                typedIDField.setPromptText("Type your registered ID");
                forgotPWArea.add(typedIDField,1,0);

                //the button showPW is if the typed id is in our database, then send the pop up message with ID and password
                Button showPW = new Button("Show Password!");
                forgotPWArea.add(showPW,2,0);
                showPW.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        String typedUserID = typedIDField.getText();
                        String matchedPW = "";

                        try {

                            BufferedReader bf = new BufferedReader(new FileReader("Data/userInformation.csv"));
                            String line;
                            while ((line = bf.readLine()) != null) {
                                String[] col = line.split(",");

                                if (col[0].equals(typedUserID)) {
                                    matchedPW = col[1];
                                }
                            }


                            if(matchedPW.equals(""))
                            {
                                //if user typed ID is not in the database,
                                //pop up message to user with error message
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("The ID IS NOT in our Database");
                                alert.setHeaderText(null);
                                alert.setContentText("Please Type ID Again.");

                                alert.showAndWait();
                                forgotPW.close();

                            }
                            else {
                                //if the user typed ID is in the databse,
                                //pop up message to user with their ID.
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("WE FIND YOUR PW");
                                alert.setHeaderText(null);
                                alert.setContentText("Your ID is " + typedUserID + " and the password is "  + matchedPW);

                                alert.showAndWait();
                                forgotPW.close();
                            }
                        }
                        catch(Exception e)
                        {
                            e.printStackTrace();
                        }

                    }
                });

                forgotPWLayout = new BorderPane();
                forgotPWLayout.setCenter(forgotPWArea);
                forgotPWLayout.setStyle("-fx-background-color: #000000;");

                forgotPW.setTitle("Find Your Password");
                forgotPW.setScene(new Scene(forgotPWLayout, 500,60));
                forgotPW.show();

            }
        });

        loginLayout = new BorderPane();
        loginLayout.setCenter(loginArea);
        loginLayout.setStyle("-fx-background-color: #000000;");

        loginPage.setTitle("Log into FoodDelivery");
        loginPage.setScene(new Scene(loginLayout, 400,200));

        loginPage.show();
    }

    private static boolean loginValidate(String checkID, String checkPw) throws Exception
    {
        BufferedReader bf = new BufferedReader(new FileReader("Data/userInformation.csv"));
        String line;
        while((line = bf.readLine()) != null)
        {
            String[] col = line.split(",");

            if(col[0].equals(checkID) && col[1].equals(checkPw))
            {
                return true;
            }
        }
        return false;
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
