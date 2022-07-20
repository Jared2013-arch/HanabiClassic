package cn.hanabi.gui.newStyle.clickui.impl;

import cn.hanabi.Hanabi;
import cn.hanabi.gui.common.font.noway.ttfr.HFontRenderer;
import cn.hanabi.gui.newStyle.clickui.ClickUINew;
import cn.hanabi.utils.render.RenderUtil;
import cn.hanabi.utils.render.TranslateUtil;
import cn.hanabi.value.Value;

import java.awt.*;

public class ModeValue {
    public Value values;

    public float length, x, y;

    public String name;

    public TranslateUtil anima = new TranslateUtil(0, 0);

    public boolean isOpen = false;

    public ModeValue(Value values) {
        this.values = values;
        this.name = values.getModeTitle();
        this.anima.setXY(18, 0);
        this.isOpen = false;
    }

    public void draw(float x, float y, float mouseX, float mouseY) {
        this.x = x;
        this.y = y;
        anima.interpolate(isOpen ? values.mode.size() * 18 : 18, 0, 0.2f);
        length = 30 + anima.getX() - 18;
        HFontRenderer font = Hanabi.INSTANCE.fontManager.wqy18;
        float width = 0;
        for (String mode : values.getModes()) {
            float w1 = font.getStringWidth(mode) + 30;
            if (w1 > width)
                width = w1;
        }
        font.drawString(name, x + 20, y + 8, new Color(124, 124, 124, 255).getRGB());
        RenderUtil.drawRoundRect(x + 220 - width, y + 2, x + 220, y + anima.getX(), 2, new Color(238, 238, 238, 255).getRGB());
        if (this.isOpen || anima.getX() != 18)
            RenderUtil.drawRoundRect(x + 221 - width, y + 18, x + 219, y + 19, 1, new Color(218, 218, 218, 255).getRGB());

        int i = 0;
        float modeY = 18;
        while (i < values.mode.size()) {
            if (values.getModeAt(values.getCurrentMode()) != values.mode.get(i)) {
                font.drawCenteredString((String) values.mode.get(i), x + 220 - width / 2, y + modeY + 6, new Color(130, 130, 130, 255).getRGB());
                modeY += 18.0f;
            }
            ++i;
        }
        RenderUtil.drawRoundRect(x + 220 - width, y + anima.getX() + 3, x + 220, y + values.mode.size() * 18 + 3, 0, new Color(255, 255, 255).getRGB());
        font.drawCenteredString(values.getModeAt(values.getCurrentMode()), x + 220 - width / 2, y + 6, new Color(124, 124, 124, 255).getRGB());
    }

    public void handleMouse(float mouseX, float mouseY, int key) {
        HFontRenderer font = Hanabi.INSTANCE.fontManager.wqy18;
        float width = 0;
        for (String mode : values.getModes()) {
            float w1 = font.getStringWidth(mode) + 30;
            if (w1 > width)
                width = w1;
        }
        if (ClickUINew.isHover(mouseX, mouseY, x + 220 - width, y + 3, x + 220, y + anima.getX() + 2) && ClickUINew.isHover(mouseX, mouseY, x, y, x + 240, y + 235) && key == 0) {
            int i = 0;
            float modeY = 18;
            while (i < values.mode.size()) {
                if (values.getModeAt(values.getCurrentMode()) != values.mode.get(i)) {
                    if (ClickUINew.isHover(mouseX, mouseY, x + 220-width, y + modeY + 4, x + 220, y + modeY + 22)) {
                        values.setCurrentMode(i);
                    }
                    modeY += 18.0f;
                }
                ++i;
            }
            isOpen = !isOpen;
        }
    }

    public float getLength() {
        return this.length;
    }
}
