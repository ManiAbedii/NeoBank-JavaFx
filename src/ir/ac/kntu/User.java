package ir.ac.kntu;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User {

    private String firstName;
    private String lastName;
    private String socialNum;
    private String phoneNum;
    private String password;
    private List<InvestmentFund> funds = new ArrayList<>();
    private List<Ticket> tickets = new ArrayList<>();
    private List<Loan> loans = new ArrayList<>();
    private List<Loan> loanRequests = new ArrayList<>();

    public User(String firstName, String lastName, String socialNum, String phoneNum, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.socialNum = socialNum;
        this.phoneNum = phoneNum;
        this.password = password;
        this.account = new Account(phoneNum);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSocialNum() {
        return socialNum;
    }

    public void setSocialNum(String socialNum) {
        this.socialNum = socialNum;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isPasswordCorrect(String password) {
        return this.password.equals(password);
    }

    public void addFund(InvestmentFund fund) {
        funds.add(fund);
    }

    public void removeFund(InvestmentFund fund) {
        funds.remove(fund);
    }

    public List<InvestmentFund> getCpyOfFunds() {
        return new ArrayList<>(funds);
    }

    public void addTicket(Ticket ticket) {
        tickets.add(ticket);
    }

    public void removeTicket(Ticket ticket) {
        tickets.remove(ticket);
    }

    public List<Ticket> getCpyOfTickets() {
        return new ArrayList<>(tickets);
    }

    public void addLoan(Loan loan) {
        loans.add(loan);
    }

    public void removeLoan(Loan loan) {
        loans.remove(loan);
    }

    public List<Loan> getCpyOfLoans() {
        return new ArrayList<>(loans);
    }

    public void addLoanReq(Loan loan) {
        loanRequests.add(loan);
    }

    public List<Loan> getCpyOfLoanReqs() {
        return new ArrayList<>(loanRequests);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof User user)) return false;
        return Objects.equals(phoneNum, user.phoneNum) && Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(phoneNum);
    }

    // Account:
    private Account account;

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    //Contacts:
    private List<Contact> contacts = new ArrayList<>();

    public void addContact(Contact contact) {
        contacts.add(contact);
    }

    public void removeContact(Contact contact) {
        contacts.remove(contact);
    }

    public List<Contact> getCpyOfContacts() {
        return new ArrayList<>(contacts);
    }
}
