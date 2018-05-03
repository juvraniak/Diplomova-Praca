package liteshell.loaders;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.Optional;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import liteshell.plugins.ShellPlugin;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xvraniak@stuba.sk
 */
@Slf4j
public class JarLoader {


    private JarLoader() {
    }
    public static Optional<ShellPlugin> loadCommand(Path jarPath) {
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
                    if (obj instanceof ShellPlugin) {
                        return Optional.of((ShellPlugin) obj);
                    }
                }
            }
        } catch (IOException | ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            log.debug(e.getMessage());
        }
        return Optional.empty();
    }

    private static URL[] getJarUrls(String jarPath) {
        try {
            return new URL[]{new URL("jar:file:" + jarPath + "!/")};
        } catch (MalformedURLException e) {
            log.debug(e.getMessage());
        }
        return null;
    }
}
