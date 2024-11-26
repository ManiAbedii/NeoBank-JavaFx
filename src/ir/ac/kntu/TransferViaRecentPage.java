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

import java.util.ArrayList;
import java.util.List;

public class TransferViaRecentPage {

    private final Stage stage;
    private final User user;

    public TransferViaRecentPage(Stage stage, User user) {
        this.stage = stage;
        this.user = user;
    }

    public void start() {

        // Title Label
        Label titleLabel = new Label("Transfer via Recent Account Numbers");
        titleLabel.setFont(new Font("Times New Roman", 24));
        titleLabel.setStyle("-fx-text-fill: #00ACC1;"); // Accent color

        // List Label
        Label listLabel = new Label("Choose an account number:");
        listLabel.setFont(new Font("Times New Roman", 16));
        listLabel.setStyle("-fx-text-fill: #555555;"); // Subtle text color

        // ListView for Account Numbers
        ListView<String> listView = new ListView<>();
        List<String> recentTransfersAccountNumbers = findRecentTransfers();
        listView.getItems().addAll(recentTransfersAccountNumbers);
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

        // Button Actions
        backButton.setOnAction(actionEvent -> {
            TransferMethodChoosePage backPage = new TransferMethodChoosePage(stage, user);
            backPage.start();
        });

        choiceButton.setOnAction(actionEvent -> {
            String selectedItem = listView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                User destUser = Helper.findUserByAccNum(selectedItem);
                if (destUser != null) {
                    TransferFinalPage finalPage = new TransferFinalPage(stage, user, destUser);
                    finalPage.start();
                } else {
                    Helper.showErrorAlert("Transfer Failed", "Account Number is invalid");
                    start();
                }
            } else {
                Helper.showErrorAlert("Transfer Failed", "No account number was selected");
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

    private List<String> findRecentTransfers() {
        List<String> recentAccNums = new ArrayList<>();
        for (Transaction transaction : user.getAccount().getCpyTransactions()) {
            if (transaction.getTransactionType().equals(TransactionType.Transfer)) {
                recentAccNums.add(transaction.getAccNum());
            }
        }
        return recentAccNums;
    }
}
