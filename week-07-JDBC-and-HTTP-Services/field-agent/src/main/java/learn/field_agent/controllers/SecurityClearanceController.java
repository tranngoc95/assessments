package learn.field_agent.controllers;

import learn.field_agent.domain.Result;
import learn.field_agent.domain.SecurityClearanceService;
import learn.field_agent.models.SecurityClearance;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping("/api/security-clearance")
public class SecurityClearanceController {

    private final SecurityClearanceService service;

    public SecurityClearanceController(SecurityClearanceService service){
        this.service=service;
    }

    @GetMapping
    public List<SecurityClearance> findAll(){
        return service.findAll();
    }

    @GetMapping("/{clearanceId}")
    public ResponseEntity<SecurityClearance> findById(@PathVariable int clearanceId){
        SecurityClearance clearance = service.findById(clearanceId);
        if(clearance==null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(clearance);
    }

    @PostMapping
    public ResponseEntity<Object> add(@RequestBody SecurityClearance clearance) {
        Result<SecurityClearance> result = service.add(clearance);
        if(result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }
        return ErrorResponse.build(result);
    }

    @PutMapping("/{clearanceId}")
    public ResponseEntity<Object> update(@PathVariable int clearanceId, @RequestBody SecurityClearance clearance) {
        if(clearanceId != clearance.getSecurityClearanceId()) {
            return new ResponseEntity <>(HttpStatus.CONFLICT);
        }

        Result<SecurityClearance> result = service.update(clearance);

        if(result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return ErrorResponse.build(result);
    }

    @DeleteMapping("/{clearanceId}")
    public ResponseEntity<Object> safeDeleteById(@PathVariable int clearanceId) {
        Result<SecurityClearance> result = service.safeDeleteById(clearanceId);
        if(result.isSuccess()) {
            return new ResponseEntity <>(HttpStatus.NO_CONTENT);
        }
        return ErrorResponse.build(result);
    }

    @DeleteMapping("/{clearanceId}/full")
    public ResponseEntity<Void> fullDeleteById(@PathVariable int clearanceId) {
        if(service.fullDeleteById(clearanceId)){
            return new ResponseEntity <>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity <>(HttpStatus.NOT_FOUND);
    }
}
