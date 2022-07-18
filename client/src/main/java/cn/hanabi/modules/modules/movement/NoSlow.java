package cn.hanabi.modules.modules.movement;

import aLph4anTi1eaK_cN.Annotation.ObfuscationClass;
import cn.hanabi.events.EventPacket;
import cn.hanabi.events.EventPostMotion;
import cn.hanabi.events.EventPreMotion;
import cn.hanabi.events.EventUpdate;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.utils.game.PlayerUtil;
import cn.hanabi.utils.math.TimeHelper;
import cn.hanabi.value.Value;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.S30PacketWindowItems;
import net.minecraft.util.BlockPos;

import static cn.hanabi.Wrapper.sendPacketNoEvent;



@ObfuscationClass
public class NoSlow extends Mod {
    public Value<String> mode = new Value<String>("NoSlow", "Mode", 0)
            .LoadValue(new String[]{"Vanilla", "NCP", "Vulcan", "AAC5"});

    private boolean isBlocking;
    TimeHelper ms = new TimeHelper();
    private boolean lastBlockingStat = false;
    private boolean nextTemp = false;

    public NoSlow() {
        super("NoSlow", Category.MOVEMENT);
        // TODO 自动生成的构造函数存根
    }

    @Override
    public void onDisable() {
        ms.reset();
        nextTemp = false;
    }

    @EventTarget
    public void onPre(EventPreMotion e) {
        if (!mc.thePlayer.isUsingItem())
            return;

        if (mode.isCurrentMode("NCP")) {
            mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange((mc.thePlayer.inventory.currentItem + 1 > 8) ? 0 : (mc.thePlayer.inventory.currentItem + 1)));
            mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
        }
    }

    @EventTarget
    public void onUpdate(EventUpdate e) {
        if(mc.thePlayer == null || mc.theWorld == null)
            return;
        if((mode.isCurrentMode("Vulcan")) && (lastBlockingStat || isBlocking)) {
            if(ms.hasReached(230) && nextTemp) {
                nextTemp = false;
            }
            if(!nextTemp) {
                lastBlockingStat = isBlocking;
                if (!isBlocking) {
                    return;
                }
                sendPacketNoEvent(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, mc.thePlayer.inventory.getCurrentItem(), 0f, 0f, 0f));
                nextTemp = true;
                ms.reset();
            }
        }
    }

    @EventTarget
    public void onPost(EventPostMotion e) {
        if (!mc.thePlayer.isUsingItem()) return;
        if(mode.isCurrentMode("Vulcan")) {
            if ((mc.thePlayer.isUsingItem() || mc.thePlayer.isBlocking())) {
                mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, mc.thePlayer.inventory.getCurrentItem(), 0f, 0f, 0f));
            }
        }
    }

    @EventTarget
    public void onPacket(EventPacket event) {
        if((mode.isCurrentMode("Vulcan")) && (lastBlockingStat || isBlocking)) {
            if (ms.hasReached(230) && nextTemp) {
                boolean canAttack = false;
                if (event.packet instanceof C03PacketPlayer) {
                    canAttack = true;
                }
                if (!((event.packet instanceof C02PacketUseEntity || event.packet instanceof C0APacketAnimation) && !canAttack)) {
                    sendPacketNoEvent(event.packet);
                }
            }
            if ((event.packet instanceof C07PacketPlayerDigging || event.packet instanceof C08PacketPlayerBlockPlacement) && isBlocking) {
                event.setCancelled(true);
            } else if (event.packet instanceof C03PacketPlayer || event.packet instanceof C0APacketAnimation || event.packet instanceof C0BPacketEntityAction || event.packet instanceof C02PacketUseEntity || event.packet instanceof C07PacketPlayerDigging || event.packet instanceof C08PacketPlayerBlockPlacement) {

                event.setCancelled(true);
            }
        }
    }
}
