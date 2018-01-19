package terminal.common.plugins;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Juraj Vraniak (JVraniak@sk.ibm.com)
 * created : 14/01/2018.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public final class PluginMeta {
    private String version;
    private String name;
    private String packagePath;
}
