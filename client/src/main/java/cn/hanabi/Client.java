package cn.hanabi;

import aLph4anTi1eaK_cN.Annotation.Setup;
import cn.hanabi.events.EventLoop;
import cn.hanabi.events.EventWorldChange;
import cn.hanabi.irc.IRCClient;
import com.darkmagician6.eventapi.EventManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Client {
    public static String username = "SuperSkidder";
    public static String rank = "admin";
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

    @Setup
    public static void Load() {
        client = new IRCClient();
        client.connect();

        //从本地socket获取账户密码
        try {
            ServerSocket socketServer = new ServerSocket(8912);
            Socket accept = socketServer.accept();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(accept.getInputStream()));
            String s = bufferedReader.readLine();
            client.username = s.split("\247")[0];
            client.password = s.split("\247")[0];
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Hanabi.INSTANCE.startClient();
    }

    public static void doLogin() {
        new Hanabi();
    }
}

