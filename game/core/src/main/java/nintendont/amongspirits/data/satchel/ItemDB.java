package nintendont.amongspirits.data.satchel;

import java.util.List;

public class ItemDB {
    public static final int TUMBLESTONE_ID = 0;
    public static final int YUMENJIANG_ID = 1;
    public static final int ORAN_BERRY_ID = 2;

    private List<Item> items;

    public ItemDB(List<Item> items) {
        this.items = items;
    }

    public List<Item> getItems() {
        return items;
    }

    public Item getItemById(int id) {
        for (Item item : items) {
            if (item.getId() == id)
                return item;
        }
        return null;
    }
}
