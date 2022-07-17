package cn.hanabi.modules.modules.combat;

import cn.hanabi.events.EventPreMotion;
import cn.hanabi.injection.interfaces.IKeyBinding;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.utils.game.InvUtils;
import cn.hanabi.utils.game.PlayerUtil;
import cn.hanabi.utils.math.TimeHelper;
import cn.hanabi.value.Value;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.potion.Potion;

public class AutoHead extends Mod {
    public static Value<Double> slot = new Value<>("AutoEat", "Slot", 7.0, 1.0, 9.0, 1.0);
    public static Value<Double> delay = new Value<>("AutoEat", "Delay", 0.0, 0.0, 2.0, 0.05);
    public static Value<Integer> packets = new Value<>("AutoEat", "PacketAmount", 30, 0, 100, 1);
    public static Value<Double> health = new Value<>("AutoEat", "Health", 14.0, 1.0, 20.0, 0.5);
    public static Value<String> healMode = new Value<>("AutoEat", "HealMode", 0);

    TimeHelper timer = new TimeHelper();
    private int tempSlot;


    public AutoHead() {
        super("AutoEat", Category.COMBAT);
        healMode.LoadValue(new String[]{"Regen", "Absorption", "GApple"});
    }


    @EventTarget
    public void onMotion(EventPreMotion event) {
        if(!timer.isDelayComplete(delay.getValue() * 1000))
            return;
        if (mc.thePlayer.getHealth() >= health.getValue() || (healMode.isCurrentMode("Regen") && mc.thePlayer.isPotionActive(Potion.regeneration)) || (healMode.isCurrentMode("Absorption") && mc.thePlayer.getAbsorptionAmount() != 0)) {
            ((IKeyBinding) mc.gameSettings.keyBindUseItem).setPress(false);
            mc.thePlayer.inventory.currentItem = tempSlot;
            return;
        }


        InventoryPlayer inventory = mc.thePlayer.inventory;
        int slot = 0;
        if (healMode.isCurrentMode("GApple")) {
            slot = getGAppleFromInventory();
        } else {
            slot = getHeadFromInventory();
        }

        if (slot == -1)
            return;

        tempSlot = inventory.currentItem;
        PlayerUtil.debug(slot);

        if (healMode.isCurrentMode("GApple")) {
            mc.thePlayer.inventory.currentItem = slot - 36;
            ((IKeyBinding) mc.gameSettings.keyBindUseItem).setPress(true);
            for (int i = 0; i < packets.getValue(); i++) {
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(mc.thePlayer.onGround));
            }
        } else {
            mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(slot - 36));
            mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(inventory.getCurrentItem()));
            mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(tempSlot));
        }

        timer.reset();
    }

    private int getGAppleFromInventory() {
        for (int i = 36; i < 45; i++) {
            ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

            if (InvUtils.isItemEmpty(stack.getItem()))
                continue;

            if (stack.getItem() instanceof ItemAppleGold)
                return i;
        }

        for (int i = 9; i < 36; i++) {
            ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

            if (InvUtils.isItemEmpty(stack.getItem()))
                continue;

            if (Item.getIdFromItem(stack.getItem()) != 397)
                continue;

            mc.playerController.windowClick(mc.thePlayer.openContainer.windowId, i, slot.getValue().intValue(), 2, mc.thePlayer);
        }

        return -1;
    }

    private int getHeadFromInventory() {
        for (int i = 36; i < 45; i++) {
            ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

            if (InvUtils.isItemEmpty(stack.getItem()))
                continue;

            if (Item.getIdFromItem(stack.getItem()) != 397)
                continue;

            return i;
        }

        for (int i = 9; i < 36; i++) {
            ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

            if (InvUtils.isItemEmpty(stack.getItem()))
                continue;

            if (Item.getIdFromItem(stack.getItem()) != 397)
                continue;

            mc.playerController.windowClick(mc.thePlayer.openContainer.windowId, i, slot.getValue().intValue(), 2, mc.thePlayer);
        }

        return -1;
    }
}
