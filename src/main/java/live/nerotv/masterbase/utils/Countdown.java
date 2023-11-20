package live.nerotv.masterbase.utils;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public abstract class Countdown {

    private int time;
    protected BukkitTask task;
    protected final Plugin plugin;

    public Countdown(int time, Plugin plugin) {
        this.time = time;
        this.plugin = plugin;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void addTime(int time) {
        setTime(getTime()+time);
    }

    public void removeTime(int time) {
        setTime(getTime()-time);
    }

    public abstract void count(int current);

    public final void start() {
        task = new BukkitRunnable() {
            @Override
            public void run() {
                count(time);
                if (time-- <= 0) cancel();
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    public final void startTicks() {
        task = new BukkitRunnable() {
            @Override
            public void run() {
                count(time);
                if (time-- <= 0) cancel();
            }
        }.runTaskTimer(plugin, 0L, 0L);
    }
}