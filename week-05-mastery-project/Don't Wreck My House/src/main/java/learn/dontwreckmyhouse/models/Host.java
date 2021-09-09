package learn.dontwreckmyhouse.models;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Host {
    private String hostID;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String address;
    private String city;
    private State state;
    private int postalCode;
    private BigDecimal standardRate;
    private BigDecimal weekendRate;

    public Host(){

    }

    public Host(String hostID, String lastName, String email, String phoneNumber, String address, String city,
                State state, int postalCode, BigDecimal standardRate, BigDecimal weekendRate) {
        this.hostID = hostID;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.standardRate = standardRate;
        this.weekendRate = weekendRate;
    }
}
