package learn.dontwreckmyhouse.models;

import lombok.Data;

@Data
public class Guest {
    private int guestID;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private State state;
    private boolean active=true;

    public Guest() {

    }

    public Guest(int guestID, String firstName, String lastName, String email, String phoneNumber, State state) {
        this.guestID = guestID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.state = state;
    }
}
