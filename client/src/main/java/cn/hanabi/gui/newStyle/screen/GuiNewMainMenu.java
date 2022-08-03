package cn.hanabi.gui.newStyle.screen;

import cn.hanabi.Hanabi;
import cn.hanabi.gui.classic.altmanager.GuiAltManager;
import cn.hanabi.gui.common.font.noway.ttfr.HFontRenderer;
import cn.hanabi.utils.math.AnimationUtil;
import cn.hanabi.utils.render.ParticleUtils;
import cn.hanabi.utils.render.RenderUtil;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

import java.awt.*;

public class GuiNewMainMenu extends GuiScreen {
    private float selectionAnimY;
    private int i1;
    private String cur = "";

    @Override
    public void initGui() {
        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        ScaledResolution sr = new ScaledResolution(mc);
        RenderUtil.drawRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), -1);
        ParticleUtils.drawParticles(mouseX, mouseY);

        RenderUtil.drawImage(new ResourceLocation("Client/new/mainmenu/changelog.png"), 30, 30, 284 / 2, 263 / 2);
        Hanabi.INSTANCE.fontManager.wqy16.drawString("[+] Design improvement", 50, 70, new Color(160, 160, 160).getRGB());
        Hanabi.INSTANCE.fontManager.wqy16.drawString("[+] Bypasses", 50, 85, new Color(160, 160, 160).getRGB());
        Hanabi.INSTANCE.fontManager.wqy16.drawString("[+] Verify fix", 50, 100, new Color(160, 160, 160).getRGB());
        Hanabi.INSTANCE.fontManager.wqy16.drawString("[~] Bug fixes", 50, 115, new Color(160, 160, 160).getRGB());


        RenderUtil.drawImage(new ResourceLocation("Client/new/loading/logo.png"), sr.getScaledWidth() / 2f - 128 / 4f, sr.getScaledHeight() / 2f - 148, 128 / 2f, 128 / 2f);

        int x = (sr.getScaledWidth() - 269 / 2) / 2;
        int y1 = (sr.getScaledHeight() - 261 / 2) / 2;
        int width1 = 269 / 2;
        int height1 = 248 / 2;
        RenderUtil.drawImage(new ResourceLocation("Client/new/mainmenu/selectBG.png"), x, y1, width1, height1);
        int height2 = 92 / 2;

        String[] strs = new String[]{"Single Player", "Multi Player", "Settings", "Alts Manager"};
        int y = y1;
        GlStateManager.disableBlend();
        RenderUtil.drawCustomImage2(x - 10, selectionAnimY, 309 / 2f, height2, new ResourceLocation("Client/new/mainmenu/selection.png"));
        if (selectionAnimY == 0) {
            selectionAnimY = y - 10;
        }
        selectionAnimY = AnimationUtil.moveUD(selectionAnimY, (float) i1, 0.4f, 0.3f);

        for (int i = 0; i < 4; i++) {
            HFontRenderer font = Hanabi.INSTANCE.fontManager.comfortaa18;
            String str = strs[i];
            boolean hoveringAppend = RenderUtil.isHoveringAppend(mouseX, mouseY, x, y, width1, 32);
            if (hoveringAppend || cur.equals(str)) {
                i1 = y - 10;
                cur = str;
                RenderUtil.drawImage(new ResourceLocation("Client/new/mainmenu/icons/" + str.toLowerCase() + ".png"), x + 30, y + 8, 13, 13, new Color(90, 90, 90));
                font.drawString(str, x + 50, y + 10, new Color(90, 90, 90).getRGB());
                GlStateManager.disableBlend();
                if (hoveringAppend && Mouse.isButtonDown(0)) {
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
                font.drawString(str, x + 50, y + 10, new Color(197, 197, 197).getRGB());
                GlStateManager.disableBlend();
                RenderUtil.drawImage(new ResourceLocation("Client/new/mainmenu/icons/" + str.toLowerCase() + ".png"), x + 30, y + 8, 13, 13, new Color(197, 197, 197));
            }
            y += 32;
        }


    }
}
