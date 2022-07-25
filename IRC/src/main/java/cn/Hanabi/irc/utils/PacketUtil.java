package cn.hanabi.irc.utils;

import cn.hanabi.irc.server.utils.LogUtil;
import com.google.gson.Gson;
import cn.hanabi.irc.packets.Packet;

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
        if(result.type != Packet.Type.HEARTBEAT) {
            LogUtil.packet(" [Received] " + result.type.name() + ":  " + content);
        }
        return result;
    }

    public static String pack(Packet packet) {
        Gson gson = new Gson();
        String s = gson.toJson(packet);
        if(packet.type != Packet.Type.HEARTBEATREP) {
            LogUtil.packet(" [Send] " + packet.type.name() + ":  " + s);
        }
        return s;
    }


}
