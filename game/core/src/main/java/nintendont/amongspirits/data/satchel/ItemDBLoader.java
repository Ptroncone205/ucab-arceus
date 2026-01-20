package nintendont.amongspirits.data.satchel;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import nintendont.amongspirits.data.satchel.effects.ConsumableEffect;
import nintendont.amongspirits.data.satchel.effects.HealEffect;

import java.util.ArrayList;
import java.util.List;

public class ItemDBLoader {
    private final JsonReader jsonReader = new JsonReader();

    public ItemDB load() {
        FileHandle jsonFile = Gdx.files.internal("data/items.json");
        JsonValue root = jsonReader.parse(jsonFile);

        ArrayList<Item> items = new ArrayList<Item>();
        for (JsonValue itemJson : root) {
            Item item = createItem(itemJson);
            if (item != null) {
                items.add(item);
            } else {
                Gdx.app.log("ItemLoader", "Unknown item type: " + itemJson.getString("type"));
            }
        }

        return new ItemDB(items);
    }

    private Item createItem(JsonValue itemJson) {
        String type = itemJson.getString("type");
        switch (type) {
            case "REGULAR":
                return new Item(
                    itemJson.getInt("id"),
                    itemJson.getString("name"),
                    itemJson.getString("desc"),
                    itemJson.getBoolean("isMaterial"),
                    new AssetDescriptor<>(itemJson.getString("icon"), Texture.class));
            case "THROWABLE":
                return new ThrowableItem(
                    itemJson.getInt("id"),
                    itemJson.getString("name"),
                    itemJson.getString("desc"),
                    itemJson.getBoolean("isMaterial"),
                    new AssetDescriptor<>(itemJson.getString("icon"), Texture.class));
            case "CONSUMABLE":
                return new ConsumableItem(
                    itemJson.getInt("id"),
                    itemJson.getString("name"),
                    itemJson.getString("desc"),
                    itemJson.getBoolean("isMaterial"),
                    new AssetDescriptor<>(itemJson.getString("icon"), Texture.class),
                    createConsumableEffect(itemJson.get("effect")));
            default:
                return null;
        }
    }

    private ConsumableEffect createConsumableEffect(JsonValue effectJson) {
        String type = effectJson.getString("type");
        switch (type) {
            case "HEAL":
                return new HealEffect(effectJson.getInt("value"));
            default:
                return null;
        }
    }
}
