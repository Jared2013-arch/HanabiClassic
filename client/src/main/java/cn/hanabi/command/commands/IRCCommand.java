package cn.hanabi.command.commands;

import cn.hanabi.Client;
import cn.hanabi.Hanabi;
import cn.hanabi.command.Command;
import cn.hanabi.irc.ClientHandler;
import cn.hanabi.irc.packets.impl.PacketMessage;
import cn.hanabi.irc.packets.impl.clientside.PacketCommand;
import cn.hanabi.irc.packets.impl.clientside.PacketGet;
import cn.hanabi.irc.utils.PacketUtil;
import cn.hanabi.utils.game.PlayerUtil;

import java.util.List;

public class IRCCommand extends Command {
    public IRCCommand() {
        super("irc", "i");
    }

    @Override
    public void run(String alias, String[] args) {
        if (args != null) {
            if (args[0].equals("get")) {
                ClientHandler.context.writeAndFlush(
                        PacketUtil.pack(
                                new PacketGet("2", args[1])
                        )
                );
            } else if (args[0].equals("cmd")) {
                String[] cmdarg = new String[args.length - 1];
                System.arraycopy(args, 1, cmdarg, 0, args.length - 1);
                ClientHandler.context.writeAndFlush(
                        PacketUtil.pack(
                                new PacketCommand(cmdarg)
                        )
                );
            } else {
                StringBuilder sb = new StringBuilder();
                for (String arg : args) {
                    sb.append(arg).append(" ");
                }
                ClientHandler.context.writeAndFlush(
                        PacketUtil.pack(
                                new PacketMessage(sb.toString())
                        )
                );
            }
        } else {
            PlayerUtil.tellPlayer("Usage: .irc <message>");
        }
    }

    @Override
    public List<String> autocomplete(int arg, String[] args) {
        return null;
    }
}
