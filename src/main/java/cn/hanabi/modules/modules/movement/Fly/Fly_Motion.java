package cn.hanabi.modules.modules.movement.Fly;


import cn.hanabi.injection.interfaces.IKeyBinding;
import cn.hanabi.utils.game.PlayerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;


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
            mc.thePlayer.motionY -= 0.005;
            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
        }

//        if (PlayerUtil.isMoving2() && mc.gameSettings.keyBindJump.isKeyDown()) {
//            if (!ModManager.getModule(Blink.class).getState())
//                ModManager.getModule(Blink.class).setState(true);
//        } else {
//            if (ModManager.getModule(Blink.class).getState())
//                ModManager.getModule(Blink.class).setState(false);
//        }

        if (((IKeyBinding) mc.gameSettings.keyBindSneak).getPress()) {
            mc.thePlayer.motionY -= 1;
        } else if (((IKeyBinding) mc.gameSettings.keyBindJump).getPress()) {
            mc.thePlayer.motionY += 0.42;
            mc.thePlayer.motionX = 0;
            mc.thePlayer.motionZ = 0;
        }
    }

}
