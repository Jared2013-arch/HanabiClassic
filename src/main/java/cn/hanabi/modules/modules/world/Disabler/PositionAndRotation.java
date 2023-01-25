package cn.hanabi.modules.modules.world.Disabler;

import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public class PositionAndRotation {
	
	private double x, y, z;
	private float yaw, pitch;

	public PositionAndRotation(double x, double y, double z, float yaw, float pitch) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }
	
    public PositionAndRotation(C03PacketPlayer packet) {
        this.x = packet.getPositionX();
        this.y = packet.getPositionY();
        this.z = packet.getPositionZ();
        this.yaw = packet.getYaw();
        this.pitch = packet.getPitch();
    }
    
    public PositionAndRotation(S08PacketPlayerPosLook packet) {
        this.x = packet.getX();
        this.y = packet.getY();
        this.z = packet.getZ();
        this.yaw = packet.getYaw();
        this.pitch = packet.getPitch();
    }
    
    public void setPositionAndRotation(double x, double y, double z, float yaw, float pitch) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }
    
    public void setPositionAndRotation(S08PacketPlayerPosLook packet) {
        this.x = packet.getX();
        this.y = packet.getY();
        this.z = packet.getZ();
        this.yaw = packet.getYaw();
        this.pitch = packet.getPitch();
    }
    
    public void reset() {
    	this.x = 0;
        this.y = 0;
        this.z = 0;
        this.yaw = 0;
        this.pitch = 0;
    }
    
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }
    
    public float getYaw() {
		return yaw;
	}
    
    public float getPitch() {
		return pitch;
	}

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }
    
    public void setYaw(float yaw) {
		this.yaw = yaw;
	}
    
    public void setPitch(float pitch) {
		this.pitch = pitch;
	}
}
