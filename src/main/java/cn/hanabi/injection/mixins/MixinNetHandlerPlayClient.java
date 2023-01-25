package cn.hanabi.injection.mixins;

import cn.hanabi.events.EventTeleport;
import cn.hanabi.injection.interfaces.INetHandlerPlayClient;
import com.darkmagician6.eventapi.EventManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.PacketThreadUtil;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(NetHandlerPlayClient.class)
public class MixinNetHandlerPlayClient implements INetHandlerPlayClient {
    @Shadow
    private Minecraft gameController;

    @Final
    @Shadow
    private NetworkManager netManager;

    @Shadow
    private boolean doneLoadingTerrain;

    @Override
    public boolean getdoneLoadingTerrain(){
        return doneLoadingTerrain;
    }

}
