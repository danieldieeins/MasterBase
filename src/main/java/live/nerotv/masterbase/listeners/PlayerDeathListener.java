package live.nerotv.masterbase.listeners;

import live.nerotv.masterbase.Main;
import live.nerotv.masterbase.objects.User;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.UUID;

public class PlayerDeathListener implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        if (Main.isStarted) {
            Main.users.remove(e.getPlayer().getUniqueId().toString());
            if(Main.users.size()==1) {
                UUID winnerID = UUID.fromString(Main.users.get(0));
                if(Bukkit.getPlayer(winnerID)!=null) {
                    Player winner = Bukkit.getPlayer(winnerID);
                    winner.sendTitle("§aDu hast gewonnen!","§7Alle Gegner sind ausgeschieden§8. §7Herzlichen Glückwunsch§8!");
                    winner.playSound(winner.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 100, 1);
                }
                for(Player all:Bukkit.getOnlinePlayers()) {
                    if(!all.getUniqueId().toString().equals(winnerID.toString())) {
                        all.sendTitle("§a"+Bukkit.getOfflinePlayer(winnerID).getName(),"§7hat gewonnen§8!");
                        all.playSound(all.getLocation(),Sound.ENTITY_PLAYER_LEVELUP,100,1);
                    }
                }
            }
            Player p = e.getPlayer();
            User u = Main.getUser(p.getUniqueId());
            if(u.getPlayerStorage().get("player.died")==null) {
                if (p.getKiller() != null) {
                    Bukkit.broadcastMessage("§8[§c-§8] §e" + p.getName() + "§7 wurde von §e" + p.getKiller().getName() + "§7 getötet und ist ausgeschieden§8!");
                } else {
                    Bukkit.broadcastMessage("§8[§c-§8] §e" + p.getName() + "§7 ist ausgeschieden§8!");
                }
                u.getPlayerStorage().set("player.died", true);
            } else {
                e.setKeepInventory(true);
                e.setKeepLevel(true);
                return;
            }
            p.getWorld().strikeLightningEffect(p.getLocation());
            u.setLogoutReason("death");
            if (p.getUniqueId().toString().equals("cd673163-7e9d-4bf3-91b3-cd65ff147fff") || p.getUniqueId().toString().equals("6447757f-59fe-4206-ae3f-dc68ff2bb6f0")) {
                Main.countdowns.get(e.getPlayer().getUniqueId()).setTime(-1);
                p.setGameMode(GameMode.SPECTATOR);
                u.getPlayerStorage().set("player.time."+Main.getDate(), false);
                p.kickPlayer("§cDu bist ausgeschieden§8.");
            } else {
                p.banPlayer("§cDu bist ausgeschieden§8.");
            }
        } else {
            e.setCancelled(true);
        }
        e.getPlayer().setKiller(null);
    }
}