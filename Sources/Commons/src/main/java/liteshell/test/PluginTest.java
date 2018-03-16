package liteshell.test;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert;


/**
 * @author xvraniak@stuba.sk
 */

public interface PluginTest {


    void regexTest();


    void commandTest();

    default void assertRegex(Optional<List<String>> patterns, List<String> correctCommand, List<String> incorrectCommand) {
        if(patterns.isPresent()) {
            for (String pattern : patterns.get()) {
                Pattern p = Pattern.compile(pattern);
                correctCommand.stream().forEach(command -> Assert.assertTrue(match.apply(command, p)));
                incorrectCommand.stream().forEach(command -> Assert.assertFalse(match.apply(command, p)));
            }
        }else {
            Assert.assertTrue("List of String patterns have to be provided!",false);
        }
    }

    BiFunction<String, Pattern, Boolean> match = (str, p) -> {Matcher m = p.matcher(str);
        return m.matches();};
}
