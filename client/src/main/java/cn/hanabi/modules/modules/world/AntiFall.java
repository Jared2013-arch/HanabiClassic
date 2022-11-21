package cn.hanabi.modules.modules.world;


import cn.hanabi.Wrapper;
import cn.hanabi.events.EventPacket;
import cn.hanabi.events.EventPreMotion;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.utils.game.PlayerUtil;
import cn.hanabi.utils.math.TimeHelper;
import cn.hanabi.value.Value;
import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.EventType;
import net.minecraft.block.BlockAir;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.BlockPos;

import java.util.ArrayList;


public class AntiFall extends Mod {

    public static Value<Double> falldistance = new Value("AntiFall", "FallDistance", 10d, 0d, 10d, 0.1d);
    public static Value<Double> delay = new Value("AntiFall", "Delay", 800d, 200d, 2000d, 100d);

    public Value<Boolean> onlyvoid = new Value("AntiFall", "OnlyVoid", true);

    public Value<Boolean> nodmg = new Value("AntiFall", "0 DMG", true);
    public Value<Boolean> extraPacket = new Value("AntiFall", "ExtraPacket", true);


    public AntiFall() {
        super("AntiFall", Category.WORLD);
        // TODO 自动生成的构造函数存根
    }

    public static boolean isAboveVoid() {
        if (mc.thePlayer.posY < 0) return true;
        for (int i = (int) (mc.thePlayer.posY - 1); i > 0; --i)
            if (!(mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, i, mc.thePlayer.posZ)).getBlock() instanceof BlockAir))
                return false;
        return !mc.thePlayer.onGround;
    }

    float[] last = new float[]{0, 0, 0};
    TimeHelper timerUtil = new TimeHelper();
    ArrayList<Packet> packets = new ArrayList<>();
    boolean falling;

    @EventTarget
    public void onSend(EventPacket e) {
        if (e.getEventType().equals(EventType.SEND)) {
            //clear
            if (mc.thePlayer != null && mc.thePlayer.ticksExisted < 100) {
                packets.clear();
                return;
            }
            if (e.packet instanceof C03PacketPlayer) {
                if (isAboveVoid()) {
                    e.setCancelled(true);
                    packets.add(e.packet);
                    if (timerUtil.hasReached(delay.getValue().intValue())) {
                        mc.thePlayer.setPosition(last[0], last[1], last[2]);
                        if (extraPacket.getValue()) {
                            Wrapper.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(
                                    last[0], last[1] - 1, last[2], true));
                        }
                        mc.thePlayer.motionY = 0;
                        packets.clear();
                        timerUtil.reset();
                    }
                } else {
                    last = new float[]{(float) mc.thePlayer.posX, (float) mc.thePlayer.posY, (float) mc.thePlayer.posZ};
                    if (packets.size() > 0) {
                        for (Packet packet : packets) {
                            Wrapper.sendPacketNoEvent(packet);
                        }
                        packets.clear();
                    }
                    timerUtil.reset();
                }
            }
        } else if (e.getEventType().equals(EventType.RECIEVE)) {
            if (e.getPacket() instanceof S08PacketPlayerPosLook) {
                packets.clear();
            }
        }
    }
}
