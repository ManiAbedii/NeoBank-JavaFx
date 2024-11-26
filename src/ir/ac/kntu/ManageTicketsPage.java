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

public class ManageTicketsPage {

    private final Stage stage;
    private final User user;

    public ManageTicketsPage(Stage stage, User user) {
        this.stage = stage;
        this.user = user;
    }

    public void start() {

        // Title Label
        Label titleLabel = new Label("Manage Tickets");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.DARKSLATEGRAY);  // Accent color for the title

        // Ticket List
        Label listLabel = new Label("Tickets:");
        listLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        listLabel.setTextFill(Color.DARKSLATEGRAY);  // Accent color for the label

        ListView<Ticket> listView = new ListView<>();
        listView.getItems().addAll(user.getCpyOfTickets());
        listView.setStyle(
                "-fx-background-color: #F5F5F5; " +
                        "-fx-border-color: #B0C4DE; " +
                        "-fx-border-width: 2; " +
                        "-fx-border-radius: 5;"
        );
        listView.setPrefHeight(300);

        // Buttons
        Button choiceButton = createStyledButton("Ticket Info");
        Button backButton = createStyledButton("<- Back");

        // Define button actions
        backButton.setOnAction(actionEvent -> {
            SuppReqsPage suppReqsPage = new SuppReqsPage(stage, user);
            suppReqsPage.start();
        });

        choiceButton.setOnAction(actionEvent -> {
            Ticket selectedItem = listView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                showTicketInfo(selectedItem);
            } else {
                Helper.showErrorAlert("Ticket Info Failed", "No ticket was selected");
            }
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
        gridPane.add(listView, 0, 2, 2, 1);

        gridPane.add(choiceButton, 0, 3);
        gridPane.add(backButton, 1, 3);

        GridPane.setHalignment(choiceButton, javafx.geometry.HPos.CENTER);
        GridPane.setHalignment(backButton, javafx.geometry.HPos.CENTER);

        Scene scene = new Scene(gridPane, 600, 450);
        stage.setScene(scene);
        stage.show();
    }

    private void showTicketInfo(Ticket ticket) {
        Helper.showSuccessAlert("Ticket Info", ticket.showFullInfo());
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
