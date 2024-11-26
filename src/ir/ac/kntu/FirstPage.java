package ir.ac.kntu;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class FirstPage extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // Labels
        Label bigTitle = new Label("Welcome To FeriBank!");
        bigTitle.setFont(new Font("Arial", 35));
        bigTitle.setTextFill(Color.DARKBLUE);  // Dark Blue Color
        bigTitle.setLineSpacing(3);

        Label label = new Label("Press Start to continue");
        label.setFont(new Font("Arial", 16));
        label.setTextFill(Color.SILVER);  // Silver Color

        // Button
        Button button = new Button("Start");
        button.setFont(new Font("Arial", 16));
        button.setDefaultButton(true);
        button.setStyle("-fx-background-color: #00BFFF; -fx-text-fill: white;");  // Cyan Color

        button.setOnAction(actionEvent -> {
            LoginPage loginPage = new LoginPage(stage);
            loginPage.start(); // Ensure LoginPage.start(Stage) is correctly defined
        });

        // VBox layout
        VBox vbox = new VBox(20); // 20px spacing between elements
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(bigTitle, label, button);

        // StackPane layout
        StackPane stackPane = new StackPane(vbox);
        stackPane.setStyle("-fx-background-color: #F0F8FF;");  // Light Cyan Background Color
        Scene scene = new Scene(stackPane, 600, 450);

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        DataBase.addUserToTheBank(new User("Mani", "Abedii", "0521643082", "09906868721", "3962"));
        launch(args);
    }
}