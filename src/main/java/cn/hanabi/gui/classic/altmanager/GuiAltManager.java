package cn.hanabi.gui.classic.altmanager;

import cn.hanabi.Hanabi;
import cn.hanabi.api.MicrosoftLogin;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class GuiAltManager extends GuiScreen {


    String status = "Waiting for login...";

    @Override
    public void initGui() {
        super.initGui();
        GuiButton loginRefreshToken = new GuiButton(0, this.width / 2 - 100, this.height / 2, "Login RefreshToken");
        GuiButton microsoftLogin = new GuiButton(1, this.width / 2 - 100, this.height / 2 + 30, "Microsoft Login");
        GuiButton back = new GuiButton(2, this.width / 2 - 100, this.height / 2 + 60, "Back");
        this.buttonList.clear();
        this.buttonList.add(loginRefreshToken);
        this.buttonList.add(microsoftLogin);
        this.buttonList.add(back);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        Hanabi.INSTANCE.fontManager.comfortaa28.drawCenteredString("Alt Manager", (float) this.width / 2, 20, -1);
        Hanabi.INSTANCE.fontManager.comfortaa22.drawCenteredString(status, (float) this.width / 2, 40, new Color(55, 255, 55).getRGB());
        status = MicrosoftLogin.loginProgressMessage;
        super.drawScreen(mouseX, mouseY, partialTicks);

    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);


        if (button.id == 0) {
            // get clipboard text
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            Transferable contents = clipboard.getContents(null);
            if (contents != null) {
                try {
                    String text = (String) contents.getTransferData(DataFlavor.stringFlavor);
                    if (text.contains("refresh:")){
                        text = text.split("refresh:")[1];
                    }
                    MicrosoftLogin.loginViaRefreshToken(text);
                } catch (UnsupportedFlavorException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        if (button.id == 1) {
            MicrosoftLogin.loginViaBrowser();
        }
        if (button.id == 2) {
            this.mc.displayGuiScreen(new GuiMainMenu());
        }
    }
}
