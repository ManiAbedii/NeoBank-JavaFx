package ir.ac.kntu;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class ManageFundPage {

    private final Stage stage;
    private final User user;

    public ManageFundPage(Stage stage, User user) {
        this.stage = stage;
        this.user = user;
    }

    public void start() {

        // Title Label
        Label titleLabel = new Label("Manage Funds");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.DARKSLATEGRAY);  // Accent color for the title

        // Buttons
        Button proceedButton = createStyledButton("Fund Info");
        Button backButton = createStyledButton("<- Back");

        // TableView
        TableView<InvestmentFund> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<InvestmentFund, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleColumn.setMinWidth(150);  // Minimum width for a consistent look

        TableColumn<InvestmentFund, Double> balanceColumn = new TableColumn<>("Balance");
        balanceColumn.setCellValueFactory(new PropertyValueFactory<>("balance"));
        balanceColumn.setMinWidth(150);

        TableColumn<InvestmentFund, String> typeColumn = new TableColumn<>("Type");
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        typeColumn.setMinWidth(150);

        table.getColumns().add(titleColumn);
        table.getColumns().add(balanceColumn);
        table.getColumns().add(typeColumn);

        ObservableList<InvestmentFund> data = FXCollections.observableArrayList(user.getCpyOfFunds());

        table.setItems(data);

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

        gridPane.add(table, 0, 1, 3, 1);

        gridPane.add(backButton, 0, 2);
        gridPane.add(proceedButton, 2, 2);

        GridPane.setHalignment(backButton, javafx.geometry.HPos.RIGHT);
        GridPane.setHalignment(proceedButton, javafx.geometry.HPos.LEFT);

        // Add functionality for buttons
        backButton.setOnAction(event -> {
            FundsFirstPage firstPage = new FundsFirstPage(stage, user);
            firstPage.start();
        });

        table.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            proceedButton.setOnAction(event -> {
                if (newValue != null) {
                    FundInfoPage fundInfoPage = new FundInfoPage(stage, user, newValue);
                    fundInfoPage.start();
                } else {
                    Helper.showErrorAlert("Task Failed", "No fund is selected");
                }
            });
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
