package nintendont.amongspirits.data.online.packets;

public class PlayerChallengeRequestPacket {
    public String type;
    public int targetPlayerId;
    public BattlePlayerPacket challenger;

    public PlayerChallengeRequestPacket() {
    }

    public PlayerChallengeRequestPacket(String type, int targetPlayerId, BattlePlayerPacket challenger) {
        this.type = type;
        this.targetPlayerId = targetPlayerId;
        this.challenger = challenger;
    }
}
