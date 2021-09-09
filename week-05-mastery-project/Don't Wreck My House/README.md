# Assessment M05: Don't Wreck My House

### Requirements
- View existing reservations for a host
    - search for a host and pick one out of a list      x
    - host not found -> display msg     x
    - host no reservation -> display msg        x
    - show all reservations for a host      x
    - for each reservation: show guest, dates, totals, etc.     x
    - sort reservations     x
- Create a reservation for a guest with a host
    - Booking
        - search for a guest and pick one out       x
        - search for a host and pick one out        x
        - show all future reservations for that host to choose available dates      x
        - enter a start date and end date       x
        - calculate total, display summary, ask user to confirm     x
            - standard rate & weekend rate
        - print confirmation & save reservation     x
    - Validation
        - Guest, host, start & end dates: required      x
        - Guests & hosts: existed       x
        - Start date come b4 end date       x
        - Reservations don't overlap        x
        - Start date in future      x
- Edit existing reservations
    - Editing
        - Find a reservation        x
        - Only can edit start and end date      x
        - Recalculate the total, display a summary, and ask the user to confirm     x
    - Validation
        - Guest, host, start & end dates: required      x
        - Guest & host: exist       x
        - Start date come b4 end date       x
        - Reservations don't overlap        x
- Cancel a future reservation.
    - Canceling
        - Find reservation      x
        - Show only future reservations     x
        - Display success on msg        x
    - Validation
        - Cannot cancel past reservations       x
    
### State
- test data folder
  - seed & test csv for reservation, host, guest
- reservation data folder
  - each host csv file
- hosts.csv
- guests.csv

### Models
#### Host
- Variables
    - int hostID
    - String email
    - int phoneNumber
    - String address
    - String city
    - String state
    - int postalCode
    - BigDecimal standardRate
    - BigDecimal weekendRate
- Methods:
    - getters & setters
#### Guest
- Variables
    - int guestID
    - String firstName
    - String lastName
    - String email
    - int phoneNumber
    - String state
- Methods:
    - getters & setters
#### Reservation
- Variables
    - String reservationID
    - LocalDate startDate
    - LocalDate endDate
    - Host host
    - Guest guest
- Methods:
    - getters & setters
    - getPrice()

### Data
#### GuestRepository
- findAll()
- findByName()
- findById()
- findByState()
- findByPhoneNumber()
#### HostRepository
- findAll()
- findByName()
- findById()
- findByState()
- findByPhoneNumber()
#### ReservationRepository
- findAll()
- findByDate()
- findByHost()
- addReservation()
- updateReservation()
- deleteReservation()
- writeToFile()
- serialize()
- deserialize()

### Domain
#### GuestService
- findAll()
- findByName()
- findById()
- findByState()
- findByPhoneNumber()
#### HostService
- findAll()
- findByName()
- findById()
- findByState()
- findByPhoneNumber()
#### ReservationService
- findAll()
- findByDate()
- findByHost()
- addReservation()
- updateReservation()
- deleteReservation()
- validation()
#### Result<T>
- Variables:
    - List<String> message
    - T payload
- Methods
    - Getter & Setter
    - addErrorMessage(String msg)
    - boolean isSuccess()

### UI
#### ConsoleIO
- print(String msg)
- println(String msg)
- readString(String prompt)
- readInt(String prompt)
- readBigDecimal(String prompt)
- readLocalDate(String prompt)
#### View
- void displayMenu()
- void displayReservation(List<Reservation> reservations)
- Reservation makeReservation()
- Reservation editReservation(Reservation reservation)
- Reservation chooseReservation(List<Reservation> reservations)
- Host chooseHost(List<Host> hosts)
- void enterToContinue()

#### Controller
- run()
- runApp()
- viewReservationByDate()
- viewReservationByHost()
- addReservation()
- editReservation()
- cancelReservation()

### App
- Spring DI Annotation      x

### Steps
- Create Guest, Host, Reservation Models        x
- Create Guest, Host, Reservation Repositories      x
- Create Repositories Tests       x
- Create Guest, Host, Reservation Service       x
- Create Repositories Doubles & Service Tests    x
- Create ConsoleIO      x
- Create View, Controller & App     x