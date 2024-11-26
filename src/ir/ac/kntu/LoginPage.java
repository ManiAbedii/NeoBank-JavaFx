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

public class LoginPage {
    private final Stage stage;

    public LoginPage(Stage stage) {
        this.stage = stage;
    }

    public void start() {
        // Title
        Label titleLabel = new Label("Login");
        titleLabel.setFont(new Font("Arial", 26));
        titleLabel.setTextFill(Color.DARKBLUE);  // Dark Blue Color

        // Labels
        Label phoneNumLabel = new Label("Phone Number:");
        phoneNumLabel.setFont(new Font("Arial", 14));
        phoneNumLabel.setTextFill(Color.DARKBLUE);  // Dark Blue Color

        Label passwordLabel = new Label("Password:");
        passwordLabel.setFont(new Font("Arial", 14));
        passwordLabel.setTextFill(Color.DARKBLUE);  // Dark Blue Color

        // TextFields
        TextField phoneNumField = new TextField();
        phoneNumField.setPromptText("Enter your phone number");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");

        // Button
        Button loginButton = new Button("Login");
        loginButton.setFont(new Font("Arial", 16));
        loginButton.setDefaultButton(true);
        loginButton.setStyle("-fx-background-color: #00BFFF; -fx-text-fill: white;");  // Cyan Color

        // Hyperlink
        Hyperlink signUpLink = new Hyperlink("Don't have an account? Sign Up here!");
        signUpLink.setFont(new Font("Arial", 12));
        signUpLink.setTextFill(Color.DARKBLUE);  // Dark Blue Color

        // GridPane layout
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));
        gridPane.setVgap(15);
        gridPane.setHgap(10);
        gridPane.setAlignment(Pos.CENTER);

        gridPane.add(phoneNumLabel, 0, 0);
        gridPane.add(phoneNumField, 1, 0);
        gridPane.add(passwordLabel, 0, 1);
        gridPane.add(passwordField, 1, 1);
        gridPane.add(signUpLink, 1, 2);
        gridPane.add(loginButton, 1, 3);

        // Centering title and form
        VBox vbox = new VBox(20, titleLabel, gridPane);
        vbox.setAlignment(Pos.CENTER);
        vbox.setStyle("-fx-background-color: white;");  // White Background Color

        // Buttons & Links:
        loginButton.setDisable(true);
        phoneNumField.textProperty().addListener((observable, oldValue, newValue) ->
                loginButton.setDisable(newValue.trim().isEmpty() || passwordField.getText().trim().isEmpty()));
        passwordField.textProperty().addListener((observable, oldValue, newValue) ->
                loginButton.setDisable(newValue.trim().isEmpty() || phoneNumField.getText().trim().isEmpty()));

        loginButton.setOnAction(event -> {
            String phoneNum = phoneNumField.getText();
            String password = passwordField.getText();

            if (checkPhoneNumAndPassword(phoneNum, password)) {
                proceed(DataBase.getUser(phoneNum));
            }
        });

        signUpLink.setOnAction(event -> {
            SignUpPage signUpPage = new SignUpPage(stage);
            signUpPage.start();
        });

        Scene scene = new Scene(vbox, 600, 450);
        stage.setScene(scene);
        stage.show();
    }

    private boolean checkPhoneNumAndPassword(String phoneNum, String password) {
        User user = DataBase.getUser(phoneNum); //returns null if doesnt exist

        if (user == null) {
            Helper.showErrorAlert("Login Failed", "Invalid phone number");
            return false;
        } else if (!user.isPasswordCorrect(password)) {
            Helper.showErrorAlert("Login Failed", "Incorrect password");
            return false;
        }
        return true;
    }

    private void proceed(User user) {
        Helper.showSuccessAlert("Login Successful", "Welcome Back!");
        DashboardPage dashboardPage = new DashboardPage(stage);
        dashboardPage.start(user);
    }
}