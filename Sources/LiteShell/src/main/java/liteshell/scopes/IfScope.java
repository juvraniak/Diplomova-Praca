package liteshell.scopes;

import java.util.List;
import java.util.Optional;
import liteshell.commands.ios.CommandIO;
import liteshell.commands.ios.DefaultCommadIO;
import liteshell.parser.Parser;
import lombok.Getter;

/**
 * @author xvraniak@stuba.sk
 */

public class IfScope extends ScopeImpl implements Parser {

  private List<String> content;
  private int index;
  @Getter
  private ScopeImpl functionScope;

  private String condition;

  public IfScope(String scopeName, ScopeImpl parent, ScopeImpl functionScope, List<String> content,
      int index) {
    super(scopeName, parent);
    this.content = content;
    this.index = index;
    this.functionScope = functionScope;
  }
  //budem si vytvarat premenne na urcovanie booleanu v ife
  public int preProcess() {
    String line = content.get(index);
    index++;
    if (line.endsWith("{")) {
      line = line.substring(0, line.length() - 1);
      //TODO: ak nie dalsi riadok by mal zacinat { ale to neriesim musi to zatial takto byt
    }
    StringBuilder sb = new StringBuilder();
    condition = line.trim().substring("if(".length(), line.trim().length() - 1);
    for (; index < content.size(); index++) {
      line = content.get(index);
      if (line.endsWith(";")) {
        if (line.trim().startsWith("return")) {
          line = functionScope.assignReturnValue(line, getFunctionScope().getReturnType());
        }
        sb.append(line.trim());
        sb.deleteCharAt(sb.length() - 1);
        if (isFunctionCall(sb.toString())) {
          this.addCommand("fcall " + sb.toString().trim());
        } else {
          this.addCommand("$(" + sb.toString().trim() + ");");
        }
        sb.delete(0, sb.toString().length());
      } else if (line.endsWith("\\")) {
        sb.append(line.substring(0, line.length() - 1));

      } else if (line.trim().endsWith("}")) {
        if (line.trim().length() != 1) {
          //TODO: handle the command before }
          //or maybe it will be prohibited and throw exception...
        }
//        currentLine = i + 1;
        break;
      } else if (line.trim().startsWith("if(")) {
        String ifName = "if" + index;
        IfScope newIf = new IfScope(ifName, (ScopeImpl) functionScope.getParent(), functionScope,
            content,
            index);
        index = newIf.preProcess();
        listOfScopes.put(ifName, newIf);
        functionScope.addCommand("fcall " + ifName + "()");
      } else if (line.trim().startsWith("for(")) {
        String forName = "for" + index;
        ForScope newFor = new ForScope(forName, (ScopeImpl) functionScope.getParent(),
            functionScope, content,
            index);
        index = newFor.preProcess();
        listOfScopes.put(forName, newFor);
        functionScope.addCommand("fcall " + newFor + "()");
      }
    }

    return index;
  }

  @Override
  public void executeScript(String function) {
    if (evalueateCondition()) {
      super.executeScript(function);
    }
  }

  private boolean evalueateCondition() {
    boolean isCommand = condition.startsWith("$(");
    boolean isInitializedVariable = condition.startsWith("${");

    String toExecute = isCommand ? "$(booleanPrep " + condition
        .substring(condition.indexOf("(") + 1, condition.length()) : condition;
    CommandIO out =
        isCommand || isInitializedVariable ? getParent().getExecutor().execute(toExecute, this)
            : getCommandIO();

    Boolean booleanValue = resolveBoolean(
        out.getCommandOutput().get().reduce(String::concat).get());
    if (booleanValue != null) {
      return booleanValue;
    }
    return false;
  }

  private Boolean resolveBoolean(String replacement) {
    return replacement.equals("true") ? true : replacement.equals("false") ? false : null;
  }

  private CommandIO getCommandIO() {
    CommandIO temp = DefaultCommadIO.of(Optional.empty());
    temp.setCommandOutput(CommandIO.prepareIO(condition));
    return temp;


  }
}
