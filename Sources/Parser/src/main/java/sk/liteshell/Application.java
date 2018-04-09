package  sk.liteshell;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Stream;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

public class Application {

    public static void main(String[] args) throws IOException {
        MustacheFactory mf = new DefaultMustacheFactory();
//        Mustache m = mf.compile(System.getProperty("user.dir")+ File.separator+"src/main/resources/templates/input.ls");

        String fileName = System.getProperty("user.dir")+ File.separator+"src/main/resources/templates/input.ls";

        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {

            Stream<String> stringStream = Stream.<String>builder().add("a").build();
            Arrays.stream(, 1,2);

            stream.forEach(System.out::println);

        } catch (IOException e) {
            e.printStackTrace();
        }


        HashMap<String, Object> scopes = new HashMap<String, Object>();
        scopes.put("name", "Mustache");
        scopes.put("feature", "Perfect!");

        Writer writer = new OutputStreamWriter(System.out);
         mf = new DefaultMustacheFactory();
        Mustache mustache = mf.compile(new StringReader("{{name}}"), "name");
        mustache.execute(writer, scopes);
        writer.flush();
    }
}
