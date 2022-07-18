package cn.hanabi.modules.modules.movement;

import cn.hanabi.Wrapper;
import cn.hanabi.events.EventMove;
import cn.hanabi.events.EventPacket;
import cn.hanabi.events.EventUpdate;
import cn.hanabi.injection.interfaces.IC03PacketPlayer;
import cn.hanabi.injection.interfaces.IKeyBinding;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.utils.game.BlockUtils;
import cn.hanabi.utils.game.MoveUtils;
import cn.hanabi.utils.pathfinder.PathUtils;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.block.material.Material;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    public void onUpdate(EventUpdate event) {
        final int buttonIndex = Arrays.asList("Right").indexOf(true);
        if (mc.currentScreen == null && Mouse.isButtonDown(buttonIndex) && delay <= 0) {
            endPos = objectPosition.getBlockPos();


            if (BlockUtils.getBlock(endPos).getMaterial() == Material.air) {
                endPos = null;
                return;
            }

            delay = 6;
        }

        if (delay > 0)
            --delay;

        if (endPos != null) {
            final double endX = (double) endPos.getX() + 0.5D;
            final double endY = endPos.getY() + 1 + fixedY;
            final double endZ = (double) endPos.getZ() + 0.5D;

        }
    }

    @EventTarget
    public void onPacket(EventPacket event) {
        final Packet<?> packet = event.getPacket();

        if (disableLogger)
            return;

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
