package nintendont.amongspirits.data.savedata;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import nintendont.amongspirits.entities.items.Item;
import nintendont.amongspirits.managers.ItemFactory;

public class ItemSerializer implements Json.Serializer<Item>{

    @Override
    public void write(Json json, Item item, Class knownType) {
        json.writeObjectStart();

        json.writeValue("id", item.getID());
        json.writeValue("x", item.pos.x);
        json.writeValue("y", item.pos.y);
        json.writeValue("z", item.pos.z);

        json.writeObjectEnd();
    }

    @Override
    public Item read(Json json, JsonValue jsonData, Class type) {
        
        Integer id = jsonData.getInt("id");
        float x = jsonData.getFloat("x");
        float y = jsonData.getFloat("y");
        float z = jsonData.getFloat("z");
        Vector3 pos = new Vector3(x,y,z);

        Item item = ItemFactory.createItem(id);
        item.pos = pos;

        return item;
    }
    
}
