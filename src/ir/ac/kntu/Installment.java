package ir.ac.kntu;

import java.time.Duration;
import java.time.Instant;

enum InstallmentStatus {
    PAID("Paid"),
    UNPAID("Unpaid"),
    OVERDUE("Overdue");

    private final String displayName;

    InstallmentStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

public class Installment {

    private double amount;
    private Instant startDate;
    private Instant dueDate;
    private final int idNumber;
    private InstallmentStatus installmentStatus;

    public Installment(int idNumber, double amount, Instant startDate) {
        this.idNumber = idNumber;
        this.amount = amount;
        this.startDate = startDate;
        this.dueDate = startDate.plus(Duration.ofMinutes(1));
        this.installmentStatus = InstallmentStatus.UNPAID;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getIdNumber() {
        return idNumber;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getDueDate() {
        return dueDate;
    }

    public void setDueDate(Instant dueDate) {
        this.dueDate = dueDate;
    }

    public InstallmentStatus getInstallmentStatus() {
        return installmentStatus;
    }

    public void setInstallmentStatus(InstallmentStatus installmentStatus) {
        this.installmentStatus = installmentStatus;
    }

    @Override
    public String toString() {
        return "Installment Number No." + idNumber
                + "\nStart Date: " + Helper.instantToString(startDate)
                + "\nDue Date: " + Helper.instantToString(dueDate)
                + "\nPayment Status: " + installmentStatus.getDisplayName();
    }
}
