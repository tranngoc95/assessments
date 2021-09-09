package learn.dontwreckmyhouse.domain;

import learn.dontwreckmyhouse.data.DataException;
import learn.dontwreckmyhouse.data.HostRepository;
import learn.dontwreckmyhouse.models.Host;
import learn.dontwreckmyhouse.models.State;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HostService {
    private final HostRepository repository;

    public HostService(HostRepository repository){
        this.repository=repository;
    };

    public List<Host> findByLastName(String name) throws DataException {
        return repository.findByLastName(name);
    }

    public List<Host> findByState(State state) throws DataException {
        return repository.findByState(state);
    }

    public Host findById(String hostID) throws DataException {
        return repository.findById(hostID);
    }

    public Host findByEmail(String email) throws DataException {
        return repository.findByEmail(email);
    }


}
