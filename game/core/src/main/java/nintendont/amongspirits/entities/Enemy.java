package nintendont.amongspirits.entities;

import nintendont.amongspirits.data.spirits.Team;

public class Enemy {
    private final String name;
    private Team team;
    private boolean wild;

    public Enemy(String name, Team team, boolean wild) {
        this.name = name;
        this.team = team;
        this.wild = wild;
    }

    public String getName() {
        return name;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public boolean isWild() {
        return wild;
    }
}
