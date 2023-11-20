package live.nerotv.masterbase.commands;

import live.nerotv.masterbase.Main;
import live.nerotv.masterbase.utils.Countdown;
import live.nerotv.masterbase.utils.MasterTimer;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class StartCommand implements CommandExecutor {

    private boolean isStarting = false;

    @Override
    public boolean onCommand(@NotNull CommandSender s, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if(s.hasPermission("pvpmasters.start")) {
            if(isStarting) {
                s.sendMessage("§8[§4PvP Masters§8] §cPVP Masters§7 startet bereits§8!");
                if(s instanceof Player p) {
                    p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_BREAK, 100, 100);
                }
                return false;
            }
            isStarting = true;
            if(Main.isStarted) {
                s.sendMessage("§8[§4PvP Masters§8] §cPVP Masters§7 ist bereits gestartet§8!");
                if(s instanceof Player p) {
                    p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_BREAK, 100, 100);
                }
                return false;
            } else {
                new Countdown(30, Main.getInstance()) {
                    @Override
                    public void count(int current) {
                        if(current==30) {
                            Bukkit.broadcastMessage("§8[§9PvP Masters§8] §eStaffel 3§7 startet in §e30 Sekunden§8!");
                            for(Player all:Bukkit.getOnlinePlayers()) {
                                all.playSound(all.getLocation(),Sound.BLOCK_NOTE_BLOCK_PLING,100,100);
                            }
                        } else if(current==20) {
                            Bukkit.broadcastMessage("§8[§9PvP Masters§8] §eStaffel 3§7 startet in §e20 Sekunden§8!");
                            for(Player all:Bukkit.getOnlinePlayers()) {
                                all.playSound(all.getLocation(),Sound.BLOCK_NOTE_BLOCK_PLING,100,100);
                            }
                        } else if(current<=10) {
                            if(current==1) {
                                Bukkit.broadcastMessage("§8[§9PvP Masters§8] §eStaffel 3§7 startet in §eeiner Sekunde§8!");
                                for(Player all:Bukkit.getOnlinePlayers()) {
                                    all.playSound(all.getLocation(),Sound.BLOCK_NOTE_BLOCK_PLING,100,100);
                                }
                            } else if(current==0) {
                                Main.isProtected = true;
                                new Countdown(600, Main.getInstance()) {
                                    @Override
                                    public void count(int current) {
                                        if(current==0) {
                                            Main.isProtected = false;
                                            Bukkit.broadcastMessage("§8[§9PvP Masters§8] §7Die §eSchutzzeit§7 ist nun beendet§8. §7Lasst die Kämpfe beginnen§8!");
                                            for(Player all:Bukkit.getOnlinePlayers()) {
                                                all.playSound(all.getLocation(),Sound.BLOCK_NOTE_BLOCK_PLING,100,100);
                                            }
                                        } else if(current==300) {
                                            Bukkit.broadcastMessage("§8[§9PvP Masters§8] §7Die §eSchutzzeit§7 endet in §efünf Minuten§8!");
                                            for(Player all:Bukkit.getOnlinePlayers()) {
                                                all.playSound(all.getLocation(),Sound.BLOCK_NOTE_BLOCK_PLING,100,100);
                                            }
                                        }
                                    }
                                }.start();
                                Bukkit.getWorlds().get(0).setTime(0);
                                Main.config.set("config.started",true);
                                Bukkit.broadcastMessage("§8[§9PvP Masters§8] §eStaffel 3§7 startet in §eJETZT§8. §7Viel Erfolg an alle Teams§8!");
                                Bukkit.broadcastMessage("§8[§9PvP Masters§8] §eAlle Teilnehmer§7 können sich §e10 Minuten lang§7 nicht angreifen§8!");
                                Main.isStarted = true;
                                Bukkit.getWorlds().get(0).setDifficulty(Difficulty.NORMAL);
                                Bukkit.getWorlds().get(1).setDifficulty(Difficulty.NORMAL);
                                MasterTimer.setWorldborder(5000,250);
                                for(Player all:Bukkit.getOnlinePlayers()) {
                                    all.playSound(all.getLocation(),Sound.BLOCK_NOTE_BLOCK_PLING,100,100);
                                    Main.countdowns.get(all.getUniqueId()).start();
                                }
                            } else {
                                Bukkit.broadcastMessage("§8[§9PvP Masters§8] §eStaffel 3§7 startet in §e"+current+" Sekunden§8!");
                                for(Player all:Bukkit.getOnlinePlayers()) {
                                    all.playSound(all.getLocation(),Sound.BLOCK_NOTE_BLOCK_PLING,100,100);
                                }
                            }
                        }
                    }
                }.start();
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
