package cn.hanabi.modules.modules.player;


import cn.hanabi.Wrapper;
import cn.hanabi.events.EventGui;
import cn.hanabi.events.EventPacket;
import cn.hanabi.events.EventUpdate;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.modules.ModManager;
import cn.hanabi.modules.modules.world.Scaffold;
import cn.hanabi.utils.game.PlayerUtil;
import cn.hanabi.utils.math.TimeHelper;
import cn.hanabi.value.Value;
import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.EventType;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.S00PacketKeepAlive;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.LinkedList;


public class InvMove extends Mod {

    private final Value mode = new Value("InvMove", "Mode", 0);
    Value<Double> delay = new Value<Double>("InvMove", "Delay", 100.0, 0.0, 1000.0, 10.0);
    private boolean isWalking;


    public InvMove() {
        super("InvMove", Category.PLAYER);
        mode.addValue("Vanilla");
        mode.addValue("Hypixel");
    }

    @EventTarget
    public void onWindow(EventGui event) {
        setKeyStat();
    }

    boolean act;
    TimeHelper timer = new TimeHelper();
    TimeHelper timer2 = new TimeHelper();

    LinkedList<Packet> packets = new LinkedList<>();

    @EventTarget
    public void onPacket(EventPacket eventPacket) {
        if (mode.isCurrentMode("Hypixel")) {
            Packet<?> packet = eventPacket.getPacket();
            if (eventPacket.getEventType().equals(EventType.RECIEVE))
                return;
            if (packet instanceof C00PacketKeepAlive)
                return;
            if(ModManager.getModule(Scaffold.class).getState())
                return;
//            System.out.println(eventPacket.getPacket());
            System.out.println(act + "   " + packets.size());
            if (mc.currentScreen == null || (mc.currentScreen instanceof GuiChat)) {
                return;
            }

            if (packet instanceof C09PacketHeldItemChange || packet instanceof C0EPacketClickWindow || packet instanceof C0BPacketEntityAction || packet instanceof C0FPacketConfirmTransaction) {
                act = true;
                PlayerUtil.tellPlayer(eventPacket.packet.toString());
                timer.reset();
            }
            if (act) {
                if (packet instanceof C03PacketPlayer) {
                    packets.add(packet);
                    eventPacket.setCancelled(true);
                }
            }
        }
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (timer.isDelayComplete(delay.getValue())) {
            act = false;
            timer.reset();
        }
        if (mc.currentScreen != null && !(mc.currentScreen instanceof GuiChat)) {
            isWalking = true;

//            if (mode.isCurrentMode("Hypixel")) {
//                try {
//                    int i = 0;
//                    for (; i < 8; i++) {
//                        ItemStack stack = mc.thePlayer.inventory.getStackInSlot(i);
//                        if (stack == null) break; // 空手
//                        if (!(stack.getItem() instanceof ItemFood) && !(stack.getItem() instanceof ItemSword) && Item.getIdFromItem(stack.getItem()) != 345)
//                            break; // 不能为Food Sword 指南针
//                    }
//
//                    if (i == 8 && Item.getIdFromItem(mc.thePlayer.inventory.getStackInSlot(8).getItem()) == 345) i--;
//
//
//                    mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(i));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    // TODO: handle exception
//                }
        } else {
            if(packets.size()>200)
                packets.clear();
            if (packets.size() > 0 && timer2.delay(40, true)) {
                //send packets
                Wrapper.sendPacketNoEvent(packets.get(0));
                packets.remove(0);
            }
        }

        setKeyStat();
//        } else {
//            if (isWalking) {
//                if (mode.isCurrentMode("Hypixel")) {
//                    mc.thePlayer.sendQueue
//                            .addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
//                }
//                isWalking = false;
//            }
//        }

    }

    private void setKeyStat() {
        KeyBinding[] key = {mc.gameSettings.keyBindForward, mc.gameSettings.keyBindBack,
                mc.gameSettings.keyBindLeft, mc.gameSettings.keyBindRight,
                mc.gameSettings.keyBindSprint, mc.gameSettings.keyBindJump};
        KeyBinding[] array;
        for (int length = (array = key).length, i = 0; i < length; ++i) {
            KeyBinding b = array[i];
            KeyBinding.setKeyBindState(b.getKeyCode(), Keyboard.isKeyDown(b.getKeyCode()));
        }
    }
}
