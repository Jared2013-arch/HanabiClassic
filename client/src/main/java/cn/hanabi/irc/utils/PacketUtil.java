package cn.hanabi.irc.utils;

import cn.hanabi.irc.packets.Packet;
import com.google.gson.Gson;
import org.apache.commons.io.Charsets;

public class PacketUtil {

    public static <T extends Packet> T unpack(String content, Class<T> type) {
        content = new String(content.getBytes(),Charsets.UTF_8);
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
        return new String(gson.toJson(packet).getBytes(), Charsets.UTF_8);
    }


}
