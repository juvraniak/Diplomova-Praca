package loaders;

import plugins.ShellPlugin;

import java.nio.file.Path;

public class JarLoader implements Loader {
    @Override
    public ShellPlugin loadCommand(String name) {
        return null;
    }

    @Override
    public ShellPlugin loadCommand(Path name) {
        return null;
    }
}
