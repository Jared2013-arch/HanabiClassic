package cn.Hanabi.irc.utils;

import com.google.gson.Gson;
import top.fpsmaster.irc.packets.Packet;

public class PacketUtil {

    public static <T extends Packet> T unpack(String content, Class<T> type) {
        Gson gson = new Gson();
        T result = null;
        try {

            result = gson.fromJson(content, type);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Json transform failed:" + content);
        }
        return result;
    }

    public static String pack(Packet packet) {
        Gson gson = new Gson();
        return gson.toJson(packet);
    }


}
