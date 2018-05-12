package liteshell.plugins;

import java.util.Map;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author xvraniak@stuba.sk
 */

public class PluginFactoryTest {

  private PluginFactory pluginFactory;

  @Before
  public void loadFactory() {
    pluginFactory = PluginFactory.get();
  }

  @Test
  public void changePluginTest() {
//    Map<String, ShellPlugin> shellPlugins = pluginFactory.getShellPlugins();
//    ShellPlugin copyPlugin = shellPlugins.get("copy");
//    Assert.assertTrue(copyPlugin.getInfo().getVersion().equals("2.0"));
//    pluginFactory.changePlugin("copy 1.0");
//    copyPlugin = shellPlugins.get("copy");
//    Assert.assertTrue(copyPlugin.getInfo().getVersion().equals("1.0"));
  }
}
