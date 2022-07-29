package cn.hanabi.modules.modules.movement.Fly;

import aLph4anTi1eaK_cN.Annotation.ObfuscationClass;
import cn.hanabi.injection.interfaces.IKeyBinding;
import cn.hanabi.modules.ModManager;
import cn.hanabi.modules.modules.combat.KillAura;
import cn.hanabi.modules.modules.combat.TargetStrafe;
import cn.hanabi.utils.game.MoveUtils;
import cn.hanabi.utils.game.PlayerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;


@ObfuscationClass
public class Fly_Motion {
    Minecraft mc = Minecraft.getMinecraft();

    public void onPre() {
        this.mc.thePlayer.motionY = 0.0;

        if (PlayerUtil.MovementInput()) {
            PlayerUtil.setSpeed(Fly.timer.getValueState() * 0.5);
        } else {
            PlayerUtil.setSpeed(0);
        }
        if (Fly.down.getValue() && mc.thePlayer.motionY == 0 && !mc.thePlayer.onGround && PlayerUtil.isMoving2()) {
            mc.thePlayer.motionY -= 0.01;
//            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 0.0001, mc.thePlayer.posZ, false));
        }

        if (((IKeyBinding) mc.gameSettings.keyBindSneak).getPress()) {
            mc.thePlayer.motionY -= 1;
        } else if (((IKeyBinding) mc.gameSettings.keyBindJump).getPress()) {
            mc.thePlayer.jump();
//            mc.thePlayer.motionY += 0.05;
            mc.thePlayer.motionX = 0;
            mc.thePlayer.motionZ = 0;
        }
    }

}
