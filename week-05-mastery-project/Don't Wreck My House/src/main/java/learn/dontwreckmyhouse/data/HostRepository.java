package learn.dontwreckmyhouse.data;

import learn.dontwreckmyhouse.models.Host;
import learn.dontwreckmyhouse.models.State;

import java.util.List;

public interface HostRepository {
    List<Host> findAll() throws DataException;
    List<Host> findByLastName(String name) throws DataException;
    List<Host> findByState(State state) throws DataException;
    Host findById(String hostId) throws DataException;
    Host findByEmail(String email) throws DataException;
}
