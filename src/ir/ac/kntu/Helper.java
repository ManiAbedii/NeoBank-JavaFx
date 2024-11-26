package ir.ac.kntu;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.time.Instant;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Helper {

    public static void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        styleAlert(alert);
        alert.showAndWait();
    }

    public static void showSuccessAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        styleAlert(alert);
        alert.showAndWait();
    }

    public static Alert showConfirmAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        styleAlert(alert);
        return alert;
    }

    private static void styleAlert(Alert alert) {
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.initStyle(StageStyle.UNDECORATED);

        alert.getDialogPane().setStyle(
                "-fx-background-color: white; " +
                        "-fx-border-color: #00ACC1; " +
                        "-fx-border-width: 2; " +
                        "-fx-border-radius: 10; " +
                        "-fx-background-radius: 10;"
        );

        alert.getDialogPane().lookup(".content.label").setStyle("-fx-text-fill: #333333; -fx-font-size: 14px;");

        ButtonBar buttonBar = (ButtonBar) alert.getDialogPane().lookup(".button-bar");
        buttonBar.getButtons().forEach(button -> {
            button.setStyle(
                    "-fx-background-color: #00ACC1; " +
                            "-fx-text-fill: white; " +
                            "-fx-font-size: 14px; " +
                            "-fx-background-radius: 5;"
            );
            button.setOnMouseEntered(e -> button.setStyle(
                    "-fx-background-color: #00838F; " +
                            "-fx-text-fill: white; " +
                            "-fx-font-size: 14px; " +
                            "-fx-background-radius: 5;"
            ));
            button.setOnMouseExited(e -> button.setStyle(
                    "-fx-background-color: #00ACC1; " +
                            "-fx-text-fill: white; " +
                            "-fx-font-size: 14px; " +
                            "-fx-background-radius: 5;"
            ));
        });
    }

    public static boolean passwordIsWeak(String password) {
        Pattern spaces = Pattern.compile("\s");
        Matcher spaceMatcher = spaces.matcher(password);
        if (spaceMatcher.find()) {
            return true;
        }
        Pattern upperCase, lowerCase, numbers, specialCharacters;
        Matcher upperCaseMatch, lowerCaseMatch, numbersMatch, specCharMatch;

        upperCase = Pattern.compile("[A-Z]+");
        lowerCase = Pattern.compile("[a-z]+");
        numbers = Pattern.compile("[0-9]+");
        specialCharacters = Pattern.compile("[@#$%&_]+");

        upperCaseMatch = upperCase.matcher(password);
        if (!upperCaseMatch.find()) {
            return true;
        }
        lowerCaseMatch = lowerCase.matcher(password);
        if (!lowerCaseMatch.find()) {
            return true;
        }
        numbersMatch = numbers.matcher(password);
        if (!numbersMatch.find()) {
            return true;
        }
        specCharMatch = specialCharacters.matcher(password);
        return !specCharMatch.find();
    }

    public static boolean isInPhoneNumCorrectFormat(String phoneNum) {
        return phoneNum.matches("09[0-9]{9}");
    }

    public static String instantToString(Instant date) {
        return date.toString().substring(0, 10) + " /// " + date.toString().substring(11, 19);
    }

    public static User findUserByAccNum(String accNum) {
        for (User user : DataBase.getCpyUsersOfTheBank()) {
            if (user.getAccount().getAccNum().equals(accNum)) {
                return user;
            }
        }
        return null;
    }

    public static int calculateRemainder(double amount) {
        int intAmount = (int) amount;
        String strAmount = Integer.toString(intAmount);
        int lenght = strAmount.length();
        int biArzeshLen = (int) (0.75 * lenght);
        String biArzesh = strAmount.substring(lenght - biArzeshLen);
        intAmount = Integer.parseInt(biArzesh);
        if (intAmount == 0) {
            return 0;
        }

        int powOf10 = biArzeshLen;
        return (int) (Math.pow(10, powOf10)) - intAmount;
    }
}
