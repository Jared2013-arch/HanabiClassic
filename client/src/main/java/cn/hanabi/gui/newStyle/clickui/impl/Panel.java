package cn.hanabi.gui.newStyle.clickui.impl;

import cn.hanabi.Hanabi;
import cn.hanabi.gui.common.font.noway.ttfr.HFontRenderer;
import cn.hanabi.gui.newStyle.clickui.ClickUINew;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.modules.ModManager;
import cn.hanabi.utils.fontmanager.HanabiFonts;
import cn.hanabi.utils.math.TimeHelper;
import cn.hanabi.utils.render.RenderUtil;
import cn.hanabi.utils.render.TranslateUtil;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;


public class Panel {
    private final Category category;

    private float desX, desY, x, y, dragX, dragY;

    private boolean needMove;

    private final long delay;

    private int wheel;

    private final String name;

    private final TranslateUtil anima = new TranslateUtil(1, 0);
    private final TranslateUtil translate = new TranslateUtil(0, 0);

    private final TimeHelper timer = new TimeHelper();

    public Panel(float x, float y, long delay, Category category) {
        this.desX = x;
        this.desY = y;
        this.delay = delay;
        this.category = category;
        this.name = category.toString();
        this.anima.setXY(1, 0);
        this.needMove = false;
        this.dragX = 0;
        this.dragY = 0;
    }

    public void draw(float mouseX, float mouseY) {
        //处理拖动
        float alpha = 0.01f;
        float yani = 0;
        if (needMove) {
            setXY(mouseX - dragX, mouseY - dragY);
            anima.interpolate(85, 0, 0.08f);
            alpha = anima.getX() / 100;
        } else {
            anima.interpolate(100, 10, 0.06f);
            alpha = anima.getX() / 100;
        }
        if (!Mouse.isButtonDown(0) && needMove) needMove = false;

        //处理动画
        yani = 10;

        x = desX;
        y = desY + 10 - yani;

        HFontRenderer titlefont = Hanabi.INSTANCE.fontManager.usans24;
        HFontRenderer font = Hanabi.INSTANCE.fontManager.raleway20;
        HFontRenderer icon = Hanabi.INSTANCE.fontManager.icon30;

        float mstartY = y + 40;
        float maddY = 22;

        //Panel背景
        RenderUtil.drawRoundRect(x, y, x + 140, y + 260, 2, new Color(255, 255, 255, (int) (255 * alpha)).getRGB());
        RenderUtil.drawRoundRect(x, y, x + 140, y + 26, 2, new Color(72, 151, 244, (int) (255 * alpha)).getRGB());


        //Panel标题
        titlefont.drawString(this.name, x + 30, y + 11, new Color(255, 255, 255, (int) (255 * alpha)).getRGB());
        String iconstr = "";
        switch (category.toString()) {
            case "Combat": {
                iconstr = HanabiFonts.ICON_CLICKGUI_COMBAT;
                break;
            }
            case "Movement": {
                iconstr = HanabiFonts.ICON_CLICKGUI_MOVEMENT;
                break;
            }
            case "Player": {
                iconstr = HanabiFonts.ICON_CLICKGUI_PLAYER;
                break;
            }
            case "Render": {
                iconstr = HanabiFonts.ICON_CLICKGUI_RENDER;
                break;
            }
            case "World": {
                iconstr = HanabiFonts.ICON_CLICKGUI_WORLD;
                break;
            }
            case "Ghost": {
                iconstr = HanabiFonts.ICON_CLICKGUI_GHOST;
                break;
            }
        }
        icon.drawString(iconstr, x + 10, y + 13, new Color(255, 255, 255, (int) (255 * alpha)).getRGB());

        //Mod显示
        GL11.glPushMatrix();
        GL11.glEnable(3089);
        RenderUtil.doGlScissor((int) x, (int) mstartY - 4, 140, (int) (y + 260 - 5 - (mstartY - 4)));
        float modY = translate.getX();
        for (Mod m : ModManager.getModules(category)) {
            //判断搜索栏
            if (ClickUINew.isSearching && !ClickUINew.searchcontent.equalsIgnoreCase("") && ClickUINew.searchcontent != null) {
                if (!m.getName().toLowerCase().contains(ClickUINew.searchcontent.toLowerCase())) continue;
            }
            boolean mhover = ClickUINew.currentMod == null && ClickUINew.isHover(mouseX, mouseY, x, mstartY + modY - 4, x + 140, mstartY + modY + 17) && ClickUINew.isHover(mouseX, mouseY, x, mstartY - 4, x + 140, y + 260 - 5);
            if (mhover) {
                RenderUtil.drawRect(x, mstartY + modY - 8, x + 140, mstartY + modY + maddY - 8, new Color(249, 249, 249, (int) (255 * alpha)).getRGB());
            }
            if (m.isEnabled()) {
                Hanabi.INSTANCE.fontManager.micon15.drawString("B", x + 12, mstartY + modY + 1, new Color(180, 180, 180, (int) (255 * alpha)).getRGB());
                font.drawString(m.getName(), x + 25, mstartY + modY, new Color(72, 151, 244, (int) (255 * alpha)).getRGB());
            } else {
                font.drawString(m.getName(), x + 25, mstartY + modY, new Color(165, 165, 165, (int) (255 * alpha)).getRGB());
            }
            if (m.hasValues())
                font.drawString(">", x + 120, mstartY + modY, new Color(165, 165, 165, (int) (255 * alpha)).getRGB());

            modY += maddY;
        }
        GL11.glDisable(3089);
        GL11.glPopMatrix();

        //处理滚动
        float moduleHeight = modY - translate.getX() - 1;
        if (Mouse.hasWheel() && ClickUINew.isHover(mouseX, mouseY, x, mstartY - 4, x + 140, y + 260 - 5) && ClickUINew.currentMod == null) {
            if ((ClickUINew.real > 0 && wheel < 0)) {
                for (int i = 0; i < 5; i++) {
                    if (!(wheel < 0))
                        break;
                    wheel += 5;
                }
            } else {
                for (int i = 0; i < 5; i++) {
                    if (!(ClickUINew.real < 0 && moduleHeight > y + 260 - 5 - (mstartY - 4) && Math.abs(wheel) < (moduleHeight - (y + 260 - 5 - (mstartY - 4)))))
                        break;
                    wheel -= 5;
                }
            }
        }
        translate.interpolate(wheel, 0, 20.0E-2f);

        //滚动条
        float sliderh = Math.min(y + 260 - 5 - (mstartY - 4), (y + 260 - 5 - (mstartY - 4)) * (y + 260 - 5 - (mstartY - 4)) / moduleHeight);
        float slidert = -(y + 260 - 5 - (mstartY - 4) - sliderh) * (translate.getX()) / (moduleHeight - (y + 260 - 5 - (mstartY - 4)));
        if (sliderh < y + 260 - 5 - (mstartY - 4)) {
            GL11.glPushMatrix();
            GL11.glEnable(3089);
            RenderUtil.doGlScissor((int) x + 137, (int) mstartY - 4, 1, (int) (y + 260 - 5 - (mstartY - 4)));
            RenderUtil.drawRect(x + 137, mstartY - 4 + slidert, x + 138, mstartY - 4 + slidert + sliderh, new Color(220, 220, 220, (int) (255 * alpha)).getRGB());
            GL11.glDisable(3089);
            GL11.glPopMatrix();
        }
    }

    public void handleMouseClicked(float mouseX, float mouseY, int key) {
        float mstartY = y + 40;
        float maddY = 22;

        //处理拖动
        boolean tophover = ClickUINew.currentMod == null && ClickUINew.isHover(mouseX, mouseY, x, y, x + 140, mstartY);
        if (tophover && key == 0) {
            dragX = mouseX - desX;
            dragY = mouseY - desY;
            needMove = true;
        }

        //处理Mod的MouseClicked的Event
        float modY = mstartY + translate.getX();
        for (Mod m : ModManager.getModules(category)) {
            //判断搜索栏
            if (ClickUINew.isSearching && !ClickUINew.searchcontent.equalsIgnoreCase("") && ClickUINew.searchcontent != null) {
                if (!m.getName().toLowerCase().contains(ClickUINew.searchcontent.toLowerCase())) continue;
            }
            boolean mhover = ClickUINew.currentMod == null && ClickUINew.isHover(mouseX, mouseY, x, modY - 4, x + 140, modY + 17) && ClickUINew.isHover(mouseX, mouseY, x, mstartY - 4, x + 140, y + 260 - 5);
            if (mhover) {
                if (key == 0) m.set(!m.isEnabled(), false);
                if (key == 1 && m.hasValues()) {
                    ClickUINew.currentMod = m;
                    ClickUINew.settingwheel = 0;
                    ClickUINew.settingtranslate.setXY(0, 0);
                    ClickUINew.animatranslate.setXY(0, 0);
                }
            }
            modY += maddY;
        }
    }

    public void handleMouseReleased(float mouseX, float mouseY, int key) {
        float mstartY = y + 40;

        //处理拖动
        boolean tophover = ClickUINew.currentMod == null && ClickUINew.isHover(mouseX, mouseY, x, y, x + 140, mstartY);
        if (tophover && key == 0) {
            dragX = mouseX - desX;
            dragY = mouseY - desY;
            needMove = false;
        }
    }

    public void resetAnimation() {
        timer.reset();
        anima.setXY(1, 0);
        needMove = false;
        dragX = 0;
        dragY = 0;
    }

    public void resetTranslate() {
        translate.setXY(0, 0);
        wheel = 0;
    }

    public void setXY(float x, float y) {
        this.desX = x;
        this.desY = y;
    }
}
