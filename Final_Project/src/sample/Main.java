package sample;

import javafx.application.Application;
import javafx.beans.property.adapter.JavaBeanIntegerPropertyBuilder;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.commons.validator.routines.EmailValidator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.List;

public class Main extends Application {

    //order summary page variable
    TableView totalTable;

    //guestInfo page variable
    private BorderPane guestInfoLayout;
    private TextField guestFirstNameField, guestEmailField, guestPhoneField, guestAddressField, guestLastNameField;
    private Label guestFirstNameError, guestErrorEmail, guestErrorAddress, guestErrorPhone, guestLastNameError;
    private int guestInfoErrorCount;
    private FileWriter guestDataSave;
    private Stage guestInfo;

    private TableView table;

    private TextField total;
    private double totalDollars;
    private Stage orderStage;

    private Stage japMenuStage;
    private Stage canMenuStage;

    private BorderPane titleLayout;
    private BorderPane mainLayout;
    private BorderPane menuLayout;

    private Stage mainStage;
    private Stage menuStage;

    //login page variables
    private Stage loginPage;
    private BorderPane loginLayout;
    private TextField typeID, typePassword;
    private Label typeIDError,typePasswordError;
    private String loginId, loginPw;

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

    //variable to check the user continue as guest or login
    private int checkUserType;

    @Override
    public void start(Stage titleStage) throws Exception{

        //sign up stage
        signUpStage = new Stage();

        //forgot userid stage
        forgotID = new Stage();

        //forgot pw stage
        forgotPW = new Stage();

        //login area
        loginPage = new Stage();
        loginId ="";
        loginPw ="";

        //title page layout and title
        titleStage.setTitle("Food Delivery App");
        titleLayout = new BorderPane();

        //login button and continue as guest button area
        GridPane textArea = new GridPane();
        textArea.setPadding(new Insets(2, 2, 2, 2));
        textArea.setVgap(10);
        textArea.setHgap(10);

        Label nameLabel = new Label("Welcome to FoodDelivery!");
        nameLabel.setStyle("-fx-font-size: 15pt");
        textArea.add(nameLabel, 22, 5);

        //continue as guest button, if the user doesn't have the user id and password
        Button skip = new Button("Continue as guest");
        skip.setPrefSize(150,60);
        skip.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //if the user continue as guest, then checkUserType will be 1
                checkUserType = 1;

                titleStage.hide();
                try {
                    mainStage();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mainStage.show();
            }
        });
        textArea.add(skip, 23, 8);

        //this button will go to login page.
        Button addButton = new Button("Login!");
        addButton.setPrefSize(100,60);
        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {

                //Move to log in window
                titleStage.hide();
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
                        loginId = typeID.getText();
                        loginPw = typePassword.getText();
                        try
                        {
                            //if the id and pw meet out database, the go to Searching page, if not, type again.
                            boolean checkLoginInfo = loginValidate(loginId,loginPw);
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

                                //user login to the fooddelivery
                                checkUserType = 0;

                                //pop up message to user for the congratualtion.
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("Congratulation");
                                alert.setHeaderText(null);
                                alert.setContentText("Now, You have successfully log into the FoodDelivery");

                                alert.showAndWait();

                                //after successful login, then the login page will hide and the searching Page will pop up
                                loginPage.hide();

                                try {
                                    mainStage();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                mainStage.show();
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
                                        //loginPage.show();
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
                        signUpLayout.setPadding(new Insets(20));

                        Image image = new Image ("http://i.imgur.com/YgxlKFw.jpg");
                        signUpLayout.setBackground(new Background(new BackgroundImage(image, BackgroundRepeat.REPEAT,
                                BackgroundRepeat.REPEAT,
                                BackgroundPosition.DEFAULT,
                                BackgroundSize.DEFAULT)));

                        //signUpLayout.setStyle("-fx-background-image: url(\"/img/background.jpg\");-fx-background-size: 500, 500;-fx-background-repeat: no-repeat;");
                        //signUpLayout.setStyle("-fx-background-color: #FFFFFF;");

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
                loginPage.setScene(new Scene(loginLayout, 600,200));

                loginPage.show();
            }
        });
        textArea.add(addButton, 22, 8);

        GridPane imageArea = new GridPane();
        ImageView image = new ImageView(new Image(Main.class.getResourceAsStream("background-food.jpg")));
        image.setFitWidth(750);
        image.setFitHeight(300);
        imageArea.add(image, 0, 0, 1, 2);


        titleLayout.setTop(imageArea);
        titleLayout.setCenter(textArea);
        Scene scene = new Scene(titleLayout, 750, 700);
        titleStage.setScene(scene);
        titleStage.show();
    }
    public void mainStage() throws Exception{

        mainLayout = new BorderPane();
        mainStage = new Stage();

        GridPane fill = new GridPane();
        fill.setPadding(new Insets(2, 2, 2, 2));
        fill.setVgap(10);
        fill.setHgap(10);

        Label restaurant = new Label("Select desired restaurant");
        fill.add(restaurant, 10, 10);

        ObservableList<String> options =
                FXCollections.observableArrayList(
                        "Japanese Restaurant",
                        "Canadian Burger Restaurant"
                );
        final ComboBox<String> restaurantChoice = new ComboBox<String>(options);

        restaurantChoice.setValue("Select one");
        fill.add(restaurantChoice, 10, 11);

        Button searchBTN = new Button("Search!");
        searchBTN.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //search for restaurant name
                mainStage.hide();

                String selected = restaurantChoice.getValue();

                if(selected.equals("Japanese Restaurant")){
                    try {
                        japMenuStage();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    japMenuStage.show();
                }
                else if(selected.equals("Canadian Burger Restaurant")){
                    try {
                        canMenuStage();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    canMenuStage.show();
                }
            }
        });
        fill.add(searchBTN, 10,12);


        mainLayout.setCenter(fill);
        mainStage.setTitle("Main menu");
        mainStage.setScene(new Scene(mainLayout,500,500));
    }

    public void japMenuStage() throws Exception{

        japMenuStage = new Stage();
        menuLayout = new BorderPane();

        table = new TableView();

        TableColumn<MenuItems, String> foodName = new TableColumn<>("Order");
        foodName.setMinWidth(50);
        foodName.setCellValueFactory(new PropertyValueFactory<>("foodName"));

        TableColumn<MenuItems, Integer> amount = new TableColumn<>("Amount");
        amount.setMinWidth(50);
        amount.setCellValueFactory(new PropertyValueFactory<>("amount"));

        TableColumn<MenuItems, Double> dollar = new TableColumn<>("($)");
        dollar.setMinWidth(50);
        dollar.setCellValueFactory(new PropertyValueFactory<>("dollar"));

        table.getColumns().add(foodName);
        table.getColumns().add(amount);
        table.getColumns().add(dollar);

        GridPane menu = new GridPane();
        menu.setPadding(new Insets(10,10,10,10));
        menu.setVgap(10);
        menu.setHgap(10);

        Label menuLabel = new Label("Menu");
        menu.add(menuLabel,0,2);

        ObservableList<String> soupOptions =
                FXCollections.observableArrayList(
                        "Miso Soup ($1.00)",
                        "Seafood Soup ($2.50)",
                        "Green Salad ($1.00)",
                        "Crab Meat Salad ($3.00)"
                );
        final ComboBox<String> soupChoice = new ComboBox<String>(soupOptions);
        final String[] soupArray = {
                "Miso Soup ($1.00)",
                "Seafood Soup ($2.50)",
                "Green Salad ($1.00)",
                "Crab Meat Salad ($3.00)"
        };

        soupChoice.setValue("Soups and Salads");
        menu.add(soupChoice, 1, 4);
        int[] soupCount = new int[4];
        double[] soupDollar = new double[4];

        Button addSoup = new Button("+");
        addSoup.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String choice = soupChoice.getValue();

                if(choice.equals(soupArray[0])) {
                    soupCount[0]++;
                    soupDollar[0]+=1;
                    totalDollars+=1;
                }
                else if(choice.equals(soupArray[1])) {
                    soupCount[1]++;
                    soupDollar[1]+=2.5;
                    totalDollars+=2.5;
                }
                else if(choice.equals(soupArray[2])) {
                    soupCount[2]++;
                    soupDollar[2]+=1;
                    totalDollars+=1;
                }
                else if(choice.equals(soupArray[3])) {
                    soupCount[3]++;
                    soupDollar[3]+=3;
                    totalDollars+=3;
                }
                for(int i = 0; i < soupCount.length; i++) {
                    System.out.println(soupArray[i] + " " + soupCount[i] + " $" + soupDollar[i]);
                }
                System.out.println("Total : $" + totalDollars);
                System.out.println();
            }
        });
        menu.add(addSoup, 2, 4);

        Button remSoup = new Button("-");
        remSoup.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String choice = soupChoice.getValue();

                if(choice.equals(soupArray[0]) && soupCount[0] != 0) {
                    soupCount[0]--;
                    soupDollar[0]-=1;
                    totalDollars-=1;
                }
                else if(choice.equals(soupArray[1]) && soupCount[1] != 0) {
                    soupCount[1]--;
                    soupDollar[1]-=2.5;
                    totalDollars-=2.5;
                }
                else if(choice.equals(soupArray[2]) && soupCount[2] != 0) {
                    soupCount[2]--;
                    soupDollar[2]-=1;
                    totalDollars-=1;
                }
                else if(choice.equals(soupArray[3]) && soupCount[3] != 0) {
                    soupCount[3]--;
                    soupDollar[3]-=3;
                    totalDollars-=3;
                }

                for(int i = 0; i < soupCount.length; i++)
                    System.out.println(soupArray[i] + " " + soupCount[i] + " $" + soupDollar[i]);
                System.out.println("Total : $" + totalDollars);
                System.out.println();
            }
        });
        menu.add(remSoup, 3, 4);

        ObservableList<String> appOptions =
                FXCollections.observableArrayList(
                        "Salmon Tar ($6.00)",
                        "Gyoza(6pcs) ($4.00)",
                        "Yam Tempura ($5.50)"
                );
        final ComboBox<String> appChoice = new ComboBox<String>(appOptions);
        final String[] appArray = {
                "Salmon Tar ($6.00)",
                "Gyoza(6pcs) ($4.00)",
                "Yam Tempura ($5.50)"
        };

        appChoice.setValue("Appetizers");
        menu.add(appChoice, 1, 6);
        int[] appCount = new int[3];
        double[] appDollar = new double[3];

        Button addApp = new Button("+");
        addApp.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String choice = appChoice.getValue();

                if(choice.equals(appArray[0])) {
                    appCount[0]++;
                    appDollar[0]+=6;
                    totalDollars+=6;
                }
                else if(choice.equals(appArray[1])) {
                    appCount[1]++;
                    appDollar[1]+=4;
                    totalDollars+=4;
                }
                else if(choice.equals(appArray[2])) {
                    appCount[2]++;
                    appDollar[2]+=5.5;
                    totalDollars+=5.5;
                }

                for(int i = 0; i < appCount.length; i++)
                    System.out.println(appArray[i] + ": " + appCount[i] + " $" + appDollar[i]);
                System.out.println("Total : $" + totalDollars);
                System.out.println();
            }
        });
        menu.add(addApp, 2, 6);

        Button remApp = new Button("-");
        remApp.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String choice = appChoice.getValue();

                if(choice.equals(appArray[0]) && appCount[0] != 0) {
                    appCount[0]--;
                    appDollar[0]-=6;
                    totalDollars-=6;
                }
                else if(choice.equals(appArray[1]) && appCount[1] != 0) {
                    appCount[1]--;
                    appDollar[1]-=4;
                    totalDollars-=4;
                }
                else if(choice.equals(appArray[2]) && appCount[2] != 0) {
                    appCount[2]--;
                    appDollar[2]-=5.5;
                    totalDollars-=5.5;
                }

                for(int i = 0; i < appCount.length; i++)
                    System.out.println(appArray[i] + " " + appCount[i] + " $" + appDollar[i]);
                System.out.println("Total : $" + totalDollars);
                System.out.println();
            }
        });
        menu.add(remApp, 3, 6);

        ObservableList<String> sushiOptions =
                FXCollections.observableArrayList(
                        "Salmon Sushi(2pcs) ($4.00)",
                        "Saba Sushi(2pcs) ($4.00)",
                        "Tuna Sushi(2pcs) ($4.50)"
                );
        final ComboBox<String> sushiChoice = new ComboBox<String>(sushiOptions);
        final String[] sushiArray = {
                "Salmon Sushi(2pcs) ($4.00)",
                "Saba Sushi(2pcs) ($4.00)",
                "Tuna Sushi(2pcs) ($4.50)"
        };

        sushiChoice.setValue("Sushi");
        menu.add(sushiChoice, 1, 8);
        int[] sushiCount = new int[3];
        double[] sushiDollar = new double[3];

        Button addSushi = new Button("+");
        addSushi.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String choice = sushiChoice.getValue();

                if(choice.equals(sushiArray[0])) {
                    sushiCount[0]++;
                    sushiDollar[0]+=4;
                    totalDollars+=4;
                }
                else if(choice.equals(sushiArray[1])) {
                    sushiCount[1]++;
                    sushiDollar[1]+=4;
                    totalDollars+=4;
                }
                else if(choice.equals(sushiArray[2])) {
                    sushiCount[2]++;
                    sushiDollar[2]+=4.5;
                    totalDollars+=4.5;
                }

                for(int i = 0; i < sushiCount.length; i++)
                    System.out.println(sushiArray[i] + " " + sushiCount[i] + " $" + sushiDollar[i]);
                System.out.println("Total : $" + totalDollars);
                System.out.println();
            }
        });
        menu.add(addSushi, 2, 8);

        Button remSushi = new Button("-");
        remSushi.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String choice = sushiChoice.getValue();

                if(choice.equals(sushiArray[0]) && sushiCount[0] != 0) {
                    sushiCount[0]--;
                    sushiDollar[0]-=4;
                    totalDollars-=4;
                }
                else if(choice.equals(sushiArray[1]) && sushiCount[1] != 0) {
                    sushiCount[1]--;
                    sushiDollar[1]-=4;
                    totalDollars-=4;
                }
                else if(choice.equals(sushiArray[2]) && sushiCount[2] != 0) {
                    sushiCount[2]--;
                    sushiDollar[2]-=4.5;
                    totalDollars-=4.5;
                }

                for(int i = 0; i < sushiCount.length; i++)
                    System.out.println(sushiArray[i] + " " + sushiCount[i] + " $" + sushiDollar[i]);
                System.out.println("Total: $" + totalDollars);
                System.out.println();
            }
        });
        menu.add(remSushi, 3, 8);

        ObservableList<String> makiOptions =
                FXCollections.observableArrayList(
                        "Cucumber Roll ($4.50)",
                        "Vegetable Roll ($4.50)",
                        "California Roll ($4.50)",
                        "Salmon Avocado Roll ($4.50)",
                        "Rainbow Roll ($4.50)"
                );
        final ComboBox<String> makiChoice = new ComboBox<String>(makiOptions);
        final String[] makiArray = {
                "Cucumber Roll ($4.50)",
                "Vegetable Roll ($4.50)",
                "California Roll ($4.50)",
                "Salmon Avocado Roll ($4.50)",
                "Rainbow Roll ($4.50)"
        };

        makiChoice.setValue("Maki Rolls");
        menu.add(makiChoice, 1, 10);
        int[] makiCount = new int[5];
        double[] makiDollar = new double[5];

        Button addMaki = new Button("+");
        addMaki.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String choice = makiChoice.getValue();

                if(choice.equals(makiArray[0])) {
                    makiCount[0]++;
                    makiDollar[0]+=4.5;
                    totalDollars+=4.5;
                }
                else if(choice.equals(makiArray[1])) {
                    makiCount[1]++;
                    makiDollar[1]+=4.5;
                    totalDollars+=4.5;
                }
                else if(choice.equals(makiArray[2])) {
                    makiCount[2]++;
                    makiDollar[2]+=4.5;
                    totalDollars+=4.5;
                }
                else if(choice.equals(makiArray[3])) {
                    makiCount[3]++;
                    makiDollar[3]+=4.5;
                    totalDollars+=4.5;
                }
                else if(choice.equals(makiArray[4])) {
                    makiCount[4]++;
                    makiDollar[4]+=4.5;
                    totalDollars+=4.5;
                }
                for(int i = 0; i < makiCount.length; i++)
                    System.out.println(makiArray[i] + " " + makiCount[i] + " $" + makiDollar[i]);
                System.out.println("Total: $" + totalDollars);
                System.out.println();
            }
        });
        menu.add(addMaki, 2, 10);

        Button remMaki = new Button("-");
        remMaki.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String choice = makiChoice.getValue();

                if(choice.equals(makiArray[0]) && makiCount[0] != 0) {
                    makiCount[0]--;
                    makiDollar[0]-=4.5;
                    totalDollars-=4.5;
                }
                else if(choice.equals(makiArray[1]) && makiCount[1] != 0) {
                    makiCount[1]--;
                    makiDollar[1]-=4.5;
                    totalDollars-=4.5;
                }
                else if(choice.equals(makiArray[2]) && makiCount[2] != 0) {
                    makiCount[2]--;
                    makiDollar[2]-=4.5;
                    totalDollars-=4.5;
                }
                else if(choice.equals(makiArray[3]) && makiCount[3] != 0) {
                    makiCount[3]--;
                    makiDollar[3]-=4.5;
                    totalDollars-=4.5;
                }
                else if(choice.equals(makiArray[3]) && makiCount[4] != 0) {
                    makiCount[4]--;
                    makiDollar[4]-=4.5;
                    totalDollars-=4.5;
                }

                for(int i = 0; i < makiCount.length; i++)
                    System.out.println(makiArray[i] + " " + makiCount[i] + " $" + makiDollar[i]);
                System.out.println("Total: $" + totalDollars);
                System.out.println();
            }
        });
        menu.add(remMaki, 3, 10);

        Button update = new Button("Update order");
        update.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                table.getItems().clear();

                for(int i = 0; i < soupCount.length; i++) {
                    if(soupCount[i] != 0)
                        table.getItems().add(new MenuItems(soupArray[i], soupCount[i], soupDollar[i]));
                }
                for(int i = 0; i < appCount.length; i++) {
                    if (appCount[i] != 0)
                        table.getItems().add(new MenuItems(appArray[i], appCount[i], appDollar[i]));
                }
                for(int i = 0; i < sushiCount.length; i++) {
                    if (sushiCount[i] != 0)
                        table.getItems().add(new MenuItems(sushiArray[i], sushiCount[i], sushiDollar[i]));
                }
                for(int i = 0; i < makiCount.length; i++) {
                    if (makiCount[i] != 0)
                        table.getItems().add(new MenuItems(makiArray[i], makiCount[i], makiDollar[i]));
                }
                total = new TextField();
                total.setText(Double.toString(totalDollars));

                menu.add(total, 12, 12);
            }
        });
        menu.add(update, 5, 15);

        Button order = new Button("Order now!");
        order.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(totalDollars == 0)
                {
                    //pop up message to user that user has to order somthing in order to go to ORDER page.
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("ERROR");
                    alert.setHeaderText(null);
                    alert.setContentText("You don't order anything");
                    alert.showAndWait();
                }
                else {
                    japMenuStage.hide();
                    boolean guestOrUser = decideUserType();
                    if(!guestOrUser)
                    {
                        guestInfo();
                        guestInfo.show();

                    }
                    else {
                        orderMenu();

                        orderStage.show();
                    }
                }

            }
        });
        menu.add(order, 16, 12);

        Label hours = new Label("Hours\n\tMonday - Friday: 12:30pm to 10:00pm\n\tSaturday: 12:30pm to 11:00pm\n\tSunday: CLOSED");
        menu.add(hours, 0, 12);

        Label location = new Label("Location");
        menu.add(location, 0, 14);

        GridPane imageArea = new GridPane();
        ImageView image = new ImageView(new Image(Main.class.getResourceAsStream("JAPANESE.PNG")));
        image.setFitWidth(300);
        image.setFitHeight(200);
        imageArea.add(image, 1, 1, 50, 50);

        menuLayout.setCenter(imageArea);
        menuLayout.setTop(menu);
        menuLayout.setRight(table);
        japMenuStage.setTitle("Japanese restaurant Menu");
        japMenuStage.setScene(new Scene(menuLayout, 800, 700));}

    public void canMenuStage() throws Exception{

        canMenuStage = new Stage();
        menuLayout = new BorderPane();

        table = new TableView();

        TableColumn<MenuItems, String> foodName = new TableColumn<>("Order");
        foodName.setMinWidth(50);
        foodName.setCellValueFactory(new PropertyValueFactory<>("foodName"));

        TableColumn<MenuItems, Integer> amount = new TableColumn<>("Amount");
        amount.setMinWidth(50);
        amount.setCellValueFactory(new PropertyValueFactory<>("amount"));

        TableColumn<MenuItems, Double> dollar = new TableColumn<>("($)");
        dollar.setMinWidth(50);
        dollar.setCellValueFactory(new PropertyValueFactory<>("dollar"));

        table.getColumns().add(foodName);
        table.getColumns().add(amount);
        table.getColumns().add(dollar);

        GridPane menu = new GridPane();
        menu.setPadding(new Insets(10,10,10,10));
        menu.setVgap(10);
        menu.setHgap(10);

        Label menuLabel = new Label("Menu");
        menu.add(menuLabel,0,2);

        ObservableList<String> burgerOptions =
                FXCollections.observableArrayList(
                        "Classic burger ($10.00)",
                        "C B House burger ($12.00)",
                        "BBQ Burger ($13.00)",
                        "Mushroom burger ($12.00)",
                        "Southwest Turkey burger ($12.50)"
                );
        final ComboBox<String> burgerChoice = new ComboBox<String>(burgerOptions);
        final String[] burgerArray = {
                "Classic burger ($10.00)",
                "C B House burger ($12.00)",
                "BBQ Burger ($13.00)",
                "Mushroom burger ($12.00)",
                "Southwest Turkey burger ($12.50)"
        };

        burgerChoice.setValue("Burgers");
        menu.add(burgerChoice, 1, 4);
        int[] burgerCount = new int[5];
        double[] burgerDollar = new double[5];

        Button addBurger = new Button("+");
        addBurger.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String choice = burgerChoice.getValue();

                if(choice.equals(burgerArray[0])) {
                    burgerCount[0]++;
                    burgerDollar[0]+=10.00;
                    totalDollars+=10.00;
                }
                else if(choice.equals(burgerArray[1])) {
                    burgerCount[1]++;
                    burgerDollar[1]+=12.00;
                    totalDollars+=12.00;
                }
                else if(choice.equals(burgerArray[2])) {
                    burgerCount[2]++;
                    burgerDollar[2]+=13.00;
                    totalDollars+=13.00;
                }
                else if(choice.equals(burgerArray[3])) {
                    burgerCount[3]++;
                    burgerDollar[3]+=12.00;
                    totalDollars+=12.00;
                }
                else if(choice.equals(burgerArray[4])) {
                    burgerCount[4]++;
                    burgerDollar[4]+=12.50;
                    totalDollars+=12.50;
                }
                for(int i = 0; i < burgerCount.length; i++)
                    System.out.println(burgerArray[i] + " " + burgerCount[i] + " $" + burgerDollar[i]);
                System.out.println("Total: $" + totalDollars);
                System.out.println();
            }
        });
        menu.add(addBurger, 2, 4);

        Button remBurger = new Button("-");
        remBurger.setOnAction(new EventHandler<ActionEvent>() {


            @Override
            public void handle(ActionEvent event) {
                String choice = burgerChoice.getValue();

                if(choice.equals(burgerArray[0]) && burgerCount[0] != 0) {
                    burgerCount[0]--;
                    burgerDollar[0]-=10.00;
                    totalDollars-=10.00;
                }
                else if(choice.equals(burgerArray[1]) && burgerCount[1] != 0) {
                    burgerCount[1]--;
                    burgerDollar[1]-=12.00;
                    totalDollars-=12.00;
                }
                else if(choice.equals(burgerArray[2]) && burgerCount[2] != 0) {
                    burgerCount[2]--;
                    burgerDollar[2]-=13.00;
                    totalDollars-=13.00;
                }
                else if(choice.equals(burgerArray[3]) && burgerCount[3] != 0) {
                    burgerCount[3]--;
                    burgerDollar[3]-=12.00;
                    totalDollars-=12.00;
                }
                else if(choice.equals(burgerArray[4]) && burgerCount[4] != 0) {
                    burgerCount[4]--;
                    burgerDollar[4]-=12.50;
                    totalDollars-=12.50;
                }


                for(int i = 0; i < burgerCount.length; i++)
                    System.out.println(burgerArray[i] + " " + burgerCount[i] + " $" + burgerDollar[i]);
                System.out.println("Total: $" + totalDollars);
                System.out.println();
            }
        });
        menu.add(remBurger, 3, 4);

        ObservableList<String> bevOptions =
                FXCollections.observableArrayList(
                        "Coca-Cola ($2.50)",
                        "Sprite ($2.50)",
                        "Nestea Iced tea ($2.50)"
                );
        final ComboBox<String> bevChoice = new ComboBox<String>(bevOptions);
        final String[] bevArray = {
                "Coca-Cola ($2.50)",
                "Sprite ($2.50)",
                "Nestea Iced tea ($2.50)"
        };

        bevChoice.setValue("Beverages");
        menu.add(bevChoice, 1, 6);
        int[] bevCount = new int[3];
        double[] bevDollar = new double[3];

        Button addBev = new Button("+");
        addBev.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String choice = bevChoice.getValue();

                if(choice.equals(bevArray[0])) {
                    bevCount[0]++;
                    bevDollar[0]+=2.50;
                    totalDollars+=2.50;
                }
                else if(choice.equals(bevArray[1])) {
                    bevCount[1]++;
                    bevDollar[1]+=2.50;
                    totalDollars+=2.50;
                }
                else if(choice.equals(bevArray[2])) {
                    bevCount[2]++;
                    bevDollar[2]+=2.50;
                    totalDollars+=2.50;
                }

                for(int i = 0; i < bevCount.length; i++)
                    System.out.println(bevArray[i] + " " + bevCount[i] + " $" + bevDollar[i]);
                System.out.println("Total: $" + totalDollars);
                System.out.println();
            }
        });
        menu.add(addBev, 2, 6);

        Button remBev = new Button("-");
        remBev.setOnAction(new EventHandler<ActionEvent>() {


            @Override
            public void handle(ActionEvent event) {
                String choice = bevChoice.getValue();

                if(choice.equals(bevArray[0]) && bevCount[0] != 0) {
                    bevCount[0]--;
                    bevDollar[0]-=2.50;
                    totalDollars-=2.50;
                }
                else if(choice.equals(bevArray[1]) && bevCount[1] != 0) {
                    bevCount[1]--;
                    bevDollar[1]-=2.50;
                    totalDollars-=2.50;
                }
                else if(choice.equals(bevArray[2]) && bevCount[2] != 0) {
                    bevCount[2]--;
                    bevDollar[2]-=2.50;
                    totalDollars-=2.50;
                }

                for(int i = 0; i < bevCount.length; i++)
                    System.out.println(bevArray[i] + " " + bevCount[i] + " $" + bevDollar[i]);
                System.out.println("Total: $" + totalDollars);
                System.out.println();
            }
        });
        menu.add(remBev, 3, 6);

        Label hours = new Label("Hours\n\tMonday - Friday: 11:30am to 10:00pm\n\tSaturday: 12:30pm to 11:00pm\n\tSunday: 12:30pm to 8:00pm");
        menu.add(hours, 0, 12);

        Label location = new Label("Location");
        menu.add(location, 0, 14);

        Button update = new Button("Update order");
        update.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                table.getItems().clear();

                for(int i = 0; i < burgerCount.length; i++) {
                    if(burgerCount[i] != 0)
                        table.getItems().add(new MenuItems(burgerArray[i], burgerCount[i], burgerDollar[i]));
                }
                for(int i = 0; i < bevCount.length; i++) {
                    if (bevCount[i] != 0)
                        table.getItems().add(new MenuItems(bevArray[i], bevCount[i], bevDollar[i]));
                }

                total = new TextField();
                total.setText(Double.toString(totalDollars));

                menu.add(total, 12, 12);
            }
        });

        menu.add(update, 5, 15);

        Button order = new Button("Order now!");
        order.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(totalDollars == 0)
                {
                    //pop up message to user that user has to order somthing in order to go to ORDER page.
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("ERROR");
                    alert.setHeaderText(null);
                    alert.setContentText("You don't order anything");
                    alert.showAndWait();
                }
                else {
                    canMenuStage.hide();
                    boolean guestOrUser = decideUserType();
                    if(!guestOrUser)
                    {
                        guestInfo();
                        guestInfo.show();

                    }
                    else {
                        orderMenu();

                        orderStage.show();
                    }
                }

            }
        });
        menu.add(order, 16, 12);

        GridPane imageArea = new GridPane();
        ImageView image = new ImageView(new Image(Main.class.getResourceAsStream("CANADIAN.PNG")));
        image.setFitWidth(300);
        image.setFitHeight(200);
        imageArea.add(image, 1, 1, 50, 50);

        menuLayout.setTop(menu);
        menuLayout.setCenter(imageArea);
        menuLayout.setRight(table);
        canMenuStage.setTitle("Canadian burger restaurant Menu");
        canMenuStage.setScene(new Scene(menuLayout, 800, 700));
    }
    private boolean decideUserType()
    {
        if(checkUserType == 1)
        {
            return false;
        }
        else
            return true;
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

    public void orderMenu(){
        orderStage = new Stage();

        GridPane totalArea = new GridPane();
        totalArea.setPadding(new Insets(10,10,10,10));
        totalArea.setVgap(10);
        totalArea.setHgap(10);

        Label subTotalLabel = new Label("SubTotal :");
        totalArea.add(subTotalLabel,0,0);

        Label tipLabel = new Label("Delivery Tip(10%) : ");
        totalArea.add(tipLabel,0,1);

        Label hstLabel = new Label("HST(13%) : ");
        totalArea.add(hstLabel,0,2);

        Label totalLabel = new Label("Total : ");
        totalArea.add(totalLabel,0,3);

        TextField subTotalField = new TextField();
        TextField tipField = new TextField();
        TextField hstField = new TextField();
        TextField totalField = new TextField();
        subTotalField.setEditable(false);
        tipField.setEditable(false);
        hstField.setEditable(false);
        totalField.setEditable(false);

        totalArea.add(subTotalField,1,0);
        totalArea.add(tipField,1,1);
        totalArea.add(hstField,1,2);
        totalArea.add(totalField,1,3);

        double tip = totalDollars * 0.1;
        double hst = totalDollars * 0.13;
        double finalTotal = tip + hst + totalDollars;

        subTotalField.setText(Double.toString(totalDollars));
        hstField.setText(Double.toString(hst));
        tipField.setText(Double.toString(tip));
        totalField.setText(Double.toString(finalTotal));

        GridPane orderTitleArea = new GridPane();
        orderTitleArea.setAlignment(Pos.CENTER);

        Text orderTitleText = new Text();
        orderTitleText.setText("ORDER SUMMARY");
        orderTitleText.setFont((Font.font("Verdana", FontWeight.BOLD, 35)));
        orderTitleArea.getChildren().add(orderTitleText);

        GridPane summaryArea = new GridPane();
        summaryArea.setPadding(new Insets(10,10,10,10));
        summaryArea.setVgap(10);
        summaryArea.setHgap(10);

        Label fullName = new Label("Full Name");
        summaryArea.add(fullName,0,0);

        Label emailSummary = new Label("Email");
        summaryArea.add(emailSummary,2,0);

        Label phoneSummary = new Label("Phone #");
        summaryArea.add(phoneSummary,4,0);

        Label addressSummary = new Label("Address");
        summaryArea.add(addressSummary,6,0);

        TextField nameSummaryField = new TextField();
        TextField emailSummaryField = new TextField();
        TextField phoneSummaryField = new TextField();
        TextField addressSummaryField = new TextField();
        nameSummaryField.setEditable(false);
        emailSummaryField.setEditable(false);
        phoneSummaryField.setEditable(false);
        addressSummaryField.setEditable(false);

        summaryArea.add(nameSummaryField,0,1);
        summaryArea.add(emailSummaryField,2,1);
        summaryArea.add(phoneSummaryField,4,1);
        summaryArea.add(addressSummaryField,6,1);

        try {

            File file;
            if (loginId.equals("")) {
                file = new File("Data/guestInfoData.csv");
                BufferedReader bf = new BufferedReader(new FileReader(file));
                String line;
                while ((line = bf.readLine()) != null) {
                    String[] col = line.split(",");
                    nameSummaryField.setText(col[0] + " " + col[1]);
                    addressSummaryField.setText(col[4]);
                    phoneSummaryField.setText(col[3]);
                    emailSummaryField.setText(col[2]);
                }
            } else
            {
                file = new File("Data/userInformation.csv");
                BufferedReader bf = new BufferedReader(new FileReader(file));
                String line;
                while ((line = bf.readLine()) != null) {
                    String[] col = line.split(",");
                    if(col[0].equals(loginId)) {
                        nameSummaryField.setText(col[2] + " " + col[3]);
                        addressSummaryField.setText(col[6]);
                        phoneSummaryField.setText(col[5]);
                        emailSummaryField.setText(col[4]);
                    }
                }

            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        BorderPane orderLayout = new BorderPane();
        orderLayout.setCenter(summaryArea);
        orderLayout.setBottom(totalArea);
        orderLayout.setTop(orderTitleArea);

        orderStage.setTitle("Canadian burger restaurant Menu");
        orderStage.setScene(new Scene(orderLayout, 800, 700));
    }

    public void guestInfo(){
        guestInfo = new Stage();
        guestInfoErrorCount = 0;

        GridPane guestInfoArea = new GridPane();
        guestInfoArea.setPadding(new Insets(10,10,10,10));
        guestInfoArea.setVgap(10);
        guestInfoArea.setHgap(10);

        //guest full name section and error label message
        Label guestFirstNameLabel = new Label("First Name:");
        guestInfoArea.add(guestFirstNameLabel, 0, 0);
        guestFirstNameLabel.setStyle("-fx-text-fill: #FFFFFF");
        guestFirstNameField = new TextField();
        guestFirstNameField.setPromptText("Full Name");
        guestInfoArea.add(guestFirstNameField, 1, 0);
        guestFirstNameError = new Label("");
        guestFirstNameError.setStyle("-fx-text-fill: #FF0000");
        guestInfoArea.add(guestFirstNameError, 2, 0);

        Label guestLastNameLabel = new Label("Last Name:");
        guestInfoArea.add(guestLastNameLabel, 0, 1);
        guestLastNameLabel.setStyle("-fx-text-fill: #FFFFFF");
        guestLastNameField = new TextField();
        guestLastNameField.setPromptText("Last Name");
        guestInfoArea.add(guestLastNameField, 1, 1);
        guestLastNameError = new Label("");
        guestLastNameError.setStyle("-fx-text-fill: #FF0000");
        guestInfoArea.add(guestLastNameError, 2, 1);

        //guest email section with error label message
        Label guestEmailLabel = new Label("E-Mail:");
        guestInfoArea.add(guestEmailLabel, 0, 2);
        guestEmailLabel.setStyle("-fx-text-fill: #FFFFFF");
        guestEmailField = new TextField();
        guestEmailField.setPromptText("E-Mail");
        guestInfoArea.add(guestEmailField, 1, 2);
        guestErrorEmail = new Label("");
        guestErrorEmail.setStyle("-fx-text-fill: #FF0000");
        guestInfoArea.add(guestErrorEmail, 2, 2);

        //guest phone section with error label message
        Label guestPhoneLabel = new Label("Phone #:");
        guestInfoArea.add(guestPhoneLabel, 0, 3);
        guestPhoneLabel.setStyle("-fx-text-fill: #FFFFFF");
        guestPhoneField = new TextField();
        guestPhoneField.setPromptText("Phone #");
        guestInfoArea.add(guestPhoneField, 1, 3);
        guestErrorPhone = new Label("");
        guestErrorPhone.setStyle("-fx-text-fill: #FF0000");
        guestInfoArea.add(guestErrorPhone, 2, 3);

        //guest addresss section with error label message
        Label addressLabel = new Label("Address :");
        guestInfoArea.add(addressLabel,0,4);
        addressLabel.setStyle("-fx-text-fill: #FFFFFF");
        guestAddressField = new TextField();
        guestInfoArea.add(guestAddressField,1,4);
        guestAddressField.setPromptText("Address");
        guestErrorAddress = new Label("");
        guestErrorAddress.setStyle("-fx-text-fill: #FF0000");
        guestInfoArea.add(guestErrorAddress,2,4);

        Button guestInfoButton = new Button("Info Save");
        guestInfoArea.add(guestInfoButton, 0,5);
        guestInfoButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                guestInfoErrorCount = 0;
                String guestFirstName = guestFirstNameField.getText();
                String guestLastName = guestLastNameField.getText();
                String guestPhone = guestPhoneField.getText();
                String guestAddress = guestAddressField.getText();
                String guestEmail = guestEmailField.getText();

                EmailValidator emailValidator = EmailValidator.getInstance();

                //guest errorChecking for email,phone and fullName
                boolean validEmail = emailValidator.isValid(guestEmail);
                boolean validPhone = validatePhoneNumber(guestPhone);
                boolean validFirstName = validateFirstName(guestFirstName);
                boolean validLastName = validateFirstName(guestLastName);

                if(!validEmail || guestEmail.equals(""))
                {
                    guestInfoErrorCount++;
                    guestErrorEmail.setText("Invalid Email or Empty Email");
                }
                else
                {
                    guestErrorEmail.setText("");
                }

                if(!validPhone || guestPhone.equals(""))
                {
                    guestInfoErrorCount++;
                    guestErrorPhone.setText("Invalid Phone or Empty Phone Number");
                }
                else
                    guestErrorPhone.setText("");

                if(!validFirstName || guestFirstName.equals(""))
                {
                    guestInfoErrorCount++;
                    guestFirstNameError.setText("Invalid First Name or First Letter should be Capital");
                }
                else
                    guestFirstNameError.setText("");

                if(!validLastName || guestLastName.equals(""))
                {
                    guestInfoErrorCount++;
                    guestLastNameError.setText("Invalid Last Name or First Letter should be Capital");
                }
                else
                    guestLastNameError.setText("");

                if(guestAddress.equals(""))
                {
                    guestInfoErrorCount++;
                    guestErrorAddress.setText("Empty Address!!");
                }
                else
                    guestErrorAddress.setText("");

                if(guestInfoErrorCount == 0)
                {
                    try
                    {
                        //guest info is different from user info, only one guest info will be save. if there is another
                        //guest is using, then new guest info will be overwrite the previous guest info.
                        guestDataSave = new FileWriter("Data/guestInfoData.csv");
                        guestDataSave.append(guestFirstName);
                        guestDataSave.append(",");
                        guestDataSave.append(guestLastName);
                        guestDataSave.append(",");
                        guestDataSave.append(guestEmail);
                        guestDataSave.append(",");
                        guestDataSave.append(guestPhone);
                        guestDataSave.append(",");
                        guestDataSave.append(guestAddress);

                        guestDataSave.flush();
                        guestDataSave.close();

                        //pop up message for saving guest info.
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("GOOD!");
                        alert.setHeaderText(null);
                        alert.setContentText("Your Information is saved now!");

                        alert.showAndWait();

                        guestInfo.hide();
                        orderMenu();
                        orderStage.show();

                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                }

            }
        });

        guestInfoLayout = new BorderPane();
        guestInfoLayout.setCenter(guestInfoArea);
        guestInfoLayout.setStyle("-fx-background-color: #000000;");

        guestInfo.setTitle("Guest Information");
        guestInfo.setScene(new Scene(guestInfoLayout, 700, 250));
        guestInfo.show();

    }




    public static void main(String[] args) {
        launch(args);
    }
}
