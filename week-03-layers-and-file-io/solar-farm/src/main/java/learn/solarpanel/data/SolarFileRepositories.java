package learn.solarpanel.data;

import learn.solarpanel.models.MaterialType;
import learn.solarpanel.models.SolarPanel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

//@Repository
public class SolarFileRepositories implements SolarRepositories{

    private final String filePath;
    private final String delimiter = ",";
    private final String delimiterReplacement = "@@@";
    private final String header = "solarID,section,row,col,yearInstalled,material,isTrack";

    public SolarFileRepositories(@Value("${filepath:./data/solar-panels.txt}") String filePath) {
        this.filePath = filePath;
    }

    // CREATE
    @Override
    public SolarPanel add(SolarPanel panel) throws DataAccessException {
        List<SolarPanel> all = findAll();
        int nextID = getNextID(all);
        panel.setSolarID(nextID);
        all.add(panel);
        writeAll(all);
        return panel;
    }

    // READ
    @Override
    public List<SolarPanel> findAll() throws DataAccessException {
        ArrayList<SolarPanel> result = new ArrayList<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(filePath))){
            reader.readLine();
            for(String line = reader.readLine(); line!=null; line= reader.readLine()){
                SolarPanel panel = new SolarPanel();
                String[] fields = line.split(delimiter);
                if(fields.length == 7){
                    panel.setSolarID(Integer.parseInt(fields[0]));
                    panel.setSection(restoreField(fields[1]));
                    panel.setRow(Integer.parseInt(fields[2]));
                    panel.setCol(Integer.parseInt(fields[3]));
                    panel.setYearInstalled(Integer.parseInt(fields[4]));
                    panel.setMaterial(MaterialType.valueOf(fields[5]));
                    panel.setTracking(Boolean.parseBoolean(fields[6]));
                    result.add(panel);
                }
            }
        } catch (IOException ex){
            throw new DataAccessException("Could not open the file path " + filePath);
        }
        return result;
    }

    @Override
    public List<SolarPanel> findBySection(String section) throws DataAccessException {
        List<SolarPanel> panels = findAll();
        ArrayList<SolarPanel> result = new ArrayList<>();
        for(SolarPanel p : panels){
            if(section.equalsIgnoreCase(p.getSection())){
                result.add(p);
            }
        }
        return result;
    }

    @Override
    public SolarPanel findOne(String section, int row, int col) throws DataAccessException {
        List<SolarPanel> panels = findAll();
        for(SolarPanel p : panels){
            if(section.equalsIgnoreCase(p.getSection()) && row==p.getRow() && col==p.getCol()){
                return p;
            }
        }
        return null;
    }

    @Override
    public List<SolarPanel> findByYearRange(int yearMin, int yearMax) throws DataAccessException {
        List<SolarPanel> panels = findAll();
        ArrayList<SolarPanel> result = new ArrayList<>();
        for(SolarPanel p : panels){
            if(p.getYearInstalled() >= yearMin && p.getYearInstalled() <= yearMax){
                result.add(p);
            }
        }
        return result;
    }

    @Override
    public List<SolarPanel> findByMaterial(MaterialType materialType) throws DataAccessException {
        List<SolarPanel> panels = findAll();
        ArrayList<SolarPanel> result = new ArrayList<>();
        for(SolarPanel p : panels){
            if(p.getMaterial()==materialType){
                result.add(p);
            }
        }
        return result;
    }

    // UPDATE
    @Override
    public boolean update(SolarPanel panel) throws DataAccessException {
        List<SolarPanel> all = findAll();
        for(int i=0; i<all.size(); i++){
            if(panel.getSolarID() == all.get(i).getSolarID()){
                all.set(i, panel);
                writeAll(all);
                return true;
            }
        }
        return false;
    }

    // DELETE
    @Override
    public boolean delete(String section, int row, int col) throws DataAccessException {
        List<SolarPanel> all = findAll();
        for(int i=0;i<all.size();i++){
            if(section.equalsIgnoreCase(all.get(i).getSection()) && all.get(i).getRow()==row && all.get(i).getCol()==col){
                all.remove(i);
                writeAll(all);
                return true;
            }
        }
        return false;
    }


    // Helper Methods
    private void writeAll(List<SolarPanel> all) throws DataAccessException{
        try(PrintWriter writer = new PrintWriter(filePath)){
            writer.println(header);
            for(SolarPanel p : all){
                writer.println(serialize(p));
            }
        } catch(FileNotFoundException ex){
            throw new DataAccessException(ex.getMessage(), ex);
        }
    }

    private String serialize(SolarPanel panel){
        return String.format("%s,%s,%s,%s,%s,%s,%s", panel.getSolarID(), cleanField(panel.getSection()),
                panel.getRow(), panel.getCol(), panel.getYearInstalled(), panel.getMaterial(), panel.getTracking());
    }

    private int getNextID(List<SolarPanel> panels){
        int id=0;
        for(SolarPanel p: panels){
            id = Math.max(id,  p.getSolarID());
        }
        return id+1;
    }


    private String cleanField(String field) {
        return field.replace(delimiter, delimiterReplacement)
                .replace("/r", "")
                .replace("/n", "");
    }

    private String restoreField(String field) {
        return field.replace(delimiterReplacement, delimiter);
    }
}