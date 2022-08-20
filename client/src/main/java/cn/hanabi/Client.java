package cn.hanabi;

import cn.hanabi.events.EventLoop;
import cn.hanabi.events.EventWorldChange;
import cn.hanabi.gui.common.font.noway.ttfr.FontLoaders;
import cn.hanabi.irc.IRCClient;
import com.darkmagician6.eventapi.EventManager;
import com.eskid.annotation.Native;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import org.lwjgl.opengl.Display;

@Native
public class Client {
    public static boolean active = true;
    public static boolean onDebug = false;

    // Map Must Know
    public static boolean map = true;

    public static WorldClient worldChange;
    public static boolean isGameInit = false;

    public static float pitch;
    public static boolean sleep = false;

    public static IRCClient client;


    public static void onGameLoop() {
        isGameInit = true;
        WorldClient world = Minecraft.getMinecraft().theWorld;
        if (worldChange == null) {
            worldChange = world;
            return;
        }

        if (world == null) {
            worldChange = null;
            return;
        }

        if (worldChange != world) {
            worldChange = world;
            EventManager.call(new EventWorldChange());
        }

        EventManager.call(new EventLoop());
    }

    public static void makeSense() {
        client = new IRCClient();
        client.connect();
        Display.setTitle(Hanabi.CLIENT_NAME + " " + Hanabi.CLIENT_VERSION);
        Hanabi.INSTANCE.fontManager = new FontLoaders();

    }

    public static void doLogin() {
        new Hanabi();
    }
}

