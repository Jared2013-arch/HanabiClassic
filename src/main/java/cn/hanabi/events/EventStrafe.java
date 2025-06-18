package cn.hanabi.events;

import cn.hanabi.modules.modules.movement.Speed.Speed;
import com.darkmagician6.eventapi.events.callables.EventCancellable;

import static cn.hanabi.Wrapper.mc;

public class EventStrafe extends EventCancellable {
    public float forward;
    public float strafe;
    public float friction;
    public float yaw;

    public EventStrafe(float forward, float strafe, float friction, float yaw) {
        this.forward = forward;
        this.strafe = strafe;
        this.friction = friction;
        this.yaw = yaw;
    }

    public void setFriction(float friction) {
        this.friction = friction;
    }

    public void setMotion(double speed) {
        mc.thePlayer.motionX = 0;
        mc.thePlayer.motionZ = 0;
        speed *= strafe != 0 && forward != 0 ? 0.91 : 1;
        setFriction((float) speed);
    }

    public void setMotionLegit(float friction) {
        setFriction(mc.thePlayer.onGround ? friction : friction * 0.43F);
    }

    public void setMotionPartialStrafe(float friction, float strafeComponent) {
        float remainder = 1F - strafeComponent;
        if (forward != 0 && strafe != 0)
            friction *= 0.91;
        if (mc.thePlayer.onGround) {
            setMotion(friction);
        } else if (!Speed.dmg.getValue() || mc.thePlayer.hurtTime > 0) {
            mc.thePlayer.motionX *= strafeComponent;
            mc.thePlayer.motionZ *= strafeComponent;
            setFriction(friction * remainder);
        }
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }
}
