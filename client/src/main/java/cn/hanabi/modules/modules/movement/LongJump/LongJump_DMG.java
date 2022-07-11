package cn.hanabi.modules.modules.movement.LongJump;

import cn.hanabi.events.*;
import cn.hanabi.modules.ModManager;
import cn.hanabi.utils.bypass.DamageUtil;
import cn.hanabi.utils.game.MoveUtils;
import cn.hanabi.utils.game.PlayerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;

public class LongJump_DMG {

    final Minecraft mc = Minecraft.getMinecraft();
    private int stage, tick;
    private double speed, last;
    private boolean dmged;
    private boolean jumped;


    public void onEnable() {
        tick = 1;
        speed = 0;
        last = 0;
        jumped = false;
        dmged = true;
        stage = 0;
    }

    public void onDisable() {
        PlayerUtil.setSpeed(0);
    }

    public void onPre(EventPreMotion e) {
        if (!jumped && !mc.thePlayer.onGround && mc.thePlayer.motionY > 0) {
            jumped = true;
        }
        if (tick == 1){
            DamageUtil.legitDamage();
            tick = 2;
        }
    }


    public void onPost(EventPostMotion e) {
    }


    public void onMove(EventMove event) {
        if (!dmged && stage > 0)
            PlayerUtil.setSpeed(event, 0);
        else {
            if (stage == 4) {
                event.setY(mc.thePlayer.motionY = PlayerUtil.getBaseJumpHeight() * 1.5F);
                speed = PlayerUtil.getBaseMoveSpeed() * 1.2;
            } else if (stage >= 4 && stage != 5) {
                if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0, mc.thePlayer.motionY, 0)).isEmpty() || mc.thePlayer.isCollidedVertically && mc.thePlayer.onGround)
                    ModManager.getModule("LongJump").set(false);
                event.setY(mc.thePlayer.motionY = mc.thePlayer.motionY + (.005F - (speed / PlayerUtil.getBaseMoveSpeed()) * .005F));
                speed = last - last / 159;
            }
            stage++;
            speed = Math.max(speed, PlayerUtil.getBaseMoveSpeed());
            MoveUtils.setMotion(event, speed);
        }
    }

    private int getArrowCount() {
        int arrowCount = 0;
        for (int i = 0; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (is.getItem() == Item.getItemById(262)) {
                    arrowCount += is.stackSize;
                }
            }
        }
        return arrowCount;
    }

    private int getBowCount() {
        int bowCount = 0;
        for (int i = 0; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (is.getItem() instanceof ItemBow) {
                    bowCount += is.stackSize;
                }
            }
        }
        return bowCount;
    }
}