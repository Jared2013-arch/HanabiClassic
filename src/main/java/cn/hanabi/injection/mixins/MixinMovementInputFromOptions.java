package cn.hanabi.injection.mixins;

import cn.hanabi.events.EventMoveInput;
import cn.hanabi.modules.ModManager;
import cn.hanabi.modules.modules.world.UhcHelper;
import com.darkmagician6.eventapi.EventManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.MovementInputFromOptions;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MovementInputFromOptions.class)
public class MixinMovementInputFromOptions extends MixinMovementInput{

    @Final
    @Shadow
    private GameSettings gameSettings;

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void updatePlayerMoveState() {
        this.moveStrafe = 0.0F;
        this.moveForward = 0.0F;
        if (this.gameSettings.keyBindForward.isKeyDown()) {
            ++this.moveForward;
        }

        if (this.gameSettings.keyBindBack.isKeyDown()) {
            --this.moveForward;
        }

        if (this.gameSettings.keyBindLeft.isKeyDown()) {
            ++this.moveStrafe;
        }

        if (this.gameSettings.keyBindRight.isKeyDown()) {
            --this.moveStrafe;
        }
        this.jump = this.gameSettings.keyBindJump.isKeyDown();
        this.sneak = this.gameSettings.keyBindSneak.isKeyDown();


        final EventMoveInput moveInputEvent = new EventMoveInput(moveForward, moveStrafe, jump, sneak, 0.3D);
        EventManager.call(moveInputEvent);

        final double sneakMultiplier = moveInputEvent.getSneakSlowDownMultiplier();
        this.moveForward = moveInputEvent.getForward();
        this.moveStrafe = moveInputEvent.getStrafe();
        this.jump = moveInputEvent.isJump();
        this.sneak = moveInputEvent.isSneak();

        if (this.sneak) {
            this.moveStrafe = (float) ((double) this.moveStrafe * sneakMultiplier);
            this.moveForward = (float) ((double) this.moveForward * sneakMultiplier);
        }
        if (ModManager.getModule(UhcHelper.class).isEnabled() && ModManager.getModule(UhcHelper.class).sneakily.getValue() && this.sneak)
        {
            this.moveStrafe = (float)((double)this.moveStrafe / 0.3);
            this.moveForward = (float)((double)this.moveForward / 0.3);
        }
    }
}

