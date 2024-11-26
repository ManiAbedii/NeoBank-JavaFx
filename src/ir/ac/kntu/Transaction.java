package ir.ac.kntu;

import java.time.Instant;

enum TransactionType {
    Transfer,
    Deposit,
    Receive,
    Installment
}

public class Transaction {

    private double amount;
    private TransactionType transactionType;
    private String date;
    private String accNum;

    public Transaction(double amount, TransactionType transactionType, Instant date) {
        this.amount = amount;
        this.transactionType = transactionType;
        this.date = Helper.instantToString(date);
    }

    public Transaction(double amount, TransactionType transactionType, Instant date, String accNum) {
        this.amount = amount;
        this.transactionType = transactionType;
        this.date = Helper.instantToString(date);
        this.accNum = accNum;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = Helper.instantToString(date);
    }

    public String getAccNum() {
        return accNum;
    }

    public void setAccNum(String accNum) {
        this.accNum = accNum;
    }

    @Override
    public String toString() {
        return date + " -> " + transactionType.toString() + "\nAmount: " + amount + "$";
    }
}
