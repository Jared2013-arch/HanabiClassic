package cn.hanabi.modules.modules.render;

import cn.hanabi.events.EventPostMotion;
import cn.hanabi.events.EventRender;
import cn.hanabi.events.EventUpdate;
import cn.hanabi.injection.interfaces.IRenderManager;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.utils.render.RenderUtil;
import cn.hanabi.value.Value;
import com.darkmagician6.eventapi.EventTarget;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;

public class Trail extends Mod {

    private final ArrayList<double[]> positions = new ArrayList<>();

    public final Value<Boolean> rainbow = new Value<Boolean>("Trail","Rainbow", false);

    public static Value<Double> length = new Value<>("Trail","Length", 1000d, 100d, 3000d , 100d);

    public final Value<Boolean> showInFirstPerson = new Value<Boolean>("Trail","Show In FirstPerson", true);

    int rainbowOffset;

    public Trail() {
        super("Trail", Category.RENDER);
    }

    @EventTarget
    private void onRender(EventRender event) {
        Color customColor = new Color(218, 57, 204);
        if (mc.gameSettings.thirdPersonView != 0 || showInFirstPerson.getValue()) {
            GL11.glPushMatrix();

            GL11.glEnable(GL11.GL_LINE_SMOOTH);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glLineWidth(2F);

            GL11.glBegin(GL11.GL_LINE_STRIP);

            int offset = 0;
            for (double[] pos : positions) {
                if (rainbow.getValue())
                    customColor = RenderUtil.getRainbow((offset + rainbowOffset) * 100, 6000, 0.8F, 1F);
                GL11.glColor4f(customColor.getRed() / 255F, customColor.getGreen() / 255F, customColor.getBlue() / 255F, customColor.getAlpha() / 255F);
                GL11.glVertex3d(pos[0] - ((IRenderManager) mc.getRenderManager()).getRenderPosX(), pos[1] - ((IRenderManager) mc.getRenderManager()).getRenderPosY(), pos[2] - ((IRenderManager) mc.getRenderManager()).getRenderPosZ());
                offset++;
            }

            GL11.glVertex3d(0, 0.01, 0);
            GL11.glEnd();
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glColor4f(1, 1, 1, 1);

            GL11.glPopMatrix();
        }
    }

    @EventTarget
    private void onUpdate(EventUpdate event) {
        for (int i = 0; i < positions.size(); i++) {
            final double[] position = positions.get(i);
            if (System.currentTimeMillis() - position[3] > length.getValue()) {
                rainbowOffset++;
                positions.remove(position);
            }
        }
    }

    @EventTarget
    private void onMotionUpdate(EventPostMotion event) {
        positions.add(new double[]{mc.thePlayer.posX, mc.thePlayer.posY + 0.01, mc.thePlayer.posZ, System.currentTimeMillis()});
    }
}
