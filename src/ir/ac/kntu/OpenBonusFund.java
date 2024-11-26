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

public class OpenBonusFund {

    private final Stage stage;
    private final User user;

    public OpenBonusFund(Stage stage, User user) {
        this.stage = stage;
        this.user = user;
    }

    public void start(String name) {

        // Title Label
        Label titleLabel = new Label("Open Bonus Fund");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.DARKSLATEGRAY);  // Accent color for the title

        // Amount
        Label amountLabel = new Label("Enter Initial Deposit:");
        amountLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        amountLabel.setTextFill(Color.DARKSLATEGRAY);  // Consistent label color
        TextField amountField = new TextField();
        amountField.setPromptText("Enter amount");
        amountField.setStyle(
                "-fx-background-color: #F5F5F5; " +
                        "-fx-border-color: #B0C4DE; " +
                        "-fx-border-width: 2; " +
                        "-fx-border-radius: 5;"
        );

        // Choose Duration
        Label chooseDuration = new Label("Choose Your Desired Investment Duration:");
        chooseDuration.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        chooseDuration.setTextFill(Color.DARKSLATEGRAY);  // Consistent label color

        RadioButton twoMinuteRb = new RadioButton("2-Minute Invest");
        RadioButton fourMinuteRb = new RadioButton("4-Minute Invest");
        RadioButton sixMinuteRb = new RadioButton("6-Minute Invest");

        ToggleGroup durationToggleGroup = new ToggleGroup();
        twoMinuteRb.setToggleGroup(durationToggleGroup);
        fourMinuteRb.setToggleGroup(durationToggleGroup);
        sixMinuteRb.setToggleGroup(durationToggleGroup);

        twoMinuteRb.setSelected(true);

        // Buttons
        Button proceedButton = createStyledButton("Proceed");
        Button backButton = createStyledButton("<- Back");

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

        gridPane.add(amountLabel, 0, 1);
        gridPane.add(amountField, 1, 1);

        gridPane.add(chooseDuration, 0, 2, 2, 1);
        gridPane.add(twoMinuteRb, 0, 3);
        gridPane.add(fourMinuteRb, 0, 4);
        gridPane.add(sixMinuteRb, 0, 5);

        gridPane.add(backButton, 0, 6);
        gridPane.add(proceedButton, 1, 6);

        GridPane.setHalignment(backButton, javafx.geometry.HPos.RIGHT);
        GridPane.setHalignment(proceedButton, javafx.geometry.HPos.LEFT);

        Scene scene = new Scene(gridPane, 600, 450);
        stage.setScene(scene);
        stage.show();

        // Button Actions
        backButton.setOnAction(event -> {
            CreateFundPage createFundPage = new CreateFundPage(stage, user);
            createFundPage.start();
        });

        proceedButton.setOnAction(event -> {

            String initialDepositStr = amountField.getText();
            double initialDeposit;
            if (checkAmount(initialDepositStr)) {
                initialDeposit = Double.parseDouble(initialDepositStr);
                RadioButton selectedRadioButton = (RadioButton) durationToggleGroup.getSelectedToggle();
                String selectedText = selectedRadioButton.getText();

                Alert alert = Helper.showConfirmAlert("Confirmation", "Are you sure you want to proceed?");
                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        int duration = 0;
                        switch (selectedText) {
                            case "2-Minute Invest": {
                                duration = 2;
                                break;
                            }
                            case "4-Minute Invest": {
                                duration = 4;
                                break;
                            }
                            case "6-Minute Invest": {
                                duration = 6;
                                break;
                            }
                        }
                        user.getAccount().setBalance(user.getAccount().getBalance() - initialDeposit);
                        BonusFund bonusFund = new BonusFund(name, initialDeposit, duration, DataBase.getInterestRate(), Instant.now());
                        user.addFund(bonusFund);
                        Thread thread = new Thread(() -> {
                            for (int i = 0; i < bonusFund.getDuration(); i++) {
                                try {
                                    Thread.sleep(60_000);
                                    double amount = bonusFund.getBalance() * (1 + bonusFund.getInterestRate() / 100.0);
                                    bonusFund.setBalance(amount);
                                    Transaction transaction = new Transaction(amount, TransactionType.Receive, Instant.now());
                                    bonusFund.addTransaction(transaction);
                                } catch (InterruptedException e) {
                                    System.out.println("Interrupted\n");
                                }
                            }
                            user.getAccount().setBalance(user.getAccount().getBalance() + bonusFund.getBalance());
                            Transaction transaction = new Transaction(bonusFund.getBalance(), TransactionType.Receive, Instant.now());
                            user.getAccount().addTransaction(transaction);
                            user.removeFund(bonusFund);
                        });
                        thread.start();
                        Helper.showSuccessAlert("Successful", "Fund was created successfully");
                        FundsFirstPage firstPage = new FundsFirstPage(stage, user);
                        firstPage.start();
                    }
                });
            }
        });
    }

    private boolean checkAmount(String amount) {
        double amountInDouble;
        try {
            amountInDouble = Double.parseDouble(amount);
            if (amountInDouble <= 0) {
                Helper.showErrorAlert("Open Invest Failed", "Initial deposit should be above $0");
                return false;
            }
        } catch (NumberFormatException ignored) {
            Helper.showErrorAlert("Open Invest Failed", "Enter a valid number as the initial deposit");
            return false;
        }
        if (amountInDouble > user.getAccount().getBalance()) {
            Helper.showErrorAlert("Open Invest Failed", "Insufficient Balance");
            return false;
        }
        return true;
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
