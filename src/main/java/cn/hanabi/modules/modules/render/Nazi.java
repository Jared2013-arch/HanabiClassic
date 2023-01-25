package cn.hanabi.modules.modules.render;

import cn.hanabi.events.EmoteEvent;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.EventType;
import net.minecraft.client.model.ModelBiped;

public class Nazi extends Mod {
    public int heilY;

    public Nazi() {
        super("Loli",Category.RENDER);
    }


    @Override
    public void onEnable() {
        heilY = 0;
    }

    @EventTarget
    public void onEmote(EmoteEvent event) {
        if (event.getEventType() == EventType.POST){
            if (event.entity == mc.thePlayer){
                setBiped(event.getBiped());
            }
        }
    };

    public void setBiped(ModelBiped biped) {
        if (mc.gameSettings.thirdPersonView > 0) {
            biped.bipedRightArm.rotateAngleX = 0.5F;
            biped.bipedRightArm.rotateAngleY = -2.25F;
            biped.bipedLeftArm.rotateAngleX = 0.5F;
            biped.bipedLeftArm.rotateAngleY = 2.25F;
            biped.aimedBow = true;
            biped.isChild = true;
        }
    }

}
