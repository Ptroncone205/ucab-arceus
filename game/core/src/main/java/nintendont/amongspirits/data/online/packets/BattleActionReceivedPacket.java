package nintendont.amongspirits.data.online.packets;

public class BattleActionReceivedPacket {
    public int fromPlayerId;
    public BattleActionPacket action;

    public BattleActionReceivedPacket() {
    }

    public BattleActionReceivedPacket(int fromPlayerId, BattleActionPacket action) {
        this.fromPlayerId = fromPlayerId;
        this.action = action;
    }
}
