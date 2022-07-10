package cn.hanabi.events;

import com.darkmagician6.eventapi.events.Event;
import com.darkmagician6.eventapi.events.callables.EventCancellable;
import net.minecraft.network.play.client.C03PacketPlayer;

public final class EventTeleport extends EventCancellable {
    private C03PacketPlayer.C06PacketPlayerPosLook response;
    private double posX;
    private double posY;
    private double posZ;
    private float yaw;
    private float pitch;

    public EventTeleport(C03PacketPlayer.C06PacketPlayerPosLook c06PacketPlayerPosLook, double d0, double d1, double d2, float f, float f1) {
        this.response = c06PacketPlayerPosLook;
        this.posX = d0;
        this.posY = d1;
        this.posZ = d2;
        this.yaw = f;
        this.pitch = f1;
    }
    public double getPosX(){
        return posX;
    }
    public double getPosY(){
        return posY;
    }
    public double getPosZ(){
        return posZ;
    }
    public float getYaw(){
        return yaw;
    }
    public float getPitch(){
        return pitch;
    }
    public C03PacketPlayer.C06PacketPlayerPosLook getResponse(){
        return response;
    }
    public void setPosX(double posX){
        this.posX = posX;
    }
    public void setPosY(double posY){
        this.posY = posY;
    }
    public void setPosZ(double posZ){
        this.posZ = posZ;
    }
}
