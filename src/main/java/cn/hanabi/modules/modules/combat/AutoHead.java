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
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.potion.Potion;
import scala.actors.TIMEOUT;

public class AutoHead extends Mod {
    public static Value<Double> slot = new Value<>("AutoEat", "Slot", 7.0, 1.0, 9.0, 1.0);
    public static Value<Double> delay = new Value<>("AutoEat", "Delay", 0.0, 0.0, 2.0, 0.05);
    public static Value<Integer> packets = new Value<>("AutoEat", "PacketAmount", 30, 0, 100, 1);
    public static Value<Double> health = new Value<>("AutoEat", "Health", 14.0, 1.0, 20.0, 0.5);
    public static Value<String> healMode = new Value<>("AutoEat", "HealMode", 0);

    public Value<Boolean> head = new Value<>("AutoEat", "Head", false);
    public Value<Boolean> gApple = new Value<>("AutoEat", "GApple", true);
    public Value<Boolean> beef = new Value<>("AutoEat", "Beef", true);

    TimeHelper timer = new TimeHelper();
    private int tempSlot;
    private boolean isEating;


    public AutoHead() {
        super("AutoEat", Category.COMBAT);
        healMode.LoadValue(new String[]{"Regen", "Absorption"});
    }


    @EventTarget
    public void onMotion(EventPreMotion event) {
        if (timer.isDelayComplete(delay.getValue() * 1000)) {
            if ((healMode.isCurrentMode("Regen") && !mc.thePlayer.isPotionActive(Potion.regeneration) ||
                    (healMode.isCurrentMode("Absorption") && mc.thePlayer.getAbsorptionAmount() == 0)) &&
                    head.getValue() && !isEating) {
                int slot = 0;
                slot = getGAppleFromInventory();
                tempSlot = mc.thePlayer.inventory.currentItem;
                mc.thePlayer.inventory.currentItem = slot - 36;
                mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(slot - 36));
                mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
                mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(tempSlot));
                mc.thePlayer.inventory.currentItem = tempSlot;
                timer.reset();
            }

            if ((mc.thePlayer.getHealth() / mc.thePlayer.getMaxHealth()) < 0.8f && gApple.getValue() && !isEating) {
                isEating = true;
                int slot = 0;
                slot = getBeefFromInventory();
                tempSlot = mc.thePlayer.inventory.currentItem;
                mc.thePlayer.inventory.currentItem = slot - 36;
                ((IKeyBinding) (mc.gameSettings.keyBindUseItem)).setPress(true);
            } else if ((mc.thePlayer.getHealth() / mc.thePlayer.getMaxHealth()) > 0.8f && gApple.getValue() && isEating) {
                isEating = false;
                mc.thePlayer.inventory.currentItem = tempSlot;
                ((IKeyBinding) (mc.gameSettings.keyBindUseItem)).setPress(false);
                timer.reset();
            }

            if (mc.thePlayer.getFoodStats().getFoodLevel() < 20f && beef.getValue() && !isEating) {
                isEating = true;
                int slot = 0;
                slot = getBeefFromInventory();
                tempSlot = mc.thePlayer.inventory.currentItem;
                mc.thePlayer.inventory.currentItem = slot - 36;
                ((IKeyBinding) (mc.gameSettings.keyBindUseItem)).setPress(true);
            } else if (mc.thePlayer.getFoodStats().getFoodLevel() == 20f && beef.getValue() && isEating) {
                isEating = false;
                mc.thePlayer.inventory.currentItem = tempSlot;
                ((IKeyBinding) (mc.gameSettings.keyBindUseItem)).setPress(false);
                timer.reset();
            }


        }
    }

    private int getBeefFromInventory() {
        for (int i = 36; i < 45; i++) {
            ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

            if (InvUtils.isItemEmpty(stack.getItem()))
                continue;

            if (stack.getItem() instanceof ItemFood)
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
