package liteshell.keywords;

import java.util.Arrays;

/**
 * @author xvraniak@stuba.sk
 */

public enum Keyword {
  IF("if"),
  ELSE("else"),
  INT("int"),
  DOUBLE("double"),
  BOOLEAN("boolean"),
  LIST("list"),
  STRING("string"),
  MAP("map"),
  VOID("void"),
  FOR("for"),
  UNKNOWN("unknown");

  private final String keyword;

  private Keyword(String keyword) {
    this.keyword = keyword;
  }

  public static String getKeywordString(Keyword keyword) {
    return keyword.keyword;
  }

  public static Keyword getKeywordForString(String string) {
    return Arrays.asList(values()).stream().filter(v -> v.keyword.equals(string)).findAny()
        .orElse(UNKNOWN);
  }
}
