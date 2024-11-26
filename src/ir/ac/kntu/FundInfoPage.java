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

public class FundInfoPage {

    private final Stage stage;
    private final User user;
    private final InvestmentFund investmentFund;

    public FundInfoPage(Stage stage, User user, InvestmentFund investmentFund) {
        this.stage = stage;
        this.user = user;
        this.investmentFund = investmentFund;
    }

    public void start() {

        // Title Label
        Label titleLabel = new Label("Fund Info");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.DARKSLATEGRAY);  // Accent color for the title

        // Fund Information
        Label fundInfoLabel = new Label(investmentFund.toString());
        fundInfoLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        fundInfoLabel.setTextFill(Color.DARKSLATEBLUE);  // Darker color for the fund information

        // Transactions List
        Label transactionsLabel = new Label("Transactions:");
        transactionsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        transactionsLabel.setTextFill(Color.DARKSLATEGRAY);  // Accent color for the label

        ListView<Transaction> listView = new ListView<>();
        listView.getItems().addAll(investmentFund.returnCpyOfTransactions());

        // Buttons
        Button backButton = createStyledButton("<- Back");
        Button proceedButton = createStyledButton("Make a Transfer");

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

        gridPane.add(fundInfoLabel, 0, 1, 2, 1);
        GridPane.setHalignment(fundInfoLabel, javafx.geometry.HPos.CENTER);

        gridPane.add(transactionsLabel, 0, 2, 2, 1);
        GridPane.setHalignment(transactionsLabel, javafx.geometry.HPos.CENTER);

        gridPane.add(listView, 0, 3, 2, 1);

        gridPane.add(backButton, 0, 4);
        gridPane.add(proceedButton, 1, 4);

        GridPane.setHalignment(backButton, javafx.geometry.HPos.RIGHT);
        GridPane.setHalignment(proceedButton, javafx.geometry.HPos.LEFT);

        // Button actions
        backButton.setOnAction(event -> {
            ManageFundPage manageFundPage = new ManageFundPage(stage, user);
            manageFundPage.start();
        });

        proceedButton.setOnAction(event -> {
            if (investmentFund instanceof BonusFund) {
                Helper.showErrorAlert("Task Failed", "Transactions cannot be made for the bonus fund\n" +
                        "Balance will be automatically transferred into your main account ");
            } else {
                FundTransfersPage fundTransfersPage = new FundTransfersPage(stage, user, investmentFund);
                fundTransfersPage.start();
            }
        });

        Scene scene = new Scene(gridPane, 600, 450);
        stage.setScene(scene);
        stage.show();
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
