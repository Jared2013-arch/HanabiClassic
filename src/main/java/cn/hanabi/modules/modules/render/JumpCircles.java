package cn.hanabi.modules.modules.render;

import cn.hanabi.Hanabi;
import cn.hanabi.events.EventRender;
import cn.hanabi.events.EventUpdate;
import cn.hanabi.gui.common.cloudmusic.ui.MusicPlayerUI;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.utils.render.RenderUtil;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

import java.awt.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static cn.hanabi.utils.render.GLUtil.glEnable;
import static me.theresa.fontRenderer.font.opengl.renderer.SGL.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.*;

public class JumpCircles extends Mod {

    public JumpCircles() {
        super("JumpCircles", Category.RENDER);
    }


    private final Map<Vec3, Long> jumps = new HashMap<>();

    private boolean onGround = false;

    @Override
    public void onEnable() {
        super.onEnable();
        jumps.clear();
        onGround = true;
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (mc.thePlayer.onGround && !onGround) {
            jumps.put(mc.thePlayer.getPositionVector(), System.currentTimeMillis());
            onGround = true;
        }
        if (mc.thePlayer.motionY >= 0.01 || mc.thePlayer.fallDistance > 1)
            onGround = false;
    }

    @EventTarget
    public void onRender3D(EventRender event) {
        int vertices = 45;
        float increment = (float) (2 * Math.PI / vertices);
        RenderUtil.pre3D();
        glDisable(GL_CULL_FACE);
        glDisable(GL_DEPTH_TEST);
        glDepthMask(false);
        glFrontFace(GL_CW);
        Iterator<Map.Entry<Vec3, Long>> it = jumps.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Vec3, Long> object = it.next();
            long time = (System.currentTimeMillis() - object.getValue());
            float radius = MathHelper.clamp_float(time / 1000f, 0, 1);
            float x = (float) (object.getKey().xCoord - mc.getRenderManager().viewerPosX);
            float y = (float) (object.getKey().yCoord - mc.getRenderManager().viewerPosY);
            float z = (float) (object.getKey().zCoord - mc.getRenderManager().viewerPosZ);
            glBegin(GL_TRIANGLE_FAN);
            glColor4f(0, 0, 0, 0);
            glVertex3f(x, y, z);
            for(int i = 0; i <= vertices; i++) {
                RenderUtil.color(new Color(246, 6, 176), (1 - radius) * 255);
                float sin = MathHelper.sin(increment * i) * radius;
                float cos = -MathHelper.cos(increment * i) * radius;
                glVertex3f(x + sin, y, z + cos);
            }
            glEnd();
            if(radius == 1)
                it.remove();
        }
        glFrontFace(GL_CCW);
        glDepthMask(true);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);
        RenderUtil.post3D();
    }
}
