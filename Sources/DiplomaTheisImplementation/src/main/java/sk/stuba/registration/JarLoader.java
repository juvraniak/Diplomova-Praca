package sk.stuba.registration;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import terminal.common.plugins.ShellPluging;


/**
 * @author Juraj Vraniak (xvraniak@stuba.sk)
 */
public class JarLoader {

    protected static List<ShellPluging> shellCommands = new ArrayList<>();

    private JarLoader() {
    }

    //TODO : spatne na skazu refaktorovat co najskor!!!
    public static void loadJar(Path jarPath) {
        JarEntry jarEntry;
        URL[] urls = getJarUrls(jarPath.toString());

        try {
            URLClassLoader cl = URLClassLoader.newInstance(urls);
            JarInputStream jarFile = new JarInputStream(new FileInputStream(jarPath.toFile()));
            while (true) {
                jarEntry = jarFile.getNextJarEntry();
                if (jarEntry == null) {
                    break;
                }
                if (jarEntry.getName().endsWith(".class")) {
                    String className = jarEntry.getName().substring(0, jarEntry.getName().length() - 6);
                    className = className.replace('/', '.');
                    Class c = cl.loadClass(className);
                    Object obj = c.newInstance();
                    if (obj instanceof ShellPluging) {
                        shellCommands.add((ShellPluging) obj);
                        break;
                    }
                }
            }
        } catch (IOException | ClassNotFoundException | IllegalAccessException | InstantiationException e) {
//            log.debug(e.getMessage());
        }
    }

    private static URL[] getJarUrls(String jarPath) {
        try {
            return new URL[]{new URL("jar:file:" + jarPath + "!/")};
        } catch (MalformedURLException e) {
//            log.debug(e.getMessage());
        }
        return null;
    }
}
