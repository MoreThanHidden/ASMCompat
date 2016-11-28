package morethanhidden.asmcompat;

import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.*;
import net.minecraft.launchwrapper.IClassTransformer;

import java.util.ArrayList;
import java.util.List;

public class ASMCTransformer implements IClassTransformer {

    @Override
    public byte[] transform(String name, String transformedName, byte[] target) {
        boolean isObfuscated = !name.equals(transformedName);
        System.out.println("ASMCompat Transforming: " + name);
        List<Integer> itemstacks = new ArrayList<>();
        Boolean changes = false;

        try {
            ClassNode classNode = new ClassNode();
            ClassReader classReader = new ClassReader(target);
            classReader.accept(classNode, 0);
            for (MethodNode method : classNode.methods) {
                for (AbstractInsnNode instruction : method.instructions.toArray()) {

                    String ITEMSTACK = isObfuscated ? "" : "net/minecraft/item/ItemStack";
                    String GETCOUNT = isObfuscated ? "" : "getCount";

                    if (instruction.getOpcode() == Opcodes.ASTORE) {
                        //Store the Itemstacks in an Array
                        if(((MethodInsnNode)instruction.getPrevious()).owner.equals(ITEMSTACK)) {
                            System.out.println("Storing a Itemstack Variable: " + ((VarInsnNode) instruction).var);
                            itemstacks.add(((VarInsnNode)instruction).var);
                        }
                    }
                    if (instruction.getOpcode() == Opcodes.IFNONNULL) {
                        //If not Null ItemStack (we need to check if not air)
                        if(itemstacks.contains(((VarInsnNode)(instruction.getPrevious())).var)){
                            System.out.println("If not null Itemstack found");
                            method.instructions.insertBefore(instruction, new MethodInsnNode(Opcodes.INVOKEVIRTUAL, ITEMSTACK, GETCOUNT, "()I"));
                            method.instructions.insertBefore(instruction, new JumpInsnNode(Opcodes.IFNE, ((JumpInsnNode)instruction).label));
                            method.instructions.remove(instruction);
                            changes = true;
                        }
                    }
                    if (instruction.getOpcode() == Opcodes.IFNULL) {
                        //If Null ItemStack (we need to check if air)
                        if(itemstacks.contains(((VarInsnNode)(instruction.getPrevious())).var)){
                            System.out.println("If null Itemstack found");
                            method.instructions.insertBefore(instruction, new MethodInsnNode(Opcodes.INVOKEVIRTUAL, ITEMSTACK, GETCOUNT, "()I"));
                            method.instructions.insertBefore(instruction, new JumpInsnNode(Opcodes.IFEQ, ((JumpInsnNode)instruction).label));
                            method.instructions.remove(instruction);
                            changes = true;
                        }
                    }
                    if (instruction.getOpcode() == Opcodes.NEW) {
                        //Check for a new ItemStack (we should make sure this isn't 0)
                        if (((TypeInsnNode)instruction).desc.equals(ITEMSTACK)) {
                            System.out.println("New ItemStack Created");
                        }
                    }
                }
            }
            if(changes) {
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
