package ir.ac.kntu;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class TransferMethodChoosePage {

    private final Stage stage;
    private final User user;

    public TransferMethodChoosePage(Stage stage, User user) {
        this.stage = stage;
        this.user = user;
    }

    public void start() {

        // Title Label
        Label titleLabel = new Label("Transfer");
        titleLabel.setFont(new Font("Arial", 24));  // Changed font to Arial
        titleLabel.setStyle("-fx-text-fill: #00ACC1;");  // Accent color for the title

        // Method Selection
        Label radioLabel = new Label("Choose transfer method:");
        radioLabel.setFont(new Font("Arial", 16));  // Changed font to Arial
        radioLabel.setStyle("-fx-text-fill: #555555;");  // Subtle text color

        RadioButton viaAccNumRB = new RadioButton("Enter Account Number");
        RadioButton viaContactsRB = new RadioButton("Choose From Contacts");
        RadioButton viaRecentTransactionsRB = new RadioButton("Choose From Recent Account Numbers");

        ToggleGroup methodToggleGroup = new ToggleGroup();
        viaAccNumRB.setToggleGroup(methodToggleGroup);
        viaContactsRB.setToggleGroup(methodToggleGroup);
        viaRecentTransactionsRB.setToggleGroup(methodToggleGroup);

        viaAccNumRB.setSelected(true);

        // Style Radio Buttons
        viaAccNumRB.setStyle("-fx-text-fill: #333333;");
        viaContactsRB.setStyle("-fx-text-fill: #333333;");
        viaRecentTransactionsRB.setStyle("-fx-text-fill: #333333;");

        // Buttons
        Button proceedButton = new Button("Proceed");
        Button backButton = new Button("<- Back");

        // Style Buttons
        proceedButton.setStyle(
                "-fx-background-color: #00838F; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-background-radius: 5;"
        );
        proceedButton.setOnMouseEntered(e -> proceedButton.setStyle(
                "-fx-background-color: #00838F; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-background-radius: 5;"
        ));
        proceedButton.setOnMouseExited(e -> proceedButton.setStyle(
                "-fx-background-color: #00838F; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-background-radius: 5;"
        ));

        backButton.setStyle(
                "-fx-background-color: #00838F; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-background-radius: 5;"
        );
        backButton.setOnMouseEntered(e -> backButton.setStyle(
                "-fx-background-color: #00838F; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-background-radius: 5;"
        ));
        backButton.setOnMouseExited(e -> backButton.setStyle(
                "-fx-background-color: #00838F; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-background-radius: 5;"
        ));

        // Disable proceed button initially
        proceedButton.setDisable(true);

        // Update button state based on selected transfer method
        methodToggleGroup.selectedToggleProperty().addListener((observable, oldToggle, newToggle) -> {
            proceedButton.setDisable(newToggle == null);
        });

        // Button Actions
        backButton.setOnAction(event -> {
            DashboardPage dashboardPage = new DashboardPage(stage);
            dashboardPage.start(user);
        });

        proceedButton.setOnAction(event -> {
            RadioButton selectedRadioButton = (RadioButton) methodToggleGroup.getSelectedToggle();
            String selectedText = selectedRadioButton.getText();
            switch (selectedText) {
                case "Enter Account Number": {
                    TransferViaAccNumPage transferViaAccNumPage = new TransferViaAccNumPage(stage, user);
                    transferViaAccNumPage.start();
                    break;
                }
                case "Choose From Contacts": {
                    if (checkContactsList()) {
                        TransferViaContactsPage transferViaContactsPage = new TransferViaContactsPage(stage, user);
                        transferViaContactsPage.start();
                    } else {
                        Helper.showErrorAlert("Transfer Failed", "Contacts list is empty");
                        start();
                    }
                    break;
                }
                case "Choose From Recent Account Numbers": {
                    if (checkRecentsList()) {
                        TransferViaRecentPage transferViaRecentPage = new TransferViaRecentPage(stage, user);
                        transferViaRecentPage.start();
                    } else {
                        Helper.showErrorAlert("Transfer Failed", "Recent Transactions list is empty");
                        start();
                    }
                    break;
                }
            }
        });

        // Layout
        VBox vBox = new VBox(15, titleLabel, radioLabel, viaAccNumRB, viaContactsRB, viaRecentTransactionsRB, proceedButton, backButton);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(20));
        vBox.setStyle("-fx-background-color: #FFFFFF;");  // Main background color

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(vBox);

        Scene scene = new Scene(borderPane, 600, 450);
        stage.setScene(scene);
        stage.show();
    }

    private boolean checkContactsList() {
        return !user.getCpyOfContacts().isEmpty();
    }

    private boolean checkRecentsList() {
        for (Transaction transaction : user.getAccount().getCpyTransactions()) {
            if (transaction.getTransactionType().equals(TransactionType.Transfer)) {
                return true;
            }
        }
        return false;
    }
}
