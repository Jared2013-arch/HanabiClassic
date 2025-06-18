package cn.hanabi.events;

import cn.hanabi.utils.Vector2f;
import com.darkmagician6.eventapi.events.Event;

public class EventLook implements Event {
    public float yaw;
    public float pitch;

    public EventLook(float yaw, float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public void setRotation(Vector2f rotations) {
        this.yaw = rotations.x;
        this.pitch = rotations.y;
    }
}
