package cn.Hanabi.irc.client.test;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import top.fpsmaster.irc.packets.Packet;
import top.fpsmaster.irc.packets.impl.PacketMessage;
import top.fpsmaster.irc.packets.impl.clientside.PacketHeartBeat;
import top.fpsmaster.irc.packets.impl.clientside.PacketLogin;
import top.fpsmaster.irc.packets.impl.serverside.PacketGetRep;
import top.fpsmaster.irc.packets.impl.serverside.PacketServerRep;
import top.fpsmaster.irc.utils.PacketUtil;

import static top.fpsmaster.irc.client.test.ClientTest.bootstrap;

public class Handler extends SimpleChannelInboundHandler<String> {

    @Override
    public void channelInactive(final ChannelHandlerContext ctx){
        System.out.println("Disconnected from server");
    }

    @Override
    public void channelUnregistered(final ChannelHandlerContext ctx) throws InterruptedException {
        System.out.println("Reconnecting");
        bootstrap.connect("localhost", 6668).sync();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        ctx.writeAndFlush(PacketUtil.pack(new PacketLogin("SuperSkidder", "PWD")));
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Scanner sc = new Scanner(System.in);
//                while (true) {
//                    String msg = sc.nextLine();
//                    switch (msg.split(" ")[0]) {
//                        case "MSG":
//                            sendChatMessage(ctx, msg.substring(4));
//                            break;
//                        case "GET":
//                            ctx.writeAndFlush(PacketUtil.pack(new PacketGet(String.valueOf(System.currentTimeMillis()), msg.split(" ")[1])));
//                            break;
//                        case "REG":
//                            ctx.writeAndFlush(PacketUtil.pack(new PacketRegister(msg.split(" ")[1], msg.split(" ")[2], msg.length() == 4 ? msg.split(" ")[3] : "")));
//                            break;
//                        case "LOGIN":
//                            ctx.writeAndFlush(PacketUtil.pack(new PacketLogin(msg.split(" ")[1], msg.split(" ")[2])));
//                            break;
//                    }
//                }
//            }
//        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
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
                    System.out.println(packetLogin.userRank);
//                    FpsMaster.rank  = packetLogin.content;
//                    FpsMaster.INSTANCE.loggedIn = true;
                    break;
                case MESSAGE:
                    System.out.println(p.content);
//                    if(Minecraft.getMinecraft().thePlayer != null){
//                        PlayerUtil.tellPlayerWithoutPrefix(p.content);
//                    }
                    break;
                case RETURN:
                    PacketGetRep packetReturn = PacketUtil.unpack(s, PacketGetRep.class);
                    System.out.println(packetReturn.id + " " + packetReturn.content);
                    break;
                case REGISTERREP:
                    break;
            }
        }
    }
}