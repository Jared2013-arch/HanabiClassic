package cn.hanabi.modules.modules.movement;

import cn.hanabi.events.EventUpdate;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.value.Value;
import com.darkmagician6.eventapi.EventTarget;

public class MotionBoost extends Mod {
    private static Value<Double> x = new Value<>("MotionBoost", "X", 2d, 0d, 8d, 0.1);
    private static Value<Double> y = new Value<>("MotionBoost", "Y", 2d, 0d, 8d, 0.1);
    private static Value<Double> z = new Value<>("MotionBoost", "Z", 2d, 0d, 8d, 0.1);

    public MotionBoost() {
        super("MotionBoost", Category.MOVEMENT);
    }

    @EventTarget
    public void onUpdate(EventUpdate e) {
        if(mc.thePlayer != null && mc.thePlayer.hurtTime == 9){
            mc.thePlayer.motionX *= x.getValue();
            mc.thePlayer.motionY *= y.getValue();
            mc.thePlayer.motionZ *= z.getValue();
        }
    }
}
