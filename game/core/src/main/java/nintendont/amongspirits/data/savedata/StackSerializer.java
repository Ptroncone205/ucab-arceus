package nintendont.amongspirits.data.savedata;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import nintendont.amongspirits.entities.ItemStack;
import nintendont.amongspirits.entities.items.Item;
import nintendont.amongspirits.managers.ItemFactory;

public class StackSerializer implements Json.Serializer<ItemStack>{

    @Override
    public void write(Json json, ItemStack stack, Class knownType) {
        json.writeObjectStart();

        json.writeValue("id", stack.getItem().getID());
        json.writeValue("count", stack.getCount());

        json.writeObjectEnd();
    }

    @Override
    public ItemStack read(Json json, JsonValue jsonData, Class type) {
        Integer id = jsonData.getInt("id");
        int count = jsonData.getInt("count");

        Item item = ItemFactory.createItem(id);

        
        return new ItemStack(item, count);
    }
    
}
