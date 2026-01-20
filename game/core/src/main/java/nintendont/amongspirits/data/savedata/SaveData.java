package nintendont.amongspirits.data.savedata;

import java.util.ArrayList;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import nintendont.amongspirits.data.satchel.ItemStack;
import nintendont.amongspirits.entities.Player;

public class SaveData implements Json.Serializable{
    public String name;
    public ArrayList<ItemStack> inventory;

    public SaveData() {}

    public SaveData(Player player) {
        this.name = player.getName();
        this.inventory = player.getSatchel().getItems();
    }

    @Override
    public void write(Json json) {
        json.writeValue("name", name);
        json.writeValue("inventory", inventory);

        json.writeArrayStart("items");
        json.writeArrayEnd();
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        this.name = jsonData.getString("name");
        this.inventory = json.readValue("inventory", ArrayList.class, ItemStack.class, jsonData);
    }
}
