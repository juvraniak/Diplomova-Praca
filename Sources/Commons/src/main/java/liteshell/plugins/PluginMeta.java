package liteshell.plugins;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author xvraniak@stuba.sk
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PluginMeta {

    private String version;
    private String name;
}
