package learn.solarpanel.ui;

import learn.solarpanel.data.DataAccessException;
import learn.solarpanel.domain.SolarResult;
import learn.solarpanel.domain.SolarService;
import learn.solarpanel.models.MaterialType;
import learn.solarpanel.models.SolarPanel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/solar-farm")
public class SolarController {

    private final SolarService service;

    @Autowired
    public SolarController(SolarService service){
        this.service=service;
    }

    @GetMapping("/{section}")
    public List<SolarPanel> findPanelsBySection(@PathVariable String section) throws DataAccessException {
        return service.findBySection(section);
    }

    @GetMapping
    public List<SolarPanel> findAll() throws DataAccessException {
        return service.findAll();
    }

    @GetMapping("/{section}/{row}/{col}")
    public ResponseEntity<SolarPanel> findOne(@PathVariable String section, @PathVariable int row, @PathVariable int col) throws DataAccessException{
        SolarPanel panel = service.findOne(section, row, col);
        if(panel==null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(panel);
    }

    @GetMapping("/year/{yearMin}/{yearMax}")
    public List<SolarPanel> findByYearRange(@PathVariable int yearMin, @PathVariable int yearMax) throws DataAccessException{
        return service.findByYearRange(yearMin, yearMax);
    }

    @GetMapping("/material/{type}")
    public List<SolarPanel> findByMaterial(@PathVariable String type) throws DataAccessException{
        return service.findByMaterial(MaterialType.valueOf(type));
    }

    @PostMapping
    public ResponseEntity<SolarPanel> addPanel(@RequestBody SolarPanel panel) throws DataAccessException {
        SolarResult result = service.add(panel);
        return new ResponseEntity<>(result.getPanel(), getStatus(result, HttpStatus.OK));
    }

    @PutMapping("/{solarId}")
    public ResponseEntity<Void> updatePanel(@PathVariable int solarId, @RequestBody SolarPanel panel) throws DataAccessException {
        if(panel==null || solarId != panel.getSolarID()){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        SolarResult result = service.update(panel);
        return new ResponseEntity<>(getStatus(result, HttpStatus.NO_CONTENT));
    }

    @DeleteMapping("/{section}/{row}/{col}")
    public ResponseEntity<Void> delete(@PathVariable String section, @PathVariable int row, @PathVariable int col) throws DataAccessException {
        SolarResult result = service.delete(section,row,col);
        return new ResponseEntity<>(getStatus(result, HttpStatus.NO_CONTENT));
    }

    private HttpStatus getStatus(SolarResult result, HttpStatus statusDefault){
        if(!result.isSuccess()){
            return HttpStatus.BAD_REQUEST;
        }
        return statusDefault;
    }
}
