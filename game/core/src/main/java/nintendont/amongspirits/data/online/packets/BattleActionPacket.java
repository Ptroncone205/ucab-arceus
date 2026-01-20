package nintendont.amongspirits.data.online.packets;

public class BattleActionPacket {
    public int targetPlayerId;
    public Integer damage;
    public Integer heal;
    public Integer changeInvocationIndex;

    public BattleActionPacket() {
    }

    public BattleActionPacket(int targetPlayerId, Integer damage, Integer heal, Integer changeInvocationIndex) {
        this.targetPlayerId = targetPlayerId;
        this.damage = damage;
        this.heal = heal;
        this.changeInvocationIndex = changeInvocationIndex;
    }
}
