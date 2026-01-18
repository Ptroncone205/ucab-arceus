package nintendont.amongspirits.entities;

import nintendont.amongspirits.data.spirits.Team;

public class Enemy {
    private final String name;
    private Team team;

    public Enemy(String name, Team team) {
        this.name = name;
        this.team = team;
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
}
