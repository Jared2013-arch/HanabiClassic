package cn.hanabi.modules.modules.render;

import cn.hanabi.gui.AWClickGUI;
import cn.hanabi.gui.classic.clickui.ClickUI;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.modules.ModManager;
import cn.hanabi.utils.client.SoundFxPlayer;
import cn.hanabi.utils.game.PlayerUtil;
import cn.hanabi.value.Value;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;

public class ClickGUIModule extends Mod {
    public static ClickUI classicGui = new ClickUI();
    public static AWClickGUI awGui = new AWClickGUI();
    private final Value<String> mode = new Value<String>("ClickGUI", "Mode", 0).LoadValue(new String[]{"Classic", "New", "AzureWare", "5.0"});
    public static Value<Boolean> blur = new Value<>("ClickGUI", "Blur", true);
    ScaledResolution sr;
    int lastWidth = 0;

    public ClickGUIModule() {
        super("ClickGUI", Category.RENDER, true, false, Keyboard.KEY_RSHIFT);
        setState(false);
    }

    @Override
    protected void onEnable() {
        if (mc.thePlayer == null)
            return;

        if (mc.currentScreen instanceof ClickUI) {
            this.setState(false);
            return;
        }

        if (!((HUD) ModManager.getModule("HUD")).sound.isCurrentMode("Minecraft"))
            new SoundFxPlayer().playSound(SoundFxPlayer.SoundType.ClickGuiOpen, -4);
        String mode = this.mode.getModeAt(this.mode.getCurrentMode());
        if (mode.equals("Classic")) {
            mc.displayGuiScreen(classicGui);
        }else if (mode.equals("New")) {
            mc.displayGuiScreen(classicGui);
        }else if (mode.equals("AzureWare")) {
            mc.displayGuiScreen(awGui);
        } else if (mode.equals("5.0")) {
            mc.displayGuiScreen(classicGui);
        }
        setState(false);
    }
}
