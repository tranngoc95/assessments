package learn.solarpanel.domain;

import learn.solarpanel.data.DataAccessException;
import learn.solarpanel.data.SolarFileRepositoriesDouble;
import learn.solarpanel.models.MaterialType;
import learn.solarpanel.models.SolarPanel;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class SolarServiceTest {
    SolarService service = new SolarService(new SolarFileRepositoriesDouble());

    // CREATE
    @Test
    void shouldAddOneFollowRules() throws DataAccessException {
        SolarResult actual = service.add(new SolarPanel(0,"Jasmine",4,3,2001,MaterialType.POLYSI,true));
        assertTrue(actual.isSuccess());
        assertEquals("Jasmine", actual.getPanel().getSection());
    }

    @Test
    void shouldNotAddWithOutSection() throws DataAccessException{
        SolarResult actual = service.add(new SolarPanel(0,null,4,3,2001,MaterialType.POLYSI,true));
        assertFalse(actual.isSuccess());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("section"));

        actual = service.add(new SolarPanel(0," ",4,3,2001,MaterialType.POLYSI,true));
        assertFalse(actual.isSuccess());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("section"));
    }

    @Test
    void shouldNotAddOutOfRangeRow() throws DataAccessException{
        SolarResult actual = service.add(new SolarPanel(0,"Jasmine",251,3,2001,MaterialType.POLYSI,true));
        assertFalse(actual.isSuccess());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("row"));

        actual = service.add(new SolarPanel(0,"Jasmine",0,3,2001,MaterialType.POLYSI,true));
        assertFalse(actual.isSuccess());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("row"));
    }

    @Test
    void shouldNotAddOutOfRangeCol() throws DataAccessException{
        SolarResult actual = service.add(new SolarPanel(0,"Lily",3,251,2001,MaterialType.POLYSI,true));
        assertFalse(actual.isSuccess());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("column"));

        actual = service.add(new SolarPanel(0,"Lily",3,0,2001,MaterialType.POLYSI,true));
        assertFalse(actual.isSuccess());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("column"));
    }

    @Test
    void shouldNotAddFutureYear() throws DataAccessException{
        SolarResult actual = service.add(new SolarPanel(0,"Lily",3,3,2022,MaterialType.POLYSI,true));
        assertFalse(actual.isSuccess());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("year"));
    }

    @Test
    void shouldNotAddMissingMaterial() throws DataAccessException{
        SolarResult actual = service.add(new SolarPanel(0,"Lily",3,3,2002,null,true));
        assertFalse(actual.isSuccess());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("material"));
    }

    @Test
    void shouldNotAddMissingTracking() throws DataAccessException{
        SolarResult actual = service.add(new SolarPanel(0,"Lily",3,3,2002,MaterialType.POLYSI,null));
        assertFalse(actual.isSuccess());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("tracking"));
    }

    @Test
    void shouldNotAddDuplicate() throws DataAccessException{
        SolarResult actual = service.add(new SolarPanel(0,"Rose",4,3,1999,MaterialType.CDTE,false));
        assertFalse(actual.isSuccess());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("duplicate"));
    }

    @Test
    void shouldNotAddNull() throws DataAccessException{
        SolarResult actual = service.add(null);
        assertFalse(actual.isSuccess());
        assertTrue(actual.getMessages().get(0).contains("null"));
    }

    @Test
    void shouldNotAddPresetID() throws DataAccessException{
        SolarResult actual = service.add(new SolarPanel(5,"Jasmine",4,3,2001,MaterialType.POLYSI,true));
        assertFalse(actual.isSuccess());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("id"));
    }

    // READ
    @Test
    void shouldFindAll() throws DataAccessException{
        List<SolarPanel> all = service.findAll();
        assertNotNull(all);
        assertEquals(2, all.size());
    }

    @Test
    void shouldFindBySection() throws DataAccessException{
        List<SolarPanel> panels = service.findBySection("Rose");
        assertNotNull(panels);
        assertEquals(1, panels.size());
    }

    @Test
    void shouldFindOne() throws DataAccessException{
        SolarPanel panel = service.findOne("Rose",4,3);
        assertNotNull(panel);
        assertEquals(4, panel.getSolarID());
    }

    @Test
    void shouldFindByYearRange() throws DataAccessException{
        List<SolarPanel> panels = service.findByYearRange(1995,2000);
        assertNotNull(panels);
        assertEquals(1, panels.size());

    }

    @Test
    void shouldFindByMaterial() throws DataAccessException{
        List<SolarPanel> panels = service.findByMaterial(MaterialType.CDTE);
        assertNotNull(panels);
        assertEquals(1, panels.size());
    }

    // UPDATE
    @Test
    void shouldUpdateOneFollowRules() throws DataAccessException{
        SolarResult actual = service.update(new SolarPanel(4,"Rose",4,3,2001,MaterialType.POLYSI,false));
        assertTrue(actual.isSuccess());
        assertEquals("Rose", actual.getPanel().getSection());
    }

    @Test
    void shouldNotUpdateMissingSection() throws DataAccessException{
        SolarResult actual = service.update(new SolarPanel(4,null,4,3,2001,MaterialType.POLYSI,false));
        assertFalse(actual.isSuccess());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("section"));

        actual = service.update(new SolarPanel(4," ",4,3,2001,MaterialType.POLYSI,false));
        assertFalse(actual.isSuccess());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("section"));
    }

    @Test
    void shouldNotUpdateOutOfRangeRow() throws DataAccessException{
        SolarResult actual = service.update(new SolarPanel(4,"Rose",0,3,2001,MaterialType.POLYSI,false));
        assertFalse(actual.isSuccess());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("row"));

        actual = service.update(new SolarPanel(4,"Rose",254,3,2001,MaterialType.POLYSI,false));
        assertFalse(actual.isSuccess());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("row"));
    }

    @Test
    void shouldNotUpdateOutOfRangeColumn() throws DataAccessException{
        SolarResult actual = service.update(new SolarPanel(4,"Rose",3,0,2001,MaterialType.POLYSI,false));
        assertFalse(actual.isSuccess());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("column"));

        actual = service.update(new SolarPanel(4,"Rose",4,253,2001,MaterialType.POLYSI,false));
        assertFalse(actual.isSuccess());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("column"));
    }

    @Test
    void shouldNotUpdateFutureYear() throws DataAccessException{
        SolarResult actual = service.update(new SolarPanel(4,"Rose",3,3,2031,MaterialType.POLYSI,false));
        assertFalse(actual.isSuccess());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("year"));
    }

    @Test
    void shouldNotUpdateMissingMaterial() throws DataAccessException{
        SolarResult actual = service.update(new SolarPanel(4,"Rose",3,3,2001,null,false));
        assertFalse(actual.isSuccess());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("material"));
    }

    @Test
    void shouldNotUpdateMissingTracking() throws DataAccessException{
        SolarResult actual = service.update(new SolarPanel(4,"Rose",3,3,2001,MaterialType.POLYSI,null));
        assertFalse(actual.isSuccess());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("tracking"));
    }

    @Test
    void shouldNotUpdateDuplicatePanel() throws DataAccessException{
        SolarResult actual = service.update(new SolarPanel(1,"Rose",4,3,2001,MaterialType.POLYSI,false));
        assertFalse(actual.isSuccess());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("duplicate"));
    }

    @Test
    void shouldNotUpdateNull() throws DataAccessException{
        SolarResult actual = service.update(null);
        assertFalse(actual.isSuccess());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("null"));

    }

    // DELETE
    @Test
    void shouldDeleteExisting() throws DataAccessException{
        SolarResult actual = service.delete("Rose",4,3);
        assertTrue(actual.isSuccess());
    }

    @Test
    void shouldNotDeleteMissing() throws DataAccessException{
        SolarResult actual = service.delete("Orchids", 3,3);
        assertFalse(actual.isSuccess());
        assertTrue(actual.getMessages().get(0).contains("not found"));
    }

}