package terminal.common.scopes;

import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Juraj Vraniak (xvraniak@stuba.sk)
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ScopeData {

    private Map<String, Integer> integerMap;
    private Map<String, Double> doubleMap;
    private Map<String, List<?>> listMap;
    private Map<String, Set<?>> setMap;
    private Map<String, Map<?, ?>> mapMap;

}
