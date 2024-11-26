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

public class SuppReqsPage {

    private final Stage stage;
    private final User user;

    public SuppReqsPage(Stage stage, User user) {
        this.stage = stage;
        this.user = user;
    }

    public void start() {

        // Title Label
        Label titleLabel = new Label("Support Tickets");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.DARKSLATEGRAY);  // Accent color for the title

        // Create New Ticket Button
        Button createButton = createStyledButton("Submit New Ticket");

        // Manage Tickets Button
        Button manageButton = createStyledButton("Manage Tickets");

        // Back Button
        Button backButton = createStyledButton("<- Back");

        // Define button actions
        backButton.setOnAction(event -> {
            DashboardPage dashboardPage = new DashboardPage(stage);
            dashboardPage.start(user);
        });

        manageButton.setOnAction(event -> {
            if (user.getCpyOfTickets().isEmpty()) {
                Helper.showErrorAlert("No Tickets Available", "Your tickets list is empty.");
            } else {
                ManageTicketsPage manageTicketsPage = new ManageTicketsPage(stage, user);
                manageTicketsPage.start();
            }
        });

        createButton.setOnAction(event -> {
            AddTicketPage addTicketPage = new AddTicketPage(stage, user);
            addTicketPage.start();
        });

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

        gridPane.add(createButton, 0, 1, 2, 1);
        gridPane.add(manageButton, 0, 2, 2, 1);
        gridPane.add(backButton, 0, 3, 2, 1);

        GridPane.setHalignment(createButton, javafx.geometry.HPos.CENTER);
        GridPane.setHalignment(manageButton, javafx.geometry.HPos.CENTER);
        GridPane.setHalignment(backButton, javafx.geometry.HPos.CENTER);

        Scene scene = new Scene(gridPane, 600, 450);
        stage.setScene(scene);
        stage.show();
    }

    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        button.setStyle(
                "-fx-background-color: #00838F; " +
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 5;"
        );
        button.setPrefWidth(400);  // Setting a consistent width for the buttons
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
