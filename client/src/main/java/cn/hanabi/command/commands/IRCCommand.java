package cn.hanabi.command.commands;

import cn.hanabi.command.Command;
import cn.hanabi.irc.ClientHandler;
import cn.hanabi.irc.packets.impl.PacketMessage;
import cn.hanabi.irc.utils.PacketUtil;

import java.util.List;

public class IRCCommand extends Command {
    public IRCCommand() {
        super("irc");
    }

    @Override
    public void run(String alias, String[] args) {
        String msg = "";
        for (String arg : args) {
            msg = msg + " " + arg;
        }
        ClientHandler.context.writeAndFlush(PacketUtil.pack(new PacketMessage(msg)));
    }

    @Override
    public List<String> autocomplete(int arg, String[] args) {
        return null;
    }
}
