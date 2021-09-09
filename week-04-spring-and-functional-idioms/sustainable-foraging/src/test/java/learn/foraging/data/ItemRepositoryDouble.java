package learn.foraging.data;

import learn.foraging.models.Category;
import learn.foraging.models.Item;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ItemRepositoryDouble implements ItemRepository {

    public final static Item ITEM = new Item(1, "Chanterelle", Category.EDIBLE, new BigDecimal("9.99"));
    public final static Item ITEM2 = new Item(2, "Dogbane", Category.POISONOUS, new BigDecimal("5.99"));
    private final ArrayList<Item> items = new ArrayList<>();

    public ItemRepositoryDouble() {
        items.add(ITEM);
        items.add(ITEM2);
    }

    @Override
    public List<Item> findAll() {
        return new ArrayList<>(items);
    }

    @Override
    public Item findById(int id) {
        return findAll().stream()
                .filter(i -> i.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public Item add(Item item) throws DataException {
        List<Item> all = findAll();

        int nextId = all.stream()
                .mapToInt(Item::getId)
                .max()
                .orElse(0) + 1;

        item.setId(nextId);

        all.add(item);
        return item;
    }

    @Override
    public boolean update(Item item) throws DataException {
        return item.getId()==1;
    }
}
