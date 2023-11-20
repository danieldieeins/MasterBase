package live.nerotv.masterbase.listeners;

import live.nerotv.masterbase.Main;
import live.nerotv.masterbase.objects.User;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void onLogout(PlayerQuitEvent e) {
        e.setQuitMessage(null);
        User u = Main.getUser(e.getPlayer().getUniqueId());
        if(u.getHit()==1) {
            u.getPlayer().setHealth(0);
        }
        u.logout();
    }
}
