package liteshell.keywords;

/**
 * @author xvraniak@stuba.sk
 */

public enum Keyword {
  IF("if"),
  ELSE("else"),
  INT("int"),
  DOUBLE("double"),
  LIST("list"),
  STRING("string"),
  MAP("map"),
  VOID("void"),
  FOR("for");

  private final String keyword;

  private Keyword(String keyword) {
    this.keyword = keyword;
  }

  public String getKeyword(Keyword keyword) {
    return keyword.keyword;
  }
}
