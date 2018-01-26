package loaders;


import plugins.ShellPlugin;

import java.nio.file.Path;

public interface Loader {
    ShellPlugin loadCommand(String name);
    ShellPlugin loadCommand(Path name);
}
