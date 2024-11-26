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

import java.time.Instant;

public class FundTransfersPage {

    private final Stage stage;
    private final User user;
    private final InvestmentFund investmentFund;

    public FundTransfersPage(Stage stage, User user, InvestmentFund investmentFund) {
        this.stage = stage;
        this.user = user;
        this.investmentFund = investmentFund;
    }

    public void start() {

        // Title Label
        Label titleLabel = new Label("Fund Transactions");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.DARKSLATEGRAY);  // Accent color for the title

        // Transfer Option
        Label chooseWay = new Label("Choose Transfer Type:");
        chooseWay.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        chooseWay.setTextFill(Color.DARKSLATEGRAY);

        RadioButton fromFundToMain = new RadioButton("From This Fund To Main Account");
        RadioButton fromMainToFund = new RadioButton("From Main Account To This Fund");

        ToggleGroup wayToggleGroup = new ToggleGroup();
        fromFundToMain.setToggleGroup(wayToggleGroup);
        fromMainToFund.setToggleGroup(wayToggleGroup);

        fromFundToMain.setSelected(true);

        // Transfer Amount
        Label getAmount = new Label("Enter Transfer Amount:");
        getAmount.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        getAmount.setTextFill(Color.DARKSLATEGRAY);

        TextField amountField = new TextField();
        amountField.setPromptText("Amount");
        amountField.setStyle(
                "-fx-background-color: #F5F5F5; " +
                        "-fx-border-color: #B0C4DE; " +
                        "-fx-border-width: 2; " +
                        "-fx-border-radius: 5;"
        );

        // Buttons
        Button proceedButton = createStyledButton("Transfer");
        Button backButton = createStyledButton("<- Back");
        proceedButton.setDefaultButton(true);

        // GridPane Layout
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

        gridPane.add(chooseWay, 0, 1, 2, 1);
        GridPane.setHalignment(chooseWay, javafx.geometry.HPos.CENTER);

        gridPane.add(fromFundToMain, 0, 2, 2, 1);
        GridPane.setHalignment(fromFundToMain, javafx.geometry.HPos.CENTER);

        gridPane.add(fromMainToFund, 0, 3, 2, 1);
        GridPane.setHalignment(fromMainToFund, javafx.geometry.HPos.CENTER);

        gridPane.add(getAmount, 0, 4);
        gridPane.add(amountField, 1, 4);

        gridPane.add(backButton, 0, 6);
        gridPane.add(proceedButton, 1, 6);

        GridPane.setHalignment(backButton, javafx.geometry.HPos.RIGHT);
        GridPane.setHalignment(proceedButton, javafx.geometry.HPos.LEFT);

        // Button Actions
        backButton.setOnAction(event -> {
            FundInfoPage infoPage = new FundInfoPage(stage, user, investmentFund);
            infoPage.start();
        });

        proceedButton.setOnAction(event -> {
            String amountStr = amountField.getText();
            double amount;
            if (checkAmount(amountStr)) {
                amount = Double.parseDouble(amountStr);
            } else {
                return;
            }

            RadioButton selected = (RadioButton) wayToggleGroup.getSelectedToggle();
            String selectedText = selected.getText();

            switch (selectedText) {
                case "From This Fund To Main Account": {
                    if (balanceFTOMIsOk(amount)) {
                        Alert alert = Helper.showConfirmAlert("Confirmation", "Are you sure you want to proceed?");
                        alert.showAndWait().ifPresent(response -> {
                            if (response == ButtonType.OK) {
                                commitTransferFToM(amount);
                            }
                        });
                    }
                    break;
                }
                case "From Main Account To This Fund": {
                    if (investmentFund instanceof SavingFund) {
                        if (balanceMTOFIsOk(amount)) {
                            Alert alert = Helper.showConfirmAlert("Confirmation", "Are you sure you want to proceed?");
                            alert.showAndWait().ifPresent(response -> {
                                if (response == ButtonType.OK) {
                                    commitTransferMToF(amount);
                                }
                            });
                        }
                    } else {
                        Helper.showErrorAlert("Operation Failed", "Cannot transfer money TO this fund");
                    }
                    break;
                }
            }
        });

        Scene scene = new Scene(gridPane, 600, 450);
        stage.setScene(scene);
        stage.show();
    }

    private boolean checkAmount(String amount) {
        double amountInDouble;
        try {
            amountInDouble = Double.parseDouble(amount);
            if (amountInDouble <= 0) {
                Helper.showErrorAlert("Transfer Failed", "Transfer amount should be above $0");
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            Helper.showErrorAlert("Transfer Failed", "Enter a valid number for the amount");
            return false;
        }
    }

    private boolean balanceFTOMIsOk(double amount) {
        if (investmentFund.getBalance() >= amount) {
            return true;
        } else {
            Helper.showErrorAlert("Transfer Failed", "Insufficient Balance");
            return false;
        }
    }

    private boolean balanceMTOFIsOk(double amount) {
        if (user.getAccount().getBalance() >= amount) {
            return true;
        } else {
            Helper.showErrorAlert("Transfer Failed", "Insufficient Balance");
            return false;
        }
    }

    private void commitTransferFToM(double amount) {
        investmentFund.setBalance(investmentFund.getBalance() - amount);
        user.getAccount().setBalance(user.getAccount().getBalance() + amount);
        Transaction transaction = new Transaction(amount, TransactionType.Transfer, Instant.now());
        investmentFund.addTransaction(transaction);
        Helper.showSuccessAlert("Successful Transfer", "The transfer was successful");
        FundInfoPage fundInfoPage = new FundInfoPage(stage, user, investmentFund);
        fundInfoPage.start();
    }

    private void commitTransferMToF(double amount) {
        investmentFund.setBalance(investmentFund.getBalance() + amount);
        user.getAccount().setBalance(user.getAccount().getBalance() - amount);
        Transaction transaction = new Transaction(amount, TransactionType.Receive, Instant.now());
        investmentFund.addTransaction(transaction);
        Helper.showSuccessAlert("Successful Transfer", "The transfer was successful");
        FundInfoPage fundInfoPage = new FundInfoPage(stage, user, investmentFund);
        fundInfoPage.start();
    }

    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setPrefWidth(150);
        button.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        button.setStyle(
                "-fx-background-color: #00838F; " +
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 5;"
        );
        button.setOnMouseEntered(e -> button.setStyle(
                "-fx-background-color: #00838F; " +
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 5;"
        ));
        button.setOnMouseExited(e -> button.setStyle(
                "-fx-background-color: #00838F; " +
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 5;"
        ));
        return button;
    }
}
