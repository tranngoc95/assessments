package learn.dontwreckmyhouse.domain;

import learn.dontwreckmyhouse.data.DataException;
import learn.dontwreckmyhouse.data.GuestRepositoryDouble;
import learn.dontwreckmyhouse.models.Guest;
import learn.dontwreckmyhouse.models.State;
import org.junit.jupiter.api.Test;

import javax.xml.crypto.Data;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class GuestServiceTest {

    GuestService service = new GuestService(new GuestRepositoryDouble());

    //READ
    @Test
    void ShouldFindByLastName() throws DataException {
        List<Guest> actual = service.findByLastName("Lomas");
        assertNotNull(actual);
        assertEquals(1, actual.size());
    }

    @Test
    void ShouldFindByState() throws DataException {
        List<Guest> actual = service.findByState(State.NV);
        assertNotNull(actual);
        assertEquals(1, actual.size());
    }

    @Test
    void ShouldFindById() throws DataException {
        Guest actual = service.findById(1);
        assertNotNull(actual);
    }

    // CREATE
    @Test
    void shouldNotAddNull() throws DataException {
        Result<Guest> actual = service.add(null);
        assertFalse(actual.isSuccess());
        assertTrue(actual.getErrorMessages().get(0).toLowerCase().contains("null"));
    }

    @Test
    void shouldNotAddEmptyFirstname() throws DataException {
        Guest guest = new Guest(0,"","Mount","amountc@ehow.com","(312) 3713380",State.IL);
        Result<Guest> actual = service.add(guest);
        assertFalse(actual.isSuccess());
        assertTrue(actual.getErrorMessages().get(0).toLowerCase().contains("first name"));
    }

    @Test
    void shouldNotAddEmptyLastname() throws DataException {
        Guest guest = new Guest(0,"Alisun",null,"amountc@ehow.com","(312) 3713380",State.IL);
        Result<Guest> actual = service.add(guest);
        assertFalse(actual.isSuccess());
        assertTrue(actual.getErrorMessages().get(0).toLowerCase().contains("last name"));
    }

    @Test
    void shouldNotAddEmptyEmail() throws DataException {
        Guest guest = new Guest(0,"Alisun","Mount","","(312) 3713380",State.IL);
        Result<Guest> actual = service.add(guest);
        assertFalse(actual.isSuccess());
        assertTrue(actual.getErrorMessages().get(0).toLowerCase().contains("email"));
    }

    @Test
    void shouldNotAddEmptyPhoneNumber() throws DataException {
        Guest guest = new Guest(0,"Alisun","Mount","amountc@ehow.com",null,State.IL);
        Result<Guest> actual = service.add(guest);
        assertFalse(actual.isSuccess());
        assertTrue(actual.getErrorMessages().get(0).toLowerCase().contains("phone number"));
    }

    @Test
    void shouldNotAddEmptyState() throws DataException {
        Guest guest = new Guest(0,"Alisun","Mount","amountc@ehow.com","(312) 3713380",null);
        Result<Guest> actual = service.add(guest);
        assertFalse(actual.isSuccess());
        assertTrue(actual.getErrorMessages().get(0).toLowerCase().contains("state"));
    }

    @Test
    void shouldNotAddDuplicate() throws DataException {
        Guest guest = new Guest(0,"Alisun","Mount","slomas0@mediafire.com","(312) 3713380",null);
        Result<Guest> actual = service.add(guest);
        assertFalse(actual.isSuccess());
        assertTrue(actual.getErrorMessages().get(0).toLowerCase().contains("exist"));
    }

    @Test
    void shouldAdd() throws DataException {
        Guest guest = new Guest(0,"Alisun","Mount","amountc@ehow.com","(312) 3713380",State.IL);
        Result<Guest> actual = service.add(guest);
        assertTrue(actual.isSuccess());
    }

    // UPDATE
    @Test
    void shouldNotUpdateNull() throws DataException {
        Result<Guest> actual = service.update(null);
        assertFalse(actual.isSuccess());
        assertTrue(actual.getErrorMessages().get(0).toLowerCase().contains("null"));
    }

    @Test
    void shouldNotUpdateEmptyFirstname() throws DataException {
        Guest guest = new Guest(1,"","Lomas","slomas0@mediafire.com","(702) 7768761",State.NV);
        Result<Guest> actual = service.update(guest);
        assertFalse(actual.isSuccess());
        assertTrue(actual.getErrorMessages().get(0).toLowerCase().contains("first name"));
    }

    @Test
    void shouldNotUpdateEmptyLastname() throws DataException {
        Guest guest = new Guest(1,"Sullivan","","slomas0@mediafire.com","(702) 7768761",State.NV);
        Result<Guest> actual = service.update(guest);
        assertFalse(actual.isSuccess());
        assertTrue(actual.getErrorMessages().get(0).toLowerCase().contains("last name"));
    }

    @Test
    void shouldNotUpdateEmptyEmail() throws DataException {
        Guest guest = new Guest(1,"Sullivan","Lomas","","(702) 7768761",State.NV);
        Result<Guest> actual = service.update(guest);
        assertFalse(actual.isSuccess());
        assertTrue(actual.getErrorMessages().get(0).toLowerCase().contains("email"));
    }

    @Test
    void shouldNotUpdateEmptyPhoneNumber() throws DataException {
        Guest guest = new Guest(1,"Sullivan","Lomas","slomas0@mediafire.com","",State.NV);
        Result<Guest> actual = service.update(guest);
        assertFalse(actual.isSuccess());
        assertTrue(actual.getErrorMessages().get(0).toLowerCase().contains("phone number"));
    }

    @Test
    void shouldNotUpdateEmptyState() throws DataException {
        Guest guest = new Guest(1,"Sullivan","Lomas","slomas0@mediafire.com","(702) 7768761",null);
        Result<Guest> actual = service.update(guest);
        assertFalse(actual.isSuccess());
        assertTrue(actual.getErrorMessages().get(0).toLowerCase().contains("state"));
    }

    @Test
    void shouldNotUpdateDuplicate() throws DataException {
        Guest guest = new Guest(1,"Sullivan","Lomas","ogecks1@dagondesign.com","(702) 7768761",State.IL);
        Result<Guest> actual = service.update(guest);
        assertFalse(actual.isSuccess());
        assertTrue(actual.getErrorMessages().get(0).toLowerCase().contains("exist"));
    }

    @Test
    void shouldNotUpdateMissing() throws DataException {
        Guest guest = new Guest(6,"Alisun","Mount","amountc@ehow.com","(312) 3713380",State.IL);
        Result<Guest> actual = service.update(guest);
        assertFalse(actual.isSuccess());
        assertTrue(actual.getErrorMessages().get(0).toLowerCase().contains("find"));
    }

    @Test
    void shouldUpdate() throws DataException {
        Guest guest = new Guest(1,"Sulliva","Loma","slomas0@mediafire.com","(702) 7768761",State.NV);
        Result<Guest> actual = service.update(guest);
        assertTrue(actual.isSuccess());
    }

    // DELETE
    @Test
    void shouldDelete() throws DataException {
        Guest guest = new Guest(1,"Sulliva","Loma","slomas0@mediafire.com","(702) 7768761",State.NV);
        Result<Guest> actual = service.delete(guest);
        assertTrue(actual.isSuccess());
    }

    @Test
    void shouldNotDeleteMissing() throws DataException {
        Guest guest = new Guest(6,"Alisun","Mount","amountc@ehow.com","(312) 3713380",State.IL);
        Result<Guest> actual = service.delete(guest);
        assertFalse(actual.isSuccess());
        assertTrue(actual.getErrorMessages().get(0).toLowerCase().contains("find"));
    }
}