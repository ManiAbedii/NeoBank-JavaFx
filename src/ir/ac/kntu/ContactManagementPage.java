package ir.ac.kntu;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class ContactManagementPage {

    private final Stage stage;

    public ContactManagementPage(Stage stage) {
        this.stage = stage;
    }

    public void start(User user) {
        // Title
        Label titleLabel = new Label("Contacts Management");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        titleLabel.setTextFill(Color.DARKSLATEGRAY);

        // Buttons
        Button addContactButton = createStyledButton("Add Contact");
        Button viewContactsButton = createStyledButton("View & Edit Contacts List");
        Button backButton = createStyledButton("<- Back");

        // Button Actions
        addContactButton.setOnAction(event -> handleAddContactButton(user));
        viewContactsButton.setOnAction(event -> handleViewAndEditContactButton(user));
        backButton.setOnAction(event -> handleBackButton(user));

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setStyle("-fx-background-color: #F5F5F5; -fx-border-color: #B0C4DE; -fx-border-radius: 10; -fx-padding: 20;");

        // Layout
        gridPane.add(titleLabel, 0, 0, 3, 1);
        gridPane.add(addContactButton, 0, 1);
        gridPane.add(viewContactsButton, 1, 1);
        gridPane.add(backButton, 2, 1);

        // Centering content
        VBox vbox = new VBox(gridPane);
        vbox.setAlignment(Pos.CENTER);
        vbox.setStyle("-fx-background-color: #E0F7FA;");

        Scene scene = new Scene(vbox, 600, 450);
        stage.setScene(scene);
        stage.show();
    }

    private void handleAddContactButton(User user) {
        AddContactPage addContactPage = new AddContactPage(stage);
        addContactPage.start(user);
    }

    private void handleViewAndEditContactButton(User user) {
        ViewContacts viewContacts = new ViewContacts(stage);
        viewContacts.start(user);
    }

    private void handleBackButton(User user) {
        DashboardPage dashboardPage = new DashboardPage(stage);
        dashboardPage.start(user);
    }

    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setPrefWidth(200);
        button.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        button.setStyle("-fx-background-color: #00ACC1; -fx-text-fill: white; -fx-background-radius: 5;");
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #00838F; -fx-text-fill: white; -fx-background-radius: 5;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #00ACC1; -fx-text-fill: white; -fx-background-radius: 5;"));
        return button;
    }
}