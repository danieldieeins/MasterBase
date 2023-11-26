package live.nerotv.masterbase.utils;

import live.nerotv.masterbase.Main;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

public class MasterTimer {

    //ungefähr 500% effizienter - gern geschehen

    private static String date;
    public static int days;
    public static World world;
    public static World nether;

    public static void send() {
        Main.config.checkEntry("info.date", Main.getDate());
        date = Main.config.getCFG().getString("info.date");
        Main.config.checkEntry("info.days", 1);
        days = Main.config.getCFG().getInt("info.days");
        startBroadcastTimer(Bukkit.getServer().getScheduler());
    }

    private static void startBroadcastTimer(BukkitScheduler scheduler) {
        int scheduleId = scheduler.scheduleSyncDelayedTask(Main.getInstance(), () -> {
            if(Main.isStarted) {
                world.setDifficulty(Difficulty.NORMAL);
                nether.setDifficulty(Difficulty.NORMAL);
                world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS,false);
                nether.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS,false);
                nether.setGameRule(GameRule.SHOW_DEATH_MESSAGES,false);
                world.setGameRule(GameRule.SHOW_DEATH_MESSAGES,false);
                if (!date.equalsIgnoreCase(Main.getDate())) {
                    String date = Main.getDate();
                    days=days+1;
                    Main.config.set("info.date", date);
                    Main.config.set("info.days", days);
                    MasterTimer.date = date;
                }
                String time = Main.getTime(false);
                //System.out.println(time+" ("+days+")");
                if (Main.config.getCFG().contains("border.day" + days + "." + time + ".size")) {
                    long bTime = 0;
                    if (Main.config.getCFG().contains("border.day" + days + "." + time + ".time")) {
                        bTime = Main.config.getCFG().getLong("border.day" + days + "." + time + ".time");
                    }
                    setWorldborder(Main.config.getCFG().getDouble("border.day" + days + "." + time + ".size"), bTime);
                }
                if(time.equals("00-01")) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"save-all");
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"stop");
                }
            } else {
                setWorldborder(41,0,false);
                world.setDifficulty(Difficulty.PEACEFUL);
                nether.setDifficulty(Difficulty.PEACEFUL);
            }
            startBroadcastTimer(scheduler);
        },1200);
    }

    @Deprecated
    public static void setWorldborder(double size,long time) {
        setWorldborder(size,time,true);
    }

    public static void setWorldborder(double size,long time,boolean notification) {
        double s2 = world.getWorldBorder().getSize();
        if(size>world.getWorldBorder().getSize()) {
            if(notification) {
                double v = size-s2;
                v = (v/time)/2;
                Bukkit.broadcastMessage("§8[§9PvP Masters§8] §7Die WorldBorder §avergrößert §7sich§7 mit einer Geschwindigkeit von §e"+v+" Blöcken pro Sekunde§8! (von "+s2+" zu "+size+")");
                for(Player all:Bukkit.getOnlinePlayers()) {
                    all.playSound(all.getLocation(),Sound.ENTITY_CHICKEN_EGG,100,100);
                }
            }
        } else {
            if(notification) {
                double v = s2-size;
                v = (v/time)/2;
                Bukkit.broadcastMessage("§8[§9PvP Masters§8] §7Die WorldBorder §cverkleinert §7sich mit einer Geschwindigkeit von §e"+v+" Blöcken pro Sekunde§8! (von "+s2+" zu "+size+")");
                for(Player all:Bukkit.getOnlinePlayers()) {
                    all.playSound(all.getLocation(),Sound.ENTITY_CHICKEN_EGG,100,100);
                }
            }
        }
        world.getWorldBorder().setCenter(new Location(world,0.5,0,0.5));
        world.getWorldBorder().setSize(size,time);
        nether.getWorldBorder().setCenter(new Location(nether,0.5,0,0.5));
        nether.getWorldBorder().setSize(size/8,time);
    }
}