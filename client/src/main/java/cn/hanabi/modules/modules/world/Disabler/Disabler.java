package cn.hanabi.modules.modules.world.Disabler;

import cn.hanabi.Wrapper;
import cn.hanabi.events.*;
import cn.hanabi.injection.interfaces.IC03PacketPlayer;
import cn.hanabi.injection.interfaces.INetHandlerPlayClient;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.modules.ModManager;
import cn.hanabi.utils.math.TimeHelper;
import cn.hanabi.value.Value;
import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.EventType;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.Vec3;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static cn.hanabi.Wrapper.sendPacketNoEvent;

public class Disabler extends Mod {
    //MODE
    private final Value<String> DisablerMode = new Value<String>("Disabler", "DisableMode", 0)
            .LoadValue(new String[]{"Hypixel", "VulcanCombat",});

    public static final LinkedList<Packet<INetHandlerPlayServer>> packett = new LinkedList<Packet<INetHandlerPlayServer>>();
    public static final List<Packet<?>> packet = new ArrayList();
    private ArrayList<Packet> packets = new ArrayList<>();
    private static Vec3 initPos;
    public int counter = 0;
    private double currentTrans = 0;
    private double vulTickCounterUID = 0;
    private boolean cancel;
    private TimeHelper timer2 = new TimeHelper();
    private TimeHelper timer1 = new TimeHelper();
    private TimeHelper lagTimer = new TimeHelper();
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
        timer1.reset();
        timer2.reset();
        currentTrans = 0;
        packett.clear();
        lagTimer.reset();
        vulTickCounterUID = -25767;
    }

    public boolean isOnGround(double height) {
        return !mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer,
                mc.thePlayer.getEntityBoundingBox().offset(0.0D, -height, 0.0D)).isEmpty();
    }

    @EventTarget
    public void onUpdate(EventPreMotion e) {
        if(lagTimer.hasReached(5000L) && packett.size() > 4) {
            lagTimer.reset();
            while (packett.size() > 4) {
                sendPacketNoEvent(packett.poll());
            }
        }
    }

    @EventTarget
    public void onPre(EventPreMotion e) {
        if (this.mc.isSingleplayer()) {
            return;
        }
        if (DisablerMode.isCurrentMode("Hypixel")) {
            if (packet.size() > 50) {
                while (!packet.isEmpty()) {
                    sendPacketNoEvent(packet.remove(0));
                }
            }
        }
    }

    @EventTarget
    public void onMove(EventMove event) {
        if (DisablerMode.isCurrentMode("Hypixel")) {
            if (!mc.isSingleplayer()) {
                if (timer1.hasTimeElapsed(10000, true)) {
                    cancel = true;
                    timer2.reset();
                }
            }
        }
    }

    @EventTarget
    public void onPacket(EventPacket event) {
        final Packet p = event.getPacket();

        if (this.mc.isSingleplayer()) {
            return;
        }
        if (DisablerMode.isCurrentMode("VulcanCombat")) {
            if (p instanceof C0FPacketConfirmTransaction){
                if (Math.abs((Math.abs((((C0FPacketConfirmTransaction) p).getUid())) - Math.abs(vulTickCounterUID))) <= 4) {
                    vulTickCounterUID = ((C0FPacketConfirmTransaction) p).getUid();
                    packett.add(p);
                    event.setCancelled(true);
                } else if (Math.abs((Math.abs((((C0FPacketConfirmTransaction) p).getUid())) - 25767)) <= 4) {
                    vulTickCounterUID = ((C0FPacketConfirmTransaction) p).getUid();
                }
            }
        }
        if (DisablerMode.isCurrentMode("Hypixel")) {
            if (event.getEventType() == EventType.RECIEVE) {
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
            }
            if (event.getEventType() == EventType.SEND) {
                if (event.getPacket() instanceof C03PacketPlayer) {
                    C03PacketPlayer c03PacketPlayer = (C03PacketPlayer) event.getPacket();

                    // If the player isn't moving, and if the player isn't using an item, cancel the event.
                    if (!c03PacketPlayer.isMoving() && !mc.thePlayer.isUsingItem()) {
                        event.setCancelled(true);
                    }
                    doTimerDisabler(event);
                }
                if (event.getPacket() instanceof C03PacketPlayer.C05PacketPlayerLook && mc.thePlayer.isRiding()) {
                    sendPacketNoEvent(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
                } else if (event.getPacket() instanceof C0CPacketInput && mc.thePlayer.isRiding()) {
                    sendPacketNoEvent(event.getPacket());
                    sendPacketNoEvent(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
                    event.setCancelled(true);
                }
                if (p instanceof C03PacketPlayer) {
                    final C03PacketPlayer c03 = (C03PacketPlayer) p;

                    if (mc.thePlayer.ticksExisted == 1) {
                        initPos = new Vec3(c03.getPositionX() + getRandom(-1000000, 1000000), c03.getPositionY() + getRandom(-1000000, 1000000), c03.getPositionZ() + getRandom(-1000000, 1000000));
                    } else if ((((INetHandlerPlayClient) mc.thePlayer.sendQueue).getdoneLoadingTerrain() && initPos != null && mc.thePlayer.ticksExisted < 100)) {
                        ((IC03PacketPlayer) c03).setPosX(initPos.xCoord);
                        ((IC03PacketPlayer) c03).setPosY(initPos.yCoord);
                        ((IC03PacketPlayer) c03).setPosZ(initPos.zCoord);
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

                if (((INetHandlerPlayClient) mc.thePlayer.sendQueue).getdoneLoadingTerrain()) {
                    if (!event.isCancelled() && (event.getPacket() instanceof C03PacketPlayer || event.getPacket() instanceof C0FPacketConfirmTransaction || event.getPacket() instanceof C00PacketKeepAlive)) {
                        event.setCancelled(true);
                        packets.add(event.getPacket());
                    }
                }
            }
        }
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
                if (!timer2.hasTimeElapsed(400, false)) {
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
