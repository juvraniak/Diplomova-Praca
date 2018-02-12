package plugins;

public class PluginMeta {
    private String version;
    private String name;
    private String packagePath;

    public PluginMeta() {
    }

    public PluginMeta(String version, String name, String packagePath) {
        this.version = version;
        this.name = name;
        this.packagePath = packagePath;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackagePath() {
        return packagePath;
    }

    public void setPackagePath(String packagePath) {
        this.packagePath = packagePath;
    }
}
