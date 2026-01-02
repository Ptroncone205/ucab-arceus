package nintendont.amongspirits.data.savedata;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import nintendont.amongspirits.entities.ItemStack;
import nintendont.amongspirits.entities.Player;
import nintendont.amongspirits.entities.Pokemon;
import nintendont.amongspirits.entities.items.Item;
import nintendont.amongspirits.managers.ItemFactory;

public class SaveData implements Json.Serializable{
    public String name;                    // player name
    // static ArrayList<Pokemon> party;       // pokemon party
    public ArrayList<ItemStack> inventory; // inventory
    public ArrayList<Item> items;          // items in world

    public SaveData(){}

    public SaveData(Player player, ArrayList<Item> items){
    this.name = player.getName();
    this.inventory = player.getSatchel().getItems();
    this.items = items;
    }

    @Override
    public void write(Json json) {
        json.writeValue("name", name);
        json.writeValue("inventory", inventory);
        
        json.writeArrayStart("items");
        for (Item item : items){
            json.writeObjectStart();
            json.writeValue("id", item.getID());
            json.writeValue("x", item.pos.x);
            json.writeValue("y", item.pos.y);
            json.writeValue("z", item.pos.z);
            json.writeObjectEnd();
        }
        json.writeArrayEnd();
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        this.name = jsonData.getString("name");
        this.inventory = json.readValue("inventory", ArrayList.class, ItemStack.class, jsonData);
        this.items = new ArrayList<>();
        JsonValue itemList = jsonData.get("items");

        for (JsonValue data : itemList){
            Integer id = data.getInt("id");
            float x = data.getFloat("x");
            float y = data.getFloat("y");
            float z = data.getFloat("z");
            Item item = ItemFactory.createItem(id);
            item.pos = new Vector3(x,y,z);
            items.add(item);
        }
    }
    
}
