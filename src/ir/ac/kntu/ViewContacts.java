package ir.ac.kntu;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.List;

public class ViewContacts {

    private final Stage stage;

    public ViewContacts(Stage stage) {
        this.stage = stage;
    }

    public void start(User user) {
        if (user.getCpyOfContacts().isEmpty()) {
            Helper.showErrorAlert("View Contacts Failed", "Contacts list is empty");
            ContactManagementPage contactManagementPage = new ContactManagementPage(stage);
            contactManagementPage.start(user);
        } else {
            chooseContact(user.getCpyOfContacts(), user);
        }
    }

    private void chooseContact(List<Contact> contactList, User user) {

        // Title Label
        Label titleLabel = new Label("Contacts");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.DARKSLATEGRAY);  // Accent color for the title

        // ListView for contacts
        ListView<Contact> listView = new ListView<>();
        listView.getItems().addAll(contactList);
        listView.setStyle(
                "-fx-background-color: #F5F5F5; " +
                        "-fx-border-color: #B0C4DE; " +
                        "-fx-border-width: 2; " +
                        "-fx-border-radius: 10;"
        );
        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        // Buttons
        Button choiceButton = createStyledButton("Contact Info");
        Button backButton = createStyledButton("<- Back");

        // Button actions
        backButton.setOnAction(actionEvent -> {
            ContactManagementPage contactManagementPage = new ContactManagementPage(stage);
            contactManagementPage.start(user);
        });

        choiceButton.setOnAction(actionEvent -> {
            Contact selectedItem = listView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                editContact(selectedItem, user);
            } else {
                Helper.showErrorAlert("Contact Info Failed", "No contact was selected");
                start(user);
            }
        });

        // Layout
        VBox vBox = new VBox(20, titleLabel, listView, choiceButton, backButton);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(20));
        vBox.setStyle("-fx-background-color: #E0F7FA;");  // Background color for the page

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(vBox);

        Scene scene = new Scene(borderPane, 600, 450);
        stage.setScene(scene);
        stage.show();
    }

    private void editContact(Contact contact, User user) {
        EditContactPage editContactPage = new EditContactPage(stage, contact, user);
        editContactPage.start();
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