package morethanhidden.asmcompat;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

import java.util.Map;


@MCVersion("1.11")
@IFMLLoadingPlugin.TransformerExclusions({"morethanhidden.asmcompat"})
public class ASMCompat implements IFMLLoadingPlugin {
    @Override
    public String[] getASMTransformerClass() {
        return new String[]{ ASMCTransformer.class.getName() };
    }

    @Override
    public String getModContainerClass() {
        return ASMCContainer.class.getName();
    }

    public static void logger(String s) {
        LogManager.getLogger("asmcompat").log(Level.INFO, "[ASMCompat] " + s);
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
