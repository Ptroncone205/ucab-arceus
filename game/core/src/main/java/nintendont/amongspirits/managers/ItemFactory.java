package nintendont.amongspirits.managers;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import nintendont.amongspirits.entities.items.Item;
import nintendont.amongspirits.entities.items.Pokeball;

/**
 * Esta clase se encarga de crear items
 * es tecnicamente un factory :)
 */
public class ItemFactory {
    private static ArrayList<JsonValue> items = new ArrayList<>(); // lista de datos de todos los items
    private static final HashMap<String, ItemCreator> registry = new HashMap<>(); // mapa de id -> tipo de item

    interface ItemCreator {
        Item create(JsonValue data);
    }

    public static void init(){
        JsonReader json = new JsonReader();
        JsonValue data = json.parse(Gdx.files.internal("data/items.json"));
        for (JsonValue j : data){
            items.add(j);
        }
        registry.put("POKEBALL", Pokeball::new);
    }

    public static Item createItem(String id){
        JsonValue data = items.get(Integer.parseInt(id));
        ItemCreator r = registry.get(data.getString("type"));
        return r.create(data);
    }
}
