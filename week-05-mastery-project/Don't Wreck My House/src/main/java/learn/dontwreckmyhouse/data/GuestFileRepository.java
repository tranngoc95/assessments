package learn.dontwreckmyhouse.data;

import learn.dontwreckmyhouse.models.Guest;
import learn.dontwreckmyhouse.models.State;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class GuestFileRepository implements GuestRepository{

    private final String filePath;
    private final String delimiter = ",";
    private final String delimiterReplacement = "@@@";
    private static final String HEADER = "guest_id,first_name,last_name,email,phone,state,active";

    public GuestFileRepository(@Value("${guestFilePath:data/guests.csv}") String filePath){
        this.filePath = filePath;
    }

    @Override
    public List<Guest> findAll() throws DataException {
        List<Guest> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.readLine();
            for(String line = reader.readLine(); line!=null; line = reader.readLine()){
                String[] fields = line.split(",", -1);
                if(fields.length==7){
                    result.add(deserialize(fields));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<Guest> findActive() throws DataException {
        return findAll().stream()
                .filter(Guest::isActive).collect(Collectors.toList());
    }

    @Override
    public List<Guest> findByLastNameActive(String name) throws DataException {
        return findActive().stream()
                .filter(g->(name.length()<g.getLastName().length()
                        || name.length()==g.getLastName().length())
                        && g.getLastName().substring(0, name.length()).equalsIgnoreCase(name))
                .collect(Collectors.toList());
    }

    @Override
    public List<Guest> findByStateActive(State state) throws DataException {
        return findActive().stream()
                .filter(g->g.getState()==state)
                .collect(Collectors.toList());
    }

    @Override
    public Guest findByEmailActive(String email) throws DataException {
        return findActive().stream()
                .filter(g->g.getEmail().equalsIgnoreCase(email))
                .findFirst().orElse(null);
    }

    @Override
    public Guest findByIdAll(int guestId) throws DataException {
        return findAll().stream()
                .filter(a->a.getGuestID()==guestId)
                .findFirst()
                .orElse(null);
    }

    @Override
    public Guest findByIdActive(int guestId) throws DataException {
        return findActive().stream()
                .filter(a->a.getGuestID()==guestId)
                .findFirst()
                .orElse(null);
    }

    @Override
    public Guest add(Guest guest) throws DataException {
        List<Guest> all = findAll();
        guest.setGuestID(getNextID(all));
        all.add(guest);
        writeAll(all);
        return guest;
    }

    @Override
    public boolean update(Guest guest) throws DataException {
        List<Guest> all = findAll();
        for(int i=0; i<all.size(); i++){
            if(all.get(i).getGuestID()==guest.getGuestID()){
                all.set(i, guest);
                writeAll(all);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean delete(Guest guest) throws DataException {
        List<Guest> all = findAll();
        for(int i=0; i<all.size(); i++){
            if(all.get(i).getGuestID()==guest.getGuestID()){
                all.get(i).setActive(false);
                writeAll(all);
                return true;
            }
        }
        return false;
    }

    private void writeAll(List<Guest> guests) throws DataException{
        try (PrintWriter writer = new PrintWriter(filePath)){

            writer.println(HEADER);

            for(Guest g : guests){
                writer.println(serialize(g));
            }
        } catch (FileNotFoundException e) {
            throw new DataException(e);
        }
    }

    private Guest deserialize(String[] fields){
        Guest result = new Guest();
        result.setGuestID(Integer.parseInt(fields[0]));
        result.setFirstName(restoreField(fields[1]));
        result.setLastName(restoreField(fields[2]));
        result.setEmail(restoreField(fields[3]));
        result.setPhoneNumber(fields[4]);
        result.setState(State.valueOf(fields[5]));
        result.setActive(Boolean.parseBoolean(fields[6]));
        return result;
    }

    private String serialize(Guest guest){
        return String.format("%s,%s,%s,%s,%s,%s,%s",
                guest.getGuestID(), cleanField(guest.getFirstName()),
                cleanField(guest.getLastName()), cleanField(guest.getEmail()),
                guest.getPhoneNumber(), guest.getState(), guest.isActive());
    }

    private String cleanField(String field) {
        return field.replace(delimiter, delimiterReplacement)
                .replace("/r", "")
                .replace("/n", "");
    }

    private String restoreField(String field) {
        return field.replace(delimiterReplacement, delimiter);
    }

    private int getNextID(List<Guest> all) {
        return all.isEmpty() ? 1 : Collections.max(all.stream()
                .map(Guest::getGuestID).collect(Collectors.toList())) + 1;

    }
}
