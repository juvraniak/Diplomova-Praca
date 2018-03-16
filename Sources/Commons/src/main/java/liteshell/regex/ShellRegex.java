package liteshell.regex;

/**
 * @author xvraniak@stuba.sk
 */

public interface ShellRegex {

    default String types() {
        return "(void|int|double|boolean|list|map)";
    }

    default String charNums() {
        return "[a-zA-Z1-9]";
    }

    default String line() {
        return "((\t*.*[;]\n){0,1})*";
    }

    default String winPath() {
        return "([a-zA-Z]:)?(\\\\[a-zA-Z0-9_.-]+)+\\\\?";
    }

    default String unixPath() {
        return "";
    }
}
