package cn.hanabi.events;

import com.darkmagician6.eventapi.events.callables.EventCancellable;

public class EventJump extends EventCancellable {
    public float yaw;

    public EventJump(float yaw) {
        this.yaw = yaw;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }
}
