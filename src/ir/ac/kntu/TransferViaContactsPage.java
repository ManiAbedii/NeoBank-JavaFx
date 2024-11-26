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
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class TransferViaContactsPage {

    private final Stage stage;
    private final User user;

    public TransferViaContactsPage(Stage stage, User user) {
        this.stage = stage;
        this.user = user;
    }

    public void start() {

        // Title Label
        Label titleLabel = new Label("Transfer via Contacts");
        titleLabel.setFont(new Font("Times New Roman", 24));
        titleLabel.setStyle("-fx-text-fill: #00ACC1;"); // Accent color

        // List Label
        Label listLabel = new Label("Choose a contact:");
        listLabel.setFont(new Font("Times New Roman", 16));
        listLabel.setStyle("-fx-text-fill: #555555;"); // Subtle text color

        // ListView for Contacts
        ListView<Contact> listView = new ListView<>();
        listView.getItems().addAll(user.getCpyOfContacts());
        listView.setStyle(
                "-fx-background-color: #F0F0F0; " +
                        "-fx-border-color: #00ACC1; " +
                        "-fx-border-width: 2; " +
                        "-fx-border-radius: 10;"
        );
        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        // Buttons
        Button choiceButton = new Button("Proceed");
        Button backButton = new Button("<- Back");

        // Style Buttons
        choiceButton.setStyle(
                "-fx-background-color: #00838F; " +  // Dark Cyan background
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-background-radius: 5;"
        );
        choiceButton.setOnMouseEntered(e -> choiceButton.setStyle(
                "-fx-background-color: #00838F; " +  // Darker Cyan on hover
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-background-radius: 5;"
        ));
        choiceButton.setOnMouseExited(e -> choiceButton.setStyle(
                "-fx-background-color: #00838F; " +  // Dark Cyan background
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-background-radius: 5;"
        ));

        backButton.setStyle(
                "-fx-background-color: #00838F; " +  // Coral Red background
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-background-radius: 5;"
        );
        backButton.setOnMouseEntered(e -> backButton.setStyle(
                "-fx-background-color: #00838F; " +  // Darker Coral Red on hover
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-background-radius: 5;"
        ));
        backButton.setOnMouseExited(e -> backButton.setStyle(
                "-fx-background-color: #00838F; " +  // Coral Red background
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-background-radius: 5;"
        ));

        // Button actions
        backButton.setOnAction(actionEvent -> {
            TransferMethodChoosePage backPage = new TransferMethodChoosePage(stage, user);
            backPage.start();
        });

        choiceButton.setOnAction(actionEvent -> {
            Contact selectedItem = listView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                if (checkMutualContact(selectedItem)) {
                    TransferFinalPage proceed = new TransferFinalPage(stage, user, getUserOfContact(selectedItem));
                    proceed.start();
                } else {
                    Helper.showErrorAlert("Transfer Failed", "You have to be in your contact's contact list to perform this transfer");
                }
            } else {
                Helper.showErrorAlert("Transfer Failed", "No contact was selected");
                start();
            }
        });

        // Layout
        VBox vBox = new VBox(15, titleLabel, listLabel, listView, choiceButton, backButton);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(20));
        vBox.setStyle("-fx-background-color: #FFFFFF;"); // Main background color

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(vBox);

        Scene scene = new Scene(borderPane, 600, 450);
        stage.setScene(scene);
        stage.show();
    }

    private User getUserOfContact(Contact contact) {
        for (User resUser : DataBase.getCpyUsersOfTheBank()) {
            if (resUser.getPhoneNum().equals(contact.getPhoneNum())) {
                return resUser;
            }
        }
        return null;
    }

    private boolean checkMutualContact(Contact destContact) {
        User destUser = getUserOfContact(destContact);
        for (Contact contact : destUser.getCpyOfContacts()) {
            if (contact.getPhoneNum().equals(user.getPhoneNum())) {
                return true;
            }
        }
        return false;
    }
}
