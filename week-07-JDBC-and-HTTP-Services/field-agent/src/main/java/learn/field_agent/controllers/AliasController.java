package learn.field_agent.controllers;

import learn.field_agent.domain.AliasService;
import learn.field_agent.domain.Result;
import learn.field_agent.models.Alias;
import learn.field_agent.models.SingleAliasAgent;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping("/api/alias")
public class AliasController {

    private final AliasService service;

    public AliasController(AliasService service){
        this.service = service;
    }

    @GetMapping
    public List<Alias> findAll(){
        return service.findAll();
    }


    @GetMapping("/{aliasId}")
    public ResponseEntity<Alias> findById(@PathVariable int aliasId){
        Alias alias = service.findById(aliasId);
        if(alias == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(alias);
    }

    @GetMapping("/{aliasId}/agent")
    public ResponseEntity<SingleAliasAgent> findAliasAgentById(@PathVariable int aliasId){
        SingleAliasAgent aliasAgent = service.findAliasAgentById(aliasId);
        if(aliasAgent == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(aliasAgent);
    }

    @PostMapping
    public ResponseEntity<Object> add(@RequestBody Alias alias) {
        Result<Alias> result = service.add(alias);
        if(result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }
        return ErrorResponse.build(result);
    }

    @PutMapping("/{aliasId}")
    public ResponseEntity<Object> update(@PathVariable int aliasId, @RequestBody Alias alias) {
        if(aliasId!=alias.getAliasId()){
            return new ResponseEntity <>(HttpStatus.CONFLICT);
        }

        Result<Alias> result = service.update(alias);

        if(result.isSuccess()){
            return new ResponseEntity <>(HttpStatus.NO_CONTENT);
        }

        return ErrorResponse.build(result);
    }

    @DeleteMapping("/{aliasId}")
    public ResponseEntity<Alias> deleteById(@PathVariable int aliasId) {
        if (service.deleteById(aliasId)){
            return new ResponseEntity <>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity <>(HttpStatus.NOT_FOUND);
    }
}
