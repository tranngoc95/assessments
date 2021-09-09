package learn.dontwreckmyhouse.data;

import learn.dontwreckmyhouse.data.DataException;
import learn.dontwreckmyhouse.data.HostRepository;
import learn.dontwreckmyhouse.models.Host;
import learn.dontwreckmyhouse.models.State;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HostRepositoryDouble implements HostRepository {
    public static final Host HOST = new Host(
            "3edda6bc-ab95-49a8-8962-d50b53f84b15","Yearnes","eyearnes0@sfgate.com","(806) 1783815",
            "3 Nova Trail","Amarillo", State.TX,79182,new BigDecimal("340"),new BigDecimal("425"));
    public static final Host HOST1 = new Host(
            "a0d911e7-4fde-4e4a-bdb7-f047f15615e8","Rhodes","krhodes1@posterous.com","(478) 7475991",
            "7262 Morning Avenue","Macon",State.GA,31296,new BigDecimal("295"),new BigDecimal("368.75"));

    private ArrayList<Host> all = new ArrayList<>();

    public HostRepositoryDouble(){
        all.add(HOST);
        all.add(HOST1);
    }

    @Override
    public List<Host> findAll() throws DataException {
        return all;
    }

    @Override
    public List<Host> findByLastName(String name) throws DataException {
        return Arrays.asList(HOST);
    }

    @Override
    public List<Host> findByState(State state) throws DataException {
        return Arrays.asList(HOST);
    }

    @Override
    public Host findById(String hostId) throws DataException {
        return all.stream().filter(h->h.getHostID().equals(hostId))
                .findFirst().orElse(null);
    }

    @Override
    public Host findByEmail(String email) throws DataException {
        return HOST;
    }
}
