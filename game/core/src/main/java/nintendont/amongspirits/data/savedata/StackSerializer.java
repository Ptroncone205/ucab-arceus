package nintendont.amongspirits.data.savedata;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import nintendont.amongspirits.data.satchel.Item;
import nintendont.amongspirits.data.satchel.ItemDB;
import nintendont.amongspirits.data.satchel.ItemStack;

public class StackSerializer implements Json.Serializer<ItemStack> {
    private final ItemDB items;

    public StackSerializer(ItemDB items) {
        this.items = items;
    }

    @Override
    public void write(Json json, ItemStack stack, Class knownType) {
        json.writeObjectStart();

        json.writeValue("id", stack.getItem().getId());
        json.writeValue("count", stack.getCount());

        json.writeObjectEnd();
    }

    @Override
    public ItemStack read(Json json, JsonValue jsonData, Class type) {
        Integer id = jsonData.getInt("id");
        int count = jsonData.getInt("count");

        Item item = items.getItemById(id);

        return new ItemStack(item, count);
    }

}
