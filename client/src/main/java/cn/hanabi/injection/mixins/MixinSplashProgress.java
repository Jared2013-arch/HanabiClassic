package cn.hanabi.injection.mixins;

import net.minecraftforge.fml.client.SplashProgress;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(SplashProgress.class)
public class MixinSplashProgress {
    /**
     * @author SuperSkidder
     * @reason cancel forge loading progress
     */
    @Overwrite
    public static void start() {
    }
}
