package live.nerotv.masterbase.listeners;

import live.nerotv.masterbase.Main;
import live.nerotv.masterbase.objects.Team;
import live.nerotv.masterbase.objects.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChatListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        String message = e.getMessage();
        Player p = e.getPlayer();
        User u = Main.getUser(p.getUniqueId());
        String prefix = ""; String suffix = "";
        if(u.getTeam()!=null) {
            Team team = u.getTeam();
            if(team.getPrefix()!=null) {
                prefix = team.getPrefix();
            }
            if(team.getSuffix()!=null) {
                suffix = team.getSuffix();
            }
        }
        e.setCancelled(true);
        Bukkit.broadcastMessage("§f"+prefix+p.getName()+suffix+"§8: §7"+message.replace("&&","%and%").replace("&","§").replace("%and%","&"));
    }
}