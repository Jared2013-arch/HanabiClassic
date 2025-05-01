package me.yarukon.mainmenu;

import cn.hanabi.Hanabi;
import cn.hanabi.utils.color.Colors;
import cn.hanabi.utils.game.MouseInputHandler;
import cn.hanabi.utils.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class MenuSlot {
	public GuiScreen screen;
	public String text;
	public int animatedY = 0;
	public boolean init = false;

	public String icon;

	public MouseInputHandler handler = new MouseInputHandler(0);

	public MenuSlot(String text, String icon, GuiScreen scr) {
		this.screen = scr;
		this.text = text;
		this.icon = icon;
	}

	public void draw(int mouseX, int mouseY, int x, int y) {

		if (!this.init) {
			this.animatedY = y;
			init = true;
		}

		if (this.isHovering(mouseX, mouseY, x - 32, y - 32, x + 32, y + 32)) {
			this.animatedY = (int) RenderUtil.getAnimationState(animatedY, y - 10, 200);
		} else {
			this.animatedY = (int) RenderUtil.getAnimationState(animatedY, y, 200);
		}

		RenderUtil.circle(x, animatedY, 30, Colors.WHITE.c);

		if (this.isHovering(mouseX, mouseY, x - 32, y - 32, x + 32, y + 32)) {
			Hanabi.INSTANCE.fontManager.comfortaa18.drawString(text, (float) x - 18, y + 40, Colors.WHITE.c);
		}

		switch (icon) {
		case "\ue90a":
			Hanabi.INSTANCE.fontManager.icon70.drawString(icon, (float) x - 17, animatedY - 19, Colors.GREY.c);
			break;
		case "\ue90c":
			Hanabi.INSTANCE.fontManager.icon100.drawString(icon, (float) x - 24, animatedY - 28, Colors.GREY.c);
			break;
		case "\ue912":
			Hanabi.INSTANCE.fontManager.icon70.drawString(icon, (float) x - 17, animatedY - 20, Colors.GREY.c);
			break;
		case "\ue90d":
			Hanabi.INSTANCE.fontManager.icon70.drawString(icon, (float) x - 17, animatedY - 19, Colors.GREY.c);
			break;
		case "\ue910":
			Hanabi.INSTANCE.fontManager.icon70.drawString(icon, (float) x - 17, animatedY - 19, Colors.GREY.c);
		}

	}

	public void onClick(int mouseX, int mouseY, int x, int y) {
		if (this.isHovering(mouseX, mouseY, x - 32, y - 32, x + 32, y + 32) && handler.canExcecute()) {
			if (screen != null) {
				Minecraft.getMinecraft().displayGuiScreen(screen);
			} else {
				Minecraft.getMinecraft().shutdown();
			}
		}
	}

	private boolean isHovering(int mouseX, int mouseY, float xLeft, float yUp, float xRight, float yBottom) {
		return mouseX > xLeft && mouseX < xRight && mouseY > yUp && mouseY < yBottom;
	}
}