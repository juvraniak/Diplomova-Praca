package liteshell.receivers;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import liteshell.commands.ios.CommandIO;
import liteshell.scopes.Scope;

/**
 * @author xvraniak@stuba.sk
 */

public class ChangeDirectoryReceiver implements Receiver {

  @Override
  public CommandIO executeCommand(CommandIO out, String[] strings, Optional<Scope> scope) {


    out.setReturnCode(0);
    //TODO: maybe there will be some adjustment for WINshit
    if (!scope.isPresent()) {
      out.setReturnCode(-1);
      out.setCommandErrorOutput(Optional.of(Stream.of("Scope was not specified!")));
      return out;
    }

    //case cd without any path
    if (strings.length == 1) {
      setWorkingDirectory(System.getProperty("user.home"), scope.get());
      return out;
    }
    
    boolean isAbsolut = strings[1].startsWith("/");

    if (isAbsolut) {
      if (validatePath(strings[1])) {
        setWorkingDirectory(strings[1], scope.get());
      } else {
        out.setReturnCode(-1);
        out.setCommandErrorOutput(Optional.of(Stream.of("Wrong path")));
      }
      return out;
    }
    String[] splits = strings[1].split(File.separator);
    List<String> currentPath = Arrays
        .asList(scope.get().getCurrentWorkingDirectory().split(File.separator)).stream()
        .filter(i -> !i.isEmpty()).collect(Collectors.toList());
    for (String s : splits) {
      if (s.equals("..")) {
        currentPath.remove(currentPath.size() - 1);
      } else {
        currentPath.add(s);
      }
    }
    String path = listToPath(currentPath);
    if (validatePath(path)) {
      setWorkingDirectory(path, scope.get());
    }
    return out;
  }

  private String listToPath(List<String> currentPath) {
    String path = "";
    for (String s : currentPath) {
      path += File.separator + s.replace("\\", "");
    }
    return path;
  }

  private boolean validatePath(String path) {
    File f = new File(path);
    if (f.exists() && f.isDirectory()) {
      return true;
    }
    return false;
  }

  private void setWorkingDirectory(String path, Scope scope) {
    scope.getScopeVariables().getStringMap().put("pwd", path);
  }
}
