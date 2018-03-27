package  sk.liteshell;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.Writer;
import java.util.HashMap;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.sun.org.apache.xalan.internal.utils.FeatureManager.Feature;

public class Application {

    public static void main(String[] args) throws IOException {
        MustacheFactory mf = new DefaultMustacheFactory();
        Mustache m = mf.compile(System.getProperty("user.dir")+ File.separator+"src/main/resources/templates/function.mustache");

        m.append("function void getName (String cosi){"
            + "body"
            + "body"
            + "}");

        System.out.println("tu");

        HashMap<String, Object> scopes = new HashMap<String, Object>();
        scopes.put("name", "Mustache");
        scopes.put("feature", "Perfect!");

        Writer writer = new OutputStreamWriter(System.out);
         mf = new DefaultMustacheFactory();
        Mustache mustache = mf.compile(new StringReader("{{name}}, {{description}}!"), "example");
        mustache.execute(writer, scopes);
        writer.flush();
    }
}
