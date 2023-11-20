package live.nerotv.masterbase;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import live.nerotv.masterbase.commands.MasterBorderCommand;
import live.nerotv.masterbase.commands.StartCommand;
import live.nerotv.masterbase.listeners.*;
import live.nerotv.masterbase.objects.Team;
import live.nerotv.masterbase.objects.User;
import live.nerotv.masterbase.utils.Countdown;
import live.nerotv.masterbase.utils.MasterTimer;
import live.nerotv.masterbase.utils.storage.types.Config;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Main extends JavaPlugin {

    public static HashMap<UUID, User> onlineUsers;
    public static ArrayList<String> users;
    public static HashMap<UUID, Countdown> countdowns;
    public static HashMap<String, Team> teams;
    public static Config config = new Config("plugins/MasterBase/config.yml");
    private static Main instance;
    public static boolean isStarted;
    public static boolean isProtected = false;
    public static Scoreboard scoreboard;

    @Override
    public void onLoad() {
        System.out.println("Auch wenn Paper dir hier sagt, du sollst mich nerven, damit ich NICHT \"System.out.println()\" nutzen soll- ignorier es. Ich machs trotzdem.");
    }

    @Override
    public void onEnable() {
        MasterTimer.world = Bukkit.getWorlds().get(0);
        MasterTimer.nether = Bukkit.getWorlds().get(1);
        config.checkEntry("config.started",false);
        config.checkEntry("config.teams",new ArrayList<>());
        config.checkEntry("config.members",new ArrayList<>());
        users = (ArrayList<String>)config.getCFG().getList("config.members");
        isStarted = config.getCFG().getBoolean("config.started");
        instance = this;
        onlineUsers = new HashMap<>();
        teams = new HashMap<>();
        countdowns = new HashMap<>();
        MasterTimer.send();
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(),this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(),this);
        Bukkit.getPluginManager().registerEvents(new PlayerDamageListener(),this);
        Bukkit.getPluginManager().registerEvents(new PlayerDeathListener(),this);
        Bukkit.getPluginManager().registerEvents(new PlayerChatListener(),this);
        getCommand("masterborder").setExecutor(new MasterBorderCommand());
        getCommand("start").setExecutor(new StartCommand());
        Bukkit.getWorlds().get(0).setSpawnLocation(new Location(Bukkit.getWorlds().get(0),0.5,97,0.5));
        Bukkit.getWorlds().get(1).setSpawnLocation(new Location(Bukkit.getWorlds().get(1),0.5,97,0.5));
        scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        ArrayList<String> teamNames = (ArrayList<String>)config.get("config.teams");
        for(String name:teamNames) {
            if(scoreboard.getTeam(name)==null) {
                scoreboard.registerNewTeam(name);
            }
            teams.put(name,new Team(scoreboard.getTeam(name)));
        }
        System.out.println("=======================================================");
        System.out.println(getDescription().getName()+" by "+getDescription().getAuthors().get(0)+" version: "+getDescription().getVersion());
        System.out.println("Es ist Tag "+MasterTimer.days);
        System.out.println("=======================================================");
    }

    public static void checkMembers(Player player) {
        for (String name : listFilesUsingJavaIO("plugins/MasterBase/players/")) {
            player.sendMessage(" ");
            String sUUID = name.replace(".yml", "");
            UUID uuid;
            try {
                uuid = UUID.fromString(sUUID);
            } catch (IllegalArgumentException e) {
                uuid = null;
            }
            Config userFile = new Config("plugins/MasterBase/players/" + name);
            YamlConfiguration yaml = userFile.getCFG();
            player.sendMessage("UUID: " + uuid);
            String playerName;
            if(uuid==null) {
                playerName = sUUID;
            } else {
                String json = readUrl("https://sessionserver.mojang.com/session/minecraft/profile/"+sUUID);
                JsonObject ob = new Gson().fromJson(json, JsonObject.class);
                playerName = ob.get("name").getAsString();
            }
            player.sendMessage("Name: "+playerName);
            Team team = teams.get(yaml.getString("player.team"));
            player.sendMessage("Team: "+team.getName());
            player.sendMessage("Team-ID: "+team.getTeam().getName());
            player.sendMessage("Team-Prefix: "+team.getPrefix());
            player.sendMessage("Team-Suffix: "+team.getSuffix());
            player.sendMessage("Chat-Format: "+"§f"+team.getPrefix()+playerName+team.getSuffix()+"§8)");
            player.sendMessage("Tablist-Format: "+"§f"+team.getPrefix()+playerName+team.getSuffix());
            player.sendMessage("=========================================================");
        }
    }

    private static Set<String> listFilesUsingJavaIO(String dir) {
        return Stream.of(new File(dir).listFiles())
                .filter(file -> !file.isDirectory())
                .map(File::getName)
                .collect(Collectors.toSet());
    }

    @Override
    public void onDisable() {
        config.set("config.members",users);
        onlineUsers = null;
        instance = null;
    }

    private static String readUrl(String urlString) {
        try {
            BufferedReader reader = null;
            try {
                URL url = new URL(urlString);
                reader = new BufferedReader(new InputStreamReader(url.openStream()));
                StringBuffer buffer = new StringBuffer();
                int read;
                char[] chars = new char[1024];
                while ((read = reader.read(chars)) != -1)
                    buffer.append(chars, 0, read);

                return buffer.toString();
            } finally {
                if (reader != null)
                    reader.close();
            }
        } catch (Exception e) {
            return null;
        }
    }

    public static String getDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    public static String getTime(boolean seconds) {
        DateTimeFormatter dtf;
        if(seconds) {
            dtf = DateTimeFormatter.ofPattern("HH-mm-ss");
        } else {
            dtf = DateTimeFormatter.ofPattern("HH-mm");
        }
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    public static User getUser(UUID uuid) {
        if(!onlineUsers.containsKey(uuid)) {
            onlineUsers.put(uuid,new User(uuid));
        }
        return onlineUsers.get(uuid);
    }

    public static Main getInstance() {
        return instance;
    }
}
