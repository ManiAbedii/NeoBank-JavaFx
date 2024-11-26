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

public class FirstLoanReqPage {

    private final Stage stage;

    public FirstLoanReqPage(Stage stage) {
        this.stage = stage;
    }

    public void start(User user) {

        // Title Label
        Label titleLabel = new Label("Loan Requests");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.DARKSLATEGRAY);  // Accent color for the title

        // Buttons
        Button requestLoanButton = createStyledButton("Request New Loan");
        Button viewPreviousRequests = createStyledButton("View Recent Loan Requests");
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

        gridPane.add(requestLoanButton, 0, 1, 2, 1);
        GridPane.setHalignment(requestLoanButton, javafx.geometry.HPos.CENTER);

        gridPane.add(viewPreviousRequests, 0, 2, 2, 1);
        GridPane.setHalignment(viewPreviousRequests, javafx.geometry.HPos.CENTER);

        gridPane.add(backButton, 0, 3, 2, 1);
        GridPane.setHalignment(backButton, javafx.geometry.HPos.CENTER);

        Scene scene = new Scene(gridPane, 600, 450);
        stage.setScene(scene);
        stage.show();

        // Button Actions
        requestLoanButton.setOnAction(event -> {
            RequestNewLoanPage requestNewLoanPage = new RequestNewLoanPage(stage, user);
            requestNewLoanPage.start();
        });

        viewPreviousRequests.setOnAction(event -> {
            if (user.getCpyOfLoanReqs().isEmpty()) {
                Helper.showErrorAlert("No Requests Found", "Loan Requests list is empty");
            } else {
                ViewPreviousLoanRequestsPage previousLoanRequestsPage = new ViewPreviousLoanRequestsPage(stage, user);
                previousLoanRequestsPage.start();
            }
        });

        backButton.setOnAction(event -> {
            DashboardPage dashboardPage = new DashboardPage(stage);
            dashboardPage.start(user);
        });
    }

    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setPrefWidth(300);
        button.setFont(Font.font("Arial", FontWeight.BOLD, 16));
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