package cn.hanabi.injection.mixins;

import cn.hanabi.Hanabi;
import cn.hanabi.gui.newStyle.screen.GuiNewMainMenu;
import cn.hanabi.gui.newStyle.screen.GuiSwitcher;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiMainMenu.class)
public class MixinGuiMainMenu {


    @Inject(method = "initGui", at = @At("HEAD"), cancellable = true)
    public void onInit(CallbackInfo ci) {
        if(!Hanabi.INSTANCE.selected) {
            Minecraft.getMinecraft().displayGuiScreen(new GuiSwitcher());
        }else {
            if (!Hanabi.INSTANCE.newStyle) {
                Minecraft.getMinecraft().displayGuiScreen(new me.yarukon.mainmenu.GuiCustomMainMenu());
            } else {
                Minecraft.getMinecraft().displayGuiScreen(new GuiNewMainMenu());
            }
        }
        ci.cancel();
    }
}
