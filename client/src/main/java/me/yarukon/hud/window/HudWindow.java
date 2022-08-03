package me.yarukon.hud.window;


import cn.hanabi.Hanabi;
import cn.hanabi.utils.color.Colors;
import me.yarukon.BlurBuffer;
import me.yarukon.YRenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.Display;

public class HudWindow {
    public float x;
    public float y;

    public Minecraft mc = Minecraft.getMinecraft();

    // Dragging
    public float x2;
    public float y2;
    public boolean drag = false;
    public float draggableHeight;

    public String windowID;
    public float width;
    public float height;
    public String title;

    public boolean resizeable;
    public float minWidth;
    public float minHeight;
    public boolean resizing = false;

    //Icon and offset shit
    public String icon;
    public float iconOffX;
    public float iconOffY;

    public boolean hide = false;
    public long lastClickTime = 0;
    public boolean focused = false;

    public int titleBGColor;
    public int frameBGColor;
    public int textColor;
    public boolean alwaysDisplayTitle;


    public HudWindow(String windowID, float x, float y, float width, float height, String title, String icon, float draggableHeight, float iconOffX, float iconOffY, boolean disTitle) {
        this(windowID, x, y, width, height, title, icon, draggableHeight, iconOffX, iconOffY, false, 0, 0, disTitle);
    }

    public HudWindow(String windowID, float x, float y, float width, float height, String title, String icon, float draggableHeight, float iconOffX, float iconOffY, boolean resizeable, float minWidth, float minHeight) {
        this(windowID, x, y, width, height, title, icon, draggableHeight, iconOffX, iconOffY, resizeable, minWidth, minHeight, true);
    }

    public HudWindow(String windowID, float x, float y, float width, float height, String title, String icon, float draggableHeight, float iconOffX, float iconOffY) {
        this(windowID, x, y, width, height, title, icon, draggableHeight, iconOffX, iconOffY, false, 0, 0, true);
    }

    public HudWindow(String windowID, float x, float y, float width, float height, String title, String icon, float draggableHeight, float iconOffX, float iconOffY, boolean resizeable, float minWidth, float minHeight, boolean disTitle) {
        this.windowID = windowID;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.title = title;
        this.icon = icon;
        this.draggableHeight = draggableHeight;
        this.iconOffX = iconOffX;
        this.iconOffY = iconOffY;
        this.resizeable = resizeable;
        this.minWidth = minWidth;
        this.minHeight = minHeight;
        this.alwaysDisplayTitle = disTitle;
    }

    public void draw() {
        if (OpenGlHelper.shadersSupported && Minecraft.getMinecraft().getRenderViewEntity() instanceof EntityPlayer) {
            if (!HudWindowManager.blur.getValueState()) {
                if (Hanabi.INSTANCE.hasOptifine) {
                    if (Hanabi.INSTANCE.fastRenderDisabled(mc.gameSettings)) {
                        if (alwaysDisplayTitle || mc.currentScreen instanceof GuiChat) {
                            BlurBuffer.blurArea((int) x, (int) y, (int) width, (int) height + (int) draggableHeight, true);
                        } else {
                            BlurBuffer.blurArea((int) x, (int) y + draggableHeight, (int) width, (int) height, true);
                        }
                    }
                } else {
                    if (alwaysDisplayTitle || mc.currentScreen instanceof GuiChat) {
                        BlurBuffer.blurArea((int) x, (int) y, (int) width, (int) height + (int) draggableHeight, true);
                    } else {
                        BlurBuffer.blurArea((int) x, (int) y + draggableHeight, (int) width, (int) height, true);
                    }
                }
            }
        }


        textColor = 0xFFFFFFFF;
        titleBGColor = 0xCC2F74FF;
        frameBGColor = 0xAA000000;

        if (alwaysDisplayTitle || mc.currentScreen instanceof GuiChat) {
            YRenderUtil.drawRect(this.x, this.y, width, draggableHeight, titleBGColor);
        }
        YRenderUtil.drawRect(this.x, this.y + draggableHeight, width, height, frameBGColor);

        boolean displayIcon = !icon.isEmpty();
        if (displayIcon) {
            Hanabi.INSTANCE.fontManager.sessionInfoIcon14.drawString(this.icon, this.x + iconOffX, this.y + iconOffY, textColor);
        }
        if (alwaysDisplayTitle || mc.currentScreen instanceof GuiChat) {
            Hanabi.INSTANCE.fontManager.usans15.drawString(this.title, this.x + 3 + (displayIcon ? 8 : 0), this.y + (draggableHeight / 2f) - (Hanabi.INSTANCE.fontManager.usans15.FONT_HEIGHT / 2f), textColor);
        }
        if (mc.currentScreen instanceof GuiChat) {
            YRenderUtil.drawRect(this.x, this.y, width, draggableHeight, 0xff00af87);
            Hanabi.INSTANCE.fontManager.sessionInfoIcon20.drawString("A", this.x + iconOffX, this.y + iconOffY + 2, Colors.WHITE.c);
            Hanabi.INSTANCE.fontManager.usans15.drawString("Move", x + 12, this.y + (draggableHeight / 2f) - (Hanabi.INSTANCE.fontManager.usans15.FONT_HEIGHT / 2f), 0xffffffff);
        }
    }

    public void postDraw() {
        if (resizeable && mc.currentScreen instanceof GuiChat) {
            Hanabi.INSTANCE.fontManager.sessionInfoIcon14.drawString("N", this.x + width - 8, this.y + draggableHeight + height - 8, textColor);

            if (resizing) {
                String gay = (int) this.width + ", " + (int) this.height;
                YRenderUtil.drawRectNormal(this.x + this.width + 2, this.y + this.draggableHeight + this.height - 12, this.x + this.width + 2 + Hanabi.INSTANCE.fontManager.usans16.getStringWidth(gay) + 4, this.y + this.draggableHeight + this.height, frameBGColor);
                Hanabi.INSTANCE.fontManager.usans16.drawString(gay, this.x + this.width + 4, this.y + this.draggableHeight + this.height - 10, textColor);
            }
        }
    }

    public void updateScreen() {

    }

    public void mouseClick(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0) {
            if (YRenderUtil.isHoveringBound(mouseX, mouseY, x, y, width, draggableHeight)) {
                this.drag = true;
                lastClickTime = System.currentTimeMillis();
                this.x2 = mouseX - this.x;
                this.y2 = mouseY - this.y;
            }

            if (YRenderUtil.isHoveringBound(mouseX, mouseY, x + width - 8, y + draggableHeight + height - 8, 8, 8) && this.resizeable) {
                this.resizing = true;
                lastClickTime = System.currentTimeMillis();
                this.x2 = mouseX;
                this.y2 = mouseY;
            }
        }
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        if (this.drag) {
            this.drag = false;
            Hanabi.INSTANCE.fileManager.saveWindows();
        }

        if (this.resizing) {
            this.resizing = false;
            Hanabi.INSTANCE.fileManager.saveWindows();
        }
    }

    public void mouseCoordinateUpdate(int mouseX, int mouseY) {
        if (this.drag) {
            this.x = mouseX - this.x2;
            this.y = mouseY - this.y2;
        }

        if (this.resizing) {
            this.width += mouseX - x2;

            if (width < minWidth) {
                width = minWidth;
            } else {
                this.x2 = mouseX;
            }

            this.height += mouseY - y2;

            if (height < minHeight) {
                height = minHeight;
            } else {
                this.y2 = mouseY;
            }

            if (!Display.isActive()) {
                this.resizing = false;
            }
        }
    }

    public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {

    }

    public void makeCenter(float scrW, float scrH) {
        this.x = scrW / 2f - (width / 2f);
        this.y = scrH / 2f - (height / 2f);
    }

    public void hide() {
        hide = true;
    }

    public void show() {
        hide = false;
    }

    public boolean isOnFrame(int mouseX, int mouseY) {
        if (this.resizing) {
            return YRenderUtil.isHoveringBound(mouseX, mouseY, x, y, width, draggableHeight + height);
        } else {
            return resizeable ? (YRenderUtil.isHoveringBound(mouseX, mouseY, x, y, width, draggableHeight) || YRenderUtil.isHoveringBound(mouseX, mouseY, x + width - 8, y + draggableHeight + height - 8, 8, 8)) : YRenderUtil.isHoveringBound(mouseX, mouseY, x, y, width, draggableHeight);
        }
    }

    public void setFocused(boolean focused) {
        this.focused = focused;
    }

    public boolean isFocused() {
        return this.focused;
    }
}
