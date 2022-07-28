package cn.hanabi;

import aLph4anTi1eaK_cN.Annotation.Setup;
import cn.hanabi.events.EventLoop;
import cn.hanabi.events.EventWorldChange;
import cn.hanabi.irc.IRCClient;
import com.darkmagician6.eventapi.EventManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;

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

    @Setup
    public static void Load() {
        //从本地socket获取账户密码
//        Hanabi.INSTANCE.println("Getting");
//        Socket socket = null;
//        try {
//            socket = new Socket("localhost", 8912);
//            socket.setSoTimeout(5000);
//            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
//            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
//            outputStream.writeUTF("FuckYou");
//            while (!socket.isClosed()) {
//                String received = Objects.requireNonNull(inputStream).readUTF();
//                IRCClient.username = received.split("§")[0];
//                IRCClient.password = received.split("§")[1];
//            }

        client = new IRCClient();
//        client.username = JOptionPane.showInputDialog("username");
//        client.password = JOptionPane.showInputDialog("password");
        client.connect();
        Hanabi.INSTANCE.println("Client loading");
        Hanabi.INSTANCE.startClient();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }

    public static void doLogin() {
        new Hanabi();
    }
}

