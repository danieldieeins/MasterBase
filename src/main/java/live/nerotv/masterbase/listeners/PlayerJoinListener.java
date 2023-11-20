package live.nerotv.masterbase.listeners;

import live.nerotv.masterbase.Main;
import live.nerotv.masterbase.objects.User;
import live.nerotv.masterbase.utils.MasterTimer;
import live.nerotv.masterbase.utils.storage.Storage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPreLoginEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onLogin(PlayerJoinEvent e) {
        e.setJoinMessage(null);
        Player p = e.getPlayer();
        if(Main.isStarted) {
            p.sendTitle("§9Willkommen zurück §e" + p.getName(), "§7Es ist §aTag " + MasterTimer.days + " §8!");
        }
        User u = Main.getUser(p.getUniqueId());
        if(u.getPlayer()==null) {
            u.setPlayer(p);
        }
    }

    @EventHandler
    public void preLogin(PlayerPreLoginEvent e) {
        Storage storage = new Storage("plugins/MasterBase/players/" + e.getUniqueId() + ".yml");
        String time[] = Main.getTime(false).split("-", 2);
        int i = Integer.parseInt(time[0]);
        if(i<13||i>20) {
            e.setKickMessage("§cDer Server ist nur zwischen 13 und 21 Uhr betretbar§8!");
            e.setResult(PlayerPreLoginEvent.Result.KICK_OTHER);
            return;
        }
        if(storage.get("player.time."+Main.getDate())!=null) {
            if((boolean)storage.get("player.time."+Main.getDate())) {
                e.setKickMessage("§cDeine heutige Spielzeit ist abgelaufen§8!");
                e.setResult(PlayerPreLoginEvent.Result.KICK_OTHER);
            }
        }
    }
}