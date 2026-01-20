package nintendont.amongspirits.data.online.packets;

public class BattleActionRequestPacket {
    private String type;
    private int roomId;
    private int targetPlayerId;
    private BattleActionPacket action;

    public BattleActionRequestPacket() {
    }

    public BattleActionRequestPacket(String type, int roomId, int targetPlayerId, BattleActionPacket action) {
        this.type = type;
        this.roomId = roomId;
        this.targetPlayerId = targetPlayerId;
        this.action = action;
    }
}
