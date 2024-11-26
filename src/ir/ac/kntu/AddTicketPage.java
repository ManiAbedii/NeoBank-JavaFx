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

public class AddTicketPage {

    private final Stage stage;
    private final User user;

    public AddTicketPage(Stage stage, User user) {
        this.stage = stage;
        this.user = user;
    }

    public void start() {

        // Title Label
        Label titleLabel = new Label("Submit New Ticket");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.DARKSLATEGRAY);  // Accent color for the title

        // Ticket Title
        Label ticketTitle = new Label("Ticket Subject:");
        TextField titleField = new TextField();
        titleField.setPromptText("Enter the subject of your ticket");
        titleField.setStyle(
                "-fx-background-color: #F5F5F5; " +
                        "-fx-border-color: #B0C4DE; " +
                        "-fx-border-width: 2; " +
                        "-fx-border-radius: 5;"
        );

        // Explanation
        Label explanation = new Label("Additional Explanation:");
        TextArea explanationField = new TextArea();
        explanationField.setPromptText("Enter additional details about your ticket");
        explanationField.setWrapText(true);
        explanationField.setStyle(
                "-fx-background-color: #F5F5F5; " +
                        "-fx-border-color: #B0C4DE; " +
                        "-fx-border-width: 2; " +
                        "-fx-border-radius: 5;"
        );

        // Ticket Sections
        Label chooseSection = new Label("Select the Section:");
        chooseSection.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        RadioButton transactionsRb = new RadioButton("View Transactions");
        RadioButton contactsRb = new RadioButton("Contacts");
        RadioButton transferRb = new RadioButton("Transfer");
        RadioButton fundsRb = new RadioButton("Investment Funds");
        RadioButton ticketsRb = new RadioButton("Tickets");

        ToggleGroup sectionGroup = new ToggleGroup();
        transactionsRb.setToggleGroup(sectionGroup);
        contactsRb.setToggleGroup(sectionGroup);
        transferRb.setToggleGroup(sectionGroup);
        fundsRb.setToggleGroup(sectionGroup);
        ticketsRb.setToggleGroup(sectionGroup);
        transactionsRb.setSelected(true);

        // Buttons
        Button addButton = createStyledButton("Submit Ticket");
        Button backButton = createStyledButton("<- Back");

        // Disable submit button if fields are empty
        addButton.setDisable(true);
        titleField.textProperty().addListener((observable, oldValue, newValue) ->
                addButton.setDisable(newValue.trim().isEmpty() || explanationField.getText().trim().isEmpty())
        );
        explanationField.textProperty().addListener((observable, oldValue, newValue) ->
                addButton.setDisable(newValue.trim().isEmpty() || titleField.getText().trim().isEmpty())
        );

        // Define button actions
        backButton.setOnAction(event -> {
            SuppReqsPage suppReqsPage = new SuppReqsPage(stage, user);
            suppReqsPage.start();
        });

        addButton.setOnAction(event -> {
            RadioButton selectedOption = (RadioButton) sectionGroup.getSelectedToggle();
            TicketSection section = returnSection(selectedOption);
            Alert alert = Helper.showConfirmAlert("Confirmation", "Are you sure about submitting this ticket?");

            String title = titleField.getText();
            String additionalExp = explanationField.getText();

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    Ticket ticket = new Ticket(title, additionalExp, section);
                    user.addTicket(ticket);
                    Thread thread = new Thread(() -> {
                        try {
                            Thread.sleep(60_000);
                            ticket.setResponse("We will contact you soon");
                        } catch (InterruptedException e) {
                            System.out.println("Interrupted\n");
                        }
                    });
                    thread.start();

                    Helper.showSuccessAlert("Successful", "Ticket Submitted Successfully");
                    SuppReqsPage suppReqsPage = new SuppReqsPage(stage, user);
                    suppReqsPage.start();
                }
            });
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

        gridPane.add(ticketTitle, 0, 1);
        gridPane.add(titleField, 1, 1);

        gridPane.add(explanation, 0, 2);
        gridPane.add(explanationField, 1, 2);

        gridPane.add(chooseSection, 0, 3);
        gridPane.add(transactionsRb, 0, 4);
        gridPane.add(contactsRb, 0, 5);
        gridPane.add(transferRb, 0, 6);
        gridPane.add(fundsRb, 0, 7);
        gridPane.add(ticketsRb, 0, 8);

        gridPane.add(backButton, 0, 9);
        gridPane.add(addButton, 1, 9);

        GridPane.setHalignment(backButton, javafx.geometry.HPos.RIGHT);
        GridPane.setHalignment(addButton, javafx.geometry.HPos.LEFT);

        Scene scene = new Scene(gridPane, 600, 500);
        stage.setScene(scene);
        stage.show();
    }

    private TicketSection returnSection(RadioButton selected) {
        return switch (selected.getText()) {
            case "View Transactions" -> TicketSection.TRANSACTIONS;
            case "Contacts" -> TicketSection.CONTACTS;
            case "Transfer" -> TicketSection.TRANSFER;
            case "Investment Funds" -> TicketSection.FUNDS;
            case "Tickets" -> TicketSection.TICKETS;
            default -> TicketSection.TICKETS;
        };
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
