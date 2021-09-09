package learn.dontwreckmyhouse.ui;

import learn.dontwreckmyhouse.domain.Result;
import learn.dontwreckmyhouse.models.Guest;
import learn.dontwreckmyhouse.models.Host;
import learn.dontwreckmyhouse.models.Reservation;
import learn.dontwreckmyhouse.models.State;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class View {
    private final ConsoleIO IO = new ConsoleIO();
    private final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private final String EXIT_MESSAGE = " (enter \"exit\" to go back to main menu)";

    public MainMenuOption selectMainMenu(){
        displayHeader("Main Menu");
        MainMenuOption[] options = MainMenuOption.values();
        for(int i=0; i<options.length; i++){
            IO.printf("%s. %s%n", i, options[i].getMessage());
        }
        String prompt = String.format("Select [%s-%s]: ", 0, options.length-1);
        int input = IO.readInt(prompt,0, options.length-1, true);
        return options[input];
    }

    public int selectFindMethod(){
        IO.println("Find Methods:");
        IO.println("1. Find By Last Name");
        IO.println("2. Find By Email");
        IO.println("0. Exit");
        return IO.readInt("Select [0-2]: ",0, 2, true);
    }

    public void displayHeader(String message){
        IO.printf("%n%s%n", message);
        IO.println("=".repeat(message.length()));
    }

    public void displayReservationsByHost(List<Reservation> reservations, Host host){
        String header = String.format("%s: %s, %s", host.getLastName(), host.getCity(), host.getState());
        displayHeader(header);
        if(reservations==null || reservations.size()==0){
            IO.println("There is no reservation to display.");
            return;
        }
        String format = " %2s | %-10s | %-10s | %-15s, %-15s | %-40s%n";
        String message = String.format(format, "", "Start Date", "End Date", "Last Name", "First Name", "Email");
        IO.printf(message);
        IO.println("-".repeat(message.length()));
        for(int i=0; i<reservations.size(); i++){
            Reservation r = reservations.get(i);
            IO.printf(format, i+1,
                    r.getStartDate().format(FORMATTER), r.getEndDate().format(FORMATTER),
                    r.getGuest().getLastName(), r.getGuest().getFirstName(),
                    r.getGuest().getEmail());
        }
    }

    public void displayReservationsByGuest(List<Reservation> reservations, Guest guest){
        String header = String.format("Guest %s, %s: %s - %s", guest.getLastName(),
                guest.getFirstName(), guest.getState(), guest.getEmail());
        displayHeader(header);
        if(reservations==null || reservations.size()==0){
            IO.println("There is no existing reservation.");
            return;
        }
        String format = "%-10s | %-10s | %-14s | %-15s - %-5s | %-30s%n";
        String message = String.format(format, "Start Date", "End Date",
                "Host Last Name", "City", "State", "Host Email");
        IO.printf(message);
        IO.println("-".repeat(message.length()));
        for (Reservation r : reservations) {
            IO.printf(format, r.getStartDate().format(FORMATTER), r.getEndDate().format(FORMATTER),
                    r.getHost().getLastName(), r.getHost().getCity(),
                    r.getHost().getState(), r.getHost().getEmail());
        }
    }

    public void displayReservationsByFields(List<Reservation> reservations){
        displayHeader("Reservations");
        if(reservations==null || reservations.size()==0){
            IO.println("There is no existing reservation.");
            return;
        }
        String format = "%-10s | %-10s | %-15s | %-15s | %-30s, %-10s, %-5s, %-7s%n";
        String message = String.format(format, "Start Date", "End Date", "Guest Last Name",
                "Host Last Name","Host Address", "City", "State", "Zipcode");
        IO.printf(message);
        IO.println("-".repeat(message.length()));
        reservations.forEach(r-> IO.printf(format,
                r.getStartDate().format(FORMATTER), r.getEndDate().format(FORMATTER),
                r.getGuest().getLastName(), r.getHost().getLastName(),
                r.getHost().getAddress(), r.getHost().getCity(),
                r.getHost().getState(), r.getHost().getPostalCode()));

    }

    public void displayReservationResult(Result<Reservation> result, String action){
        if(result.isSuccess()){
            Reservation reservation = result.getPayLoad();
            String message = String.format("Reservation from %s to %s at %s, %s is %s for guest %s.",
                    reservation.getStartDate().format(FORMATTER), reservation.getEndDate().format(FORMATTER),
                    reservation.getHost().getCity(), reservation.getHost().getState(),
                    action, reservation.getGuest().getLastName());
            displayResult(message);
        } else {
            displayResult(false, result.getErrorMessages());
        }
    }

    public void displayGuestResult(Result<Guest> result, String action){
        if(result.isSuccess()){
            Guest guest = result.getPayLoad();
            String message = String.format("Guest %s %s from %s is %s.",
                    guest.getFirstName(), guest.getLastName(), guest.getState().getName(), action);
            displayResult(message);
        } else {
            displayResult(false, result.getErrorMessages());
        }
    }

    public void displayError(String msg){
        displayHeader("An error occurred:");
        IO.println(msg);
    }

    public Reservation createReservation(List<Reservation> reservations, Host host, Guest guest){
        displayReservationsByHost(reservations, host);
        Reservation result = new Reservation();
        result.setHost(host);
        result.setGuest(guest);
        boolean confirmation;
        boolean cont;
        do {
            cont=false;
            result.setStartDate(IO.readFutureDate("Enter start date [MM/dd/yyyy]: "));
            result.setEndDate((IO.readDate("Enter end date [MM/dd/yyyy]: ",
                    result.getStartDate(), true)));
            result.updateTotal();
            confirmation=confirmReservation(result);
            if(!confirmation){
                cont=continueReservation();
            }
        }while (cont);
        if (!confirmation) {
            IO.println("Cancel making a reservation.");
            return null;
        }
        return result;
    }

    public boolean continueReservation(){
        return IO.readBoolean("Do you want to update the dates to continue making the reservations? [y/n]: ");
    }

    public Guest createGuest(){
        Guest guest = new Guest();
        guest.setFirstName(IO.readString(
                String.format("Enter first name%s: ", EXIT_MESSAGE), true));
        if (guest.getFirstName().equalsIgnoreCase("exit")){
            return null;
        }
        guest.setLastName(getLastname());
        if (guest.getLastName().equalsIgnoreCase("exit")){
            return null;
        }
        guest.setEmail(getEmail());
        if(guest.getEmail().equals("exit")){
            return null;
        }
        guest.setPhoneNumber(IO.readString(
                String.format("Enter phone number%s: ", EXIT_MESSAGE), true));
        if(guest.getPhoneNumber().equals("exit")){
            return null;
        }
        guest.setState(selectState());
        return guest;
    }

    public Reservation updateReservation(List<Reservation> reservations){
        Reservation reservation = selectReservation(reservations);
        if(reservation==null){
            return null;
        }
        displayHeader("Updating Reservation");
        IO.println("Press [Enter] to keep original value.\n");
        LocalDate date = IO.readDate(
                String.format("Enter start date (%s): ", reservation.getStartDate().format(FORMATTER)), false);
        if(date!=null){
            reservation.setStartDate(date);
        }
        date = IO.readDate(
                String.format("Enter end date (%s): ", reservation.getEndDate().format(FORMATTER)),
                reservation.getStartDate(), false);
        if(date!=null){
            reservation.setEndDate(date);
        }
        reservation.updateTotal();
        if(!confirmReservation(reservation)){
            IO.println("Cancel updating the reservation.");
            return null;
        }
        return reservation;
    }

    public Guest updateGuest(Guest guest){
        String input = IO.readString(String.format("Enter first name (%s): ", guest.getFirstName()), false);
        if(!input.isBlank()) {
            guest.setFirstName(input);
        }
        input = getLastname(false, guest.getLastName());
        if(!input.isBlank()) {
            guest.setLastName(input);
        }
        input = getEmail(false, guest.getEmail());
        if(!input.isBlank()){
            guest.setEmail(input);
        }
        input = IO.readString(String.format("Enter phone number (%s): ", guest.getPhoneNumber()), false);
        if(!input.isBlank()) {
            guest.setPhoneNumber(input);
        }
        State state = selectState(false, guest.getState());
        if(state!=null) {
            guest.setState(state);
        }
        return guest;
    }

    public boolean confirmDeleteGuest(List<Reservation> reservations, Guest guest){
        if(reservations==null || reservations.isEmpty()){
            return true;
        }
        displayReservationsByGuest(reservations, guest);
        IO.println("The above upcoming reservations will also be canceled.");
        return IO.readBoolean(String.format("Are you sure you want to delete guest %s [y/n]?", guest.getLastName()));
    }

    public Reservation selectReservation(List<Reservation> reservations){
        if(reservations==null || reservations.size()==0){
            IO.println("\nThere is no existing reservation.");
            return null;
        }
        displayReservationsByHost(reservations, reservations.get(0).getHost());
        int input = IO.readInt(String.format("Select [1-%s]", reservations.size()), 1, reservations.size(), true);
        return reservations.get(input-1);
    }

    public State selectState(){
        return selectState(true, null);
    }

    public State selectState(boolean isRequired, State state){
        String data = state==null ? "" : " (" + state.getName() + ")";
        String prompt = String.format("Select a state [1-51]%s: ", data);
        return IO.readState(prompt, isRequired);
    }

    public Host selectHost(List<Host> hosts){
        IO.println("\nHost List:");
        for(int i=0; i< hosts.size(); i++){
            IO.printf("%s. %s: %s, %s, %s - %s%n", i+1, hosts.get(i).getLastName(),
                    hosts.get(i).getAddress(), hosts.get(i).getCity(),
                    hosts.get(i).getState(), hosts.get(i).getEmail());
        }
        IO.println("0. Exit (Going back to Main Menu)");
        Integer input = IO.readInt(String.format("Select[0 - %s]:",hosts.size()),
                0, hosts.size(), true);
        if(input==0){
            return null;
        }
        return hosts.get(input-1);
    }

    public Guest selectGuest(List<Guest> guests){
        IO.println("\nGuest List:");
        for(int i=0; i< guests.size(); i++){
            IO.printf("%s. %s, %s: %s - %s%n", i+1, guests.get(i).getLastName(),
                    guests.get(i).getFirstName(), guests.get(i).getState(), guests.get(i).getEmail());
        }
        IO.println("0. Exit (Going back to Main Menu)");
        Integer input = IO.readInt(String.format("Select[0 - %s]:",guests.size()),
                0, guests.size(), true);
        if(input==0){
            return null;
        }
        return guests.get(input-1);
    }

    public String getCity(){
        return getCity(true, "");
    }

    public String getCity(boolean isRequired, String data){
        data = data.isBlank() ? data : " (" + data + ")";
        String prompt = String.format("Enter city%s: ", data);
        return IO.readString(prompt, isRequired);
    }

    public int getZipcode(){
        return getZipcode(true, null);
    }

    public int getZipcode(boolean isRequired, Integer data){
        String dataString = data==null ? "" : " (" + data + ")";
        String prompt = String.format("Enter zipcode%s: ", dataString);
        return IO.readInt(prompt, isRequired);
    }

    public String getLastname(){
        return getLastname(true, null);
    }

    public String getLastname(boolean isRequired, String data){
        data = data==null ? "" : " (" + data + ")";
        String prompt = String.format("Enter last name%s: ", isRequired ? EXIT_MESSAGE : data);
        return IO.readString(prompt, isRequired);
    }

    public String getLastnameStart(boolean isHost){
        String prompt = String.format("%s's last name starts with%s: ", isHost ? "Host" : "Guest", EXIT_MESSAGE);
        return IO.readString(prompt, true);
    }

    public String getEmail(){
        return getEmail(true, null);
    }

    public String getEmail(boolean isRequired, String data){
        data = data==null ? "" : " (" + data + ")";
        String prompt = String.format("Enter email%s: ", isRequired ? EXIT_MESSAGE : data);
        String input;
        do {
            input=IO.readString(prompt, isRequired);
            if(input.isBlank() && !isRequired)
            {
                break;
            }
            if(input.equalsIgnoreCase("exit")){
                return "exit";
            }
            if(!input.contains("@") || !input.contains(".")){
                IO.println("The email address is invalid.");
            }
        }while (!input.contains("@") || !input.contains("."));
        return input;
    }

    public void enterToContinue() {
        IO.readString("\nPress [Enter] to continue.", false);
    }

    private void displayResult(String message){
        displayResult(true, List.of(message));
    }

    private void displayResult(boolean success, List<String> messages){
        displayHeader(success ? "Success" : "Error");
        messages.forEach(System.out::println);
    }

    private boolean confirmReservation(Reservation reservation){
        displayHeader("Summary");
        IO.println("Start: " + reservation.getStartDate().format(FORMATTER));
        IO.println("End: " + reservation.getEndDate().format(FORMATTER));
        IO.println("Total: $" + reservation.getTotal());
        return IO.readBoolean("Is this okay? [y/n]: ");
    }
}
