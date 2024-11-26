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
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoanAndInstallmentsManagement {

    private final Stage stage;
    private final User user;
    private final Loan loan;

    public LoanAndInstallmentsManagement(Stage stage, User user, Loan loan) {
        this.stage = stage;
        this.user = user;
        this.loan = loan;
    }

    public void start() {

        // Title Label
        Label titleLabel = new Label("Manage Installments for " + loan.toString());
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.DARKSLATEGRAY);  // Accent color for the title

        // Loan Information
        Label loanFullInfo = new Label(loanFullInfo());
        loanFullInfo.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        loanFullInfo.setTextFill(Color.DIMGRAY);  // Subtle color for the info label

        // RadioButtons for Installments
        List<RadioButton> radioButtons = new ArrayList<>();
        ToggleGroup installmentToggleGroup = new ToggleGroup();

        for (Installment installment : loan.getCpyOfInstallments()) {
            RadioButton radioButton = new RadioButton(installment.toString());
            radioButton.setToggleGroup(installmentToggleGroup);
            radioButtons.add(radioButton);
        }
        if (!radioButtons.isEmpty()) {
            radioButtons.get(0).setSelected(true);
        }

        // Pay Button
        Button payButton = createStyledButton("Pay Selected Installment");
        payButton.setPrefWidth(200);

        // Back Button
        Button backButton = createStyledButton("<- Back");
        backButton.setPrefWidth(150);

        // Define button actions
        payButton.setOnAction(event -> {
            RadioButton radioButtonChoice = (RadioButton) installmentToggleGroup.getSelectedToggle();
            Installment selectedInstallment = loan.getCpyOfInstallments().get(returnInstallmentIndex(radioButtonChoice.getText()) - 1);

            if (Instant.now().isBefore(selectedInstallment.getStartDate())) {
                Helper.showErrorAlert("Task Failed", "Payment time period of this installment hasn't started yet");
            } else if (selectedInstallment.getInstallmentStatus().equals(InstallmentStatus.PAID)) {
                Helper.showErrorAlert("Task Failed", "You have already paid this installment");
            } else if (!checkBalance(selectedInstallment.getAmount())) {
                Helper.showErrorAlert("Task Failed", "Insufficient Balance");
            } else {
                Alert alert = Helper.showConfirmAlert("Confirmation", "Are you sure to proceed?");
                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        user.getAccount().setBalance(user.getAccount().getBalance() - selectedInstallment.getAmount());
                        Transaction transaction = new Transaction(selectedInstallment.getAmount(), TransactionType.Installment, Instant.now());
                        user.getAccount().addTransaction(transaction);
                        selectedInstallment.setInstallmentStatus(InstallmentStatus.PAID);
                        start();
                        Helper.showSuccessAlert("Successful", "Installment No. " + selectedInstallment.getIdNumber() + " has been paid successfully");
                    }
                });
            }
        });

        backButton.setOnAction(event -> {
            LoanAndPayPage loanAndPayPage = new LoanAndPayPage(stage, user);
            loanAndPayPage.start();
        });

        // VBox Layout for scrolling content
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20));
        vbox.setAlignment(Pos.CENTER);
        vbox.setStyle("-fx-background-color: #E0F7FA;");  // Background color for the page

        // Add elements to VBox
        vbox.getChildren().addAll(titleLabel, loanFullInfo);
        vbox.getChildren().addAll(radioButtons);
        vbox.getChildren().addAll(backButton, payButton);

        // Add VBox to ScrollPane
        ScrollPane scrollPane = new ScrollPane(vbox);
        scrollPane.setFitToWidth(true);

        Scene scene = new Scene(scrollPane, 600, 450);
        stage.setScene(scene);
        stage.show();
    }

    private String loanFullInfo() {
        return "Amount: " + loan.getAmount()
                + "\nStart Date: " + Helper.instantToString(loan.getStartDate())
                + "\nDue Date: " + Helper.instantToString(loan.getDueDate())
                + "\nNumber Of Installments: " + loan.getDuration()
                + "\nEach Installment Amount: " + loan.getCpyOfInstallments().get(0).getAmount();
    }

    private boolean checkBalance(double amount) {
        return user.getAccount().getBalance() >= amount;
    }

    private static int returnInstallmentIndex(String selectedText) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(selectedText);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group());
        }
        return 0;
    }

    private Button createStyledButton(String text) {
        Button button = new Button(text);
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
