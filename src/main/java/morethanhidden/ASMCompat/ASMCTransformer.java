package morethanhidden.asmcompat;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.ArrayList;
import java.util.List;

public class ASMCTransformer implements IClassTransformer {

    String[] exclusions = {"net.minecraft", "com.mojang", "paulscode.sound", "com.sun", "oshi", "io.netty", "com.google", "joptsimple", "gnu.trove", "com.jcraft", "org.slf4j", "javassist", "com.ibm", "Config", "$wrapper"};

    @Override
    public byte[] transform(String name, String transformedName, byte[] target) {

        //Ignore excluded classes
        for(String exclude : exclusions) {
            if (transformedName.startsWith(exclude)) {
                return target;
            }
        }

        //Obfuscated Names
        String ITEMSTACK = "net/minecraft/item/ItemStack";
        String GETCOUNT = "func_190916_E";

        //Variables
        List<Integer> itemstacks = new ArrayList<>();
        int currentline = 0;
        Boolean changes = false;

        ASMCompat.logger("Transforming: " + transformedName);

        try {
            //Class Reader
            ClassNode classNode = new ClassNode();
            ClassReader classReader = new ClassReader(target);
            classReader.accept(classNode, 0);

            //Allow 1.11 Minecraft Version (if only this ran before forge -.-)
            if(classNode.visibleAnnotations != null) {
                for (AnnotationNode attr : classNode.visibleAnnotations) {
                    Integer attrLoc = attr.values.indexOf("acceptedMinecraftVersions");
                    if (!attrLoc.equals(-1)) {
                        attr.values.set(attrLoc + 1, "[1.10,1.11]");
                        ASMCompat.logger(attr.values.toString());
                    }
                }
            }

            //Run through the methods
            for (MethodNode method : classNode.methods) {
                for (AbstractInsnNode instruction : method.instructions.toArray()) {
                    if(instruction.getClass() == LineNumberNode.class){
                        currentline = ((LineNumberNode)instruction).line;
                    }
                    if (instruction.getOpcode() == Opcodes.ASTORE) {
                        //Store the Itemstacks in an Array
                        if(((MethodInsnNode)instruction.getPrevious()).owner.equals(ITEMSTACK)) {
                            itemstacks.add(((VarInsnNode)instruction).var);
                        }
                    }
                    if (instruction.getOpcode() == Opcodes.IFNONNULL) {
                        //If not Null ItemStack (we need to check if not air)
                        if(itemstacks.contains(((VarInsnNode)(instruction.getPrevious())).var)){
                            ASMCompat.logger("If not null Itemstack found: " + transformedName + ":" + currentline);
                            method.instructions.insertBefore(instruction, new MethodInsnNode(Opcodes.INVOKEVIRTUAL, ITEMSTACK, GETCOUNT, "()I"));
                            method.instructions.insertBefore(instruction, new JumpInsnNode(Opcodes.IFNE, ((JumpInsnNode)instruction).label));
                            method.instructions.remove(instruction);
                            changes = true;
                        }
                    }
                    if (instruction.getOpcode() == Opcodes.IFNULL) {
                        //If Null ItemStack (we need to check if air)
                        if(itemstacks.contains(((VarInsnNode)(instruction.getPrevious())).var)){
                            ASMCompat.logger("If null Itemstack found: " + transformedName + ":" + currentline);
                            method.instructions.insertBefore(instruction, new MethodInsnNode(Opcodes.INVOKEVIRTUAL, ITEMSTACK, GETCOUNT, "()I"));
                            method.instructions.insertBefore(instruction, new JumpInsnNode(Opcodes.IFEQ, ((JumpInsnNode)instruction).label));
                            method.instructions.remove(instruction);
                            changes = true;
                        }
                    }
                    if (instruction.getOpcode() == Opcodes.NEW) {
                        //Check for a new ItemStack (we should make sure this isn't 0)
                        if (((TypeInsnNode)instruction).desc.equals(ITEMSTACK)) {
                            ASMCompat.logger("New ItemStack Created: " + transformedName + ":" + currentline);
                        }
                    }
                }
            }

            if(changes) {
                //Class Writer
                ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
                classNode.accept(classWriter);
                target = classWriter.toByteArray();
            }

        }catch(Exception e) {
            e.printStackTrace();
        }

        return target;
    }
}
