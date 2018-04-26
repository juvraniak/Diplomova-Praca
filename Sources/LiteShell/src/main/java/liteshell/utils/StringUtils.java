package liteshell.utils;

import java.util.Arrays;

/**
 * @author xvraniak@stuba.sk
 */

public class StringUtils {

  public static String[] removeEmptyStrings(String[] split) {
    return Arrays.asList(split).stream().filter(s -> !s.isEmpty()).toArray(String[]::new);
  }
}
