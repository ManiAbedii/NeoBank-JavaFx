package ir.ac.kntu;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class DashboardPage {

    private final Stage stage;

    public DashboardPage(Stage stage) {
        this.stage = stage;
    }

    public void start(User user) {
        // Title
        Label titleLabel = new Label("Dashboard");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        titleLabel.setTextFill(Color.DARKBLUE);

        // Current Balance
        Label currentBalanceLabel = new Label("Current Balance: ");
        currentBalanceLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        currentBalanceLabel.setTextFill(Color.DARKBLUE);

        Label currentBalance = new Label(Math.round(user.getAccount().getBalance() * 100.0) / 100.0 + "$");
        currentBalance.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        currentBalance.setTextFill(Color.DARKBLUE);

        // Buttons
        Button depositButton = createStyledButton("Account Deposit");
        Button contactManagementButton = createStyledButton("Contacts Management");
        Button transferButton = createStyledButton("Money Transfer");
        Button fundsButton = createStyledButton("Investment Funds");
        Button loanReqButton = createStyledButton("Loan Request");
        Button loanPayButton = createStyledButton("Loan Management");
        Button supReqsButton = createStyledButton("Support Requests");
        Button backButton = createStyledButton("Log Out");

        depositButton.setOnAction(event -> handleDepositButton(user));
        contactManagementButton.setOnAction(event -> handleContactsManagementButton(user));
        transferButton.setOnAction(event -> handleTransferButton(user));
        fundsButton.setOnAction(event -> handleFundsButton(user));
        loanReqButton.setOnAction(event -> handleLoanReqButton(user));
        loanPayButton.setOnAction(event -> handleLoanPayButton(user));
        supReqsButton.setOnAction(event -> handleSupReqsButton(user));
        backButton.setOnAction(event -> handleBackButton());

        // Transactions Table
        Label tableLabel = new Label("Transactions");
        tableLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        tableLabel.setTextFill(Color.DARKBLUE);

        ListView<Transaction> listView = new ListView<>();
        listView.getItems().addAll(user.getAccount().getCpyTransactions());
        listView.setPrefHeight(200);
        listView.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #00BFFF; -fx-border-radius: 5;");

        // Empty Transactions Label
        Label emptyLabel = new Label("This list is empty:)");
        emptyLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        emptyLabel.setTextFill(Color.DARKBLUE);

        // Layout
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setStyle("-fx-background-color: #F0F8FF; -fx-border-color: #00BFFF; -fx-border-radius: 10; -fx-padding: 20;");

        // Aligning the title and balance
        gridPane.add(titleLabel, 0, 0, 3, 1); // span across 3 columns
        gridPane.add(currentBalanceLabel, 0, 1);
        gridPane.add(currentBalance, 1, 1, 3, 1);
        gridPane.add(tableLabel, 0, 4, 4, 1);

        if (user.getAccount().getCpyTransactions().isEmpty()) {
            gridPane.add(emptyLabel, 0, 5, 4, 1);
        } else {
            gridPane.add(listView, 0, 5, 4, 1);
        }

        gridPane.add(depositButton, 0, 2);
        gridPane.add(contactManagementButton, 1, 2);
        gridPane.add(transferButton, 2, 2);
        gridPane.add(fundsButton, 3, 2);
        gridPane.add(loanReqButton, 0, 3);
        gridPane.add(loanPayButton, 1, 3);
        gridPane.add(supReqsButton, 2, 3);
        gridPane.add(backButton, 3, 3);

        // Centering content
        VBox vbox = new VBox(gridPane);
        vbox.setAlignment(Pos.CENTER);
        vbox.setStyle("-fx-background-color: #E6F7FF;");

        Scene scene = new Scene(vbox, 600, 450);
        stage.setScene(scene);
        stage.show();
    }

    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setPrefWidth(150);
        button.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        button.setStyle("-fx-background-color: #00BFFF; -fx-text-fill: white; -fx-background-radius: 5;");
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #008CBA; -fx-text-fill: white; -fx-background-radius: 5;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #00BFFF; -fx-text-fill: white; -fx-background-radius: 5;"));
        return button;
    }

    private void handleDepositButton(User user) {
        DepositPage depositPage = new DepositPage(stage);
        depositPage.start(user);
    }

    private void handleContactsManagementButton(User user) {
        ContactManagementPage contactManagementPage = new ContactManagementPage(stage);
        contactManagementPage.start(user);
    }

    private void handleTransferButton(User user) {
        TransferMethodChoosePage transferMethodChoosePage = new TransferMethodChoosePage(stage, user);
        transferMethodChoosePage.start();
    }

    private void handleFundsButton(User user) {
        FundsFirstPage fundsFirstPage = new FundsFirstPage(stage, user);
        fundsFirstPage.start();
    }

    private void handleLoanReqButton(User user) {
        FirstLoanReqPage firstLoanReqPage = new FirstLoanReqPage(stage);
        firstLoanReqPage.start(user);
    }

    private void handleLoanPayButton(User user) {
        if (user.getCpyOfLoans().isEmpty()) {
            Helper.showErrorAlert("Task Failed", "Loan list is empty");
        } else {
            LoanAndPayPage loanAndPayPage = new LoanAndPayPage(stage, user);
            loanAndPayPage.start();
        }
    }

    private void handleSupReqsButton(User user) {
        SuppReqsPage suppReqsPage = new SuppReqsPage(stage, user);
        suppReqsPage.start();
    }

    private void handleBackButton() {
        LoginPage loginPage = new LoginPage(stage);
        loginPage.start();
    }
}
