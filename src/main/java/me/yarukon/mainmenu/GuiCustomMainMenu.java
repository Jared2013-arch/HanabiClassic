package me.yarukon.mainmenu;

import cn.hanabi.Client;
import cn.hanabi.Hanabi;
import cn.hanabi.gui.classic.altmanager.GuiAltManager;
import cn.hanabi.modules.Mod;
import cn.hanabi.utils.color.Colors;
import cn.hanabi.utils.math.TimeHelper;
import cn.hanabi.utils.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class GuiCustomMainMenu extends GuiScreen {

    public ArrayList<MenuSlot> slots = new ArrayList<MenuSlot>();
    public ScaledResolution sr;

    public TimeHelper timer = new TimeHelper();

    public boolean coolDown = true;

    @Override
    public void initGui() {
        sr = new ScaledResolution(Minecraft.getMinecraft());

        this.coolDown = true;

        this.timer.reset();

        this.slots.clear();

        this.slots.add(new MenuSlot("单人游戏", HanabiFonts.ICON_MAINMENU_SINGLE, new GuiSelectWorld(this)));
        this.slots.add(new MenuSlot("多人游戏", HanabiFonts.ICON_MAINMENU_MULTI, new GuiMultiplayer(this)));
        this.slots.add(new MenuSlot("黑卡管理", HanabiFonts.ICON_MAINMENU_ALTMANAGER, new GuiAltManager()));
        this.slots.add(new MenuSlot("游戏设置", HanabiFonts.ICON_MAINMENU_SETTINGS, new GuiOptions(this, this.mc.gameSettings)));
        this.slots.add(new MenuSlot("退出游戏", HanabiFonts.ICON_MAINMENU_QUITGAME, null));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        sr = new ScaledResolution(Minecraft.getMinecraft());

        if (this.coolDown) {
            if (this.timer.isDelayComplete(500L)) {
                coolDown = false;
                timer.reset();
            }
        }

        if (!Client.onDebug) {
            RenderUtil.drawImage(new ResourceLocation("Client/mainmenu/mainmenu.png"), 0, 0, sr.getScaledWidth(),
                    sr.getScaledHeight());
        } else {
            RenderUtil.drawImage(new ResourceLocation("Client/mainmenu/scifi.png"), 0, 0, sr.getScaledWidth(),
                    sr.getScaledHeight());
        }


        // 居中显示 每个圆形之间间隔10像素 每个圆形40像素的直径
        int defaultX = sr.getScaledWidth() / 2 - 150;
        int defaultY = sr.getScaledHeight() / 2;

        for (MenuSlot slot : this.slots) {
            slot.draw(mouseX, mouseY, defaultX, defaultY);

            if (!this.coolDown) {
                slot.onClick(mouseX, mouseY, defaultX, defaultY);
            }

            defaultX += 70;
        }

        String t1 = "欢迎回来, " + Hanabi.INSTANCE.username + "!";
        Hanabi.INSTANCE.fontManager.comfortaa18.drawString(t1,
                (float) (sr.getScaledWidth() - 55 - Hanabi.INSTANCE.fontManager.comfortaa18.getStringWidth(t1)), 20, -1);


        GL11.glPushMatrix();
        GL11.glColor4f(1, 1, 1, 0f);
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDepthMask(false);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        if (!Client.onDebug) {
            Minecraft.getMinecraft().getRenderManager().renderEngine
                    .bindTexture(new ResourceLocation("Client/mainmenu/avatar.png"));
            this.drawScaledTexturedModalRect(sr.getScaledWidth() - 46, 9, 0, 0, 32, 32, 8.1f);
        } else {
            Minecraft.getMinecraft().getRenderManager().renderEngine
                    .bindTexture(new ResourceLocation("Client/mainmenu/ava.png"));
            this.drawScaledTexturedModalRect(sr.getScaledWidth() - 46, 9, 0, 0, 32, 32, 8.1f);
        }
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        RenderUtil.drawArc(sr.getScaledWidth() - 30, 25, 16, Colors.WHITE.c, 0, 360, 2);
        GL11.glPopMatrix();

        Hanabi.INSTANCE.fontManager.comfortaa17.drawString(
                Hanabi.CLIENT_NAME + " build " + Hanabi.CLIENT_VERSION, 2f,
                sr.getScaledHeight() - 10, -1);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public void drawScaledTexturedModalRect(double x, double y, double textureX, double textureY, double width,
                                            double height, float scale) {
        float f = 0.00390625F * scale;
        float f1 = 0.00390625F * scale;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(x + 0, y + height, this.zLevel)
                .tex((float) (textureX + 0) * f, (float) (textureY + height) * f1).endVertex();
        worldrenderer.pos(x + width, y + height, this.zLevel)
                .tex((float) (textureX + width) * f, (float) (textureY + height) * f1)
                .endVertex();
        worldrenderer.pos(x + width, y + 0, this.zLevel)
                .tex((float) (textureX + width) * f, (float) (textureY + 0) * f1).endVertex();
        worldrenderer.pos(x + 0, y + 0, this.zLevel)
                .tex((float) (textureX + 0) * f, (float) (textureY + 0) * f1).endVertex();
        tessellator.draw();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
    }

}