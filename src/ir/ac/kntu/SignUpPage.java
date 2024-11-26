package ir.ac.kntu;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class SignUpPage {
    private final Stage stage;

    public SignUpPage(Stage stage) {
        this.stage = stage;
    }

    public void start() {
        // Labels
        Label titleLabel = new Label("Sign Up");
        titleLabel.setFont(new Font("Arial", 26));
        titleLabel.setTextFill(Color.DARKBLUE);  // Dark Blue Color

        Label firstNameLabel = new Label("First Name:");
        firstNameLabel.setFont(new Font("Arial", 14));
        firstNameLabel.setTextFill(Color.DARKBLUE);  // Dark Blue Color

        Label lastNameLabel = new Label("Last Name:");
        lastNameLabel.setFont(new Font("Arial", 14));
        lastNameLabel.setTextFill(Color.DARKBLUE);  // Dark Blue Color

        Label phoneNumLabel = new Label("Phone Number:");
        phoneNumLabel.setFont(new Font("Arial", 14));
        phoneNumLabel.setTextFill(Color.DARKBLUE);  // Dark Blue Color

        Label socialNumLabel = new Label("Social Number:");
        socialNumLabel.setFont(new Font("Arial", 14));
        socialNumLabel.setTextFill(Color.DARKBLUE);  // Dark Blue Color

        Label passwordLabel = new Label("Password:");
        passwordLabel.setFont(new Font("Arial", 14));
        passwordLabel.setTextFill(Color.DARKBLUE);  // Dark Blue Color

        // TextFields
        TextField firstNameField = new TextField();
        firstNameField.setPromptText("Enter your first name");

        TextField lastNameField = new TextField();
        lastNameField.setPromptText("Enter your last name");

        TextField phoneNumField = new TextField();
        phoneNumField.setPromptText("Enter your phone number");

        TextField socialNumField = new TextField();
        socialNumField.setPromptText("Enter your social number");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");

        // Button
        Button signUpButton = new Button("Sign Up");
        signUpButton.setFont(new Font("Arial", 16));
        signUpButton.setDefaultButton(true);
        signUpButton.setStyle("-fx-background-color: #00BFFF; -fx-text-fill: white;");  // Cyan Color

        // Hyperlink
        Hyperlink loginLink = new Hyperlink("Already have an account? Login here!");
        loginLink.setFont(new Font("Arial", 12));
        loginLink.setTextFill(Color.DARKBLUE);  // Dark Blue Color

        // GridPane layout
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));
        gridPane.setVgap(15);
        gridPane.setHgap(10);
        gridPane.setAlignment(Pos.CENTER);

        gridPane.add(firstNameLabel, 0, 0);
        gridPane.add(firstNameField, 1, 0);
        gridPane.add(lastNameLabel, 0, 1);
        gridPane.add(lastNameField, 1, 1);
        gridPane.add(phoneNumLabel, 0, 2);
        gridPane.add(phoneNumField, 1, 2);
        gridPane.add(socialNumLabel, 0, 3);
        gridPane.add(socialNumField, 1, 3);
        gridPane.add(passwordLabel, 0, 4);
        gridPane.add(passwordField, 1, 4);
        gridPane.add(loginLink, 1, 5);
        gridPane.add(signUpButton, 1, 6);

        // Centering title and form
        VBox vbox = new VBox(20, titleLabel, gridPane);
        vbox.setAlignment(Pos.CENTER);
        vbox.setStyle("-fx-background-color: white;");  // White Background Color

        // Buttons & Links
        signUpButton.setDisable(true);
        firstNameField.textProperty().addListener((observable, oldValue, newValue) ->
                checkFieldsFilled(signUpButton, firstNameField, lastNameField, phoneNumField, socialNumField, passwordField));
        lastNameField.textProperty().addListener((observable, oldValue, newValue) ->
                checkFieldsFilled(signUpButton, firstNameField, lastNameField, phoneNumField, socialNumField, passwordField));
        phoneNumField.textProperty().addListener((observable, oldValue, newValue) ->
                checkFieldsFilled(signUpButton, firstNameField, lastNameField, phoneNumField, socialNumField, passwordField));
        socialNumField.textProperty().addListener((observable, oldValue, newValue) ->
                checkFieldsFilled(signUpButton, firstNameField, lastNameField, phoneNumField, socialNumField, passwordField));
        passwordField.textProperty().addListener((observable, oldValue, newValue) ->
                checkFieldsFilled(signUpButton, firstNameField, lastNameField, phoneNumField, socialNumField, passwordField));

        signUpButton.setOnAction(actionEvent -> {
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String phoneNum = phoneNumField.getText();
            String socialNum = socialNumField.getText();
            String password = passwordField.getText();

            if (checkPhoneNumFormat(phoneNum) && checkPreviousSignUp(phoneNum) && checkPassword(password)) {
                DataBase.addUserToTheBank(new User(firstName, lastName, socialNum, phoneNum, password));
                proceed();
            }
        });

        loginLink.setOnAction(event -> {
            LoginPage loginPage = new LoginPage(stage);
            loginPage.start();
        });

        Scene scene = new Scene(vbox, 600, 450);
        stage.setScene(scene);
        stage.show();
    }

    private void checkFieldsFilled(Button signUpButton, TextField firstNameField, TextField lastNameField, TextField phoneNumField, TextField socialNumField, PasswordField passwordField) {
        boolean allFieldsFilled = !firstNameField.getText().trim().isEmpty()
                && !lastNameField.getText().trim().isEmpty()
                && !phoneNumField.getText().trim().isEmpty()
                && !socialNumField.getText().trim().isEmpty()
                && !passwordField.getText().trim().isEmpty();

        signUpButton.setDisable(!allFieldsFilled);
    }

    private boolean checkPreviousSignUp(String phoneNum) {
        User user = DataBase.getUser(phoneNum);
        if (user != null) {
            Helper.showErrorAlert("Sign Up Failed", "This user has already signed up");
            return false;
        }
        return true;
    }

    private boolean checkPassword(String password) {
        if (Helper.passwordIsWeak(password)) {
            Helper.showErrorAlert("Sign Up Failed", "Password is weak\n" +
                    "(Password should contain at least one lower-case and upper-case letter, number and special character)");
            return false;
        }
        return true;
    }

    private boolean checkPhoneNumFormat(String phoneNum) {
        if (!Helper.isInPhoneNumCorrectFormat(phoneNum)) {
            Helper.showErrorAlert("Sign Up Failed", "Phone number should be in [09*********] format");
            return false;
        }
        return true;
    }

    private void proceed() {
        Helper.showSuccessAlert("Sign Up Successful", "Welcome! Glad to have you here :)");
        LoginPage loginPage = new LoginPage(stage);
        loginPage.start();
    }
}