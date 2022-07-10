package cn.hanabi.modules.modules.world.Disabler;

import cn.hanabi.Wrapper;
import cn.hanabi.events.EventMove;
import cn.hanabi.events.EventPacket;
import cn.hanabi.events.EventPreMotion;
import cn.hanabi.events.EventWorldChange;
import cn.hanabi.injection.interfaces.IC03PacketPlayer;
import cn.hanabi.injection.interfaces.INetHandlerPlayClient;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.modules.ModManager;
import cn.hanabi.utils.TimeHelper;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static cn.hanabi.Wrapper.sendPacketNoEvent;

public class Disabler extends Mod {

    private static final Minecraft mc = Minecraft.getMinecraft();
    public static final List<Packet<?>> packet = new ArrayList();
    private ArrayList<Packet> packets = new ArrayList<>();
    private static Vec3 initPos;
    public int counter = 0;
    public double posX;
    public double posY;
    public double posZ;
    private boolean cancel;
    private TimeHelper timer2 = new TimeHelper();
    private TimeHelper timer = new TimeHelper();
    private boolean active;
    public Disabler() {
        super("Disabler", Category.WORLD);
    }

    @Override
    public void onEnable() {
        counter = 0;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        counter = 0;
        super.onDisable();
    }

    @EventTarget
    public void onLoadWorld(EventWorldChange event) {
        timer.reset();
        timer2.reset();
    }

    public boolean isOnGround(double height) {
        return !mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer,
                mc.thePlayer.getEntityBoundingBox().offset(0.0D, -height, 0.0D)).isEmpty();
    }

    @EventTarget
    public void onEvent(EventPreMotion e) {
        if (this.mc.isSingleplayer()) {
            return;
        }
        if (packet.size() > 50) {
            while (!packet.isEmpty()) {
                sendPacketNoEvent(packet.remove(0));
            }
        }
    }

    @EventTarget
    public void onMove(EventMove event) {
        if(!mc.isSingleplayer()) {
            if (timer.hasTimeElapsed(10000, true)) {
                cancel = true;
                timer2.reset();
            }
        }
    }

    @EventTarget
    public void onPacket(EventPacket event) {
        final Packet p =  event.getPacket();
        if (this.mc.isSingleplayer()) {
            return;
        }
        doTimerDisabler(event);
        if (event.getPacket() instanceof C03PacketPlayer.C05PacketPlayerLook &&mc.thePlayer.isRiding()){
            sendPacketNoEvent(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
        } else if (event.getPacket() instanceof C0CPacketInput && mc.thePlayer.isRiding()){
            sendPacketNoEvent(event.getPacket());
            sendPacketNoEvent(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
            event.setCancelled(true);
        }
        if(p instanceof C03PacketPlayer) {
            final C03PacketPlayer c03 = (C03PacketPlayer) p;
            if(mc.thePlayer.ticksExisted == 1) {
                initPos = new Vec3(c03.getPositionX() + getRandom(-100000, 100000), c03.getPositionY() + getRandom(-100000, 100000), c03.getPositionZ() + getRandom(-100000, 100000));
            } else if(((INetHandlerPlayClient)mc.thePlayer.sendQueue).getdoneLoadingTerrain() && initPos != null && mc.thePlayer.ticksExisted < 100) {
                ((IC03PacketPlayer)c03).setPosX(initPos.xCoord);
                ((IC03PacketPlayer)c03).setPosY(initPos.yCoord);
                ((IC03PacketPlayer)c03).setPosZ(initPos.zCoord);
            }
        }
        if (p instanceof S08PacketPlayerPosLook) {
            S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook) event.getPacket();
            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(packet.getX(), packet.getY(), packet.getZ(), false));
            mc.thePlayer.motionX = mc.thePlayer.motionY = mc.thePlayer.motionZ = 0;
            mc.thePlayer.setPosition(packet.getX(), packet.getY(), packet.getZ());
            mc.thePlayer.prevPosX = mc.thePlayer.posX;
            mc.thePlayer.prevPosY = mc.thePlayer.posY;
            mc.thePlayer.prevPosZ = mc.thePlayer.posZ;
            mc.displayGuiScreen(null);
            event.setCancelled(true);
        }
        if (p instanceof C0BPacketEntityAction) {
            event.setCancelled(true);
        }
        if (((INetHandlerPlayClient)mc.getNetHandler()).getdoneLoadingTerrain()) {
            if (!event.isCancelled() && (event.getPacket() instanceof C03PacketPlayer ||event.getPacket() instanceof C0FPacketConfirmTransaction || event.getPacket() instanceof C00PacketKeepAlive)) {
                event.setCancelled(true);
                packets.add(event.getPacket());
            }
        }
    }

    public TimeHelper getTimer(){
        return this.timer;
    }

    public boolean isActive() {
        return active;
    }

    public static double getRandom(double min, double max) {
        if (min == max) {
            return min;
        } else if (min > max) {
            final double d = min;
            min = max;
            max = d;
        }
        return ThreadLocalRandom.current().nextDouble(min, max);
    }

    //Bypass Timer
    private void doTimerDisabler(EventPacket e) {
        if (e.getPacket() instanceof C03PacketPlayer) {
            C03PacketPlayer c03PacketPlayer = (C03PacketPlayer) e.getPacket();
            if (!c03PacketPlayer.isMoving() && !mc.thePlayer.isUsingItem()) {
                e.setCancelled(true);
            }
            if (cancel) {
                if (!timer2.hasTimeElapsed(600, false)) {
                    if (!ModManager.getModule("Scaffold").isEnabled()) {
                        e.setCancelled(true);
                        packets.add(e.getPacket());
                    }
                } else {
                    packets.forEach(Wrapper::sendPacketNoEvent);
                    packets.clear();
                    cancel = false;
                }
            }
        }
    }
}
