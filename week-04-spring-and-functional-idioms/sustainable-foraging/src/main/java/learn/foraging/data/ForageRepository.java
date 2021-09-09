package learn.foraging.data;

import learn.foraging.models.Forage;
import learn.foraging.models.Forager;

import java.time.LocalDate;
import java.util.List;

public interface ForageRepository {
    List<Forage> findByDate(LocalDate date);

    List<Forage> findAll();

    List<Forage> findByForager(Forager forager) throws DataException;

    Forage add(Forage forage) throws DataException;

    boolean update(Forage forage) throws DataException;

    String getDirectory();
}
