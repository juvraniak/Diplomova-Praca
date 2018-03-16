package liteshell.plugins;

import java.util.List;
import java.util.Optional;

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
    private Optional<List<String>> matcher;
}
