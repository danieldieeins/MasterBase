package live.nerotv.masterbase.objects;

import live.nerotv.masterbase.Main;
import live.nerotv.masterbase.utils.Countdown;
import live.nerotv.masterbase.utils.storage.Storage;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.UUID;

public class User {

    private UUID uuid;
    private OfflinePlayer offlinePlayer;
    private Player player;
    private Storage playerStorage;
    private String logoutReason;
    private Team team;
    private int hit;

    public User(UUID uuid) {
        hit = 0;
        this.uuid = uuid;
        Storage playerStorage = new Storage("plugins/MasterBase/players/" + uuid + ".yml");
        this.playerStorage = playerStorage;
        team = null;
        if(playerStorage.getYaml().getCFG().contains("player.team")) {
            if (Main.teams.containsKey(playerStorage.getYaml().getCFG().getString("player.team"))) {
                team = Main.teams.get(playerStorage.getYaml().getCFG().getString("player.team"));
            }
        }
        playerStorage.set("player.login." + Main.getDate() + "." + Main.getTime(true), "login");
        offlinePlayer = Bukkit.getOfflinePlayer(uuid);
        if (Bukkit.getPlayer(uuid) != null) {
            player = Bukkit.getPlayer(uuid);
            player.setScoreboard(Main.scoreboard);
            if(!Main.countdowns.containsKey(uuid)) {
                Main.countdowns.put(uuid,new Countdown(1200, Main.getInstance()) {
                    @Override
                    public void count(int current) {
                        Player p = Bukkit.getPlayer(uuid);
                        UUID u = uuid;
                        if(p!=null) {
                            int sec = current%60;
                            String seconds;
                            if(sec<10) {
                                seconds = "0"+sec;
                            } else {
                                seconds = sec+"";
                            }
                            p.sendActionBar("§8" + current / 60 + ":" +seconds /*current % 60*/);
                        }
                        if (current == 121) {
                            if(p!=null) {
                                p.sendMessage("§cAchtung§8: §7Deine Spielzeit endet in §ezwei Minuten§8.");
                                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 100, 100);
                            }
                        } else if (current == 60) {
                            if(p!=null) {
                                p.sendMessage("§cAchtung§8: §7Deine Spielzeit endet in §eeiner Minute§8.");
                                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 100, 100);
                            }
                        } else if (current <= 10) {
                            if(p!=null) {
                                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 100, 100);
                            }
                            if (current == 0) {
                                if(playerStorage!=null) {
                                    playerStorage.set("player.time." + Main.getDate(), true);
                                } else {
                                    new Storage("plugins/MasterBase/players/" + u + ".yml").set("player.time." + Main.getDate(), true);
                                }
                                if(p!=null) {
                                    //p.sendMessage("§8[§9PvP Masters§8] §7Deine reguläre Spielzeit ist abgelaufen§8. §fAber es ist §aFinale§8, §7also rein da§8!");
                                    p.kickPlayer("§cDeine heutige Spielzeit ist abgelaufen§8!");
                                }
                            } else if (current == 1) {
                                if(p!=null) {
                                    p.sendMessage("§cAchtung§8: §7Deine Spielzeit endet in §e" + current + "§e Sekunde§8.");
                                }
                            } else {
                                if(p!=null) {
                                    p.sendMessage("§cAchtung§8: §7Deine Spielzeit endet in §e" + current + "§e Sekunden§8.");
                                }
                            }

                        }
                    }
                });
                if(Main.isStarted) {
                    if(playerStorage.get("player.died")==null) {
                        Main.countdowns.get(uuid).start();
                    }
                }
            }
            if(team!=null) {
                if(!team.getTeam().getPlayers().contains(player)) {
                    team.getTeam().addPlayer(player);
                }
                player.setPlayerListName(team.getPrefix()+player.getName()+team.getSuffix());
            }
        }
        logoutReason = "logout";
        if(playerStorage.get("player.died")==null) {
            Bukkit.broadcastMessage("§8[§a+§8] §e" + offlinePlayer.getName());
        }
    }

    public void setHit(int hit) {
        this.hit = hit;
    }

    public int getHit() {
        return hit;
    }

    public void setLogoutReason(String logoutReason) {
        this.logoutReason = logoutReason;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public String getLogoutReason() {
        return logoutReason;
    }

    public UUID getUUID() {
        return uuid;
    }

    public Storage getPlayerStorage() {
        return playerStorage;
    }

    public Player getPlayer() {
        return player;
    }

    public OfflinePlayer getOfflinePlayer() {
        return offlinePlayer;
    }

    public void sendMessage(String message) {
        if(player!=null) {
            player.sendMessage(message);
        }
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
        playerStorage.set("player.team",team.getTeam().getName());
    }

    public void logout() {
        Main.onlineUsers.remove(uuid);
        if (!logoutReason.equals("death")) {
            if(playerStorage.get("player.died")==null) {
                Bukkit.broadcastMessage("§8[§c-§8] §e" + offlinePlayer.getName());
            }
        }
        playerStorage.set("player.logout." + Main.getDate() + "." + Main.getTime(true), logoutReason);
        playerStorage = null;
        player = null;
        offlinePlayer = null;
        logoutReason = null;
        team = null;
        uuid = null;
        System.gc();
    }
}