package learn.solarpanel.data;

import learn.solarpanel.models.SolarPanel;
import learn.solarpanel.models.MaterialType;

import java.util.List;

public interface SolarRepositories {
    //CREATE
    SolarPanel add(SolarPanel panel) throws DataAccessException;

    //READ
    List<SolarPanel> findAll() throws DataAccessException;
    List<SolarPanel> findBySection(String section) throws DataAccessException;
    SolarPanel findOne(String section, int row, int col) throws DataAccessException;
    List<SolarPanel> findByYearRange(int yearMin, int yearMax) throws DataAccessException;
    List<SolarPanel> findByMaterial(MaterialType materialType) throws DataAccessException;

    //UPDATE
    boolean update(SolarPanel panel) throws DataAccessException;

    //DELETE
    boolean delete(String section, int row, int col) throws DataAccessException;
}
