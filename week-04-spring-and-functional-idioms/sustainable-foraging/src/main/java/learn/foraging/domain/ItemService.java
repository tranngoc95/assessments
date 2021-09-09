package learn.foraging.domain;

import learn.foraging.data.DataException;
import learn.foraging.data.ForageFileRepository;
import learn.foraging.data.ForageRepository;
import learn.foraging.data.ItemRepository;
import learn.foraging.models.Category;
import learn.foraging.models.Forage;
import learn.foraging.models.Item;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ItemService {

    private final ItemRepository repository;

    private final ForageRepository forageRepo;

    public ItemService(ItemRepository repository, ForageRepository forageRepo) {
        this.repository = repository;
        this.forageRepo = forageRepo;
    }

    public List<Item> findAll(){
        return repository.findAll();
    }

    public List<Item> findByCategory(Category category) {
        return repository.findAll().stream()
                .filter(i -> i.getCategory() == category)
                .collect(Collectors.toList());
    }

    public Item findByID(int itemID){
        return repository.findAll().stream()
                .filter(i -> i.getId() == itemID)
                .findFirst()
                .orElse(null);
    }

    public Result<Item> add(Item item) throws DataException {
        Result<Item> result = validate(item, true);

        if (!result.isSuccess()) {
            return result;
        }

        result.setPayload(repository.add(item));

        return result;
    }

    public Result<Item> update(Item item) throws  DataException {
        Result<Item> result = validate(item, false);

        if (!result.isSuccess()) {
            return result;
        }

        if(!repository.update(item)){
            result.addErrorMessage("Cannot find Item " + item.getName());
        }

        result.setPayload(item);

        return result;
    }

    private Result<Item> validate(Item item, boolean isAdd) throws DataException {
        Result<Item> result = new Result<>();
        if (item == null) {
            result.addErrorMessage("Item must not be null.");
            return result;
        }
        List<Item> otherItems = repository.findAll();
        otherItems = isAdd ? otherItems : otherItems.stream()
                .filter(a->a.getId()!=item.getId()).collect(Collectors.toList());

        if (item.getName() == null || item.getName().isBlank()) {
            result.addErrorMessage("Item name is required.");
        } else if (otherItems.stream()
                .anyMatch(i -> i.getName().equalsIgnoreCase(item.getName()))) {
            result.addErrorMessage(String.format("Item '%s' is a duplicate.", item.getName()));
        }

        if (item.getDollarPerKilogram() == null) {
            result.addErrorMessage("$/Kg is required.");
        } else if (item.getDollarPerKilogram().compareTo(BigDecimal.ZERO) < 0
                || item.getDollarPerKilogram().compareTo(new BigDecimal("7500.00")) > 0) {
            result.addErrorMessage("%/Kg must be between 0.00 and 7500.00.");
        }

        if(item.getCategory()==null) {
            result.addErrorMessage("Category is required.");
        }

        if((item.getCategory()==Category.INEDIBLE || item.getCategory()==Category.POISONOUS)
                && item.getDollarPerKilogram().compareTo(BigDecimal.ZERO)!=0){
            result.addErrorMessage("Inedible and Poisonous items should have a price of 0.");
        }
        return result;
    }

    public Map<Item, Double> reportKgPerItem() throws DataException {
        List<Forage> forageList = forageRepo.findAll();
        if(forageList==null){
            return null;
        }
        Map<Item, Double> availableItems= forageList.stream()
                .collect(Collectors.groupingBy(Forage::getItem,Collectors.summingDouble(Forage::getKilograms)));
        Map<Item, Double> otherItems = findAll().stream().filter(a-> !availableItems.containsKey(a))
                .collect(Collectors.toMap(a->a, a -> 0d));
        availableItems.putAll(otherItems);
            return availableItems;

    }

    public Map<Category, BigDecimal> reportCategory() throws DataException {
//        List<Forage> forages = forageRepo.findAll();
//        Map<Category, BigDecimal> result = new HashMap<>();
//        for(Category c: Category.values()){
//            result.put(c, new BigDecimal(0));
//        }
//
//        if(forages!=null) {
//            for (Forage f : forages) {
//                BigDecimal value = result.get(f.getItem().getCategory()).add(f.getValue());
//                result.put(f.getItem().getCategory(), value);
//            }
//        }

        return forageRepo.findAll().stream().collect(Collectors.groupingBy(a->a.getItem().getCategory()))
                .entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        a->a.getValue().stream()
                                .map(Forage::getValue).reduce(((total, each) -> total = total.add(each)))
                                .orElse(BigDecimal.ZERO)));

    }

}
