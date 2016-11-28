package morethanhidden.testmod;

//Testmod for 1.10 itemstacks will be removed from build

import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = TestMod.MODID, version = TestMod.VERSION, acceptedMinecraftVersions = "1.10.2")
public class TestMod{
    public static final String MODID = "testmod";
    public static final String VERSION = "1.0";

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        ItemStack itemstack = new ItemStack((Block)null);

        // some example code that dosent work in 1.11
        if(itemstack == null){
            System.out.println("test1: itemstack is null");
        }else{
            System.out.println("test1: itemstack is not null");
        }

        if(itemstack != null){
            System.out.println("test2: itemstack is not null");
        }else{
            System.out.println("test2: itemstack is null");
        }

        //Working Code in 1.11
        if(itemstack.getCount() != 0){
            System.out.println("test4: itemstack is not null");
        }else{
            System.out.println("test4: itemstack is null");
        }

        if(itemstack.getCount() == 0){
            System.out.println("test5: itemstack is null");
        }else{
            System.out.println("test5: itemstack is not null");
        }

        //This is air in 1.11
        itemstack = new ItemStack(Items.APPLE, 0, 1);
        if(itemstack.getItem() == Items.APPLE){
            System.out.println("test3: itemstack is an apple");
        }else{
            System.out.println("test3: itemstack is not an apple");
        }

    }
}