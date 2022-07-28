package cn.hanabi.irc;

import cn.hanabi.Client;
import cn.hanabi.Hanabi;
import cn.hanabi.gui.common.GuiLogin;
import cn.hanabi.gui.common.GuiRegister;
import cn.hanabi.irc.packets.Packet;
import cn.hanabi.irc.packets.impl.PacketMessage;
import cn.hanabi.irc.packets.impl.clientside.PacketHeartBeat;
import cn.hanabi.irc.packets.impl.clientside.PacketLogin;
import cn.hanabi.irc.packets.impl.serverside.PacketGetRep;
import cn.hanabi.irc.packets.impl.serverside.PacketRegisterRep;
import cn.hanabi.irc.packets.impl.serverside.PacketServerRep;
import cn.hanabi.irc.utils.PacketUtil;
import cn.hanabi.utils.auth.Check;
import cn.hanabi.utils.game.PlayerUtil;
import com.eskid.annotation.Native;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.minecraft.client.Minecraft;

@Native
public class ClientHandler extends SimpleChannelInboundHandler<String> {

    public static ChannelHandlerContext context;
    private int rec;
    public static long currentTime = System.currentTimeMillis();;

    @Override
    public void channelInactive(final ChannelHandlerContext ctx) {
        Hanabi.INSTANCE.println("Disconnected from server");
        Hanabi.INSTANCE.loggedIn = false;
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) {
        if (rec < 10) {
            Hanabi.INSTANCE.println("IRC Reconnecting...");
            Client.client.reconnect();
            Hanabi.INSTANCE.loggedIn = true;
            rec++;
        } else {
            Hanabi.INSTANCE.crash();
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        context = ctx;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (System.currentTimeMillis() - currentTime > 2000) {
                        Hanabi.INSTANCE.crash();
                    }
                    ctx.writeAndFlush(PacketUtil.pack(new PacketHeartBeat(String.valueOf(System.currentTimeMillis()))));
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
        }).start();
    }

    private void sendChatMessage(ChannelHandlerContext ctx, String msg) {
        ctx.writeAndFlush(PacketUtil.pack(new PacketMessage(msg)));
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        Packet p = PacketUtil.unpack(s, Packet.class);
        if (p != null) {
            switch (p.type) {
                case LOGINREP:
                    PacketServerRep packetLogin = PacketUtil.unpack(s, PacketServerRep.class);
                    GuiLogin.staus = "(" + packetLogin.content + ")";
                    Hanabi.INSTANCE.rank = packetLogin.content;
                    Hanabi.INSTANCE.loggedIn = true;
                    break;
                case MESSAGE:
                    Hanabi.INSTANCE.println(p.content);
                    if (Minecraft.getMinecraft().thePlayer != null) {
                        PlayerUtil.tellPlayerWithoutPrefix(p.content);
                    }
                    break;
                case RETURN:
                    PacketGetRep packetReturn = PacketUtil.unpack(s, PacketGetRep.class);
                    PlayerUtil.tellPlayerWithoutPrefix(packetReturn.content);
                    break;
                case REGISTERREP:
                    PacketRegisterRep packetRegisterRep = PacketUtil.unpack(s, PacketRegisterRep.class);
                    GuiRegister.status = packetRegisterRep.content;
                    break;
                case HEARTBEATREP:
                    currentTime = System.currentTimeMillis();
                    break;
            }
        }
    }
}