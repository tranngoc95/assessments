package learn.dontwreckmyhouse.ui;

import lombok.Getter;

public enum MainMenuOption {
    EXIT("Exit"),
    VIEW_RESERVATIONS_BY_HOST("View Reservations by Host"),
    VIEW_RESERVATIONS_BY_GUEST("View Reservations by Guest"),
    VIEW_RESERVATIONS_BY_STATE("View Reservations by State"),
    VIEW_RESERVATIONS_BY_CITY("View Reservations by City"),
    VIEW_RESERVATIONS_BY_ZIPCODE("View Reservations by Zip Code"),
    MAKE_RESERVATION("Make a Reservation"),
    UPDATE_RESERVATION("Update a Reservation"),
    CANCEL_RESERVATION("Cancel a Reservation"),
    ADD_GUEST("Add a Guest"),
    EDIT_GUEST("Edit a Guest"),
    DELETE_GUEST("Delete a Guest");

    @Getter
    private String message;

    MainMenuOption(String message){
        this.message=message;
    }
}
