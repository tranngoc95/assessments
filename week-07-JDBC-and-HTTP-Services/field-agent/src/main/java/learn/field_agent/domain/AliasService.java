package learn.field_agent.domain;

import learn.field_agent.data.AgentRepository;
import learn.field_agent.data.AliasRepository;
import learn.field_agent.models.Alias;
import learn.field_agent.models.SingleAliasAgent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AliasService {

    private final AliasRepository repository;

    private final AgentRepository agentRepository;

    public AliasService(AliasRepository repository, AgentRepository agentRepository) {
        this.repository = repository;
        this.agentRepository = agentRepository;
    }

    public List<Alias> findAll() {
        return repository.findAll();
    }

    public Alias findById(int aliasId) {
        return repository.findById(aliasId);
    }

    @Transactional
    public SingleAliasAgent findAliasAgentById(int aliasId){
        SingleAliasAgent result = new SingleAliasAgent();
        result.setAlias(findById(aliasId));
        if(result.getAlias()==null){
            return null;
        } else {
            result.setAgent(agentRepository.findById(result.getAlias().getAgentId()));
            return result;
        }
    }

    public Result<Alias> add(Alias alias) {
        Result<Alias> result = validate(alias, true);

        if(result.isSuccess()){
            alias = repository.add(alias);
            result.setPayload(alias);
        }

        return result;
    }

    public Result<Alias> update(Alias alias) {
        Result<Alias> result = validate(alias, false);

        if(result.isSuccess() && !repository.update(alias)){
            String msg = String.format("aliasId: %s, not found", alias.getAliasId());
            result.addMessage(msg, ResultType.NOT_FOUND);
        }

        return result;
    }

    public boolean deleteById(int aliasId) {
        return repository.deleteById(aliasId);
    }

    public Result<Alias> validate(Alias alias, boolean isAdd) {
        Result<Alias> result = new Result<>();

        if (alias == null) {
            result.addMessage("alias cannot be null", ResultType.INVALID);
            return result;
        }

        if (isAdd && alias.getAliasId() != 0) {
            result.addMessage("aliasId cannot be set for `add` operation", ResultType.INVALID);
        }

        if ((!isAdd && alias.getAliasId() == 0)) {
            result.addMessage("aliasId must be set for `update` operation", ResultType.INVALID);
        }

        if (Validations.isNullOrBlank(alias.getName())) {
            result.addMessage("name is required", ResultType.INVALID);
        }

        List<Alias> aliases = repository.findByName(alias.getName())
                .stream().filter(a -> a.getAliasId() != alias.getAliasId()).collect(Collectors.toList());
        if (aliases.size() > 0) {
            if (alias.getPersona() == null){
                result.addMessage("persona is required since name is duplicated", ResultType.INVALID);
            } else if(aliases.stream().anyMatch(a -> a.getPersona().equalsIgnoreCase(alias.getPersona()))) {
                result.addMessage("name and persona should not be duplicated", ResultType.INVALID);
            }
        }
        return result;
    }
}
