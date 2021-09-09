package learn.dontwreckmyhouse.data;

import learn.dontwreckmyhouse.models.Host;
import learn.dontwreckmyhouse.models.State;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class HostFileRepository implements HostRepository {

    private final String filePath;
    private final String delimiter = ",";
    private final String delimiterReplacement = "@@@";
    private static final String HEADER = "id,last_name,email,phone,address,city,state,postal_code,standard_rate,weekend_rate";


    public HostFileRepository(@Value("${hostFilePath:data/hosts.csv}") String filePath){
        this.filePath=filePath;
    }

    @Override
    public List<Host> findAll() throws DataException {
        List<Host> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.readLine();
            for(String line = reader.readLine(); line!=null; line = reader.readLine()){
                String[] fields = line.split(",", -1);
                if(fields.length==10){
                    result.add(deserialize(fields));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<Host> findByLastName(String name) throws DataException {
        return findAll().stream()
                .filter(h->(name.length()<h.getLastName().length()
                        || name.length()==h.getLastName().length()) &&
                        h.getLastName().substring(0, name.length()).equalsIgnoreCase(name))
                .collect(Collectors.toList());
    }

    @Override
    public List<Host> findByState(State state) throws DataException {
        return findAll().stream()
                .filter(h->h.getState()==state)
                .collect(Collectors.toList());
    }

    @Override
    public Host findById(String hostID) throws DataException {
        return findAll().stream()
                .filter(a->hostID.equals(a.getHostID()))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Host findByEmail(String email) throws DataException {
        return findAll().stream()
                .filter(h->email.equalsIgnoreCase(h.getEmail()))
                .findFirst()
                .orElse(null);
    }



    private Host deserialize(String[] fields){
        Host result = new Host();
        result.setHostID(fields[0]);
        result.setLastName(fields[1]);
        result.setEmail(fields[2]);
        result.setPhoneNumber(fields[3]);
        result.setAddress(fields[4]);
        result.setCity(fields[5]);
        result.setState(State.valueOf(fields[6]));
        result.setPostalCode(Integer.parseInt(fields[7]));
        result.setStandardRate(new BigDecimal(fields[8]));
        result.setWeekendRate(new BigDecimal(fields[9]));
        return result;
    }

//    private void writeAll(List<Host> hosts) throws DataException{
//        try (PrintWriter writer = new PrintWriter(filePath)){
//
//            writer.println(HEADER);
//
//            for(Host h : hosts){
//                writer.println(serialize(h));
//            }
//        } catch (FileNotFoundException e) {
//            throw new DataException(e);
//        }
//    }
//
//    private String serialize(Host host){
//        return String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s",
//                host.getHostID(), host.getLastName(),
//                host.getEmail(), host.getPhoneNumber(),
//                host.getAddress(), host.getCity(),
//                host.getState(), host.getPostalCode(),
//                host.getStandardRate(), host.getWeekendRate());
//    }
//
//    private String cleanField(String field) {
//        return field.replace(delimiter, delimiterReplacement)
//                .replace("/r", "")
//                .replace("/n", "");
//    }
//
//    private String restoreField(String field) {
//        return field.replace(delimiterReplacement, delimiter);
//    }
}
