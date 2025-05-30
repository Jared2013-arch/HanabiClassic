package cn.hanabi.modules.modules.world;

import cn.hanabi.Wrapper;
import cn.hanabi.events.*;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.block.*;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.*;
import net.minecraft.util.*;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;

import java.util.*;

import static cn.hanabi.Wrapper.mc;

public class Scaffold extends Mod {
    public EnumFacing enumFacing;
    public static MovingObjectPosition objectMouseOver;
    public static List<Block> blackList = Arrays.asList(
            Blocks.air, Blocks.water, Blocks.flowing_water, Blocks.lava,
            Blocks.flowing_lava, Blocks.enchanting_table, Blocks.carpet,
            Blocks.glass_pane, Blocks.stained_glass_pane, Blocks.iron_bars,
            Blocks.snow_layer, Blocks.ice, Blocks.packed_ice, Blocks.coal_ore,
            Blocks.diamond_ore, Blocks.emerald_ore, Blocks.chest, Blocks.trapped_chest,
            Blocks.torch, Blocks.anvil, Blocks.noteblock, Blocks.jukebox, Blocks.tnt,
            Blocks.gold_ore, Blocks.lapis_ore, Blocks.lit_redstone_ore, Blocks.quartz_ore,
            Blocks.redstone_ore, Blocks.wooden_pressure_plate, Blocks.stone_pressure_plate,
            Blocks.light_weighted_pressure_plate, Blocks.heavy_weighted_pressure_plate,
            Blocks.stone_button, Blocks.wooden_button, Blocks.lever, Blocks.tallgrass,
            Blocks.tripwire, Blocks.tripwire_hook, Blocks.rail, Blocks.waterlily,
            Blocks.red_flower, Blocks.red_mushroom, Blocks.brown_mushroom, Blocks.vine,
            Blocks.trapdoor, Blocks.yellow_flower, Blocks.ladder, Blocks.furnace,
            Blocks.sand, Blocks.gravel, Blocks.ender_chest, Blocks.cactus,
            Blocks.dispenser, Blocks.dropper, Blocks.crafting_table, Blocks.web,
            Blocks.pumpkin, Blocks.sapling, Blocks.cobblestone_wall, Blocks.oak_fence,
            Blocks.redstone_torch
    );
    public static float rotationPitch;
    public static float rotationYaw;

    public Scaffold() {
        super("Scaffold", Category.PLAYER);
    }


    @EventTarget
    public void onPreMotion(EventPreMotion event) {
        mc.thePlayer.setSprinting(false);
        rotationYaw = mc.thePlayer.rotationYaw + 180;
        rotationPitch = 78.0f;
        mc.thePlayer.rotationYawHead = rotationYaw;
        mc.thePlayer.renderYawOffset = rotationYaw;
        event.setPitch(rotationPitch);
        event.setYaw(rotationYaw);
        for (int i = 0; i < 10; i++) {
            MovingObjectPosition movingObjectPosition = rayTraceBlock(getVectorForRotation(rotationPitch, rotationYaw), new Vec3(event.x, event.y + mc.thePlayer.eyeHeight, event.z));
            objectMouseOver = movingObjectPosition;
            if (movingObjectPosition != null) {
                if (movingObjectPosition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && (!mc.thePlayer.onGround || movingObjectPosition.sideHit != EnumFacing.UP)) {
                    break;
                }
            }
            rotationPitch++;
        }
    }

    @EventTarget
    public void onPostMotion(EventPostMotion event) {
        if (objectMouseOver != null && objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            if (mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem(), objectMouseOver.getBlockPos(), objectMouseOver.sideHit, objectMouseOver.hitVec))
                mc.thePlayer.swingItem();
        }
    }

    @EventTarget
    public void onPacket(EventPacket e) {

    }

    @EventTarget
    public void onSafeWalk(EventSafeWalk e) {
        e.setSafe(true);
    }

    protected final net.minecraft.util.Vec3 getVectorForRotation(float pitch, float yaw) {
        float f = MathHelper.cos(-yaw * ((float) Math.PI / 180F) - (float) Math.PI);
        float f1 = MathHelper.sin(-yaw * ((float) Math.PI / 180F) - (float) Math.PI);
        float f2 = -MathHelper.cos(-pitch * ((float) Math.PI / 180F));
        float f3 = MathHelper.sin(-pitch * ((float) Math.PI / 180F));
        return new net.minecraft.util.Vec3((double) (f1 * f2), (double) f3, (double) (f * f2));
    }

    public net.minecraft.util.MovingObjectPosition rayTraceBlock(net.minecraft.util.Vec3 vec31, net.minecraft.util.Vec3 vec_player) {
        float blockReachDistance = mc.playerController.getBlockReachDistance();
//        net.minecraft.util.Vec3 vec3 = mc.thePlayer.getPositionEyes(Wrapper.getTimer().elapsedPartialTicks);
        net.minecraft.util.Vec3 vec32 = vec_player.addVector(vec31.xCoord * blockReachDistance, vec31.yCoord * blockReachDistance, vec31.zCoord * blockReachDistance);
        return mc.theWorld.rayTraceBlocks(vec_player, vec32, false, false, true);
    }
}