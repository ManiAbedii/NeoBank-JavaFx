package ir.ac.kntu;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Account {

    private final String accNum;
    private double balance;
    private List<Transaction> recentTransactions = new ArrayList<>();

    public Account(String phoneNum) {
        this.accNum = "6037" + phoneNum;
        this.balance = 0.0;
    }

    public String getAccNum() {
        return accNum;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void addTransaction(Transaction transaction) {
        recentTransactions.add(transaction);
    }

    public List<Transaction> getCpyTransactions() {
        return new ArrayList<>(recentTransactions);
    }
}
