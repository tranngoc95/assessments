package learn.solarpanel.data;

import learn.solarpanel.data.DataAccessException;
import learn.solarpanel.data.SolarRepositories;
import learn.solarpanel.models.MaterialType;
import learn.solarpanel.models.SolarPanel;

import java.util.List;

public class SolarFileRepositoriesDouble implements SolarRepositories {

    @Override
    public SolarPanel add(SolarPanel panel) throws DataAccessException {
        return null;
    }

    @Override
    public List<SolarPanel> findAll() throws DataAccessException {
        return List.of(new SolarPanel(4,"Rose",4,3,1999,MaterialType.CDTE,false),
                new SolarPanel(1,"Orchids",10,10,2008,MaterialType.POLYSI,true));
    }

    @Override
    public List<SolarPanel> findBySection(String section) throws DataAccessException {
        return List.of(new SolarPanel(4,"Rose",4,3,1999,MaterialType.CDTE,false));
    }

    @Override
    public SolarPanel findOne(String section, int row, int col) throws DataAccessException {
        return new SolarPanel(4,"Rose",4,3,1999,MaterialType.CDTE,false);
    }

    @Override
    public List<SolarPanel> findByYearRange(int yearMin, int yearMax) throws DataAccessException {
        return List.of(new SolarPanel(4,"Rose",4,3,1999,MaterialType.CDTE,false));
    }

    @Override
    public List<SolarPanel> findByMaterial(MaterialType materialType) throws DataAccessException {
        return List.of(new SolarPanel(4,"Rose",4,3,1999,MaterialType.CDTE,false));
    }

    @Override
    public boolean update(SolarPanel panel) throws DataAccessException {
        return panel.getSolarID()==4;
    }

    public boolean delete(String section, int row, int col) throws DataAccessException {
        return section.equals("Rose") && row==4 && col==3;
    }
}
