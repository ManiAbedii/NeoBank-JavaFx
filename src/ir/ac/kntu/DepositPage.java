package ir.ac.kntu;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.time.Instant;

public class DepositPage {

    private final Stage stage;

    public DepositPage(Stage stage) {
        this.stage = stage;
    }

    public void start(User user) {
        // Title
        Label titleLabel = new Label("Account Deposit");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        titleLabel.setTextFill(Color.DARKSLATEGRAY);

        // Current Balance
        Label currentBalanceLabel = new Label("Current Balance: ");
        currentBalanceLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        currentBalanceLabel.setTextFill(Color.DARKSLATEGRAY);

        Label currentBalance = new Label(user.getAccount().getBalance() + "$");
        currentBalance.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        currentBalance.setTextFill(Color.DARKSLATEGRAY);

        // Enter Deposit Amount
        Label askAmount = new Label("Enter Deposit Amount: ");
        askAmount.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        askAmount.setTextFill(Color.DARKSLATEGRAY);

        TextField askAmountField = new TextField();
        askAmountField.setPrefWidth(200);

        // Buttons
        Button depositButton = createStyledButton("Deposit");
        depositButton.setDefaultButton(true);
        Button backButton = createStyledButton("<- Back");

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setStyle("-fx-background-color: #F5F5F5; -fx-border-color: #B0C4DE; -fx-border-radius: 10; -fx-padding: 20;");

        // Layout
        gridPane.add(titleLabel, 0, 0, 3, 1);
        gridPane.add(currentBalanceLabel, 0, 1);
        gridPane.add(currentBalance, 1, 1);
        gridPane.add(askAmount, 0, 3);
        gridPane.add(askAmountField, 1, 3);
        gridPane.add(backButton, 1, 6);
        gridPane.add(depositButton, 2, 6);

        // Centering content
        VBox vbox = new VBox(gridPane);
        vbox.setAlignment(Pos.CENTER);
        vbox.setStyle("-fx-background-color: #E0F7FA;");

        // Disable deposit button if amount field is empty
        depositButton.setDisable(true);
        askAmountField.textProperty().addListener((observable, oldValue, newValue) -> {
            depositButton.setDisable(newValue.trim().isEmpty());
        });

        // Button Actions
        depositButton.setOnAction(event -> {
            String amount = askAmountField.getText();
            if (checkAmount(amount)) {
                deposit(user, amount);
            } else {
                start(user);
            }
        });

        backButton.setOnAction(event -> {
            DashboardPage dashboardPage = new DashboardPage(stage);
            dashboardPage.start(user);
        });

        Scene scene = new Scene(vbox, 600, 450);
        stage.setScene(scene);
        stage.show();
    }

    private boolean checkAmount(String amount) {
        double amountInDouble;
        try {
            amountInDouble = Double.parseDouble(amount);
            if (amountInDouble <= 0) {
                Helper.showErrorAlert("Deposit Failed", "Deposit amount should be above 0$");
                return false;
            }
            return true;
        } catch (NumberFormatException ignored) {
            Helper.showErrorAlert("Deposit Failed", "Enter a valid number as the amount");
            return false;
        }
    }

    private void deposit(User user, String amountStr) {
        Alert alert = Helper.showConfirmAlert("Confirm Deposit", "Amount of " + amountStr + "$ will be deposited into your account\nProceed?");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                double amount = Double.parseDouble(amountStr);
                if (userHasRemainderFund(user)) {
                    addToRemainder(user, amount);
                    amount -= Helper.calculateRemainder(amount);
                }
                user.getAccount().setBalance(user.getAccount().getBalance() + amount);
                Transaction transaction = new Transaction(amount, TransactionType.Deposit, Instant.now());
                user.getAccount().addTransaction(transaction);
            }
        });
        DashboardPage dashboardPage = new DashboardPage(stage);
        dashboardPage.start(user);
    }

    private boolean userHasRemainderFund(User user) {
        for (InvestmentFund investmentFund : user.getCpyOfFunds()) {
            if (investmentFund instanceof RemainderFund) {
                return true;
            }
        }
        return false;
    }

    private void addToRemainder(User user, double amount) {
        for (InvestmentFund investmentFund : user.getCpyOfFunds()) {
            if (investmentFund instanceof RemainderFund) {
                investmentFund.setBalance(investmentFund.getBalance() + Helper.calculateRemainder(amount));
                Transaction transaction = new Transaction(investmentFund.getBalance(), TransactionType.Receive, Instant.now());
                investmentFund.addTransaction(transaction);
            }
        }
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