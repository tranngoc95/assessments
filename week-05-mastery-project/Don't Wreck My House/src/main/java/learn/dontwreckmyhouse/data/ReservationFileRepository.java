package learn.dontwreckmyhouse.data;

import learn.dontwreckmyhouse.models.Reservation;
import learn.dontwreckmyhouse.models.State;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class ReservationFileRepository implements ReservationRepository {

    private final String directory;
    private final HostRepository hostRepo;
    private final GuestRepository guestRepo;
    private static final String HEADER = "id,start_date,end_date,guest_id,total";

    public ReservationFileRepository(@Value("${reservationDirectory:data/reservations}") String directory,
                                     GuestRepository guestRepo, HostRepository hostRepo){
        this.directory=directory;
        this.guestRepo=guestRepo;
        this.hostRepo=hostRepo;
    }

    @Override
    public List<Reservation> findByHostId(String hostID){
        List<Reservation> result = new ArrayList<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(getFilePath(hostID)))) {
            reader.readLine();
            for(String line = reader.readLine(); line!=null; line = reader.readLine()){
                String[] fields = line.split(",", -1);
                result.add(deserialize(fields, hostID));
            }

        } catch (IOException | DataException e) {

        }
        return result;
    }

    @Override
    public List<Reservation> findAll() {
        File folder = new File(directory);
        File[] filesList = folder.listFiles();
        if(filesList==null){
            return Collections.emptyList();
        }
        return Arrays.stream(filesList).filter(File::isFile)
                .map(a->findByHostId(a.getName().substring(0,a.getName().length()-4)))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    @Override
    public List<Reservation> findUpcomingByHostId(String hostID) {
        return findByHostId(hostID).stream()
                .filter(r->!r.getStartDate().isBefore(LocalDate.now()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Reservation> findUpcomingByGuestID(int guestID) {
        return findByGuestId(guestID).stream()
                .filter(r->!r.getStartDate().isBefore(LocalDate.now()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Reservation> findUpcomingByHostAndGuest(String hostID, int guestID) {
        return findByHostAndGuest(hostID, guestID).stream()
                .filter(r->!r.getStartDate().isBefore(LocalDate.now()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Reservation> findByGuestId(int guestID) {
        return findAll().stream()
                .filter(r -> r.getGuest().getGuestID()==guestID)
                .collect(Collectors.toList());
    }

    @Override
    public List<Reservation> findByState(State state) {
        return findAll().stream()
                .filter(r -> r.getHost().getState()==state)
                .collect(Collectors.toList());
    }

    @Override
    public List<Reservation> findByCity(String city) {
        return findAll().stream()
                .filter(r->r.getHost().getCity().equalsIgnoreCase(city))
                .collect(Collectors.toList());
    }

    @Override
    public List<Reservation> findByZipCode(int zipCode) {
        return findAll().stream()
                .filter(r->r.getHost().getPostalCode()==zipCode)
                .collect(Collectors.toList());
    }

    @Override
    public List<Reservation> findByHostAndGuest(String hostID, int guestID){
        return findAll().stream()
                .filter(r->r.getHost().getHostID().equals(hostID)
                        && r.getGuest().getGuestID()==guestID)
                .collect(Collectors.toList());
    }

    @Override
    public Reservation findByID(String reservationID) {
        return findAll().stream()
                .filter(r->r.getReservationID().equals(reservationID))
                .findFirst().orElse(null);
    }

    @Override
    public Reservation add(Reservation reservation) throws DataException {
        List<Reservation> all = findByHostId(reservation.getHost().getHostID());
        reservation.setReservationID(java.util.UUID.randomUUID().toString());
        all.add(reservation);
        writeAll(all, reservation.getHost().getHostID());
        return reservation;
    }

    @Override
    public boolean update(Reservation reservation) throws DataException {
        List<Reservation> all = findByHostId(reservation.getHost().getHostID());
        for(int i=0; i<all.size(); i++){
            if(all.get(i).getReservationID().equalsIgnoreCase(reservation.getReservationID())){
                all.set(i, reservation);
                writeAll(all, reservation.getHost().getHostID());
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean delete(Reservation reservation) throws DataException {
        List<Reservation> all = findByHostId(reservation.getHost().getHostID());
        for(int i=0; i<all.size(); i++){
            if(all.get(i).getReservationID().equalsIgnoreCase(reservation.getReservationID())){
                all.remove(i);
                writeAll(all, reservation.getHost().getHostID());
                return true;
            }
        }
        return false;
    }

    private String getFilePath(String hostID) {
        return Paths.get(directory, hostID + ".csv").toString();
    }

    private void writeAll(List<Reservation> reservations, String hostID) throws DataException{
        try (PrintWriter writer = new PrintWriter(getFilePath(hostID))){

            writer.println(HEADER);

            for(Reservation r : reservations){
                writer.println(serialize(r));
            }
        } catch (FileNotFoundException e) {
            throw new DataException(e);
        }
    }

    private Reservation deserialize(String[] fields, String hostID) throws DataException {
        Reservation result = new Reservation();
        result.setReservationID(fields[0]);
        result.setStartDate(LocalDate.parse(fields[1]));
        result.setEndDate(LocalDate.parse(fields[2]));
        result.setGuest(guestRepo.findByIdAll(Integer.parseInt(fields[3])));
        result.setTotal(new BigDecimal(fields[4]));
        result.setHost(hostRepo.findById(hostID));
        return result;
    }

    private String serialize(Reservation reservation){
        return String.format("%s,%s,%s,%s,%s",
                reservation.getReservationID(),
                reservation.getStartDate(),
                reservation.getEndDate(),
                reservation.getGuest().getGuestID(),
                reservation.getTotal());
    }

}
