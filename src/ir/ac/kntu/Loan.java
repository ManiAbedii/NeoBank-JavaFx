package ir.ac.kntu;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

enum LoanReqStatus {
    IN_PROGRESS("In Progress"),
    APPROVED("Approved"),
    REJECTED("Rejected");

    private final String displayName;

    LoanReqStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

public class Loan {

    //If loan is approved we use these fields:
    private final String title;
    private double amount;
    private Instant startDate;
    private int duration;
    private Instant dueDate;
    private List<Installment> installments = new ArrayList<>();

    public Loan(String title, double amount, Instant startDate, int duration) {
        this.title = title;
        this.amount = amount;
        this.duration = duration;
        this.startDate = startDate;
        this.dueDate = startDate.plus(Duration.ofMinutes(duration));
        double installmentAmount = Math.round(amount/duration * 100.0) / 100.0;
        for (int i = 1; i <= duration; i++) {
            installments.add(new Installment(i, installmentAmount, startDate.plus(Duration.ofMinutes(i - 1))));
        }
    }

    //else
    private LoanReqStatus loanReqStatus;

    public Loan(String title, double amount, int duration) {
        this.title = title;
        this.amount = amount;
        this.duration = duration;
        this.loanReqStatus = LoanReqStatus.IN_PROGRESS;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Instant getDueDate() {
        return dueDate;
    }

    public void setDueDate(Instant dueDate) {
        this.dueDate = dueDate;
    }

    public LoanReqStatus getLoanStatus() {
        return loanReqStatus;
    }

    public void setLoanStatus(LoanReqStatus loanReqStatus) {
        this.loanReqStatus = loanReqStatus;
    }

    public void addInstallment(Installment installment) {
        installments.add(installment);
    }

    public void removeInstallment(Installment installment) {
        installments.remove(installment);
    }

    public List<Installment> getCpyOfInstallments() {
        return new ArrayList<>(installments);
    }

    public String getTitle() {
        return title;
    }

    public String loanReqToString() {
        return title
                + "\nAmount: " + amount
                + "\nDuration: " + duration
                + "\nRequest Status: " + loanReqStatus.getDisplayName();
    }

    @Override
    public String toString() {
        return title;
    }
}
