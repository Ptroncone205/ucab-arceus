package nintendont.amongspirits.managers;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import nintendont.amongspirits.entities.items.Consumable;
import nintendont.amongspirits.entities.items.Item;
import nintendont.amongspirits.entities.items.MaterialItem;
import nintendont.amongspirits.entities.items.Pokeball;

/**
 * Esta clase se encarga de crear items
 * es tecnicamente un factory :)
 */
public class ItemFactory {
    private static final ArrayList<JsonValue> items = new ArrayList<>(); // lista de datos de todos los items
    private static final HashMap<String, ItemCreator> registry = new HashMap<>(); // mapa de id -> tipo de item
    private static AssetManager assets;

    interface ItemCreator {
        Item create(JsonValue data, AssetManager assets);
    }

    public static void init(AssetManager assets){
        ItemFactory.assets = assets;
        JsonReader json = new JsonReader();
        JsonValue data = json.parse(Gdx.files.internal("data/items.json"));
        for (JsonValue j : data){
            items.add(j);
            System.out.println("size of items"+items.size());
        }
        registry.put("POKEBALL", Pokeball::new);
        registry.put("MATERIAL", MaterialItem::new);
        registry.put("CONSUMABLE", Consumable::new);
    }

    public static Item createItem(Integer id){
        JsonValue data = items.get(id);
        ItemCreator r = registry.get(data.getString("type"));
        return r.create(data, assets);
    }
}
