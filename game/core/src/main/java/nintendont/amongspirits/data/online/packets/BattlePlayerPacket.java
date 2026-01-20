package nintendont.amongspirits.data.online.packets;

public class BattlePlayerPacket {
    public String name;
    public int activeInvocationIndex;
    public TeamInvocationPacket[]  team;

    public BattlePlayerPacket() {
    }

    public BattlePlayerPacket(String name, int activeInvocationIndex, TeamInvocationPacket[] team) {
        this.name = name;
        this.activeInvocationIndex = activeInvocationIndex;
        this.team = team;
    }
}
