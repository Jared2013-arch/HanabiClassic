package cn.hanabi.modules.modules.movement;

import cn.hanabi.Wrapper;
import cn.hanabi.events.*;
import cn.hanabi.injection.interfaces.IC03PacketPlayer;
import cn.hanabi.injection.interfaces.IKeyBinding;
import cn.hanabi.injection.interfaces.IRenderManager;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.utils.game.BlockUtils;
import cn.hanabi.utils.game.MoveUtils;
import cn.hanabi.utils.pathfinder.PathUtils;
import cn.hanabi.utils.render.RenderUtil;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_BLEND;

public class Teleport extends Mod {
    private boolean hadGround;
    private double fixedY;
    private final List<Packet<?>> packets = new ArrayList<>();
    private boolean disableLogger = false;
    private boolean zitter = false;
    private boolean doTeleport = false;
    private boolean freeze = false;

    private int delay;
    private BlockPos endPos;
    private MovingObjectPosition objectPosition;

    public Teleport() {
        super("Teleport", Category.MOVEMENT);
    }

    @Override
    public void onDisable() {
        fixedY = 0D;
        delay = 0;
        Wrapper.getTimer().timerSpeed = 1F;
        endPos = null;
        hadGround = false;
        freeze = false;
        disableLogger = false;

        packets.clear();

        super.onDisable();
    }


    @EventTarget
    public void onUpdate(EventPreMotion event) {
        final int buttonIndex = Arrays.asList(true).indexOf("Right");
        if (mc.currentScreen == null && delay <= 0) {
            endPos = objectPosition.getBlockPos();

            if (BlockUtils.getBlock(endPos).getMaterial() == Material.air) {
                endPos = null;
                return;
            }
            delay = 6;
        }

        if (delay > 0)
            --delay;
    }

    @EventTarget
    public void onRender3D(EventRender event) {

        final Vec3 lookVec = new Vec3(mc.thePlayer.getLookVec().xCoord * 300, mc.thePlayer.getLookVec().yCoord * 300, mc.thePlayer.getLookVec().zCoord * 300);
        final Vec3 posVec = new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + 1.62, mc.thePlayer.posZ);

        objectPosition = mc.thePlayer.worldObj.rayTraceBlocks(posVec, posVec.add(lookVec), false, true, false);

        if (objectPosition == null || objectPosition.getBlockPos() == null)
            return;

        final BlockPos belowBlockPos = new BlockPos(objectPosition.getBlockPos().getX(), objectPosition.getBlockPos().getY() - 1, objectPosition.getBlockPos().getZ());

        fixedY = BlockUtils.getBlock(objectPosition.getBlockPos()) instanceof BlockFence ? (mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(objectPosition.getBlockPos().getX() + 0.5D - mc.thePlayer.posX, objectPosition.getBlockPos().getY() + 1.5D - mc.thePlayer.posY, objectPosition.getBlockPos().getZ() + 0.5D - mc.thePlayer.posZ)).isEmpty() ? 0.5D : 0D) : BlockUtils.getBlock(belowBlockPos) instanceof BlockFence ? (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(objectPosition.getBlockPos().getX() + 0.5D - mc.thePlayer.posX, objectPosition.getBlockPos().getY() + 0.5D - mc.thePlayer.posY, objectPosition.getBlockPos().getZ() + 0.5D - mc.thePlayer.posZ)).isEmpty() || BlockUtils.getBlock(objectPosition.getBlockPos()).getCollisionBoundingBox(mc.theWorld, objectPosition.getBlockPos(), BlockUtils.getBlock(objectPosition.getBlockPos()).getDefaultState()) == null ? 0D : 0.5D - BlockUtils.getBlock(objectPosition.getBlockPos()).getBlockBoundsMaxY()) : BlockUtils.getBlock(objectPosition.getBlockPos()) instanceof BlockSnow ? BlockUtils.getBlock(objectPosition.getBlockPos()).getBlockBoundsMaxY() - 0.125D : 0D;

        final int x = objectPosition.getBlockPos().getX();
        final double y = (BlockUtils.getBlock(objectPosition.getBlockPos()).getCollisionBoundingBox(mc.theWorld, objectPosition.getBlockPos(), BlockUtils.getBlock(objectPosition.getBlockPos()).getDefaultState()) == null ? objectPosition.getBlockPos().getY() + BlockUtils.getBlock(objectPosition.getBlockPos()).getBlockBoundsMaxY() : BlockUtils.getBlock(objectPosition.getBlockPos()).getCollisionBoundingBox(mc.theWorld, objectPosition.getBlockPos(), BlockUtils.getBlock(objectPosition.getBlockPos()).getDefaultState()).maxY) - 1D + fixedY;
        final int z = objectPosition.getBlockPos().getZ();

        if(!(BlockUtils.getBlock(objectPosition.getBlockPos()) instanceof BlockAir)) {
            final RenderManager renderManager = mc.getRenderManager();

            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            glEnable(GL_BLEND);
            glLineWidth(2F);
            glDisable(GL_TEXTURE_2D);
            glDisable(GL_DEPTH_TEST);
            glDepthMask(false);
            RenderUtil.drawFilledBox(new AxisAlignedBB(x - ((IRenderManager) renderManager).getRenderPosX(), (y + 1) - ((IRenderManager) renderManager).getRenderPosY(), z - ((IRenderManager) renderManager).getRenderPosZ(), x - ((IRenderManager) renderManager).getRenderPosX() + 1.0D, y + 1.2D - ((IRenderManager) renderManager).getRenderPosY(), z - ((IRenderManager) renderManager).getRenderPosZ() + 1.0D));
            glEnable(GL_TEXTURE_2D);
            glEnable(GL_DEPTH_TEST);
            glDepthMask(true);
            glDisable(GL_BLEND);

            RenderUtil.renderNameTag(Math.round(mc.thePlayer.getDistance(x + 0.5D, y + 1D, z + 0.5D)) + "m", x + 0.5, y + 1.7, z + 0.5);
            GlStateManager.resetColor();
        }
    }

    @EventTarget
    public void onPacket(EventPacket event) {
        final Packet<?> packet = event.getPacket();

        if (packet instanceof C03PacketPlayer) {
            final C03PacketPlayer packetPlayer = (C03PacketPlayer) packet;
            if (endPos == null)
                return;

            ((IC03PacketPlayer)packetPlayer).setPosX(endPos.getX() + 0.5D);
            ((IC03PacketPlayer)packetPlayer).setPosY(endPos.getY() + 1);
            ((IC03PacketPlayer)packetPlayer).setPosZ(endPos.getZ() + 0.5D);
            mc.thePlayer.setPosition(endPos.getX() + 0.5D, endPos.getY() + 1, endPos.getZ() + 0.5D);
        }
    }
}
