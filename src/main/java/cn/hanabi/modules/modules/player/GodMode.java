package cn.hanabi.modules.modules.player;

import cn.hanabi.events.EventUpdate;
import cn.hanabi.gui.classic.clickui.impl.ModeValue;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.value.Value;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.network.play.client.C03PacketPlayer;
import scala.Int;

public class GodMode extends Mod {
    public Value<String> mode = new Value("GodMode", "mode", 1);
    public Value<Integer> delay = new Value<Integer>("GodMode", "delay", 2, 0, 20, 1);

    public GodMode() {
        super("GodMode", Category.PLAYER);
        mode.addValue("AAC");
        mode.addValue("IDK");
    }

    @EventTarget
    public void onUpdate(EventUpdate e) {
        if (mode.getCurrentMode() == 0) {
            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 0.1, mc.thePlayer.posZ, mc.thePlayer.onGround));
        } else {
            if (mc.thePlayer.ticksExisted % delay.getValue() == 0) {
                mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 21, mc.thePlayer.posZ, mc.thePlayer.onGround));
                mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 21, mc.thePlayer.posZ, mc.thePlayer.onGround));
            }
        }
    }
}
