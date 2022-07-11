package cn.hanabi.gui.newStyle.screen;

import cn.hanabi.Hanabi;
import cn.hanabi.gui.classic.altmanager.GuiAltManager;
import cn.hanabi.gui.common.font.noway.ttfr.HFontRenderer;
import cn.hanabi.utils.render.ParticleUtils;
import cn.hanabi.utils.render.RenderUtil;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

import java.awt.*;

public class GuiNewMainMenu extends GuiScreen {
    @Override
    public void initGui() {
        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        ScaledResolution sr = new ScaledResolution(mc);
        RenderUtil.drawImage(new ResourceLocation("Client/new/mainmenu/bg.png"), 0, 0, sr.getScaledWidth(), sr.getScaledHeight());
        ParticleUtils.drawParticles(mouseX, mouseY);
        RenderUtil.drawImage(new ResourceLocation("Client/new/mainmenu/logo.png"), 5, sr.getScaledHeight() - 32, 122, 31);
        RenderUtil.drawImage(new ResourceLocation("Client/new/mainmenu/exit.png"), sr.getScaledWidth() - 14, 6, 8, 8);
        RenderUtil.drawImage(new ResourceLocation("Client/new/mainmenu/settings.png"), sr.getScaledWidth() - 60, sr.getScaledHeight() - 18, 10, 10);
        Hanabi.INSTANCE.fontManager.wqy16.drawStringWithShadow("Settings", sr.getScaledWidth() - 44, sr.getScaledHeight() - 17, -1, 100);
        String[] strs = new String[]{"Alts Manager", "Multi Players", "Single Player"};
        for (int i = 0; i < 3; i++) {
            int y = sr.getScaledHeight() - 70 - i * 25;
            HFontRenderer font = Hanabi.INSTANCE.fontManager.comfortaa20;
            String str = strs[i];
            if (RenderUtil.isHoveringAppend(mouseX, mouseY, 30, y - 5, font.getStringWidth(str), font.FONT_HEIGHT + 8)) {
                font.drawStringWithShadow(str, 32, y, -1, 100);
                if (Mouse.isButtonDown(0)) {
                    switch (i) {
                        case 0:
                            mc.displayGuiScreen(new GuiAltManager());
                            break;
                        case 1:
                            mc.displayGuiScreen(new GuiMultiplayer(this));
                            break;
                        case 2:
                            mc.displayGuiScreen(new GuiSelectWorld(this));
                            break;
                    }
                }
            } else {
                font.drawStringWithShadow(str, 30, y, new Color(200, 200, 200).getRGB(), 100);
            }
        }


    }
}
