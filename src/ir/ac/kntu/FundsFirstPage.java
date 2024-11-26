package ir.ac.kntu;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class FundsFirstPage {

    private final Stage stage;
    private final User user;

    public FundsFirstPage(Stage stage, User user) {
        this.stage = stage;
        this.user = user;
    }

    public void start() {

        // Title Label
        Label titleLabel = new Label("Investment Funds");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.DARKSLATEGRAY);  // Accent color for the title

        // Buttons
        Button createButton = createStyledButton("Open New Funds");
        Button manageButton = createStyledButton("Manage Funds");
        Button backButton = createStyledButton("<- Back");

        // GridPane layout
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setStyle("-fx-background-color: #E0F7FA;");  // Background color for the page

        // Add elements to GridPane
        gridPane.add(titleLabel, 0, 0, 3, 1);
        GridPane.setColumnSpan(titleLabel, 3);
        GridPane.setHalignment(titleLabel, javafx.geometry.HPos.CENTER);

        gridPane.add(createButton, 0, 1);
        gridPane.add(manageButton, 0, 2);
        gridPane.add(backButton, 0, 3);

        GridPane.setHalignment(createButton, javafx.geometry.HPos.CENTER);
        GridPane.setHalignment(manageButton, javafx.geometry.HPos.CENTER);
        GridPane.setHalignment(backButton, javafx.geometry.HPos.CENTER);

        Scene scene = new Scene(gridPane, 600, 450);
        stage.setScene(scene);
        stage.show();

        // Button Actions
        createButton.setOnAction(event -> {
            CreateFundPage createFund = new CreateFundPage(stage, user);
            createFund.start();
        });

        manageButton.setOnAction(event -> {
            if (!user.getCpyOfFunds().isEmpty()) {
                ManageFundPage manageFund = new ManageFundPage(stage, user);
                manageFund.start();
            } else {
                Helper.showErrorAlert("Task Failed", "Funds list is empty");
                start();
            }
        });

        backButton.setOnAction(event -> {
            DashboardPage dashboardPage = new DashboardPage(stage);
            dashboardPage.start(user);
        });
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
