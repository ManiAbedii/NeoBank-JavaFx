package ir.ac.kntu;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.time.Instant;

public class TransferFinalPage {

    private final Stage stage;
    private final User user;
    private final User destUser;

    public TransferFinalPage(Stage stage, User user, User destUser) {
        this.stage = stage;
        this.user = user;
        this.destUser = destUser;
    }

    public void start() {

        // Title Label
        Label titleLabel = new Label("Transfer Amount");
        titleLabel.setFont(new Font("Times New Roman", 24));
        titleLabel.setStyle("-fx-text-fill: #00ACC1;"); // Accent color for the title

        // Amount Label and Text Field
        Label amountLabel = new Label("Enter Amount:");
        amountLabel.setFont(new Font("Times New Roman", 16));
        amountLabel.setStyle("-fx-text-fill: #555555;"); // Subtle text color

        TextField amountField = new TextField();
        amountField.setPromptText("Enter amount in $");
        amountField.setStyle("-fx-border-color: #00ACC1; -fx-border-radius: 5;"); // Accent color for the border

        // Buttons
        Button transferButton = new Button("Transfer");
        Button backButton = new Button("<- Back");

        // Style Buttons
        transferButton.setStyle(
                "-fx-background-color: #00ACC1; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-background-radius: 5;"
        );
        transferButton.setOnMouseEntered(e -> transferButton.setStyle(
                "-fx-background-color: #00838F; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-background-radius: 5;"
        ));
        transferButton.setOnMouseExited(e -> transferButton.setStyle(
                "-fx-background-color: #00ACC1; " +
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

        // Disable transfer button initially
        transferButton.setDisable(true);

        // Update button state based on amount field
        amountField.textProperty().addListener((observable, oldValue, newValue) -> {
            transferButton.setDisable(newValue.trim().isEmpty());
        });

        // Button Actions
        backButton.setOnAction(event -> {
            TransferMethodChoosePage backPage = new TransferMethodChoosePage(stage, user);
            backPage.start();
        });

        transferButton.setOnAction(event -> {
            String amountText = amountField.getText();
            if (checkAmount(amountText)) {
                Alert alert = Helper.showConfirmAlert("Transfer Confirmation",
                        String.format("Are you sure you want to transfer $%.2f to %s %s?",
                                Double.parseDouble(amountText), destUser.getFirstName(), destUser.getLastName()));
                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        double amountInDouble = Double.parseDouble(amountText);

                        // Perform the transfer
                        user.getAccount().setBalance(user.getAccount().getBalance() - (1.01 * amountInDouble));

                        // Record the transactions
                        Transaction transaction = new Transaction(amountInDouble, TransactionType.Transfer, Instant.now(), destUser.getAccount().getAccNum());
                        user.getAccount().addTransaction(transaction);

                        if (userHasRemainderFund(destUser)) {
                            addToRemainder(destUser, amountInDouble);
                            amountInDouble -= Helper.calculateRemainder(amountInDouble);
                        }

                        destUser.getAccount().setBalance(destUser.getAccount().getBalance() + amountInDouble);
                        transaction = new Transaction(amountInDouble, TransactionType.Receive, Instant.now());
                        destUser.getAccount().addTransaction(transaction);

                        Helper.showSuccessAlert("Transfer Done", "Transfer successful!");

                        // Return to the dashboard page
                        DashboardPage dashboardPage = new DashboardPage(stage);
                        dashboardPage.start(user);
                    }
                });
            }
        });

        // Layout
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setAlignment(Pos.CENTER);

        gridPane.add(titleLabel, 0, 0, 2, 1);
        gridPane.add(amountLabel, 0, 1);
        gridPane.add(amountField, 1, 1);
        gridPane.add(backButton, 0, 2);
        gridPane.add(transferButton, 1, 2);

        Scene scene = new Scene(gridPane, 600, 450);
        stage.setScene(scene);
        stage.show();
    }

    private boolean checkAmount(String amountText) {
        double amount;
        try {
            amount = Double.parseDouble(amountText);
            if (amount <= 0) {
                Helper.showErrorAlert("Transfer Failed", "Transfer amount must be greater than $0.");
                return false;
            }
            if (amount * 1.01 > user.getAccount().getBalance()) {
                Helper.showErrorAlert("Transfer Failed", "Insufficient Balance.");
                return false;
            }
        } catch (NumberFormatException e) {
            Helper.showErrorAlert("Transfer Failed", "Please enter a valid number.");
            return false;
        }
        return true;
    }

    private boolean userHasRemainderFund(User user) {
        for (InvestmentFund fund : user.getCpyOfFunds()) {
            if (fund instanceof RemainderFund) {
                return true;
            }
        }
        return false;
    }

    private void addToRemainder(User user, double amount) {
        for (InvestmentFund fund : user.getCpyOfFunds()) {
            if (fund instanceof RemainderFund) {
                RemainderFund remainderFund = (RemainderFund) fund;
                remainderFund.setBalance(remainderFund.getBalance() + Helper.calculateRemainder(amount));
                Transaction transaction = new Transaction(Helper.calculateRemainder(amount), TransactionType.Receive, Instant.now());
                remainderFund.addTransaction(transaction);
            }
        }
    }
}
