package learn.foraging.domain;

import learn.foraging.data.DataException;
import learn.foraging.data.ForageRepositoryDouble;
import learn.foraging.data.ItemRepositoryDouble;
import learn.foraging.models.Category;
import learn.foraging.models.Item;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ItemServiceTest {

    ItemService service = new ItemService(new ItemRepositoryDouble(), new ForageRepositoryDouble());

    @Test
    void shouldNotSaveNullName() throws DataException {
        Item item = new Item(0, null, Category.EDIBLE, new BigDecimal("5.00"));
        Result<Item> result = service.add(item);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotSaveBlankName() throws DataException {
        Item item = new Item(0, "   \t\n", Category.EDIBLE, new BigDecimal("5.00"));
        Result<Item> result = service.add(item);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotSaveNullDollars() throws DataException {
        Item item = new Item(0, "Test Item", Category.EDIBLE, null);
        Result<Item> result = service.add(item);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotSaveNegativeDollars() throws DataException {
        Item item = new Item(0, "Test Item", Category.EDIBLE, new BigDecimal("-5.00"));
        Result<Item> result = service.add(item);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotSaveTooLargeDollars() throws DataException {
        Item item = new Item(0, "Test Item", Category.EDIBLE, new BigDecimal("9999.00"));
        Result<Item> result = service.add(item);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotSaveNullCategory() throws DataException {
        Item item = new Item(0, "Test Item", null, new BigDecimal("23.00"));
        Result<Item> result = service.add(item);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotSavePoisonousWithPrice() throws DataException {
        Item item = new Item(0, "Test Item", Category.POISONOUS, new BigDecimal("5.00"));
        Result<Item> result = service.add(item);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldSave() throws DataException {
        Item item = new Item(0, "Test Item", Category.EDIBLE, new BigDecimal("5.00"));

        Result<Item> result = service.add(item);

        assertNotNull(result.getPayload());
        assertEquals(3, result.getPayload().getId());
    }

    @Test
    void shouldReportKgPerItem() throws DataException {
        assertEquals(2, service.reportKgPerItem().size());
    }

    @Test
    void shouldReportCategoryValue() throws DataException {
        Map<Category, BigDecimal> actual = service.reportCategory();
        assertEquals(1, actual.size());
    }

    @Test
    void shouldUpdate() throws DataException {
        Item item = new Item(1, "Chanterelle", Category.EDIBLE, new BigDecimal("19.99"));
        Result<Item> actual = service.update(item);
        assertTrue(actual.isSuccess());
        assertEquals(new BigDecimal("19.99"), actual.getPayload().getDollarPerKilogram());
    }

    @Test
    void shouldNotUpdateNull() throws DataException {
        Result<Item> actual = service.update(null);
        assertFalse(actual.isSuccess());
        assertTrue(actual.getErrorMessages().get(0).toLowerCase().contains("null"));
    }

    @Test
    void shouldNotUpdateNoName() throws DataException {
        Item item = new Item(1, " ", Category.EDIBLE, new BigDecimal("19.99"));
        Result<Item> actual = service.update(item);
        assertFalse(actual.isSuccess());
        assertTrue(actual.getErrorMessages().get(0).toLowerCase().contains("name"));
    }

    @Test
    void shouldNotUpdateDuplicateName() throws DataException {
        Item item = new Item(1, "Dogbane", Category.EDIBLE, new BigDecimal("19.99"));
        Result<Item> actual = service.update(item);
        assertFalse(actual.isSuccess());
        assertTrue(actual.getErrorMessages().get(0).toLowerCase().contains("duplicate"));
    }

    @Test
    void shouldNotUpdateNullDollarPerKg() throws DataException {
        Item item = new Item(1, "Chanterelle", Category.EDIBLE, null);
        Result<Item> actual = service.update(item);
        assertFalse(actual.isSuccess());
        assertTrue(actual.getErrorMessages().get(0).toLowerCase().contains("$/kg"));
    }

    @Test
    void shouldNotUpdateNullCategory() throws DataException {
        Item item = new Item(1, "Chanterelle", null, new BigDecimal("19.99"));
        Result<Item> actual = service.update(item);
        assertFalse(actual.isSuccess());
        assertTrue(actual.getErrorMessages().get(0).toLowerCase().contains("category"));
    }
}