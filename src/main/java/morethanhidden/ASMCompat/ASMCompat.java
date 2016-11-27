package morethanhidden.asmcompat;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.MCVersion;

import java.util.Map;

@MCVersion("1.11")
//TODO Find a better way to exclude base minecraft classes
@IFMLLoadingPlugin.TransformerExclusions({"morethanhidden.asmcompat", "net.minecraft", "com.mojang", "paulscode.sound", "com.sun", "oshi", "io.netty", "com.google", "joptsimple", "gnu.trove", "com.jcraft"})
public class ASMCompat implements IFMLLoadingPlugin {

    @Override
    public String[] getASMTransformerClass() {
        return new String[]{ ASMCTransformer.class.getName() };
    }

    @Override
    public String getModContainerClass() {
        return ASMCContainer.class.getName();
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {

    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
