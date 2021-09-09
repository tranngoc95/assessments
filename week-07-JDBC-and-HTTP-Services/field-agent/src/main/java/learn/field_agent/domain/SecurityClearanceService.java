package learn.field_agent.domain;

import learn.field_agent.data.AgentJdbcTemplateRepository;
import learn.field_agent.data.SecurityClearanceRepository;
import learn.field_agent.models.SecurityClearance;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SecurityClearanceService {

    private final SecurityClearanceRepository repository;

    public SecurityClearanceService(SecurityClearanceRepository repository) {
        this.repository = repository;
    }

    public List<SecurityClearance> findAll(){
        return repository.findAll();
    }

    public SecurityClearance findById(int clearanceId){
        return repository.findById(clearanceId);
    }

    public Result<SecurityClearance> add(SecurityClearance clearance){
        Result<SecurityClearance> result = validate(clearance, true);
        if(result.isSuccess()){
            clearance = repository.add(clearance);
            result.setPayload(clearance);
        }
        return result;
    }

    public Result<SecurityClearance> update(SecurityClearance clearance){
        Result<SecurityClearance> result = validate(clearance, false);

        if(result.isSuccess() && !repository.update(clearance)){
            String msg = String.format("securityClearanceId: %s, not found", clearance.getSecurityClearanceId());
            result.addMessage(msg, ResultType.NOT_FOUND);
        }
        return result;
    }

    public Result<SecurityClearance> safeDeleteById(int clearanceId){
        Result<SecurityClearance> result = new Result<>();

        if(repository.checkReferenceKey(clearanceId)){
            result.addMessage("In-used security clearance cannot be deleted", ResultType.INVALID);
        } else if(!repository.safeDeleteById(clearanceId)){
            String msg = String.format("securityClearanceId: %s, not found", clearanceId);
            result.addMessage(msg, ResultType.NOT_FOUND);
        }
        return result;
    }

    public boolean fullDeleteById(int clearanceId){
        return repository.fullDeleteById(clearanceId);
    }

    public Result<SecurityClearance> validate(SecurityClearance clearance, boolean isAdd){
        Result<SecurityClearance> result = new Result<>();

        if (clearance == null) {
            result.addMessage("security clearance cannot be null", ResultType.INVALID);
            return result;
        }

        if(isAdd && clearance.getSecurityClearanceId()!=0){
            result.addMessage("securityClearanceId cannot be set for `add` operation", ResultType.INVALID);
        } else if((!isAdd && clearance.getSecurityClearanceId()==0)) {
            result.addMessage("securityClearanceId must be set for `update` operation", ResultType.INVALID);
        }

        if(Validations.isNullOrBlank(clearance.getName())){
            result.addMessage("name is required", ResultType.INVALID);
        }

        SecurityClearance exist = repository.findByName(clearance.getName());
        if(exist != null && (isAdd || exist.getSecurityClearanceId() != clearance.getSecurityClearanceId())) {
            result.addMessage("name cannot be duplicated", ResultType.INVALID);
        }

        return result;
    }
}
