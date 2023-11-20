package live.nerotv.masterbase.commands;

import live.nerotv.masterbase.utils.MasterTimer;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MasterBorderCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender s, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if(s.hasPermission("pvpmasters.border")) {
            if(args.length>0) {
                try {
                    double size = Double.parseDouble(args[0]);
                    long time = 0;
                    if (args.length > 1) {
                        time = Long.parseLong(args[1]);
                    }
                    MasterTimer.setWorldborder(size, time);
                    s.sendMessage("§8[§9PvP Masters§8] §7Du hast die Worldborder auf §e" + size + "§7 gesetzt§8. §7Sie wird in §e" + time + " Sekunden§7 da sein§8. (Nether: "+size/8+" ("+time+"))");
                    if (s instanceof Player p) {
                        p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 100, 100);
                    }
                } catch (NumberFormatException e) {
                    s.sendMessage("§8[§4PvP Masters§8] §7Dies ist §ckeine§7 gültige Zahl§8.");
                    if(s instanceof Player p) {
                        p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_BREAK, 100, 100);
                    }
                }
            } else {
                s.sendMessage("§8[§4PvP Masters§8] §7Du musst mindestens ein Argument (in Form einer Zahl) angeben§8.");
                s.sendMessage("§4Syntax§8: §c/masterborder [Größe] §7(Geschwindigkeit)");
                if(s instanceof Player p) {
                    p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_BREAK, 100, 100);
                }
            }
        } else {
            s.sendMessage("§8[§4PvP Masters§8] §7Das darfst du §cnicht§8.");
            if(s instanceof Player p) {
                p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_BREAK, 100, 100);
            }
        }
        return false;
    }
}
