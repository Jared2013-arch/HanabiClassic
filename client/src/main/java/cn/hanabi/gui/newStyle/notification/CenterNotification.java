package cn.hanabi.gui.newStyle.notification;

import cn.hanabi.Hanabi;
import cn.hanabi.gui.classic.notifications.Notification;
import cn.hanabi.gui.common.font.noway.ttfr.HFontRenderer;
import cn.hanabi.utils.math.AnimationUtil;
import cn.hanabi.utils.math.TimeHelper;
import cn.hanabi.utils.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;

public class CenterNotification extends Notification {
    double animY, animA = 0; // 0-100
    int delay = 2000;
    TimeHelper timer = new TimeHelper();

    public CenterNotification(String message, Type type) {
        super(message, type);
    }

    @Override
    public void draw(double getY, double lastY) {
        super.draw(getY, lastY);
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        if (!timer.hasReached(delay)) {
            if (animY == 0) {
                animY = lastY;
            }
            animY = AnimationUtil.moveUD(((float) animY), ((float) getY), 0.3f, 0.2f);
            animA = AnimationUtil.moveUD(((float) animA), 100f, 0.3f, 0.2f);
        } else {
            animY = AnimationUtil.moveUD(((float) animY), ((float) lastY), 0.3f, 0.2f);
            animA = AnimationUtil.moveUD(((float) animA), 0f, 0.3f, 0.2f);
        }

        getY = animY;
        HFontRenderer font = Hanabi.INSTANCE.fontManager.comfortaa18;
        float width = font.getStringWidth(message) + 10;
        float xPosition = sr.getScaledWidth() - width / 2;
        RenderUtil.drawRoundRect3(xPosition, (float) getY, xPosition + width, (float) (getY + 20), 5, new Color(0, 0, 0, Math.max(((int) animA), 0)).getRGB());
        font.drawString(message, xPosition + 5, (float) (getY + 5), new Color(255, 255, 255, Math.max((int) (animA * 255), 0)).getRGB());
    }

    @Override
    public boolean shouldDelete() {
        return timer.hasReached(delay) && animA == 0;
    }

    @Override
    public double getHeight() {
        return 30;
    }
}
