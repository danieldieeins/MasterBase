package live.nerotv.masterbase.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MasterMessageCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender s, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if(!(s instanceof Player p)) {
            if(args.length==0) {
                s.sendMessage("§8[§4PvP Masters§8] §7Du musst mindestens ein Argument angeben§8.");
                s.sendMessage("§4Syntax§8: §c/mastermessage [Nachricht]");
            } else {
                String m="";
                for(int i=0;i<args.length;i++) {
                    m=m+args[i]+" ";
                }
                s.sendMessage("§e§bMasterChat§8: §7"+m);
            }
        } else {
            p.performCommand("nichtexis");
        }
        return false;
    }
}