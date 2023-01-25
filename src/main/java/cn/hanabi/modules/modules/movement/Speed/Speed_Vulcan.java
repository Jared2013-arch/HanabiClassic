package cn.hanabi.modules.modules.movement.Speed;


import cn.hanabi.Wrapper;
import cn.hanabi.events.EventUpdate;
import cn.hanabi.injection.interfaces.IKeyBinding;
import cn.hanabi.utils.game.MoveUtils;
import cn.hanabi.utils.game.PlayerUtil;
import net.minecraft.client.settings.GameSettings;

import static cn.hanabi.Wrapper.mc;


public class Speed_Vulcan {

    public void onUpdate(EventUpdate e){
        Wrapper.getTimer().timerSpeed = 1.00f;
        if (Math.abs(mc.thePlayer.movementInput.moveStrafe) < 0.1f) {
            mc.thePlayer.jumpMovementFactor = 0.0265f;
        }else {
            mc.thePlayer.jumpMovementFactor = 0.0244f;
        }
        ((IKeyBinding) mc.gameSettings.keyBindSneak).setPress(GameSettings.isKeyDown(mc.gameSettings.keyBindJump)) ;

        if (MoveUtils.getSpeed() < 0.215f && !mc.thePlayer.onGround) {
            MoveUtils.strafe(0.215f);
        }
        if (mc.thePlayer.onGround && MoveUtils.isMoving()) {
            Wrapper.getTimer().timerSpeed = 1.22f;
            ((IKeyBinding) mc.gameSettings.keyBindSneak).setPress(false);
            mc.thePlayer.jump();
            MoveUtils.strafe();
            if(MoveUtils.getSpeed() < 0.5f) {
                MoveUtils.strafe(0.485f);
            }
        }else if (!PlayerUtil.isMoving()) {
            Wrapper.getTimer().timerSpeed = 1.00f;
            mc.thePlayer.motionX = 0.0;
            mc.thePlayer.motionZ = 0.0;
        }
    }
}
