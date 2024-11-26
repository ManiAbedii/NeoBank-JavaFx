package ir.ac.kntu;

enum TicketSection {
    TRANSACTIONS("Transactions"),
    CONTACTS("Contacts"),
    TRANSFER("Transfer"),
    FUNDS("Funds"),
    TICKETS("Tickets");

    private final String displayName;

    TicketSection(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

public class Ticket {

    private String title;
    private String description;
    private TicketSection ticketSection;
    private String response;

    public Ticket(String title, String description, TicketSection ticketSection) {
        this.title = title;
        this.description = description;
        this.ticketSection = ticketSection;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TicketSection getTicketSection() {
        return ticketSection;
    }

    public void setTicketSection(TicketSection ticketSection) {
        this.ticketSection = ticketSection;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String showFullInfo() {
        String output = "Title: " + title
                + "\nDescription: " + description
                + "\nSection: " + ticketSection.getDisplayName();
        if (response == null) {
            output += "\nNo Response";
        } else {
            output += "\nResponse: " + response;
        }
        return output;
    }

    @Override
    public String toString() {
        return title;
    }
}
