package ir.ac.kntu;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class TransferViaAccNumPage {

    private final Stage stage;
    private final User user;

    public TransferViaAccNumPage(Stage stage, User user) {
        this.stage = stage;
        this.user = user;
    }

    public void start() {

        // Title Label
        Label titleLabel = new Label("Transfer via Account Number");
        titleLabel.setFont(new Font("Arial", 24));  // Changed font to Arial for consistency
        titleLabel.setStyle("-fx-text-fill: #00ACC1;");  // Accent color for the title

        // Ask Account Number Label
        Label askAccNum = new Label("Enter Account Number:");
        askAccNum.setFont(new Font("Arial", 16));  // Changed font to Arial for consistency
        askAccNum.setStyle("-fx-text-fill: #555555;");  // Subtle text color

        // Account Number TextField
        TextField accNumField = new TextField();
        accNumField.setPromptText("Account Number");
        accNumField.setStyle(
                "-fx-background-color: #F5F5F5; " +  // Light Gray background for the text field
                        "-fx-border-color: #B0C4DE; " +  // Light Steel Blue border color
                        "-fx-border-width: 2; " +
                        "-fx-border-radius: 5;"  // Rounded corners
        );

        // Proceed and Back Buttons
        Button proceedButton = new Button("Proceed");
        Button backButton = new Button("<- Back");

        // Style Buttons
        proceedButton.setStyle(
                "-fx-background-color: #00838F; " +  // Dark Cyan background
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-background-radius: 5;"
        );
        proceedButton.setOnMouseEntered(e -> proceedButton.setStyle(
                "-fx-background-color: #00838F; " +  // Darker Cyan on hover
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-background-radius: 5;"
        ));
        proceedButton.setOnMouseExited(e -> proceedButton.setStyle(
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

        // Disable proceed button if no text in the text field
        proceedButton.setDisable(true);
        accNumField.textProperty().addListener((observable, oldValue, newValue) -> {
            proceedButton.setDisable(newValue.trim().isEmpty());
        });

        // Button Actions
        backButton.setOnAction(event -> {
            TransferMethodChoosePage backPage = new TransferMethodChoosePage(stage, user);
            backPage.start();
        });

        proceedButton.setOnAction(event -> {
            String accNum = accNumField.getText().trim();
            User destUser = Helper.findUserByAccNum(accNum);
            if (destUser == null) {
                Helper.showErrorAlert("Transfer Failed", "Account Number is invalid");
                start();
            } else {
                TransferFinalPage transferFinalPage = new TransferFinalPage(stage, user, destUser);
                transferFinalPage.start();
            }
        });

        // Layout
        VBox vBox = new VBox(15, titleLabel, askAccNum, accNumField, proceedButton, backButton);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(20));
        vBox.setStyle("-fx-background-color: #FFFFFF;");  // Main background color

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(vBox);

        Scene scene = new Scene(borderPane, 600, 450);
        stage.setScene(scene);
        stage.show();
    }
}