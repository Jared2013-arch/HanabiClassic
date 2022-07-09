package cn.hanabi.modules.modules.player;

import cn.hanabi.events.EventPacket;
import cn.hanabi.events.EventPreMotion;
import cn.hanabi.events.EventWorldChange;
import cn.hanabi.modules.Mod;
import cn.hanabi.utils.ChatUtils;
import cn.hanabi.utils.MathUtils;
import cn.hanabi.utils.PlayerUtil;
import cn.hanabi.utils.TimeHelper;
import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.EventType;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Items;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;
import net.minecraft.util.Vec3;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;

import static cn.hanabi.Wrapper.sendPacketNoEvent;

public class Disabler extends Mod {

    private final Deque<TimestampedPacket> transactionQueue = new ArrayDeque<>();
    private final TimeHelper packetTimer = new TimeHelper();
    private final TimeHelper spikeTimer = new TimeHelper();
    public final List<Packet<?>> packet = new CopyOnWriteArrayList<>();
    private final TimeHelper timer = new TimeHelper();
    private long lastTransaction, lastPositionPacket;
    private final Queue<DelayedPacket> packetQueue = new ConcurrentLinkedQueue<>();
    private Vec3 initialPosition;
    private final List<IgnoredPosition> ignoredPositionList = new CopyOnWriteArrayList<>();
    private int lagbacks, balance;
    private final ConcurrentLinkedQueue<Packet<?>> packets = new ConcurrentLinkedQueue<>();
    //private final Stopwatch stopwatch = new Stopwatch();
    public static boolean aba;
    private static final TimeHelper timer1 = new TimeHelper();
    private static Vec3 initPos;
    public static List<Packet> packetList = new CopyOnWriteArrayList<>();
    private static List<Packet> hypList = new CopyOnWriteArrayList<>();
    public int counter = 0;
    public double posX;
    public double posY;
    public double posZ;
    private boolean cancel;
    private TimeHelper timer2 = new TimeHelper();
    private TimeHelper packetDelay = new TimeHelper();
    private short id;
    private List<Packet> listTransactions = new CopyOnWriteArrayList();
    private boolean afterFly;
    Packet packetIn;
    private boolean active;
    private final PositionAndRotation S08 = new PositionAndRotation(0, 0, 0, 0, 0);
    private final ArrayDeque<C0FPacketConfirmTransaction> C0FQueue = new ArrayDeque<>();
    private final ArrayDeque<C00PacketKeepAlive> C00Queue = new ArrayDeque<>();
    private long nextSend;
    private boolean spike;
    private long packetsToCancel;

    @EventTarget
    private void onWorld(EventWorldChange e){
        transactionQueue.clear();
        lastTransaction = 0;
        ignoredPositionList.clear();
        packet.clear();
        packets.clear();
        nextSend = 0;
        timer.reset();
        timer2.reset();
    };

    @EventTarget
    private void onMotion(EventPreMotion event) {
        if(mc.isSingleplayer()) {
            return;
        }
                if (packetsToCancel > 0) return;

                if (packetDelay.hasReached(mc.thePlayer.ticksExisted < 120 ? 1920 : MathUtils.getRandom(1, 250))) {
                    while (!packetQueue.isEmpty())
                        sendPacketNoEvent(packetQueue.remove().packet);
                    packetDelay.reset();
                }
                if (mc.thePlayer.ticksExisted == 120) {
                    ChatUtils.success("Disabled");
                    event.setX(event.getX() + 1);
                    event.setZ(event.getZ() + 1);
                }
    }

    @EventTarget
    private void onPacket(EventPacket event) {
        if(mc.isSingleplayer() || event.getEventType().equals(EventType.POST))
            return;

        final Packet<?> p = event.getPacket();
                doTimerDisabler(event);
                if (p instanceof C03PacketPlayer) {
                    final C03PacketPlayer C03 = (C03PacketPlayer) packet;
                    if (packetsToCancel > 0) {
                        event.setCancelled(true);
                        packetsToCancel--;
                        return;
                    }
                    if (!C03.isMoving() && C03.getRotating()) {
                        event.setCancelled(true);
                    }
                }
                if (mc.thePlayer.ticksExisted < 120) {
                    if (p instanceof C03PacketPlayer) {
                        final C03PacketPlayer C03 = (C03PacketPlayer) packet;
                        if (!C03.isMoving() && !C03.getRotating()) {
                            event.setCancelled(true);
                        } else {
                            transactionQueue.push(new TimestampedPacket(event.getPacket()));
                            event.setCancelled(true);
                        }
                    }
                }
                if (event.getPacket() instanceof C03PacketPlayer) {
                    if (mc.currentScreen instanceof GuiDownloadTerrain) {
                        mc.displayGuiScreen(null);
                    }
                    sendPacketNoEvent(new C0CPacketInput());
                    C03PacketPlayer packetPlayer = (C03PacketPlayer) event.getPacket();
                    if (isIgnored(packetPlayer)) {
                        packetPlayer.y += Math.random() * 1.0e-9;
                    }
                    for (DelayedPacket delayedPacket : packetQueue) {
                        if (mc.thePlayer.ticksExisted % 8 == 0) {
                            delayedPacket.endDelta -= 73;
                        }
                        if (System.currentTimeMillis() - delayedPacket.getStartTime() >= delayedPacket.getEndDelta()) {
                            sendPacketNoEvent(delayedPacket.getPacket());
                            packetQueue.remove(delayedPacket);
                        }
                    }
                }
                if (event.getPacket() instanceof C03PacketPlayer) {
                    if(!(event.getPacket() instanceof C03PacketPlayer.C04PacketPlayerPosition || event.getPacket() instanceof C03PacketPlayer.C05PacketPlayerLook || event.getPacket() instanceof C03PacketPlayer.C06PacketPlayerPosLook)) {
                        C03PacketPlayer c03 = (C03PacketPlayer) event.getPacket();
                        event.setCancelled((!c03.isMoving() && !c03.getRotating() && !mc.thePlayer.isUsingItem()) || mc.thePlayer.ticksExisted < 120);
                        if (mc.thePlayer.ticksExisted < 120) {
                            transactionQueue.push(new TimestampedPacket(c03));
                        }
                    }
                }
                if (event.getPacket()  instanceof C0FPacketConfirmTransaction || event.getPacket() instanceof C00PacketKeepAlive) {
                    if (mc.thePlayer.ticksExisted >= 100) {
                        transactionQueue.push(new TimestampedPacket(event.getPacket()));
                    }
                    event.setCancelled(true);
                }

                if (mc.thePlayer != null && mc.thePlayer.ticksExisted == 0) {
                    packetQueue.clear();
                    ignoredPositionList.clear();
                }

                if (p instanceof C0BPacketEntityAction) {
                    event.setCancelled(true);
                }

                if(mc.thePlayer.ticksExisted <= 5) return;

                if (event.getPacket() instanceof C03PacketPlayer) {
                    final C03PacketPlayer wrapper = (C03PacketPlayer) event.getPacket();

                    if (mc.thePlayer.ticksExisted == 1) {
                        initialPosition = new Vec3(wrapper.x + MathUtils.random(-1000000, 1000000), wrapper.y + MathUtils.random(-1000000, 1000000), wrapper.z + MathUtils.random(-1000000, 1000000));
                    } else if (mc.thePlayer.sendQueue.doneLoadingTerrain && initialPosition != null && mc.thePlayer.ticksExisted < 100) {
                        wrapper.x = initialPosition.xCoord;
                        wrapper.y = initialPosition.yCoord;
                        wrapper.z = initialPosition.zCoord;
                    }
                }
                if ((event.getPacket() instanceof C00PacketKeepAlive || event.getPacket() instanceof C0FPacketConfirmTransaction) && mc.thePlayer.ticksExisted > 70) {
                    packetQueue.add(new DelayedPacket(event.getPacket(), 400L));
                    event.setCancelled(true);
                }
                if (event.getPacket() instanceof C0FPacketConfirmTransaction) {
                    packet.add(event.getPacket());
                    event.setCancelled(true);
                }
                if ((event.getPacket() instanceof C0FPacketConfirmTransaction || event.getPacket() instanceof C00PacketKeepAlive) && (mc.thePlayer.ticksExisted % 600 <= 3 || mc.thePlayer.ticksExisted < 20)) {
                    sendPacketDelayedNoEvent(event.getPacket(), 400L);
                    event.setCancelled(true);
                }
    }

    private boolean isIgnored(C03PacketPlayer packetPlayer) {
        for (IgnoredPosition ignoredPosition : ignoredPositionList) {
            if (ignoredPosition.getX() == packetPlayer.getPositionX() &&
                    ignoredPosition.getY() == packetPlayer.getPositionY() &&
                    ignoredPosition.getZ() == packetPlayer.getPositionZ()) {
                return true;
            }
        }
        return false;
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
    @EventTarget
    private final Listener<TeleportEvent> teleportEventListener = new Listener<>(event -> {
        if(modeProperty.getValue() == DisablerMode.HYPIXEL2){
            if (mc.thePlayer.sendQueue.doneLoadingTerrain) {
                ignoredPositionList.add(new IgnoredPosition(event.getPosX(), event.getPosY(), event.getPosZ()));
                if(ignoredPositionList.size() > 0) {
                    if (mc.thePlayer.ticksExisted < 120) {
                        for (int i = 0; i < 10; i++) {
                            sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(event.getPosX(), event.getPosY() + 1e-6, event.getPosZ(), false));
                        }
                        sendPacketNoEvent(event.getResponse());
                        if (mc.thePlayer.getDistance(event.getPosX(), event.getPosY(), event.getPosZ()) <= 8) {
                            event.setCancelled(true);
                        }
                    } else {
                        event.setPosX(event.getPosX() + MathUtil.randomDouble(-10, 10));
                        event.setPosZ(event.getPosZ() + MathUtil.randomDouble(-10, 10));
                    }
                }
            }
        }
    });

    @EventTarget
    private final Listener<MovePlayerEvent> movePlayerEventListener = new Listener<>(event -> {
        if (timer1.hasTimeElapsed(10000, true)) {
            cancel = true;
            timer2.reset();
        }
    });
    @EventTarget
    private void onPacketReceived(EventPacket event) {
                if (event.getPacket() instanceof S08PacketPlayerPosLook && event.getEventType().equals(EventType.RECIEVE)) {
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

    public void directPacketRecieve(EventPacket event) {
        // used only for listeners without modules :)
        if (event.getPacket() instanceof S32PacketConfirmTransaction && mc.currentScreen instanceof GuiContainer) {
            ((GuiContainer) mc.currentScreen).onServerTransaction(event.getPacket());
        }
    }

    private final class TimestampedPacket {
        private final Packet<?> packet;
        private final long timestamp;

        public TimestampedPacket(final Packet<?> packet) {
            this.packet = packet;
            this.timestamp = System.currentTimeMillis();
        }
    }

    @Override
    public void onEnable() {
        super.onEnable();
        transactionQueue.clear();
        counter = 0;
        spike = false;
        timer.reset();
        packet.clear();
        packets.clear();
        nextSend = 0;
        ignoredPositionList.clear();
        lastTransaction = 0;
        lagbacks = balance = 0;
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    private void doTimerDisabler(EventPacket e) {
        if (e.getPacket() instanceof C03PacketPlayer) {
            C03PacketPlayer c03PacketPlayer = (C03PacketPlayer) e.getPacket();

            // If the player isn't moving, and if the player isn't using an item, cancel the event.
            if (!c03PacketPlayer.isMoving() && !mc.thePlayer.isUsingItem()) {
                e.setCancelled(true);
            }
            if (cancel) {
                if (!timer2.hasReached(400)) {
                    e.setCancelled(true);
                    packets.add(e.getPacket());
                }
            }
        }
    }

    public boolean playerInLobby() {
        return mc.thePlayer.inventory.hasItem(Items.compass);
    }


    private static final class DelayedPacket {
        private final long startTime = System.currentTimeMillis();
        private final Packet packet;
        private long endDelta;

        private DelayedPacket(Packet packet, long endDelta) {
            this.packet = packet;
            this.endDelta = endDelta;
        }

        public Packet getPacket() {
            return packet;
        }

        public long getEndDelta() {
            return endDelta;
        }

        public long getStartTime() {
            return startTime;
        }
    }

    private static final class IgnoredPosition {
        private final double x, y, z;

        private IgnoredPosition(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public double getZ() {
            return z;
        }

    }
}