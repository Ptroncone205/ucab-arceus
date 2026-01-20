package nintendont.amongspirits.data.online.packets;

public class JoinRoomRequestPacket {
    public String type;
    public int roomId;
    public String name;
    public int activeInvocationIndex;
    public TeamInvocationPacket[]  team;

    public JoinRoomRequestPacket() {
    }

    public JoinRoomRequestPacket(String type, int roomId, String name, int activeInvocationIndex, TeamInvocationPacket[] team) {
        this.type = type;
        this.roomId = roomId;
        this.name = name;
        this.activeInvocationIndex = activeInvocationIndex;
        this.team = team;
    }
}
