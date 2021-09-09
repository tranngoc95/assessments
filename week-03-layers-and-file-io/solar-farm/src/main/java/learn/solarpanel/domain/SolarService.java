package learn.solarpanel.domain;

import learn.solarpanel.data.DataAccessException;
import learn.solarpanel.data.SolarRepositories;
import learn.solarpanel.models.SolarPanel;
import learn.solarpanel.models.MaterialType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SolarService {
    private final SolarRepositories repositories;

    public SolarService(SolarRepositories repositories) {
        this.repositories = repositories;
    }

    // CREATE
    public SolarResult add(SolarPanel panel) throws DataAccessException{
        SolarResult result = validation(panel);

        if(panel!=null) {
            if (panel.getSolarID() > 0) {
                result.addMessage("Solar panel ID should not be set.");
            }

            List<SolarPanel> all = findAll();
            for (SolarPanel p : all) {
                if (p.getSection().equals(panel.getSection())
                        && p.getRow() == panel.getRow() && p.getCol() == panel.getCol()) {
                    result.addMessage("Duplicate panel is not allowed.");
                }
            }
        }

        if(result.isSuccess()){
            panel = repositories.add(panel);
            result.setPanel(panel);
            result.setAction("added");
        }

        return result;
    }

    // READ
    public List<SolarPanel> findAll() throws DataAccessException {
        return repositories.findAll();
    }

    public List<SolarPanel> findBySection(String sect) throws DataAccessException {
        return repositories.findBySection(sect);
    }

    public SolarPanel findOne(String sect, int row, int col) throws DataAccessException {
        return repositories.findOne(sect, row, col);
    }

    public List<SolarPanel> findByYearRange(int yearMin, int yearMax) throws DataAccessException {
        return repositories.findByYearRange(yearMin, yearMax);
    }

    public List<SolarPanel> findByMaterial(MaterialType material) throws DataAccessException {
        return repositories.findByMaterial(material);
    }

    // UPDATE
    public SolarResult update(SolarPanel panel) throws DataAccessException {
        SolarResult result = validation(panel);

        if(panel!=null){
            List<SolarPanel> all = findAll();
            for(SolarPanel p : all){
                if (p.getSolarID()!=panel.getSolarID() && p.getSection().equals(panel.getSection())
                        && p.getRow()==panel.getRow() && p.getCol()==panel.getCol()){
                    result.addMessage("Duplicate panel is not allowed.");
                    break;
                }
            }
        }

        if(result.isSuccess()){
            repositories.update(panel);
            result.setPanel(panel);
            result.setAction("updated");
        }

        return result;
    }

    // DELETE
    public SolarResult delete(String section, int row, int col) throws DataAccessException {
        SolarResult result = new SolarResult();
        result.setPanel(findOne(section,row,col));
        result.setAction("deleted");
        boolean success = repositories.delete(section, row, col);
        if(!success){
            String msg = String.format("Panel %s-%s-%s is not found.", section, row, col);
            result.addMessage(msg);
        }
        return result;
    }

    // Validation
    private SolarResult validation(SolarPanel panel){
        SolarResult result = new SolarResult();
        if(panel==null){
            result.addMessage("Panel cannot be null");
            return result;
        }
        if(panel.getSection()==null || panel.getSection().isBlank()){
            result.addMessage("Section is required and cannot be blank.");
        }
        if(panel.getRow()<=0 || panel.getRow()>250){
            result.addMessage("Row should be a positive number less than or equal to 250.");
        }
        if(panel.getCol()<=0 || panel.getCol()>250){
            result.addMessage("Column should be a positive number less than or equal to 250.");
        }
        if(panel.getYearInstalled() > 2021) {
            result.addMessage("Year cannot be in future.");
        }
        if(panel.getMaterial()==null){
            result.addMessage("Material is required.");
        }
        if(panel.getTracking()==null){
            result.addMessage("Tracking information is required.");
        }
        return result;
    }

}
