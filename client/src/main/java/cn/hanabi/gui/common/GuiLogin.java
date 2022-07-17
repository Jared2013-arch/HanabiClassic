package cn.hanabi.gui.common;

import cn.hanabi.Hanabi;
import cn.hanabi.gui.classic.altmanager.*;
import cn.hanabi.irc.ClientHandler;
import cn.hanabi.irc.IRCClient;
import cn.hanabi.irc.packets.impl.clientside.PacketLogin;
import cn.hanabi.irc.utils.PacketUtil;
import cn.hanabi.utils.render.RenderUtil;
import net.minecraft.client.gui.*;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.io.IOException;

public class GuiLogin extends GuiScreen {

    public static String staus;
    private PasswordField password;
    private GuiTextField username;
    public static String status;
    public GuiScreen per;

    public GuiLogin(GuiScreen per) {
        this.per = per;
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        try {
            switch (button.id) {
//                case 1: {
//                    this.mc.displayGuiScreen(new GuiMainMenu());
//                    break;
//                }
                case 0: {
                    ClientHandler.context.writeAndFlush(PacketUtil.pack(new PacketLogin(username.getText(), password.getText())));
                    break;
                }
                case 2:{
                    this.mc.displayGuiScreen(new GuiRegister());
                    break;
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void drawScreen(final int x, final int y, final float z) {
        if (Hanabi.INSTANCE.loggedIn)
            mc.displayGuiScreen(per);
        this.drawDefaultBackground();
        ScaledResolution res = new ScaledResolution(mc);
        RenderUtil.drawRect(0, 0, res.getScaledWidth(), res.getScaledHeight(), 0);
        this.username.drawTextBox();
        this.password.drawTextBox();
        this.drawCenteredString(this.mc.fontRendererObj, "Hanabi Login", this.width / 2, 20, -1);
        this.drawCenteredString(this.mc.fontRendererObj, status, this.width / 2, 29, -1);
        if (this.username.getText().isEmpty()) {
            this.drawString(this.mc.fontRendererObj, "Username", this.width / 2 - 96, 66, -7829368);
        }
        if (this.password.getText().isEmpty()) {
            this.drawString(this.mc.fontRendererObj, "Password", this.width / 2 - 96, 106, -7829368);
        }
        super.drawScreen(x, y, z);
    }

    @Override
    public void initGui() {
        final int var3 = this.height / 4 + 24;
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, var3 + 72 + 12, "Login"));
        this.buttonList.add(new GuiButton(2, this.width / 2 - 100, var3 + 72 + 12 + 24 + 24, "Register"));
        this.username = new GuiTextField(var3, this.mc.fontRendererObj, this.width / 2 - 100, 60, 200, 20);
        this.password = new PasswordField(this.mc.fontRendererObj, this.width / 2 - 100, 100, 200, 20);
        this.username.setFocused(true);
        Keyboard.enableRepeatEvents(true);
    }

    @Override
    protected void keyTyped(final char character, final int key) {
        try {
            super.keyTyped(character, key);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (character == '\t') {
            if (!this.username.isFocused() && !this.password.isFocused()) {
                this.username.setFocused(true);
            } else {
                this.username.setFocused(this.password.isFocused());
                this.password.setFocused(!this.username.isFocused());
            }
        }
        if (character == '\r') {
            this.actionPerformed(this.buttonList.get(0));
        }
        this.username.textboxKeyTyped(character, key);
        this.password.textboxKeyTyped(character, key);
    }

    @Override
    protected void mouseClicked(final int x, final int y, final int button) {
        try {
            super.mouseClicked(x, y, button);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.username.mouseClicked(x, y, button);
        this.password.mouseClicked(x, y, button);
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    public void updateScreen() {
        this.username.updateCursorCounter();
        this.password.updateCursorCounter();
    }
}
