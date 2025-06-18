package cn.hanabi.injection.mixins;

import cn.hanabi.events.EventLook;
import cn.hanabi.injection.interfaces.IEntityLivingBase;
import cn.hanabi.modules.ModManager;
import cn.hanabi.modules.modules.movement.NoJumpDelay;
import cn.hanabi.modules.modules.render.HUD;
import cn.hanabi.modules.modules.render.HitAnimation;
import cn.hanabi.modules.modules.world.Scaffold;
import cn.hanabi.utils.client.SoundFxPlayer;
import com.darkmagician6.eventapi.EventManager;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityLivingBase.class)
public abstract class MixinEntityLivingBase implements IEntityLivingBase {
    @Shadow
    private int jumpTicks;

    @Shadow
    public abstract boolean isPotionActive(Potion potionIn);

    @Shadow
    public abstract PotionEffect getActivePotionEffect(Potion potionIn);

    /**
     * @author
     */
    @Overwrite
    private int getArmSwingAnimationEnd() {
        int speed = this.isPotionActive(Potion.digSpeed) ? 6 - (1 + this.getActivePotionEffect(Potion.digSpeed).getAmplifier()) : (this.isPotionActive(Potion.digSlowdown) ? 6 + (1 + this.getActivePotionEffect(Potion.digSlowdown).getAmplifier()) * 2 : 6);
        if ((this.equals(Minecraft.getMinecraft().thePlayer)) && ModManager.getModule("HitAnimation").getState()) {
            speed = (int) (speed * (ModManager.getModule(HitAnimation.class).swingSpeed.getValue()));
        }
        return speed;
    }


    @Inject(method = "onLivingUpdate", at = @At("HEAD"))
    private void headLiving(CallbackInfo callbackInfo) {
        if (ModManager.getModule(NoJumpDelay.class).isEnabled())
            jumpTicks = 0;
    }


      @Inject(method = "handleStatusUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;playSound(Ljava/lang/String;FF)V"), cancellable = true)
       private void handleSound(CallbackInfo callbackInfo) {
          if (!((HUD)ModManager.getModule("HUD")).hitsound.isCurrentMode("Minecraft")) {
              if (((HUD)ModManager.getModule("HUD")).hitsound.isCurrentMode("Ding"))
                  new SoundFxPlayer().playSound(SoundFxPlayer.SoundType.Ding, -7);
              else
                  new SoundFxPlayer().playSound(SoundFxPlayer.SoundType.Crack, -7);
              callbackInfo.cancel();
          }
       }


    @Inject(method = "attackEntityFrom", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;playSound(Ljava/lang/String;FF)V"), cancellable = true)
    private void handleSound(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (!((HUD)ModManager.getModule("HUD")).hitsound.isCurrentMode("Minecraft")) {
            if (((HUD)ModManager.getModule("HUD")).hitsound.isCurrentMode("Ding"))
                new SoundFxPlayer().playSound(SoundFxPlayer.SoundType.Ding, -7);
            else
                new SoundFxPlayer().playSound(SoundFxPlayer.SoundType.Crack, -7);
            cir.cancel();
        }
    }


    @Override
    public int runGetArmSwingAnimationEnd() {
        return this.getArmSwingAnimationEnd();
    }

    @Override
    public int getJumpTicks() {
        return jumpTicks;
    }

    @Override
    public void setJumpTicks(int a) {
        jumpTicks = a;
    }


    /**
     * @author
     * @reason
     */
    @Overwrite
    public Vec3 getLook(float partialTicks) {
        float rotationYaw = ((EntityLivingBase) (Object)this).rotationYaw;
        float rotationPitch = ((EntityLivingBase) (Object)this).rotationPitch;
        if (ModManager.getModule(Scaffold.class).isEnabled()) {
            rotationPitch = Scaffold.rotationPitch;
            rotationYaw = Scaffold.rotationYaw;
            return this.getVector(rotationPitch, rotationYaw);
        }

        EventLook eventLook = new EventLook(rotationYaw, rotationPitch);
        EventManager.call(eventLook);

        if (partialTicks == 1.0F) {
            return this.getVector(rotationPitch, rotationYaw);
        } else {
            float f = ((EntityLivingBase) (Object)this).prevRotationPitch + (rotationPitch - ((EntityLivingBase) (Object)this).prevRotationPitch) * partialTicks;
            float f1 = ((EntityLivingBase) (Object)this).prevRotationYaw + (rotationYaw - ((EntityLivingBase) (Object)this).prevRotationYaw) * partialTicks;
            return this.getVector(f, f1);
        }
    }

    protected Vec3 getVector(float pitch, float yaw) {
        float f = MathHelper.cos(-yaw * 0.017453292F - (float) Math.PI);
        float f1 = MathHelper.sin(-yaw * 0.017453292F - (float) Math.PI);
        float f2 = -MathHelper.cos(-pitch * 0.017453292F);
        float f3 = MathHelper.sin(-pitch * 0.017453292F);
        return new Vec3((double) (f1 * f2), (double) f3, (double) (f * f2));
    }


}
