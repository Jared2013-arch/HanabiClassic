package cn.hanabi.injection.mixins;

import cn.hanabi.injection.interfaces.INetHandlerPlayClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(NetHandlerPlayClient.class)
public class MixinNetHandlerPlayClient implements INetHandlerPlayClient {

    @Shadow
    private boolean doneLoadingTerrain;

    @Override
    public boolean getdoneLoadingTerrain(){
        return doneLoadingTerrain;
    }

}
