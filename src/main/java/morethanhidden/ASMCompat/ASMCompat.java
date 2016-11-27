package morethanhidden.asmcompat;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import java.util.Map;

@IFMLLoadingPlugin.MCVersion("1.11")
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
