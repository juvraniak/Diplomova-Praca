package scopes;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ScopeData {
    private Map<String, Integer> integerMap;
    private Map<String, Double> doubleMap;
    private Map<String, List<?>> listMap;
    private Map<String, Set<?>> setMap;
    private Map<String, Map<?, ?>> mapMap;

    public ScopeData() {
    }

    public ScopeData(Map<String, Integer> integerMap, Map<String, Double> doubleMap, Map<String, List<?>> listMap, Map<String, Set<?>> setMap, Map<String, Map<?, ?>> mapMap) {
        this.integerMap = integerMap;
        this.doubleMap = doubleMap;
        this.listMap = listMap;
        this.setMap = setMap;
        this.mapMap = mapMap;
    }

    public Map<String, Integer> getIntegerMap() {
        return integerMap;
    }

    public void setIntegerMap(Map<String, Integer> integerMap) {
        this.integerMap = integerMap;
    }

    public Map<String, Double> getDoubleMap() {
        return doubleMap;
    }

    public void setDoubleMap(Map<String, Double> doubleMap) {
        this.doubleMap = doubleMap;
    }

    public Map<String, List<?>> getListMap() {
        return listMap;
    }

    public void setListMap(Map<String, List<?>> listMap) {
        this.listMap = listMap;
    }

    public Map<String, Set<?>> getSetMap() {
        return setMap;
    }

    public void setSetMap(Map<String, Set<?>> setMap) {
        this.setMap = setMap;
    }

    public Map<String, Map<?, ?>> getMapMap() {
        return mapMap;
    }

    public void setMapMap(Map<String, Map<?, ?>> mapMap) {
        this.mapMap = mapMap;
    }
}
