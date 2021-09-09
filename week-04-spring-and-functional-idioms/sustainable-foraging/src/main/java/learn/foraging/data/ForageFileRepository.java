package learn.foraging.data;

import learn.foraging.models.Forage;
import learn.foraging.models.Forager;
import learn.foraging.models.Item;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ForageFileRepository implements ForageRepository {

    private static final String HEADER = "id,forager_id,item_id,kg";
    private final String directory;

    private ItemRepository itemRepo;
    private ForagerRepository foragerRepo;

    public ForageFileRepository(@Value("${forageDirectory}") String directory,
                                ItemRepository itemRepo, ForagerRepository foragerRepo) {
        this.directory = directory;
        this.itemRepo=itemRepo;
        this.foragerRepo=foragerRepo;

    }

    @Override
    public List<Forage> findByDate(LocalDate date) {
        ArrayList<Forage> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(getFilePath(date)))) {

            reader.readLine(); // read header

            for (String line = reader.readLine(); line != null; line = reader.readLine()) {

                String[] fields = line.split(",", -1);
                if (fields.length == 4) {
                    result.add(deserialize(fields, date));
                }
            }
        } catch (IOException ex) {
            // don't throw on read
        }
        return result;
    }

    public List<Forage> findAll(){
        File folder = new File(getDirectory());
        File[] filesList = folder.listFiles();
        if(filesList==null){
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return Arrays.stream(filesList).filter(File::isFile)
                .map(a->findByDate(LocalDate.parse(a.getName().substring(0,a.getName().length()-4), formatter)))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    @Override
    public List<Forage> findByForager(Forager forager) throws DataException {
        return findAll().stream()
                .filter(a->a.getForager().equals(forager))
                .collect(Collectors.toList());
    }

    @Override
    public Forage add(Forage forage) throws DataException {
        List<Forage> all = findByDate(forage.getDate());
        forage.setId(java.util.UUID.randomUUID().toString());
        all.add(forage);
        writeAll(all, forage.getDate());
        return forage;
    }

    @Override
    public boolean update(Forage forage) throws DataException {
        List<Forage> all = findByDate(forage.getDate());
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getId().equals(forage.getId())) {
                all.set(i, forage);
                writeAll(all, forage.getDate());
                return true;
            }
        }
        return false;
    }

    @Override
    public String getDirectory() {
        return directory;
    }

    private String getFilePath(LocalDate date) {
        return Paths.get(directory, date + ".csv").toString();
    }

    private void writeAll(List<Forage> forages, LocalDate date) throws DataException {
        try (PrintWriter writer = new PrintWriter(getFilePath(date))) {

            writer.println(HEADER);

            for (Forage item : forages) {
                writer.println(serialize(item));
            }
        } catch (FileNotFoundException ex) {
            throw new DataException(ex);
        }
    }

    private String serialize(Forage forage) {
        return String.format("%s,%s,%s,%s",
                forage.getId(),
                forage.getForager().getId(),
                forage.getItem().getId(),
                forage.getKilograms());
    }

    private Forage deserialize(String[] fields, LocalDate date) {
        Forage result = new Forage();
        result.setId(fields[0]);
        result.setDate(date);
        result.setKilograms(Double.parseDouble(fields[3]));

        Forager forager = foragerRepo.findById(fields[1]);
        result.setForager(forager);

        Item item = itemRepo.findById(Integer.parseInt(fields[2]));
        result.setItem(item);
        return result;
    }
}
