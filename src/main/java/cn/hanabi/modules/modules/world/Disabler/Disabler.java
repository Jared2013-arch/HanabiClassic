package cn.hanabi.modules.modules.world.Disabler;

import cn.hanabi.Hanabi;
import cn.hanabi.Wrapper;
import cn.hanabi.events.EventPacket;
import cn.hanabi.events.EventPreMotion;
import cn.hanabi.events.EventWorldChange;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.modules.ModManager;
import cn.hanabi.modules.modules.world.Scaffold;
import cn.hanabi.utils.math.TimeHelper;
import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.EventType;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;

import java.util.ArrayList;
import java.util.Queue;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

public class Disabler extends Mod {

    private final Queue<C0FPacketConfirmTransaction> confirmTransactionQueue = new ConcurrentLinkedQueue<>();

    private final Queue<C00PacketKeepAlive> keepAliveQueue = new ConcurrentLinkedQueue<>();

    private ArrayList<Packet> packets = new ArrayList<>();
    private final CopyOnWriteArrayList<C0EPacketClickWindow> clickWindowPackets = new CopyOnWriteArrayList<>();

    private boolean disabled = false;

    private int lastuid = 0;

    public boolean isCraftingItem = false;

    private final TimeHelper lastRelease = new TimeHelper();

    private int cancelledPackets = 0;

    public TimeHelper timedOutTimer = new TimeHelper();

    private float yawDiff = 0f;

    private TimeHelper timer2 = new TimeHelper();


    public Disabler() {
        super("Disabler", Category.WORLD);
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @EventTarget
    private void LoadWorld(EventWorldChange e) {
        timer2.reset();
        confirmTransactionQueue.clear();
        keepAliveQueue.clear();
        disabled = false;
        isCraftingItem = false;
        clickWindowPackets.clear();
        lastuid = 0;
        cancelledPackets = 0;
    }

    @EventTarget
    private void onPacket(EventPacket e) {
        if (e.getEventType().equals(EventType.SEND)) {
            final Packet packet = e.getPacket();
            if (disabled) {
                if (packet instanceof C03PacketPlayer && !(packet instanceof C03PacketPlayer.C04PacketPlayerPosition || packet instanceof C03PacketPlayer.C05PacketPlayerLook || packet instanceof C03PacketPlayer.C06PacketPlayerPosLook) && !ModManager.getModule(Scaffold.class).isEnabled()) {
                    cancelledPackets++;
                    e.setCancelled(true);
                }
            }
//            if (ModManager.getModule("Speed").isEnabled()) {
//                if (e.getPacket() instanceof C0CPacketInput) {
//                    C0CPacketInput p = (C0CPacketInput) e.getPacket();
//                    p.setStrafeSpeed(p.getStrafeSpeed() * 0.9999f);
//                    e.packet = p;
//                }
//            }
            if (packet instanceof C0FPacketConfirmTransaction) {
                if (((C0FPacketConfirmTransaction) packet).getWindowId() == 0 && ((C0FPacketConfirmTransaction) packet).getUid() < 0 && ((C0FPacketConfirmTransaction) packet).getUid() != (-1)) {
                    if (disabled) {
                        cancelledPackets++;
                        e.setCancelled(true);
                    }
                }
            }
            if (packet instanceof C0FPacketConfirmTransaction) {
                processConfirmTransactionPacket(e);
            } else if (packet instanceof C00PacketKeepAlive) {
                processKeepAlivePacket(e);
            }
        }
    }

    @EventTarget
    private void onUpdate(EventPreMotion e) {
        if (mc.thePlayer.ticksExisted % 40 == 0) {
            float rate = (cancelledPackets / 40f * 100);
            cancelledPackets = 0;
        }
        setDisplayName(disabled ? "Active" : "Progressing");
        if (disabled) {
            if (confirmTransactionQueue.isEmpty()) {
                lastRelease.reset();
            } else {
                if (confirmTransactionQueue.size() >= 7) {
                    while (!keepAliveQueue.isEmpty())
                        Wrapper.sendPacketNoEvent(keepAliveQueue.poll());
                    while (!confirmTransactionQueue.isEmpty()) {
                        Wrapper.sendPacketNoEvent(confirmTransactionQueue.poll());
                    }
                }
            }
        }
    }

    private void processConfirmTransactionPacket(EventPacket e) {
        final Packet packet = e.getPacket();
        final int preuid = lastuid - 1;
        if (packet instanceof C0FPacketConfirmTransaction) {
            if (((C0FPacketConfirmTransaction) packet).getWindowId() == 0 || ((C0FPacketConfirmTransaction) packet).getUid() < 0) {
                if (((C0FPacketConfirmTransaction) packet).getUid() == preuid) {
                    if (!disabled) {
                        disabled = true;
                    }
                    confirmTransactionQueue.offer((C0FPacketConfirmTransaction) packet);
                    e.setCancelled(true);
                }
                lastuid = ((C0FPacketConfirmTransaction) packet).getUid();
            }
        }
    }


    private void processKeepAlivePacket(EventPacket e) {
        final Packet packet = e.getPacket();
        if (packet instanceof C00PacketKeepAlive) {
            if (disabled) {
                keepAliveQueue.offer((C00PacketKeepAlive) packet);
                e.setCancelled(true);
            }
        }
    }
}
