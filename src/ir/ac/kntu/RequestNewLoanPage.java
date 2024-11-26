package ir.ac.kntu;

import javafx.application.Platform;
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

public class RequestNewLoanPage {

    private final Stage stage;
    private final User user;

    public RequestNewLoanPage(Stage stage, User user) {
        this.stage = stage;
        this.user = user;
    }

    public void start() {

        // Title Label
        Label titleLabel = new Label("Request New Loan");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.DARKSLATEGRAY);  // Accent color for the title

        // Loan Title
        Label nameLabel = new Label("Enter Loan Title:");
        TextField nameField = new TextField();
        nameField.setPromptText("Enter loan title");
        nameField.setStyle(
                "-fx-background-color: #F5F5F5; " +
                        "-fx-border-color: #B0C4DE; " +
                        "-fx-border-width: 2; " +
                        "-fx-border-radius: 5;"
        );

        // Loan Amount
        Label amountLabel = new Label("Enter Amount:");
        TextField amountField = new TextField();
        amountField.setPromptText("Enter loan amount");
        amountField.setStyle(
                "-fx-background-color: #F5F5F5; " +
                        "-fx-border-color: #B0C4DE; " +
                        "-fx-border-width: 2; " +
                        "-fx-border-radius: 5;"
        );

        // Choose Duration
        Label chooseDuration = new Label("Choose Your Desired Loan Duration:");
        chooseDuration.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        chooseDuration.setTextFill(Color.GRAY);

        RadioButton twoMinuteRb = new RadioButton("2-Minute Loan");
        RadioButton fourMinuteRb = new RadioButton("4-Minute Loan");
        RadioButton sixMinuteRb = new RadioButton("6-Minute Loan");

        ToggleGroup durationToggleGroup = new ToggleGroup();
        twoMinuteRb.setToggleGroup(durationToggleGroup);
        fourMinuteRb.setToggleGroup(durationToggleGroup);
        sixMinuteRb.setToggleGroup(durationToggleGroup);

        twoMinuteRb.setSelected(true);

        // Buttons
        Button proceedButton = createStyledButton("Proceed");
        Button backButton = createStyledButton("<- Back");

        // Disable proceed button if fields are empty
        proceedButton.setDisable(true);
        nameField.textProperty().addListener((observable, oldValue, newValue) ->
                proceedButton.setDisable(newValue.trim().isEmpty() || amountField.getText().trim().isEmpty())
        );
        amountField.textProperty().addListener((observable, oldValue, newValue) ->
                proceedButton.setDisable(newValue.trim().isEmpty() || nameField.getText().trim().isEmpty())
        );

        // Define button actions
        backButton.setOnAction(event -> {
            FirstLoanReqPage firstLoanReqPage = new FirstLoanReqPage(stage);
            firstLoanReqPage.start(user);
        });

        proceedButton.setOnAction(event -> {

            String amountStr = amountField.getText();
            double amount;
            if (checkAmount(amountStr)) {
                amount = Double.parseDouble(amountStr);
                RadioButton selectedRadioButton = (RadioButton) durationToggleGroup.getSelectedToggle();
                String selectedText = selectedRadioButton.getText();

                Alert alert = Helper.showConfirmAlert("Confirmation", "Are you sure you want to proceed?");
                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        String title = nameField.getText();
                        int duration = switch (selectedText) {
                            case "2-Minute Loan" -> 2;
                            case "4-Minute Loan" -> 4;
                            case "6-Minute Loan" -> 6;
                            default -> 0;
                        };
                        Loan requestLoan = new Loan(title, amount, duration);
                        user.addLoanReq(requestLoan);
                        Helper.showSuccessAlert("Successful", "Loan Request was submitted successfully\n");

                        Thread thread = new Thread(() -> {
                            try {
                                Thread.sleep(30_000);
                                requestLoan.setLoanStatus(LoanReqStatus.APPROVED);
                                outerLoop:
                                for (Loan loan : user.getCpyOfLoans()) {
                                    for (Installment installment : loan.getCpyOfInstallments()) {
                                        if (installment.getInstallmentStatus().equals(InstallmentStatus.OVERDUE)) {
                                            requestLoan.setLoanStatus(LoanReqStatus.REJECTED);
                                            break outerLoop;
                                        }
                                    }
                                }

                                if (requestLoan.getLoanStatus().equals(LoanReqStatus.APPROVED)) {
                                    // Create the loan and its things
                                    Loan loan = new Loan(requestLoan.getTitle(), requestLoan.getAmount(), Instant.now(), requestLoan.getDuration());
                                    user.addLoan(loan);
                                    user.getAccount().setBalance(user.getAccount().getBalance() + requestLoan.getAmount());
                                    Transaction transaction = new Transaction(loan.getAmount(), TransactionType.Receive, Instant.now());
                                    user.getAccount().addTransaction(transaction);

                                    Thread installmentThread = new Thread(() -> {
                                        for (int i = 0; i < loan.getDuration(); i++) {
                                            try {
                                                Thread.sleep(60_000);
                                                Installment installmentOfThisIndex;
                                                Instant now = Instant.now();
                                                for (int j = 0; j <= i; j++) {
                                                    installmentOfThisIndex = loan.getCpyOfInstallments().get(j);
                                                    if (now.isAfter(loan.getCpyOfInstallments().get(j).getDueDate()) && !installmentOfThisIndex.getInstallmentStatus().equals(InstallmentStatus.PAID)) {
                                                        installmentOfThisIndex.setInstallmentStatus(InstallmentStatus.OVERDUE);
                                                    }
                                                }
                                                for (int j = 0; j <= i; j++) {
                                                    installmentOfThisIndex = loan.getCpyOfInstallments().get(j);
                                                    if (Instant.now().isAfter(installmentOfThisIndex.getDueDate()) && !installmentOfThisIndex.getInstallmentStatus().equals(InstallmentStatus.PAID)) {
                                                        Platform.runLater(() -> Helper.showSuccessAlert("Installment Reminder", "Your installment is due, Please pay up"));
                                                        break;
                                                    }
                                                }
                                            } catch (InterruptedException ig) {
                                                System.out.println("Interrupted");
                                            }
                                        }
                                    });
                                    installmentThread.start();
                                }

                            } catch (InterruptedException e) {
                                System.out.println("Interrupted");
                            }
                        });
                        thread.start();
                    } else {
                        start();
                    }
                });

            }
        });

        // GridPane layout
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

        gridPane.add(nameLabel, 0, 1);
        gridPane.add(nameField, 1, 1);

        gridPane.add(amountLabel, 0, 2);
        gridPane.add(amountField, 1, 2);

        gridPane.add(chooseDuration, 0, 3);
        gridPane.add(twoMinuteRb, 0, 4);
        gridPane.add(fourMinuteRb, 0, 5);
        gridPane.add(sixMinuteRb, 0, 6);

        gridPane.add(backButton, 0, 7);
        gridPane.add(proceedButton, 1, 7);

        GridPane.setHalignment(backButton, javafx.geometry.HPos.RIGHT);
        GridPane.setHalignment(proceedButton, javafx.geometry.HPos.CENTER);

        Scene scene = new Scene(gridPane, 600, 450);
        stage.setScene(scene);
        stage.show();
    }

    private boolean checkAmount(String amount) {
        double amountInDouble;
        try {
            amountInDouble = Double.parseDouble(amount);
            if (amountInDouble <= 0) {
                Helper.showErrorAlert("Loan Request Failed", "Amount should be above $0");
                return false;
            }
            return true;
        } catch (NumberFormatException ignored) {
            Helper.showErrorAlert("Loan Request Failed", "Enter a valid number as the amount");
            return false;
        }
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
                "-fx-background-color: #00ACC1; " +
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
