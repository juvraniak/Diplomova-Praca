package liteshell.scopes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import liteshell.keywords.Keyword;
import lombok.Getter;
import lombok.Setter;

/**
 * @author xvraniak@stuba.sk
 */

@Getter
@Setter
public class ScopeVariables {

    private Map<String, Keyword> initializedVariables;
    private Map<String, Integer> integerMap;
    private Map<String, Double> doubleMap;
    private Map<String, Boolean> booleanMap;
    private Map<String, String> stringMap;
    private Map<String, List<?>> listMap;
    private Map<String, Set<?>> setMap;
    private Map<String, Map<?, ?>> mapMap;

    public ScopeVariables() {
        this.initializedVariables = new HashMap<>();
        this.integerMap = new HashMap<>();
        this.doubleMap = new HashMap<>();
        this.booleanMap = new HashMap<>();
        this.stringMap = new HashMap<>();
        this.listMap = new HashMap<>();
        this.setMap = new HashMap<>();
        this.mapMap = new HashMap<>();
    }
}
