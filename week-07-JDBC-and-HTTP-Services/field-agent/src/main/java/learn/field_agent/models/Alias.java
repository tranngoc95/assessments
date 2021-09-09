package learn.field_agent.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Alias {
    private int aliasId;
    private String name;
    private String persona;
    private int agentId;
}
