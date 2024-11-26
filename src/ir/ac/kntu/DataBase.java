package ir.ac.kntu;

import java.util.ArrayList;
import java.util.List;

public class DataBase {

    private static final int interestRate = 5;

    public static int getInterestRate() {
        return interestRate;
    }

    // Users Data Base
    private static List<User> usersOfTheBank = new ArrayList<>();

    public static void addUserToTheBank(User user) {
        usersOfTheBank.add(user);
    }

    public static void removeUserOfTheBank(User user) {
        usersOfTheBank.remove(user);
    }

    public static User getUser(String phoneNum) {
        for (User user : usersOfTheBank) {
            if (user.getPhoneNum().equals(phoneNum)) {
                return user;
            }
        }
        return null;
    }

    public static List<User> getCpyUsersOfTheBank() {
        return new ArrayList<>(usersOfTheBank);
    }
}
