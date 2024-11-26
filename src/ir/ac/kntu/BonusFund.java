package ir.ac.kntu;

import java.time.Duration;
import java.time.Instant;

public class BonusFund extends InvestmentFund {

    private final double initialDeposit;
    private final int duration;
    private final String startDate;
    private final String dueDate;
    private final int interestRate;

    public BonusFund(String title, double initialDeposit, int duration, int interestRate, Instant startDate) {
        super(title, "Bonus Fund");
        this.initialDeposit = initialDeposit;
        this.duration = duration;
        this.interestRate = interestRate;
        this.startDate = Helper.instantToString(startDate);
        this.dueDate = Helper.instantToString(startDate.plus(Duration.ofMinutes(duration)));
        setBalance(initialDeposit);
    }

    public double getInitialDeposit() {
        return initialDeposit;
    }

    public int getDuration() {
        return duration;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public int getInterestRate() {
        return interestRate;
    }

    @Override
    public String toString() {
        return super.toString() +
                "\nInitial Deposit: " + getInitialDeposit() +
                "\nDuration: " + duration + " Minutes" +
                "\nStart Date: " + startDate +
                "\nDue Date: " + dueDate +
                "\nInterst Rate: " + interestRate;
    }
}