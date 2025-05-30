package cn.hanabi.events;

import com.darkmagician6.eventapi.events.Event;

public class EventPostMotion implements Event {

    public float pitch;
    public float yaw;

    public EventPostMotion(float pitch, float yaw) {
        //Memory prevPitch and Pitch for rotation animation
        this.pitch = pitch;
        this.yaw = yaw;

        EventPreMotion.RPPITCH = EventPreMotion.RPITCH;
        EventPreMotion.RPITCH = pitch;
    }
}
