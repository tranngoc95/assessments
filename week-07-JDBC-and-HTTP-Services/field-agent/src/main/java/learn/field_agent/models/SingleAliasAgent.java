package learn.field_agent.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SingleAliasAgent {
    private Alias alias;
    private Agent agent;
}
