package ir.ac.kntu;

import java.util.ArrayList;
import java.util.List;

public class InvestmentFund {

    private String title;
    private double balance;
    private String type;
    private List<Transaction> transactions = new ArrayList<>();

    public InvestmentFund(String title, String type) {
        this.title = title;
        this.balance = 0.0;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public void removeTransaction(Transaction transaction) {
        transactions.remove(transaction);
    }

    public List<Transaction> returnCpyOfTransactions() {
        return new ArrayList<>(transactions);
    }

    @Override
    public String toString() {
        return "Fund Title: " + getTitle() +
                "\nFund Type: " + getType() +
                "\nFund Balance: " + getBalance();
    }
}
