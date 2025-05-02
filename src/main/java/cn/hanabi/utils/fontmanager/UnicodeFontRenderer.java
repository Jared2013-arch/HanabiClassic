package cn.hanabi.utils.fontmanager;

import cn.hanabi.utils.color.Colors;
import cn.hanabi.utils.render.RenderUtil;
import me.theresa.fontRenderer.font.SlickException;
import me.theresa.fontRenderer.font.UnicodeFont;
import me.theresa.fontRenderer.font.effect.ColorEffect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.HashMap;

public class UnicodeFontRenderer extends FontRenderer {
    private final UnicodeFont font;

    public HashMap<String, Float> widthMap = new HashMap<String, Float>();
    public HashMap<String, Float> heightMap = new HashMap<String, Float>();

    public UnicodeFontRenderer(Font awtFont) {
        super(Minecraft.getMinecraft().gameSettings, new ResourceLocation("textures/font/ascii.png"),
                Minecraft.getMinecraft().getTextureManager(), false);
        this.font = new UnicodeFont(awtFont);
        this.font.addAsciiGlyphs();
        this.font.getEffects().add(new ColorEffect(Color.WHITE));
        try {
            this.font.loadGlyphs();
        } catch (SlickException exception) {
            throw new RuntimeException(exception);
        }
        String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ123456789";
        this.FONT_HEIGHT = this.font.getHeight(alphabet) / 2;
    }

    public UnicodeFontRenderer(Font awtFont, int fontPageStart, int fontPageEnd) {
        super(Minecraft.getMinecraft().gameSettings, new ResourceLocation("textures/font/ascii.png"),
                Minecraft.getMinecraft().getTextureManager(), false);
        this.font = new UnicodeFont(awtFont);
        this.font.addGlyphs(fontPageStart, fontPageEnd);

        this.font.getEffects().add(new ColorEffect(Color.WHITE));
        try {
            this.font.loadGlyphs();
        } catch (SlickException exception) {
            throw new RuntimeException(exception);
        }
        String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ123456789";
        this.FONT_HEIGHT = this.font.getHeight(alphabet) / 2;
    }

    public UnicodeFontRenderer(Font awtFont, boolean bol) {
        super(Minecraft.getMinecraft().gameSettings, new ResourceLocation("textures/font/ascii.png"),
                Minecraft.getMinecraft().getTextureManager(), false);
        this.font = new UnicodeFont(awtFont);
        this.font.addAsciiGlyphs();

        // 所有Unicode字符
        this.font.addGlyphs(0, 65535);

        this.font.getEffects().add(new ColorEffect(Color.WHITE));

        try {
            this.font.loadGlyphs();
        } catch (SlickException exception) {
            throw new RuntimeException(exception);
        }

        String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ123456789";

        this.FONT_HEIGHT = this.font.getHeight(alphabet) / 2;
    }

    public int drawString(String string, int x, int y, int color) {
        return this.drawString(string, (float) x, (float) y, color);
    }

    public int drawString(String string, float x, float y, int color) {
        if (string == null) {
            return 0;
        } else {
            GL11.glPushMatrix();
            GL11.glScaled(0.5D, 0.5D, 0.5D);
            boolean blend = GL11.glIsEnabled(3042);
            boolean lighting = GL11.glIsEnabled(2896);
            boolean texture = GL11.glIsEnabled(3553);
            if (!blend) {
                GL11.glEnable(3042);
            }

            if (lighting) {
                GL11.glDisable(2896);
            }

            if (texture) {
                GL11.glDisable(3553);
            }

            x *= 2.0F;
            y *= 2.0F;
            this.font.drawString(x, y, string, new me.theresa.fontRenderer.font.Color(color));
            if (texture) {
                GL11.glEnable(3553);
            }

            if (lighting) {
                GL11.glEnable(2896);
            }

            if (!blend) {
                GL11.glDisable(3042);
            }

            GlStateManager.color(0.0F, 0.0F, 0.0F);
            GL11.glPopMatrix();
            GlStateManager.bindTexture(0);
            return ((int) x);
        }
    }

    public int drawStringWithShadowShifted(String text, float x, float y, int color, int shadowAlpha) {
        this.drawString(text, x + 1F, y + 1F, new Color(0, 0, 0, shadowAlpha).getRGB());
        return this.drawString(text, x, y, color);
    }

    public int drawStringWithShadow(String text, float x, float y, int color) {
        this.drawString(text, x + 0.5F, y + 0.5F, RenderUtil.reAlpha(Colors.BLACK.c , 0.6f) );
        return this.drawString(text, x, y, color);
    }

    public int drawStringWithShadow(String text, float x, float y, int color,int shadowAlpha) {
        this.drawString(text, x + 0.5F, y + 0.5F, new Color(0, 0, 0, shadowAlpha).getRGB());
        return this.drawString(text, x, y, color);
    }

    public int getCharWidth(char c) {
        return this.getStringWidth(Character.toString(c));
    }

    public int getStringWidth(String string) {
        if (widthMap.containsKey(string)) {
            return widthMap.get(string).intValue();
        } else {
            float width = (float) (this.font.getWidth(string) / 2);
            widthMap.put(string, width);
            return (int) width;
        }
    }

    public float getStringHeight(String string) {
        if (heightMap.containsKey(string)) {
            return heightMap.get(string);
        } else {
            float height = (float) (this.font.getHeight(string) / 2);
            heightMap.put(string, height);
            return height;
        }
    }

    public int drawStringWithColor(String text, float x, float y, int color, int alpha) {

        text = "\247r" + text;

        float len = -1;
        String[] array = text.split("\247");
        for (String str : array) {
            if (str.length() < 1)
                continue;
            switch (str.charAt(0)) {
                case '0':
                    color = new Color(0, 0, 0).getRGB();
                    break;
                case '1':
                    color = new Color(0, 0, 170).getRGB();
                    break;
                case '2':
                    color = new Color(0, 170, 0).getRGB();
                    break;
                case '3':
                    color = new Color(0, 170, 170).getRGB();
                    break;
                case '4':
                    color = new Color(170, 0, 0).getRGB();
                    break;
                case '5':
                    color = new Color(170, 0, 170).getRGB();
                    break;
                case '6':
                    color = new Color(255, 170, 0).getRGB();
                    break;
                case '7':
                    color = new Color(170, 170, 170).getRGB();
                    break;
                case '8':
                    color = new Color(85, 85, 85).getRGB();
                    break;
                case '9':
                    color = new Color(85, 85, 255).getRGB();
                    break;
                case 'a':
                    color = new Color(85, 255, 85).getRGB();
                    break;
                case 'b':
                    color = new Color(85, 255, 255).getRGB();
                    break;
                case 'c':
                    color = new Color(255, 85, 85).getRGB();
                    break;
                case 'd':
                    color = new Color(255, 85, 255).getRGB();
                    break;
                case 'e':
                    color = new Color(255, 255, 85).getRGB();
                    break;
                case 'f':
                    color = new Color(255, 255, 255).getRGB();
                    break;
                case 'r':
                    color = new Color(255, 255, 255).getRGB();
                    break;
            }

            Color col = new Color(color);

            str = str.substring(1);
            this.drawString(str, x + len + 0.5F, y + 0.5F, Colors.BLACK.c);
            this.drawString(str, x + len, y, this.getColor(col.getRed(), col.getGreen(), col.getBlue(), alpha));
            len += this.getStringWidth(str) + 1;
        }
        return (int) len;
    }

    public int drawStringWithColor(String text, float x, float y, int color) {

        text = "\247r" + text;

        float len = -1;
        String[] array = text.split("\247");
        for (String str : array) {
            if (str.length() < 1)
                continue;
            switch (str.charAt(0)) {
                case '0':
                    color = new Color(0, 0, 0).getRGB();
                    break;
                case '1':
                    color = new Color(0, 0, 170).getRGB();
                    break;
                case '2':
                    color = new Color(0, 170, 0).getRGB();
                    break;
                case '3':
                    color = new Color(0, 170, 170).getRGB();
                    break;
                case '4':
                    color = new Color(170, 0, 0).getRGB();
                    break;
                case '5':
                    color = new Color(170, 0, 170).getRGB();
                    break;
                case '6':
                    color = new Color(255, 170, 0).getRGB();
                    break;
                case '7':
                    color = new Color(170, 170, 170).getRGB();
                    break;
                case '8':
                    color = new Color(85, 85, 85).getRGB();
                    break;
                case '9':
                    color = new Color(85, 85, 255).getRGB();
                    break;
                case 'a':
                    color = new Color(85, 255, 85).getRGB();
                    break;
                case 'b':
                    color = new Color(85, 255, 255).getRGB();
                    break;
                case 'c':
                    color = new Color(255, 85, 85).getRGB();
                    break;
                case 'd':
                    color = new Color(255, 85, 255).getRGB();
                    break;
                case 'e':
                    color = new Color(255, 255, 85).getRGB();
                    break;
                case 'f':
                    color = new Color(255, 255, 255).getRGB();
                    break;
                case 'r':
                    color = new Color(255, 255, 255).getRGB();
                    break;
            }


            str = str.substring(1);
            //Noshadow
            //this.drawString(str, x + len + 0.5F, y + 0.5F, Colors.BLACK.c);
            this.drawString(str, x + len, y, color);
            len += this.getStringWidth(str) + 1;
        }
        return (int) len;
    }

    public int getColor(int brightness, int alpha) {
        return getColor(brightness, brightness, brightness, alpha);
    }

    public int getColor(int red, int green, int blue, int alpha) {
        int color = 0;
        color |= alpha << 24;
        color |= red << 16;
        color |= green << 8;
        color |= blue;
        return color;
    }

    public void drawCenteredString(String text, float x, float y, int color) {
        this.drawString(text, x - (float) (this.getStringWidth(text) / 2), y, color);
    }

    public void drawOutlinedString(String text, float x, float y, int borderColor, int color) {
        this.drawString(text, x - 0.5f, y, borderColor);
        this.drawString(text, x + 0.5f, y, borderColor);
        this.drawString(text, x, y - 0.5f, borderColor);
        this.drawString(text, x, y + 0.5f, borderColor);
        this.drawString(text, x, y, color);

    }

    public void drawCenterOutlinedString(String text, float x, float y, int borderColor, int color) {
        this.drawString(text, x - (float) (this.getStringWidth(text) / 2) - 0.5f, y, borderColor);
        this.drawString(text, x - (float) (this.getStringWidth(text) / 2) + 0.5f, y, borderColor);
        this.drawString(text, x - (float) (this.getStringWidth(text) / 2), y - 0.5f, borderColor);
        this.drawString(text, x - (float) (this.getStringWidth(text) / 2), y + 0.5f, borderColor);
        this.drawString(text, x - (float) (this.getStringWidth(text) / 2), y, color);
    }

    public float Ex(String text, double x2, double y2, int color) {
        float shadowWidth = this.drawString(text, (float) (x2 + 0.7), (float) (y2 + 0.6), color, true);
        return Math.max(shadowWidth, this.drawString(text, (float) (x2), (float) (y2), color, false));
    }
}
