package cn.hanabi.modules.modules.world.Disabler;

import cn.hanabi.Wrapper;
import cn.hanabi.events.*;
import cn.hanabi.gui.classic.notifications.Notification;
import cn.hanabi.injection.interfaces.IC03PacketPlayer;
import cn.hanabi.injection.interfaces.INetHandlerPlayClient;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.modules.ModManager;
import cn.hanabi.utils.client.ClientUtil;
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
    private double vulTickCounterUID = 0;
    private boolean cancel;
    private TimeHelper timer2 = new TimeHelper();
    private TimeHelper timer1 = new TimeHelper();
    private TimeHelper lagTimer = new TimeHelper();

    private boolean disabled = false;
    private short lastUid;
    private LinkedList<Packet> confirmTransactionQueue = new LinkedList();
    private LinkedList<Packet> keepAliveQueue = new LinkedList();


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
        if (DisablerMode.isCurrentMode("VulcanCombat")) {
            if (lagTimer.hasReached(5000L) && packett.size() > 4) {
                lagTimer.reset();
                while (packett.size() > 4) {
                    sendPacketNoEvent(packett.poll());
                }
            }
        }
    }

    @EventTarget
    public void onPre(EventPreMotion e) {
        if (this.mc.isSingleplayer()) {
            return;
        }
        if (DisablerMode.isCurrentMode("Hypixel")) {
            if(disabled){
                if (confirmTransactionQueue.isEmpty()) {
//                    lastRelease.reset()
                } else {
                    if (confirmTransactionQueue.size() >= 6) {
                        while (!keepAliveQueue.isEmpty()) sendPacketNoEvent(keepAliveQueue.poll());
                        while (!confirmTransactionQueue.isEmpty()) {
                            Packet<INetHandlerPlayServer> poll= confirmTransactionQueue.poll();
                            sendPacketNoEvent(poll);
                        }
                    }
                }
            }
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
//            if (!mc.isSingleplayer()) {
//                if (timer1.hasTimeElapsed(10000, true)) {
//                    cancel = true;
//                    timer2.reset();
//                }
//            }
        }
    }

    @EventTarget
    public void onPacket(EventPacket event) {
        final Packet p = event.getPacket();

        if (this.mc.isSingleplayer()) {
            return;
        }
        if (DisablerMode.isCurrentMode("VulcanCombat")) {
            if (p instanceof C0FPacketConfirmTransaction) {
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
                    if (!disabled && mc.thePlayer.ticksExisted > 20) {
                        event.setCancelled(true);
                        ClientUtil.sendClientMessage("Cancelled S08", Notification.Type.INFO);
                    } else {
                        mc.thePlayer.motionX = mc.thePlayer.motionY = mc.thePlayer.motionZ = 0;
                    }
                }
            }
            if (event.getEventType() == EventType.SEND) {
                if (event.getPacket() instanceof C03PacketPlayer) {
                    C03PacketPlayer c03PacketPlayer = (C03PacketPlayer) event.getPacket();

                    if(disabled && !(c03PacketPlayer instanceof C03PacketPlayer.C04PacketPlayerPosition) && !(c03PacketPlayer instanceof C03PacketPlayer.C05PacketPlayerLook) && !(c03PacketPlayer instanceof C03PacketPlayer.C06PacketPlayerPosLook)){
                        counter++;
                        event.setCancelled(true);
                    }

                    // If the player isn't moving, and if the player isn't using an item, cancel the event.
                    if (!c03PacketPlayer.isMoving() && !mc.thePlayer.isUsingItem()) {
                        event.setCancelled(true);
                    }
                }


                if(event.getPacket() instanceof C0FPacketConfirmTransaction){
                    C0FPacketConfirmTransaction packet1 = (C0FPacketConfirmTransaction) event.getPacket();
                    int windowId = packet1.getWindowId();
                    short uid = packet1.getUid();
                    if(windowId!=0 || uid>0){
                        ClientUtil.sendClientMessage("Inventory synchronized", Notification.Type.INFO);
                    }else{
                        if(uid == --lastUid){
                            if(!disabled){
                                ClientUtil.sendClientMessage("WatchDog Disabled", Notification.Type.INFO);
                                disabled = true;
                            }
                            confirmTransactionQueue.offer(event.getPacket());
                            event.setCancelled(true);
                        }
                        lastUid = uid;
                    }
                }

                if (event.getPacket() instanceof C03PacketPlayer.C05PacketPlayerLook && mc.thePlayer.isRiding()) {
                    sendPacketNoEvent(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
                } else if (event.getPacket() instanceof C0CPacketInput && mc.thePlayer.isRiding()) {
                    sendPacketNoEvent(event.getPacket());
                    sendPacketNoEvent(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
                    event.setCancelled(true);
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
