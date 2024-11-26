package ir.ac.kntu;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class AddContactPage {

    private final Stage stage;

    public AddContactPage(Stage stage) {
        this.stage = stage;
    }

    public void start(User user) {
        // Title
        Label titleLabel = new Label("Add Contact");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        titleLabel.setTextFill(Color.DARKSLATEGRAY);

        // Labels and TextFields
        Label askFirstNameLabel = createStyledLabel("Enter first name: ");
        TextField askFirstNameField = createStyledTextField();

        Label askLastNameLabel = createStyledLabel("Enter last name: ");
        TextField askLastNameField = createStyledTextField();

        Label askPhoneNumLabel = createStyledLabel("Enter phone number: ");
        TextField askPhoneNumField = createStyledTextField();

        // Buttons
        Button backButton = createStyledButton("<- Back");
        Button addButton = createStyledButton("Add Contact");

        // GridPane for form layout
        GridPane formGrid = new GridPane();
        formGrid.setPadding(new Insets(20));
        formGrid.setHgap(10);
        formGrid.setVgap(20);
        formGrid.setAlignment(Pos.CENTER);
        formGrid.setStyle("-fx-background-color: #F5F5F5; -fx-border-color: #B0C4DE; -fx-border-radius: 10; -fx-padding: 20;");

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setHalignment(HPos.RIGHT);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHalignment(HPos.LEFT);
        formGrid.getColumnConstraints().addAll(col1, col2);

        formGrid.add(askFirstNameLabel, 0, 0);
        formGrid.add(askFirstNameField, 1, 0);
        formGrid.add(askLastNameLabel, 0, 1);
        formGrid.add(askLastNameField, 1, 1);
        formGrid.add(askPhoneNumLabel, 0, 2);
        formGrid.add(askPhoneNumField, 1, 2);

        // Button layout
        HBox buttonBox = new HBox(10, backButton, addButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(20, 0, 0, 0));

        // Main layout
        VBox mainLayout = new VBox(20, titleLabel, formGrid, buttonBox);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setStyle("-fx-background-color: #E0F7FA;");

        // Button Actions
        backButton.setOnAction(event -> {
            ContactManagementPage contactManagementPage = new ContactManagementPage(stage);
            contactManagementPage.start(user);
        });

        addButton.setDisable(true);
        askFirstNameField.textProperty().addListener((observable, oldValue, newValue) -> checkFieldsFilled(addButton, askFirstNameField, askLastNameField, askPhoneNumField));
        askLastNameField.textProperty().addListener((observable, oldValue, newValue) -> checkFieldsFilled(addButton, askFirstNameField, askLastNameField, askPhoneNumField));
        askPhoneNumField.textProperty().addListener((observable, oldValue, newValue) -> checkFieldsFilled(addButton, askFirstNameField, askLastNameField, askPhoneNumField));

        addButton.setOnAction(event -> {
            String firstName = askFirstNameField.getText();
            String lastName = askLastNameField.getText();
            String phoneNum = askPhoneNumField.getText();
            if (checkPhoneNum(phoneNum, user)) {
                Contact contact = new Contact(firstName, lastName, phoneNum);
                Helper.showSuccessAlert("Successful Operation", "Contact added successfully");
                user.addContact(contact);
                ContactManagementPage contactManagementPage = new ContactManagementPage(stage);
                contactManagementPage.start(user);
            }
        });

        Scene scene = new Scene(mainLayout, 600, 450);
        stage.setScene(scene);
        stage.show();
    }

    private void checkFieldsFilled(Button addButton, TextField firstNameField, TextField lastNameField, TextField phoneNumField) {
        boolean allFieldsFilled = !firstNameField.getText().trim().isEmpty()
                && !lastNameField.getText().trim().isEmpty()
                && !phoneNumField.getText().trim().isEmpty();

        addButton.setDisable(!allFieldsFilled);
    }

    private boolean checkPhoneNum(String phoneNum, User mainUser) {
        User user = DataBase.getUser(phoneNum);
        if (mainUser.equals(user)) {
            Helper.showErrorAlert("Add Contact Failed", "Cannot add yourself as contact");
            return false;
        }
        for (Contact contact : mainUser.getCpyOfContacts()) {
            if (contact.getPhoneNum().equals(phoneNum)) {
                Helper.showErrorAlert("Add Contact Failed", "You already have this person as your contact");
                return false;
            }
        }
        if (user == null) {
            Helper.showErrorAlert("Add Contact Failed", "Invalid phone number");
            return false;
        } else {
            return true;
        }
    }

    private Label createStyledLabel(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        label.setTextFill(Color.DARKSLATEGRAY);
        return label;
    }

    private TextField createStyledTextField() {
        TextField textField = new TextField();
        textField.setStyle("-fx-background-color: white; -fx-border-color: #B0C4DE; -fx-border-radius: 5;");
        return textField;
    }

    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setPrefWidth(150);
        button.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        button.setStyle("-fx-background-color: #00ACC1; -fx-text-fill: white; -fx-background-radius: 5;");
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #00838F; -fx-text-fill: white; -fx-background-radius: 5;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #00ACC1; -fx-text-fill: white; -fx-background-radius: 5;"));
        return button;
    }
}