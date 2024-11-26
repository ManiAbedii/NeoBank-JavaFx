package ir.ac.kntu;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class LoanAndPayPage {

    private final Stage stage;
    private final User user;

    public LoanAndPayPage(Stage stage, User user) {
        this.stage = stage;
        this.user = user;
    }

    public void start() {

        // Title Label
        Label titleLabel = new Label("View Loans");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.DARKSLATEGRAY);  // Accent color for the title

        // Sub-Label for Loans List
        Label listLabel = new Label("Loans:");
        listLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 18));
        listLabel.setTextFill(Color.DIMGRAY);  // Subtle color for the list label

        // ListView for Displaying Loans
        ListView<Loan> listView = new ListView<>();
        listView.getItems().addAll(user.getCpyOfLoans());
        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        listView.setPrefHeight(250);  // Adjust height to fit better in the layout
        listView.setStyle(
                "-fx-background-color: #F5F5F5; " +
                        "-fx-border-color: #B0C4DE; " +
                        "-fx-border-width: 2; " +
                        "-fx-border-radius: 5;"
        );

        // Buttons
        Button loanInfoButton = createStyledButton("Loan Payments");
        Button backButton = createStyledButton("<- Back");

        // Define button actions
        loanInfoButton.setOnAction(event -> {
            Loan selectedItem = listView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                LoanAndInstallmentsManagement loanAndInstallmentsManagement = new LoanAndInstallmentsManagement(stage, user, selectedItem);
                loanAndInstallmentsManagement.start();
            } else {
                Helper.showErrorAlert("Loan Info Failed", "No item was selected");
            }
        });

        backButton.setOnAction(event -> {
            DashboardPage dashboardPage = new DashboardPage(stage);
            dashboardPage.start(user);
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

        gridPane.add(listLabel, 0, 1);
        GridPane.setHalignment(listLabel, javafx.geometry.HPos.LEFT);

        gridPane.add(listView, 0, 2, 2, 1);

        gridPane.add(backButton, 0, 3);
        gridPane.add(loanInfoButton, 1, 3);

        GridPane.setHalignment(backButton, javafx.geometry.HPos.RIGHT);
        GridPane.setHalignment(loanInfoButton, javafx.geometry.HPos.LEFT);

        Scene scene = new Scene(gridPane, 600, 450);
        stage.setScene(scene);
        stage.show();
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
