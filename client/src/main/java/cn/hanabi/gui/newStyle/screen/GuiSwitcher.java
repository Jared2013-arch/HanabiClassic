package cn.hanabi.gui.newStyle.screen;

import cn.hanabi.Hanabi;
import cn.hanabi.utils.math.AnimationUtil;
import cn.hanabi.utils.render.ParticleUtils;
import cn.hanabi.utils.render.RenderUtil;
import me.yarukon.mainmenu.GuiCustomMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

import java.awt.*;

public class GuiSwitcher extends GuiScreen {

    @Override
    public void initGui() {
        super.initGui();
    }

    public float box1Anim = 0;
    public float box2Anim = 0;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        ScaledResolution sr = new ScaledResolution(mc);
        RenderUtil.drawRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), new Color(24, 24, 24).getRGB());
        ParticleUtils.drawParticles(mouseX, mouseY);

        RenderUtil.drawImage(new ResourceLocation("Client/new/switcher/HanabiSwitcher.png"), sr.getScaledWidth() / 2f - 305 / 4f, sr.getScaledHeight() / 3f - 39 / 2f, 305 / 2f, 39f);
        float xLeft1 = sr.getScaledWidth() / 2f - 310 / 2f;
        float yUp1 = sr.getScaledHeight() / 2f - box1Anim;
        float smoothSpeed = 0.5f;
        float minSpeed = 0.2f;
        int end = 10;
        if (RenderUtil.isHoveringAppend(mouseX, mouseY, xLeft1, yUp1, 299 / 2f, 64f)) {
            box1Anim = AnimationUtil.moveUD(box1Anim, end, smoothSpeed, minSpeed);
            if (Mouse.isButtonDown(0)) {
                mc.displayGuiScreen(new GuiCustomMainMenu());
                Hanabi.INSTANCE.newStyle = false;
                Hanabi.INSTANCE.selected = true;
            }
        } else {
            box1Anim = AnimationUtil.moveUD(box1Anim, 0, smoothSpeed, minSpeed);
        }

        float xLeft = sr.getScaledWidth() / 2f + 11 / 2f;
        float yUp = sr.getScaledHeight() / 2f - box2Anim;
        if (RenderUtil.isHoveringAppend(mouseX, mouseY, xLeft, yUp, 299 / 2f, 64f)) {
            box2Anim = AnimationUtil.moveUD(box2Anim, end, smoothSpeed, minSpeed);
            if (Mouse.isButtonDown(0)) {
                mc.displayGuiScreen(new GuiNewMainMenu());
                Hanabi.INSTANCE.newStyle = true;
                Hanabi.INSTANCE.selected = true;
            }
        } else {
            box2Anim = AnimationUtil.moveUD(box2Anim, 0, smoothSpeed, minSpeed);
        }
        RenderUtil.drawImage(new ResourceLocation("Client/new/switcher/Classic.png"), xLeft1, yUp1, 299 / 2f, 64f);
        RenderUtil.drawImage(new ResourceLocation("Client/new/switcher/New.png"), xLeft, yUp, 299 / 2f, 64f);
    }
}
