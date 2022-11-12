package cn.hanabi.modules.modules.world;


import cn.hanabi.events.EventPreMotion;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.utils.game.PlayerUtil;
import cn.hanabi.utils.math.TimeHelper;
import cn.hanabi.value.Value;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.network.play.client.C03PacketPlayer;


public class AntiFall extends Mod {

    public static Value<Double> falldistance = new Value("AntiFall", "FallDistance", 10d, 5d, 30d, 0.1d);
    public static Value<Double> delay = new Value("AntiFall", "Delay", 800d, 200d, 2000d, 100d);

    public Value<Boolean> onlyvoid = new Value("AntiFall", "OnlyVoid", true);

    public Value<Boolean> nodmg = new Value("AntiFall", "0 DMG", true);


    TimeHelper timer = new TimeHelper();
    boolean falling;

    public AntiFall() {
        super("AntiFall", Category.WORLD);
        // TODO 自动生成的构造函数存根
    }


    @EventTarget
    public void onUpdate(EventPreMotion e) {
        boolean canFall = !mc.thePlayer.onGround;

        final boolean aboveVoid = !onlyvoid.getValue() || PlayerUtil.isBlockUnder();

        if (canFall && PlayerUtil.isBlockUnder() && nodmg.getValue())
            e.setOnGround(true);

        if (mc.thePlayer.fallDistance >= falldistance.getValue() && aboveVoid) {
                mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX + 1 + 0.14 * Math.random(), mc.thePlayer.posY + falldistance.getValue() + 0.15 * Math.random(), mc.thePlayer.posZ + 1 + 0.15 * Math.random(), false));
                falling = true;
        } else {
            falling = false;
        }
    }

}