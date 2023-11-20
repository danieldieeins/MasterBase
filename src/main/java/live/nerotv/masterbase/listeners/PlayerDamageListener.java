package live.nerotv.masterbase.listeners;

import live.nerotv.masterbase.Main;
import live.nerotv.masterbase.objects.User;
import live.nerotv.masterbase.utils.Countdown;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerDamageListener implements Listener {

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if(Main.isStarted) {
            if (e.getEntity() instanceof Player p) {
                if (e.getDamager() instanceof Player t) {
                    User tU = Main.getUser(t.getUniqueId());
                    if(Main.isProtected) {
                        e.setCancelled(true);
                        return;
                    }
                    User u = Main.getUser(p.getUniqueId());
                    if(!u.getTeam().getName().equals(tU.getTeam().getName())) {
                        u.setHit(1);
                        new Countdown(5, Main.getInstance()) {
                            @Override
                            public void count(int current) {
                                if (current == 1) {
                                    u.setHit(0);
                                }
                            }
                        }.start();
                    }
                    int i = Main.countdowns.get(p.getUniqueId()).getTime();
                    if(!u.getTeam().getName().equals(tU.getTeam().getName())) {
                        if (i < 120) {
                            p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 40, 40);
                            Main.countdowns.get(p.getUniqueId()).setTime(120);
                        }
                    } else {
                        e.setDamage(0);
                    }
                }
            }
        } else {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if(!Main.isStarted) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(BlockBreakEvent e) {
        if(!Main.isStarted) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(BlockPlaceEvent e) {
        if(!Main.isStarted) {
            e.setCancelled(true);
        }
    }
}