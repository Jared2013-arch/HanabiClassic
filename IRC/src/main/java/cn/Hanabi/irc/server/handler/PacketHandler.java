package cn.Hanabi.irc.server.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import top.fpsmaster.irc.management.RankManager;
import top.fpsmaster.irc.management.User;
import top.fpsmaster.irc.packets.Packet;
import top.fpsmaster.irc.packets.impl.PacketMessage;
import top.fpsmaster.irc.packets.impl.clientside.PacketGet;
import top.fpsmaster.irc.packets.impl.clientside.PacketLogin;
import top.fpsmaster.irc.packets.impl.clientside.PacketRegister;
import top.fpsmaster.irc.packets.impl.serverside.PacketGetRep;
import top.fpsmaster.irc.packets.impl.serverside.PacketHeartBeatRep;
import top.fpsmaster.irc.packets.impl.serverside.PacketRegisterRep;
import top.fpsmaster.irc.packets.impl.serverside.PacketServerRep;
import top.fpsmaster.irc.server.database.DBHelper;
import top.fpsmaster.irc.server.utils.SQLUtils;
import top.fpsmaster.irc.utils.PacketUtil;

import java.util.Map;

import static top.fpsmaster.irc.server.ServerMain.debug;
import static top.fpsmaster.irc.server.handler.NettyServerHandler.channelGroup;
import static top.fpsmaster.irc.server.handler.NettyServerHandler.users;
import static top.fpsmaster.irc.utils.PacketUtil.unpack;

public class PacketHandler {
    public static void handle(ChannelHandlerContext ctx, String o) {
        Channel channel = ctx.channel();
        Packet p = unpack(o, Packet.class);
        switch (p.type) {
            case LOGIN:
                PacketLogin packetLogin = PacketUtil.unpack(o, PacketLogin.class);
                String rank = packetLogin.user.login();
                channel.writeAndFlush(PacketUtil.pack(new PacketServerRep(rank, "1.0", String.valueOf(users.size()), rank)));
                if (rank.equals("Failed to login")) {
                    channel.close();
                    ctx.close();
                    channelGroup.remove(ctx);
                    if (debug) {
                        System.out.println("[DEBUG]Login failed: " + packetLogin.user.username + " ip:" + ctx.channel().remoteAddress());
                    }
                } else {
                    channelGroup.add(channel);
                    users.put(ctx, packetLogin.user);
                    channelGroup.writeAndFlush(PacketUtil.pack(new PacketMessage("§6[IRC]" + packetLogin.user.rankInGame + packetLogin.user.username + "§r connected to the irc.")));
                    if (debug) {
                        System.out.println("[DEBUG]Login successfully: " + packetLogin.user.rank + " " + packetLogin.user.username + " ip:" + ctx.channel().remoteAddress());
                    }
                }
                break;
            case COMMAND:
                break;
            case EXIT:
                ctx.close();
                break;
            case MESSAGE:
                PacketMessage packetMessage = PacketUtil.unpack(o, PacketMessage.class);
                User user = users.get(ctx);
                channelGroup.writeAndFlush(PacketUtil.pack(new PacketMessage("§6[IRC]" + user.rankInGame + user.username + ": " + packetMessage.content)));
                break;
            case HEARTBEAT:
                sendPacket(ctx.channel(), new PacketHeartBeatRep(String.valueOf(System.currentTimeMillis())));
                break;
            case GET:
                PacketGet packetGet = PacketUtil.unpack(o, PacketGet.class);
                if (packetGet.content.equals("OnlineCount"))
                    ctx.writeAndFlush(PacketUtil.pack(new PacketGetRep(packetGet.id, String.valueOf(users.size()))));
                break;
            case REGISTER:
                PacketRegister packetRegister = PacketUtil.unpack(o, PacketRegister.class);
                if (SQLUtils.checkInject(packetRegister.username) || SQLUtils.checkInject(packetRegister.password) || SQLUtils.checkInject(packetRegister.key)) {
                    break;
                }
                String result = DBHelper.register(packetRegister.username, packetRegister.password, packetRegister.key);
                sendPacket(ctx.channel(), new PacketRegisterRep(result));
                break;
        }
    }


    public static void sendPacket(RankManager.Ranks who, Packet packet) {
        for (Map.Entry<ChannelHandlerContext, User> e : users.entrySet()) {
            if (check(e.getValue().rank, who)) {
                sendPacket(e.getKey().channel(), packet);
            }
        }
    }

    private static boolean check(RankManager.Ranks rank, RankManager.Ranks who) {
        if (who.equals(RankManager.Ranks.User))
            return true;
        if (rank.equals(who))
            return true;
        return false;
    }


    public static void sendPacket(Channel channel, Packet packet) {
        channel.writeAndFlush(PacketUtil.pack(packet));
    }

}
