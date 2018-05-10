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
  private boolean isBreak;

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
        this.addCommand("fcall " + ifName + "()");
        listOfScopes.put(ifName, newIf);
        index = newIf.preProcess();


      } else if (line.trim().startsWith("for(")) {
        String forName = "for" + index;
        ForScope newFor = new ForScope(forName, (ScopeImpl) functionScope.getParent(),
            functionScope, content,
            index);
        this.addCommand("fcall " + forName + "()");
        listOfScopes.put(forName, newFor);
        index = newFor.preProcess();


      }
    }

    return index;
  }

  @Override
  public void executeScript(String function) {
    CommandIO out;
    String fName;
    if (evalueateCondition()) {
      for (int j = 0; j < stack.size(); j++) {
        String command = stack.get(j);
        if (command.startsWith("$(break)")) {
          isBreak = true;
          break;
        } else if (command.startsWith("$(continue)")) {

        } else if (command.startsWith("$(")) {
          out = executor.execute(command, this);
          if (out.getReturnCode() == 0) {
            out.getCommandOutput()
                .ifPresent(
                    stringStream -> System.out.println(stringStream.reduce(String::concat).get()));
          } else {
            out.getCommandErrorOutput().ifPresent(System.out::println);
          }
        } else if (command.startsWith("fcall ")) {
          String[] split = command.split(" = ");
          if (split.length == 1) {
            //fcall test()
            fName = command.substring("fcall ".length());
            if (fName.startsWith("for")) {

              fName = fName.substring(0, fName.indexOf("("));
              ForScope forScope = (ForScope) ((ScopeImpl) parent).getFunctions().get(fName);
              forScope.executeScript(fName);
            } else if (fName.startsWith("if")) {
              fName = command.substring("fcall ".length());
              fName = fName.substring(0, fName.indexOf("("));
              IfScope ifScope = (IfScope) ((ScopeImpl) parent).getFunctions().get(fName);
              ifScope.executeScript(fName);
            } else {
              executeScript(fName);
            }
          } else {
            //fcall int a = test()
            System.out.println("tu");
          }

        }
      }
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
