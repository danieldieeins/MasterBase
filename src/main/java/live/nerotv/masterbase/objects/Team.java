package live.nerotv.masterbase.objects;

import live.nerotv.masterbase.utils.storage.types.Config;

import java.io.IOException;

public class Team {

    private final org.bukkit.scoreboard.Team team;
    private String name;
    private String prefix;
    private String suffix;
    private final Config saver;

    public Team(org.bukkit.scoreboard.Team team) {
        this.team = team;
        saver = new Config("plugins/MasterBase/teams/"+team.getName()+".yml");
        saver.checkEntry("team.name",team.getName());
        saver.checkEntry("team.prefix",team.getPrefix());
        saver.checkEntry("team.suffix",team.getSuffix());
        name = saver.getCFG().getString("team.name");
        prefix = saver.getCFG().getString("team.prefix");
        team.setPrefix(prefix);
        suffix = saver.getCFG().getString("team.suffix");
        team.setSuffix(suffix);
        try {
            saver.getCFG().save(saver.getFile());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public String getName() {
        return name;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
        saver.getCFG().set("team.prefix",prefix);
        try {
            saver.getCFG().save(saver.getFile());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
        saver.getCFG().set("team.suffix",suffix);
        try {
            saver.getCFG().save(saver.getFile());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public org.bukkit.scoreboard.Team getTeam() {
        return team;
    }
}