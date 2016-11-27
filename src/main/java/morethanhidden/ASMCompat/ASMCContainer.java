package morethanhidden.asmcompat;

import com.google.common.eventbus.EventBus;
import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.ModMetadata;

import java.util.Arrays;

public class ASMCContainer extends DummyModContainer {
    public ASMCContainer() {
        super(new ModMetadata());
        ModMetadata meta = getMetadata();
        meta.modId = "asmcompat";
        meta.name = "ASMCompat";
        meta.description = "Allows 1.10 mods to work on 1.11";
        meta.version = "0.1a";
        meta.logoFile = "logo.png";
        meta.url = "https://github.com/MoreThanHidden/ASMCompat";
        meta.authorList = Arrays.asList("MoreThanHidden");
    }

    @Override
    public boolean registerBus(EventBus bus, LoadController controller) {
        bus.register(this);
        return true;
    }

}
