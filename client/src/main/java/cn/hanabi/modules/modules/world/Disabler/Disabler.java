package cn.hanabi.modules.modules.world.Disabler;

import cn.hanabi.events.*;
import cn.hanabi.gui.classic.notifications.Notification;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.utils.client.ClientUtil;
import cn.hanabi.utils.math.TimeHelper;
import cn.hanabi.value.Value;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.network.Packet;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.play.client.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static cn.hanabi.Wrapper.sendPacketNoEvent;

public class Disabler extends Mod {
    //MODE
    private final Value<String> DisablerMode = new Value<String>("Disabler", "DisableMode", 0)
            .LoadValue(new String[]{"Hypixel", "VulcanCombat",});

    private final Value<Double> delay = new Value<>("Disabler", "Delay", 400d, 0d, 2000d, 10);
    //Vulcan
    public static final LinkedList<Packet<INetHandlerPlayServer>> packett = new LinkedList<Packet<INetHandlerPlayServer>>();
    public static final List<Packet<?>> packet = new ArrayList();
    private double vulTickCounterUID = 0;
    private TimeHelper lagTimer = new TimeHelper();
    private TimeHelper hypTimer = new TimeHelper();


    private boolean disabled = false;
    private LinkedList<Packet> confirmTransactionQueue = new LinkedList();
    private LinkedList<Packet> keepAliveQueue = new LinkedList();


    //Hypixel
    private LinkedList<Packet> packets = new LinkedList();
    private int cout;
    private int randomDelay = 4;


    public Disabler() {
        super("Disabler", Category.WORLD);
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @EventTarget
    public void onLoadWorld(EventWorldChange event) {
        vulTickCounterUID = -25767;
        cout = 0;
        packets.clear();
        disabled = false;
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
            if (hypTimer.isDelayComplete(delay.getValue()) && disabled) {
                while (!packets.isEmpty()) {
                    Packet remove = packets.remove(0);
                    sendPacketNoEvent(remove);
                }
                hypTimer.reset();
            }

        }
    }

    @EventTarget
    public void onMove(EventMove event) {
        if (DisablerMode.isCurrentMode("Hypixel")) {

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
            if (p instanceof C0FPacketConfirmTransaction) {
                cout++;
                if (((C0FPacketConfirmTransaction) p).getUid() < 0 && cout > 7) {
                    if (!disabled) {
                        ClientUtil.sendClientMessage("WatchDog: Disabled Hypixel", Notification.Type.SUCCESS);
                        disabled = true;
                    }
                    packets.add(p);
                    event.setCancelled(true);
                }
            }
            if (p instanceof C00PacketKeepAlive) {
                if (((C00PacketKeepAlive) p).getKey() >= 20) {
                    packets.add(p);
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
//    private void doTimerDisabler(EventPacket e) {
//        if (e.getPacket() instanceof C03PacketPlayer) {
//            C03PacketPlayer c03PacketPlayer = (C03PacketPlayer) e.getPacket();
//            if (!c03PacketPlayer.isMoving() && !mc.thePlayer.isUsingItem()) {
//                e.setCancelled(true);
//            }
//            if (cancel) {
//                if (!timer2.hasTimeElapsed(400, false)) {
//                    if (!ModManager.getModule("Scaffold").isEnabled()) {
//                        e.setCancelled(true);
//                        packets.add(e.getPacket());
//                    }
//                } else {
//                    packets.forEach(Wrapper::sendPacketNoEvent);
//                    packets.clear();
//                    cancel = false;
//                }
//            }
//        }
//    }
}
