package cn.hanabi.command.commands;

import cn.hanabi.command.Command;
import cn.hanabi.irc.ClientHandler;
import cn.hanabi.irc.packets.impl.PacketMessage;
import cn.hanabi.irc.packets.impl.clientside.PacketCommand;
import cn.hanabi.irc.utils.PacketUtil;

import java.util.List;

public class CommandIRCCommand extends Command {
    public CommandIRCCommand() {
        super("irccmd","ic","cmd");
    }

    @Override
    public void run(String alias, String[] args) {
        ClientHandler.context.writeAndFlush(PacketUtil.pack(new PacketCommand(args)));
    }

    @Override
    public List<String> autocomplete(int arg, String[] args) {
        return null;
    }
}
