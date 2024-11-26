package ir.ac.kntu;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class EditContactPage {

    private final Stage stage;
    private final Contact contact;
    private final User user;

    public EditContactPage(Stage stage, Contact contact, User user) {
        this.stage = stage;
        this.contact = contact;
        this.user = user;
    }

    public void start() {

        // Title Label
        Label titleLabel = new Label("Edit Contact");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.DARKSLATEGRAY);  // Accent color for the title

        // First Name
        Label askFirstNameLabel = new Label("First Name:");
        TextField askFirstNameField = new TextField(contact.getFirstName());
        askFirstNameField.setPromptText("Enter first name");
        askFirstNameField.setStyle(
                "-fx-background-color: #F5F5F5; " +
                        "-fx-border-color: #B0C4DE; " +
                        "-fx-border-width: 2; " +
                        "-fx-border-radius: 5;"
        );

        // Last Name
        Label askLastNameLabel = new Label("Last Name:");
        TextField askLastNameField = new TextField(contact.getLastName());
        askLastNameField.setPromptText("Enter last name");
        askLastNameField.setStyle(
                "-fx-background-color: #F5F5F5; " +
                        "-fx-border-color: #B0C4DE; " +
                        "-fx-border-width: 2; " +
                        "-fx-border-radius: 5;"
        );

        // Buttons
        Button editButton = createStyledButton("Save Changes");
        Button delButton = createStyledButton("Delete Contact");
        Button backButton = createStyledButton("<- Back");

        // Disable edit button if fields are empty
        editButton.setDisable(true);
        askFirstNameField.textProperty().addListener((observable, oldValue, newValue) ->
                editButton.setDisable(newValue.trim().isEmpty() || askLastNameField.getText().trim().isEmpty())
        );
        askLastNameField.textProperty().addListener((observable, oldValue, newValue) ->
                editButton.setDisable(newValue.trim().isEmpty() || askFirstNameField.getText().trim().isEmpty())
        );

        // Define button actions
        editButton.setOnAction(event -> {
            String firstName = askFirstNameField.getText();
            String lastName = askLastNameField.getText();

            Alert alert = Helper.showConfirmAlert("Edit Contact", "Are you sure you want to save the changes?");

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    contact.setFirstName(firstName);
                    contact.setLastName(lastName);
                    ViewContacts viewContacts = new ViewContacts(stage);
                    viewContacts.start(user);
                } else {
                    start();
                }
            });
        });

        delButton.setOnAction(event -> {
            Alert alert = Helper.showConfirmAlert("Delete Contact", "Are you sure you want to delete this contact?");

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    user.removeContact(contact);
                    ViewContacts viewContacts = new ViewContacts(stage);
                    viewContacts.start(user);
                } else {
                    start();
                }
            });
        });

        backButton.setOnAction(event -> {
            ViewContacts viewContacts = new ViewContacts(stage);
            viewContacts.start(user);
        });

        // GridPane layout
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setStyle("-fx-background-color: #E0F7FA;");  // Background color for the page

        // Add elements to GridPane
        gridPane.add(titleLabel, 0, 0, 2, 1);
        GridPane.setColumnSpan(titleLabel, 2);
        GridPane.setHalignment(titleLabel, javafx.geometry.HPos.CENTER);

        gridPane.add(askFirstNameLabel, 0, 1);
        gridPane.add(askFirstNameField, 1, 1);

        gridPane.add(askLastNameLabel, 0, 2);
        gridPane.add(askLastNameField, 1, 2);

        gridPane.add(backButton, 0, 3);
        gridPane.add(editButton, 1, 3);
        gridPane.add(delButton, 2, 3);

        GridPane.setHalignment(backButton, javafx.geometry.HPos.RIGHT);
        GridPane.setHalignment(editButton, javafx.geometry.HPos.CENTER);
        GridPane.setHalignment(delButton, javafx.geometry.HPos.LEFT);

        Scene scene = new Scene(gridPane, 600, 450);
        stage.setScene(scene);
        stage.show();
    }

    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setPrefWidth(200);
        button.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        button.setStyle(
                "-fx-background-color: #00838F; " +
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 5;"
        );
        button.setOnMouseEntered(e -> button.setStyle(
                "-fx-background-color: #00ACC1; " +
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 5;"
        ));
        button.setOnMouseExited(e -> button.setStyle(
                "-fx-background-color: #00ACC1; " +
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 5;"
        ));
        return button;
    }
}