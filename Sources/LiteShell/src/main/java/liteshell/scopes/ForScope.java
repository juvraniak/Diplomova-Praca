package liteshell.scopes;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import liteshell.commands.ios.CommandIO;
import liteshell.commands.ios.DefaultCommadIO;
import liteshell.parser.Parser;
import lombok.Getter;

/**
 * @author xvraniak@stuba.sk
 */

public class ForScope extends ScopeImpl implements Parser {

  private List<String> content;
  private int index;
  @Getter
  private ScopeImpl functionScope;

  private String init;
  private int retValue;
  private String condition;
  private String increment;
  private String var;

  private boolean isBreak;


  public ForScope(String scopeName, ScopeImpl parent, ScopeImpl functionScope, List<String> content,
      int index) {
    super(scopeName, parent);
    this.content = content;
    this.index = index;
    this.functionScope = functionScope;
  }

  public int preProcess() {
    String line = content.get(index);
    index++;
    if (line.endsWith("{")) {
      line = line.substring(0, line.length() - 1);
      //TODO: ak nie dalsi riadok by mal zacinat { ale to neriesim musi to zatial takto byt
    }
    StringBuilder sb = new StringBuilder();
    String[] forStatement = line.trim().substring("for(".length(), line.trim().length() - 1)
        .split(";");
    //TODO: aby bolo podporovane pouzit uz inicializovanej premennej treba skusit ci sa nezacina na $( atd..., enhancement
    init =
        "$(" + forStatement[0].split(" = ")[0] + scopeName + " = " + forStatement[0].split(" = ")[1]
            + ")";
    String originalVar = forStatement[0].split(" = ")[0];
    var = originalVar.split(" ")[1] + scopeName;
    originalVar = originalVar.split(" ")[1];
    condition = forStatement[1].trim();
    condition = condition.replace("${" + originalVar + "}", "${" + var + "}");
    increment = forStatement[2].trim();
    increment = increment.replace("${" + originalVar + "}", "${" + var + "}");
    for (; index < content.size(); index++) {
      line = content.get(index);
      line = line
          .trim().replace("${" + originalVar + "}", "${" + var + "}");
      if (line.endsWith(";")) {
        if (line.trim().startsWith("return")) {
          line = functionScope.assignReturnValue(line, functionScope.getReturnType());
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
  public void executeScript(String function, ScopeVariables scopeVariables) {
    CommandIO out;
    String fName;
    setScopeVariables(scopeVariables);
    for (int i = getStart(); evalueateCondition(); i = evalueateIncremet()) {

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
          String callParams;
          String afterExecuteRetValue;
          if (split.length == 1) {
            //fcall test()
            fName = command.substring("fcall ".length());
            if (fName.startsWith("for")) {

              fName = fName.substring(0, fName.indexOf("("));
              ForScope forScope = (ForScope) ((ScopeImpl) parent).getFunctions().get(fName);
//              try {
              forScope.executeScript(fName, this.getScopeVariables());
//              } catch (CloneNotSupportedException e) {
//                e.printStackTrace();
//              }
            } else if (fName.startsWith("if")) {
              fName = command.substring("fcall ".length());
              fName = fName.substring(0, fName.indexOf("("));
              IfScope ifScope = (IfScope) ((ScopeImpl) parent).getFunctions().get(fName);
//              try {
              ifScope.executeScript(fName, this.getScopeVariables());
//              } catch (CloneNotSupportedException e) {
//                e.printStackTrace();
//              }
            } else {
//              try {
              executeScript(fName, this.getScopeVariables());
//              } catch (CloneNotSupportedException e) {
//                e.printStackTrace();
//              }
            }
          } else {
            //fcall int a = test()
            System.out.println("tu");
          }

        }
      }
    }
  }


  private int evalueateIncremet() {
    CommandIO commandIO = getExecutor().execute(increment, this);
    commandIO = getExecutor().execute("$(${" + var + "});", this);
    commandIO.getCommandOutput()
        .ifPresent(o -> retValue = new BigDecimal(o.reduce(String::concat).get()).intValue());
    return retValue;
  }

  private int getStart() {
    CommandIO commandIO = getExecutor().execute(init, this);
    commandIO.getCommandOutput()
        .ifPresent(o -> retValue = new BigDecimal(o.reduce(String::concat).get()).intValue());
    return retValue;
  }

  private boolean evalueateCondition() {
    if (isBreak) {
      isBreak = false;
      return false;
    } else {
      boolean isCommand = condition.startsWith("$(");
      boolean isInitializedVariable = condition.startsWith("${");

      String toExecute = isCommand ? "$(booleanPrep " + condition
          .substring(condition.indexOf("(") + 1, condition.length()) : condition;
      CommandIO out =
          isCommand || isInitializedVariable ? this.getExecutor()
              .execute(toExecute, this.getScope())
              : getCommandIO();

      Boolean booleanValue = resolveBoolean(
          out.getCommandOutput().get().reduce(String::concat).get());
      if (booleanValue != null) {
        return booleanValue;
      }
      return false;
    }
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
