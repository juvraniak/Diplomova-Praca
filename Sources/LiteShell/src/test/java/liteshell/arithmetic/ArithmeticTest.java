package liteshell.arithmetic;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.BinaryOperator;
import java.util.stream.Stream;
import org.junit.Test;

/**
 * @author xvraniak@stuba.sk
 */

public class ArithmeticTest {

  @Test
  public void testScriptParser() {

    String pathToScript =
        System.getProperty("user.dir") + File.separator + "src/test/resources/arithmetic.ls";
    StringBuilder sb = new StringBuilder();
    try (Stream<String> lines = Files.lines(Paths.get(pathToScript), Charset.defaultCharset())) {
      sb.append(lines.findFirst().get());
    } catch (IOException e) {
      e.printStackTrace();
    }
    String command = sb.toString().trim();

    int result = compute(command);

    if (result == -34) {
      System.out.println("good");
    }

  }

  private int compute(String command) {
    String[] split = command.split("=");
    String left = split[0];
    String right = split[1];
    String[] multiplyPart = right.split("\\*");

    return 1;
  }

  BinaryOperator<BigDecimal> sumNumers = (a, b) -> a.add(b);
  BinaryOperator<BigDecimal> subtractNumers = (a, b) -> a.subtract(b);
  BinaryOperator<BigDecimal> multiplyNumers = (a, b) -> a.multiply(b);
  BinaryOperator<BigDecimal> divideNumers = (a, b) -> a.divide(b);


}
