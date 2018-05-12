package liteshell.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javafx.util.Pair;
import liteshell.scopes.ScopeImpl;
import liteshell.utils.ShellClient;
import liteshell.utils.StringUtils;

/**
 * Parser will contain only default methods to help with parsing common parts
 *
 * @author xvraniak@stuba.sk
 */

public interface Parser {

  String BLANK_SPACE = " ";
  String COMMENT = "#";
  String PIPE = "\\|";
  String INCLUDE = "include";
  ShellClient shellClient = ShellClient.getInstance();
  Map<String, ScopeImpl> listOfScopes = new HashMap<>();
  List<String> orderOfScopes = new ArrayList<>();

  default List<Pair<String, String>> handleInputParam(String parameters) {
    if (parameters.isEmpty()) {
      return new ArrayList<>();
    }
    String[] splittedParams = StringUtils.removeEmptyStrings(parameters.split(","));
    List<Pair<String, String>> in = new ArrayList<>();
    for (String s : splittedParams) {
      String[] split = s.split(BLANK_SPACE);
      in.add(new Pair<>(split[0], split[1]));
    }
    return in;
  }

  default boolean isFunctionCall(String call) {
    boolean isFunction = false;
    List<String> collect = listOfScopes.keySet().stream().filter(str -> call.contains(str + "("))
        .collect(Collectors.toList());
    if (!collect.isEmpty()) {
      isFunction = true;
    }
    return isFunction;
  }

  default ScopeImpl createNewScope(String name) {
    return new ScopeImpl(name, null);
  }

  default ScopeImpl createNewScope(String name, ScopeImpl parrent) {
    return new ScopeImpl(name, parrent);
  }
}
