package sk.stuba.configuration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Juraj Vraniak (xvraniak@stuba.sk)
 * created : 04/01/2018.
 */

public final class AplicationConfiguration {

    //    private final String
    static Map<String, Object> var;
    private Map<String, String> environemtValues = System.getenv();

    public static void main(String[] args) {

        System.out.println(System.getenv("PATH"));
        var = new HashMap<>();
        var.put("a", Integer.valueOf(1));
        var.put("b", Double.valueOf(2.5D));
        var.put("c", Arrays.asList(new String("abc")));
        var.put("d", new HashMap<>());

        var.forEach((k, v) -> {
            System.out.println(v.getClass());
        });

    }
}
