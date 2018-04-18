package liteshell.executors;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;
import liteshell.Client;
import liteshell.scopes.Scope;
import liteshell.scopes.ScopeImpl;

/**
 * @author xvraniak@stuba.sk
 */

public class ScriptParser {

  private static final String BLANK_SPACE = " ";
  private static final String COMMENT = "//";
  private static final String PIPE = "\\|";

  private Client client;

  public ScriptParser() {
    this.client = Client.getInstance();
  }

  public void parse(String scriptPath) {

    Scope scriptScope = new ScopeImpl(scriptPath, client);
    try (Stream<String> lines = Files.lines(Paths.get(scriptPath), Charset.defaultCharset())) {
      lines.forEachOrdered(line -> {
        //TODO: check if commadn
        process(line);
      });
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void process(String line) {
    if (line.startsWith(COMMENT)) {
      return;
    }
    //TODO: import will be handled as separate plugin - makes more sense now
    //check if line is a pipe
    String[] tokens = line.split(PIPE);


  }

}
