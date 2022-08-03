package cn.hanabi.irc.server.handler;

import cn.hanabi.irc.packets.impl.PacketMessage;
import cn.hanabi.irc.packets.impl.clientside.PacketCommand;
import cn.hanabi.irc.packets.impl.clientside.PacketGet;
import cn.hanabi.irc.packets.impl.clientside.PacketLogin;
import cn.hanabi.irc.packets.impl.serverside.*;
import cn.hanabi.irc.utils.LogUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import cn.hanabi.irc.management.RankManager;
import cn.hanabi.irc.management.User;
import cn.hanabi.irc.packets.Packet;
import cn.hanabi.irc.packets.impl.clientside.PacketRegister;
import cn.hanabi.irc.server.database.DBHelper;
import cn.hanabi.irc.utils.PacketUtil;

import java.text.SimpleDateFormat;
import java.util.Map;

import static cn.hanabi.irc.utils.PacketUtil.unpack;

public class PacketHandler {
    public static void handle(ChannelHandlerContext ctx, String o) {
        Channel channel = ctx.channel();
        Packet p = unpack(o, Packet.class);
        switch (p.type) {
            case LOGIN:
                PacketLogin packetLogin = PacketUtil.unpack(o, PacketLogin.class);
                String rank = packetLogin.user.login();
                if (rank.length() > 14) { //todo: WTF IS THIS
                    channel.close();
                    ctx.close();
                    NettyServerHandler.channelGroup.remove(ctx);
                    LogUtil.info("Login failed: " + packetLogin.user.username + " ip:" + ctx.channel().remoteAddress());
                } else {
                    NettyServerHandler.channelGroup.add(channel);
                    NettyServerHandler.users.put(ctx, packetLogin.user);
                    channel.writeAndFlush(PacketUtil.pack(new PacketServerRep(rank, "1.2", String.valueOf(NettyServerHandler.users.size()), rank)));
                    NettyServerHandler.channelGroup.writeAndFlush(PacketUtil.pack(new PacketMessage("§6[IRC]" + packetLogin.user.rankInGame + packetLogin.user.username + "§r connected to the irc.")));
                    LogUtil.info("Login successfully: " + packetLogin.user.rank + " " + packetLogin.user.username + " ip:" + ctx.channel().remoteAddress());
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    DBHelper.record(packetLogin.user.username, ctx.channel().remoteAddress().toString(), sdf.format(System.currentTimeMillis()));
                }
                break;
            case COMMAND:
                PacketCommand packetCommand = PacketUtil.unpack(o, PacketCommand.class);
                if (packetCommand.command[0].equals("kick")) {
                    NettyServerHandler.users.forEach((key, value) -> {
                        if (value.username.equals(packetCommand.command[1])) {
                            key.channel().writeAndFlush(PacketUtil.pack(new PacketMessage("KICKUSER")));
                            NettyServerHandler.channelGroup.writeAndFlush(PacketUtil.pack(new PacketMessage("§6[IRC]" + NettyServerHandler.users.get(ctx).username + "Kicked" + packetCommand.command[1])));
                            key.close();
                        }
                    });
                }
                break;
            case EXIT:
                ctx.close();
                break;
            case MESSAGE:
                PacketMessage packetMessage = PacketUtil.unpack(o, PacketMessage.class);
                User user = NettyServerHandler.users.get(ctx);
                NettyServerHandler.channelGroup.writeAndFlush(PacketUtil.pack(new PacketMessage("§6[IRC]" + user.rankInGame + user.username + ": " + packetMessage.content)));
                break;
            case HEARTBEAT:
                sendPacket(ctx.channel(), new PacketHeartBeatRep(String.valueOf(System.currentTimeMillis())));
                break;
            case GET:
                PacketGet packetGet = PacketUtil.unpack(o, PacketGet.class);
                if (packetGet.content.equals("OnlineCount"))
                    ctx.writeAndFlush(PacketUtil.pack(new PacketGetRep(packetGet.id, String.valueOf(NettyServerHandler.users.size()))));
                break;
            case REGISTER:
                PacketRegister packetRegister = PacketUtil.unpack(o, PacketRegister.class);
                String result = DBHelper.register(packetRegister.username, packetRegister.password, packetRegister.key);
                sendPacket(ctx.channel(), new PacketRegisterRep(result));
                break;
        }
    }


    public static void sendPacket(RankManager.Ranks who, Packet packet) {
        for (Map.Entry<ChannelHandlerContext, User> e : NettyServerHandler.users.entrySet()) {
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
