package liteshell.keywords;

/**
 * @author xvraniak@stuba.sk
 */
//not sure if this will be ever needed.
public enum Keyword {
    IF("if"),
    ELSE("else"),
    INT("int"),
    DOUBLE("double"),
    LIST("list"),
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
