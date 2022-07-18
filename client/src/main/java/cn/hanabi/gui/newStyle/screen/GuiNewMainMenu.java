package cn.hanabi.gui.newStyle.screen;

import cn.hanabi.Hanabi;
import cn.hanabi.gui.classic.altmanager.GuiAltManager;
import cn.hanabi.gui.common.font.noway.ttfr.HFontRenderer;
import cn.hanabi.utils.render.ParticleUtils;
import cn.hanabi.utils.render.RenderUtil;
import net.minecraft.client.gui.*;
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
//        RenderUtil.drawImage(new ResourceLocation("Client/new/mainmenu/bg.png"), 0, 0, sr.getScaledWidth(), sr.getScaledHeight());
        RenderUtil.drawRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), -1);
        ParticleUtils.drawParticles(mouseX, mouseY);
//        RenderUtil.drawImage(new ResourceLocation("Client/new/mainmenu/logo16.png"), 5, sr.getScaledHeight() - 32, 122, 31);
//        RenderUtil.drawImage(new ResourceLocation("Client/new/mainmenu/exit.png"), sr.getScaledWidth() - 14, 6, 8, 8);
//        RenderUtil.drawImage(new ResourceLocation("Client/new/mainmenu/settings.png"), sr.getScaledWidth() - 60, sr.getScaledHeight() - 18, 10, 10);
//        Hanabi.INSTANCE.fontManager.wqy16.drawStringWithShadow("Settings", sr.getScaledWidth() - 44, sr.getScaledHeight() - 17, -1, 100);
        RenderUtil.drawImage(new ResourceLocation("Client/new/mainmenu/changelog.png"), 30, 30, 284 / 2, 263 / 2);

        RenderUtil.drawImage(new ResourceLocation("Client/new/loading/logo.png"), sr.getScaledWidth() / 2f - 128 / 4f, sr.getScaledHeight() / 2f - 148, 128 / 2f, 128 / 2f);

        int x = (sr.getScaledWidth() - 269 / 2) / 2;
        int y1 = (sr.getScaledHeight() - 261 / 2) / 2;
        int width1 = 269 / 2;
        int height1 = 261 / 2;
        RenderUtil.drawImage(new ResourceLocation("Client/new/mainmenu/selectBG.png"), x, y1, width1, height1);
        int height2 = 92 / 2;
        RenderUtil.drawImage(new ResourceLocation("Client/new/mainmenu/selection.png"), x - 10, y1 - 10, 309 / 2, height2);

        String[] strs = new String[]{"Single Player", "Multi Player", "Settings", "Alts Manager"};
        int y = y1;
        for (int i = 0; i < 4; i++) {
            HFontRenderer font = Hanabi.INSTANCE.fontManager.comfortaa18;
            String str = strs[i];
            if (RenderUtil.isHoveringAppend(mouseX, mouseY, x, y + 10, width1, 32)) {
                RenderUtil.drawImage(new ResourceLocation("Client/new/mainmenu/icons/" + str.toLowerCase() + ".png"), x + 30, y + 8, 13, 13, new Color(90, 90, 90));
                font.drawString(str, x + 50, y + 10, new Color(90, 90, 90).getRGB());
                if (Mouse.isButtonDown(0)) {
                    switch (i) {
                        case 0:
                            mc.displayGuiScreen(new GuiSelectWorld(this));
                            break;
                        case 1:
                            mc.displayGuiScreen(new GuiMultiplayer(this));
                            break;
                        case 2:
                            mc.displayGuiScreen(new GuiOptions(this, mc.gameSettings));
                            break;
                        case 3:
                            mc.displayGuiScreen(new GuiAltManager());
                    }
                }
            } else {
                RenderUtil.drawImage(new ResourceLocation("Client/new/mainmenu/icons/" + str.toLowerCase() + ".png"), x + 30, y + 8, 13, 13, new Color(197, 197, 197));
                font.drawString(str, x + 50, y + 10, new Color(197, 197, 197).getRGB());
            }
            y += 32;
        }


    }
}
