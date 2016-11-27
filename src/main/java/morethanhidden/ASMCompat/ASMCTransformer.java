package morethanhidden.asmcompat;

import net.minecraft.launchwrapper.IClassTransformer;

public class ASMCTransformer implements IClassTransformer {

    @Override
    public byte[] transform(String name, String transformedName, byte[] target) {
        boolean isObfuscated = !name.equals(transformedName);
        System.out.println("Transforming: " + name);
        return target;
    }
}
