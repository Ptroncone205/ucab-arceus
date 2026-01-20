package nintendont.amongspirits.data.online.packets;

public class ItemCollectedPacket {
    public String type;
    public int itemId;
    public int spawnIndex;

    public ItemCollectedPacket() {
    }

    public ItemCollectedPacket(String type, int itemId, int spawnIndex) {
        this.type = type;
        this.itemId = itemId;
        this.spawnIndex = spawnIndex;
    }
}
