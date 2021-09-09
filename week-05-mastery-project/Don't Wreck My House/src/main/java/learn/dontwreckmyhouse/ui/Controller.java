package learn.dontwreckmyhouse.ui;

import learn.dontwreckmyhouse.data.DataException;
import learn.dontwreckmyhouse.domain.GuestService;
import learn.dontwreckmyhouse.domain.HostService;
import learn.dontwreckmyhouse.domain.ReservationService;
import learn.dontwreckmyhouse.domain.Result;
import learn.dontwreckmyhouse.models.Guest;
import learn.dontwreckmyhouse.models.Host;
import learn.dontwreckmyhouse.models.Reservation;
import learn.dontwreckmyhouse.models.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Controller {

    private final View view;
    private final HostService hostService;
    private final GuestService guestService;
    private final ReservationService reservationService;

    @Autowired
    public Controller(View view, HostService hostService,
                      GuestService guestService, ReservationService reservationService) {
        this.view = view;
        this.hostService = hostService;
        this.guestService = guestService;
        this.reservationService = reservationService;
    }

    public void run() {
        view.displayHeader("Welcome to Sustainable Foraging");
        try {
            runApp();
        }catch (DataException ex){
            view.displayError(ex.getMessage());
        }
        view.displayHeader("Goodbye.");
    }

    public void runApp() throws DataException {
        MainMenuOption option;
        do {
            option = view.selectMainMenu();
            switch (option) {
                case EXIT:
                    break;
                case VIEW_RESERVATIONS_BY_HOST:
                    viewReservationsByHost();
                    break;
                case VIEW_RESERVATIONS_BY_GUEST:
                    viewReservationsByGuest();
                    break;
                case VIEW_RESERVATIONS_BY_STATE:
                    viewReservationsByState();
                    break;
                case VIEW_RESERVATIONS_BY_CITY:
                    viewReservationsByCity();
                    break;
                case VIEW_RESERVATIONS_BY_ZIPCODE:
                    viewReservationsByZipCode();
                    break;
                case MAKE_RESERVATION:
                    createReservation();
                    break;
                case UPDATE_RESERVATION:
                    updateReservation();
                    break;
                case CANCEL_RESERVATION:
                    cancelReservation();
                    break;
                case ADD_GUEST:
                    addGuest();
                    break;
                case EDIT_GUEST:
                    editGuest();
                    break;
                case DELETE_GUEST:
                    deleteGuest();
            }
        }while (option!=MainMenuOption.EXIT);
    }

    public void viewReservationsByHost() throws DataException {
        view.displayHeader(MainMenuOption.VIEW_RESERVATIONS_BY_HOST.getMessage());
        Host host = getHost();
        if(host==null){
            return;
        }
        List<Reservation> reservations = reservationService.findByHostId(host.getHostID());
        view.displayReservationsByHost(reservations, host);
        view.enterToContinue();
    }

    public void viewReservationsByGuest() throws DataException {
        view.displayHeader(MainMenuOption.VIEW_RESERVATIONS_BY_GUEST.getMessage());
        Guest guest = getGuest();
        if(guest==null){
            return;
        }
        List<Reservation> reservations = reservationService.findByGuestId(guest.getGuestID());
        view.displayReservationsByGuest(reservations, guest);
        view.enterToContinue();
    }

    public void viewReservationsByState() throws DataException {
        view.displayHeader(MainMenuOption.VIEW_RESERVATIONS_BY_STATE.getMessage());
        State state = view.selectState();
        List<Reservation> reservations = reservationService.findByState(state);
        view.displayReservationsByFields(reservations);
        view.enterToContinue();
    }

    public void viewReservationsByCity() throws DataException {
        view.displayHeader(MainMenuOption.VIEW_RESERVATIONS_BY_CITY.getMessage());
        String city = view.getCity();
        List<Reservation> reservations = reservationService.findByCity(city);
        view.displayReservationsByFields(reservations);
        view.enterToContinue();
    }

    public void viewReservationsByZipCode() throws DataException {
        view.displayHeader(MainMenuOption.VIEW_RESERVATIONS_BY_ZIPCODE.getMessage());
        int zipcode = view.getZipcode();
        List<Reservation> reservations = reservationService.findByZipCode(zipcode);
        view.displayReservationsByFields(reservations);
        view.enterToContinue();
    }

    public void createReservation() throws DataException {
        view.displayHeader(MainMenuOption.MAKE_RESERVATION.getMessage());
        Guest guest = getGuest();
        if(guest==null){
            return;
        }
        Host host = getHost();
        if(host==null){
            return;
        }
        List<Reservation> reservations = reservationService.findUpcomingByHostID(host.getHostID());
        boolean cont;
        do {
            cont = false;
            Reservation reservation = view.createReservation(reservations, host, guest);
            if (reservation != null) {
                Result<Reservation> result = reservationService.add(reservation);
                view.displayReservationResult(result, "created");
                if(!result.isSuccess()){
                    cont=view.continueReservation();
                }
            }
        } while (cont);
    }

    public void updateReservation() throws DataException {
        view.displayHeader(MainMenuOption.UPDATE_RESERVATION.getMessage());
        Guest guest = getGuest();
        if(guest==null){
            return;
        }
        Host host = getHost();
        if(host==null){
            return;
        }
        List<Reservation> reservations = reservationService.findByHostAndGuest(host.getHostID(), guest.getGuestID());
        Reservation reservation = view.updateReservation(reservations);
        if(reservation!=null){
            Result<Reservation> result = reservationService.update(reservation);
            view.displayReservationResult(result, "updated");
        }
    }

    public void cancelReservation() throws DataException {
        view.displayHeader(MainMenuOption.CANCEL_RESERVATION.getMessage());
        Guest guest = getGuest();
        if(guest==null){
            return;
        }
        Host host = getHost();
        if(host==null){
            return;
        }
        List<Reservation> reservations = reservationService.findUpcomingByHostAndGuest(host.getHostID(), guest.getGuestID());
        Reservation reservation = view.selectReservation(reservations);
        if(reservation!=null){
            Result<Reservation> result = reservationService.delete(reservation);
            view.displayReservationResult(result, "deleted");
        }
    }

    public void addGuest() throws DataException {
        view.displayHeader(MainMenuOption.ADD_GUEST.getMessage());
        Guest guest = view.createGuest();
        if(guest!=null) {
            Result<Guest> result = guestService.add(guest);
            view.displayGuestResult(result, "added");
        }
    }

    public void editGuest() throws DataException {
        view.displayHeader(MainMenuOption.EDIT_GUEST.getMessage());
        Guest guest = getGuest();
        if(guest==null){
            return;
        }
        guest = view.updateGuest(guest);
        Result<Guest> result = guestService.update(guest);
        view.displayGuestResult(result, "updated");
    }

    public void deleteGuest() throws DataException {
        view.displayHeader(MainMenuOption.DELETE_GUEST.getMessage());
        Guest guest = getGuest();
        if(guest==null){
            return;
        }
        List<Reservation> reservations = reservationService.findUpcomingByGuestID(guest.getGuestID());
        if(view.confirmDeleteGuest(reservations, guest)) {
            if(reservations!=null){
                for(Reservation r: reservations){
                    Result<Reservation> rr = reservationService.delete(r);
                    view.displayReservationResult(rr, "deleted");
                }
            }
            Result<Guest> result = guestService.delete(guest);
            view.displayGuestResult(result, "deleted");
        }
    }

    // Support Methods
    private Guest getGuest() throws DataException {
        view.displayHeader("Decide A Guest:");
        int findMethod = view.selectFindMethod();
        switch (findMethod){
            case 0:
                return null;
            case 1:
                return getGuestByLastName();
            case 2:
            default:
                return getGuestByEmail();
        }
    }

    private Guest getGuestByLastName() throws DataException{
        String name;
        List<Guest> guests;
        do {
            name = view.getLastnameStart(false);
            if(name.equalsIgnoreCase("exit")){
                return null;
            }
            guests = guestService.findByLastName(name);
            if(guests==null || guests.isEmpty()){
                view.displayError(String.format("Could not find any guest whose last name starts with \"%s\"", name));
            }
        } while (guests==null || guests.isEmpty());
        return view.selectGuest(guests);
    }

    private Guest getGuestByEmail() throws DataException {
        Guest guest;
        do {
            String email = view.getEmail();
            if (email.equals("exit")) {
                return null;
            }
            guest=guestService.findByEmail(email);
            if(guest==null){
                view.displayError(String.format("Could not find any guest with emails \"%s\"", email));
            }
        } while (guest==null);
        return guest;
    }

    private Host getHost() throws DataException {
        view.displayHeader("Decide A Host:");
        int findMethod = view.selectFindMethod();
        switch (findMethod){
            case 0:
                return null;
            case 1:
                return getHostByLastName();
            case 2:
            default:
                return getHostByEmail();
        }
    }

    private Host getHostByLastName() throws DataException {
        String name;
        List<Host> hosts;
        do {
            name = view.getLastnameStart(true);
            if(name.equalsIgnoreCase("exit")){
                return null;
            }
            hosts = hostService.findByLastName(name);
            if(hosts==null || hosts.isEmpty()){
                view.displayError(String.format("Could not find any host whose last name starts with \"%s\"", name));
            }
        }while (hosts==null || hosts.isEmpty());
        return view.selectHost(hosts);
    }

    private Host getHostByEmail() throws DataException {
        Host host;
        do {
            String email = view.getEmail();
            if (email.equals("exit")) {
                return null;
            }
            host=hostService.findByEmail(email);
            if(host==null){
                view.displayError(String.format("Could not find any host with emails \"%s\"", email));
            }
        } while (host==null);
        return host;
    }
}
