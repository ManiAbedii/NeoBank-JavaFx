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

public class CreateFundPage {

    private final Stage stage;
    private final User user;

    public CreateFundPage(Stage stage, User user) {
        this.stage = stage;
        this.user = user;
    }

    public void start() {

        // Title Label
        Label titleLabel = new Label("Open New Fund");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.DARKSLATEGRAY);  // Accent color for the title

        // Fund Name
        Label nameLabel = new Label("Enter Fund's Name:");
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        nameLabel.setTextFill(Color.DARKSLATEGRAY);  // Consistent label color
        TextField nameField = new TextField();
        nameField.setPromptText("Enter fund name");
        nameField.setStyle(
                "-fx-background-color: #F5F5F5; " +
                        "-fx-border-color: #B0C4DE; " +
                        "-fx-border-width: 2; " +
                        "-fx-border-radius: 5;"
        );

        // Choose Fund Type
        Label chooseFund = new Label("Choose Your Investment Type:");
        chooseFund.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        chooseFund.setTextFill(Color.DARKSLATEGRAY);  // Consistent label color

        RadioButton savingRb = new RadioButton("Saving Fund");
        RadioButton remainderRb = new RadioButton("Remainder Fund");
        RadioButton bonusRb = new RadioButton("Bonus Fund");

        ToggleGroup fundToggleGroup = new ToggleGroup();
        savingRb.setToggleGroup(fundToggleGroup);
        remainderRb.setToggleGroup(fundToggleGroup);
        bonusRb.setToggleGroup(fundToggleGroup);

        savingRb.setSelected(true);

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

        gridPane.add(nameLabel, 0, 1);
        gridPane.add(nameField, 1, 1);

        gridPane.add(chooseFund, 0, 2, 2, 1);
        gridPane.add(savingRb, 0, 3);
        gridPane.add(remainderRb, 0, 4);
        gridPane.add(bonusRb, 0, 5);

        gridPane.add(backButton, 0, 6);
        gridPane.add(proceedButton, 1, 6);

        GridPane.setHalignment(backButton, javafx.geometry.HPos.RIGHT);
        GridPane.setHalignment(proceedButton, javafx.geometry.HPos.LEFT);

        Scene scene = new Scene(gridPane, 600, 450);
        stage.setScene(scene);
        stage.show();

        // Button Actions
        backButton.setOnAction(event -> {
            FundsFirstPage firstPage = new FundsFirstPage(stage, user);
            firstPage.start();
        });

        proceedButton.setOnAction(event -> {
            String name = nameField.getText();
            RadioButton selectedRadioButton = (RadioButton) fundToggleGroup.getSelectedToggle();
            String selectedText = selectedRadioButton.getText();

            switch (selectedText) {
                case "Saving Fund": {
                    SavingFund savingFund = new SavingFund(name);
                    user.addFund(savingFund);
                    Helper.showSuccessAlert("Successful Operation", "Fund Opened Successfully");
                    FundsFirstPage firstPage = new FundsFirstPage(stage, user);
                    firstPage.start();
                    break;
                }
                case "Remainder Fund": {
                    if (checkIfRemainderAlready()) {
                        RemainderFund fund = new RemainderFund(name);
                        user.addFund(fund);
                        Helper.showSuccessAlert("Successful Operation", "Fund Opened Successfully");
                        FundsFirstPage firstPage = new FundsFirstPage(stage, user);
                        firstPage.start();
                    } else {
                        Helper.showErrorAlert("Operation Failed", "Cannot open more than one remainder fund");
                        start();
                    }
                    break;
                }
                case "Bonus Fund": {
                    OpenBonusFund bonusFund = new OpenBonusFund(stage, user);
                    bonusFund.start(name);
                    break;
                }
            }
        });
    }

    private boolean checkIfRemainderAlready() {
        for (InvestmentFund investmentFund : user.getCpyOfFunds()) {
            if (investmentFund instanceof RemainderFund) {
                return false;
            }
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
