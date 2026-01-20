package nintendont.amongspirits.data.savedata;

import java.util.ArrayList;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import nintendont.amongspirits.data.codex.Codex;
import nintendont.amongspirits.data.satchel.ItemStack;
import nintendont.amongspirits.data.spirits.Invocation;
import nintendont.amongspirits.data.spirits.Spirit;
import nintendont.amongspirits.entities.Player;

public class SaveData implements Json.Serializable{
    public String name;
    public ArrayList<ItemStack> inventory;
    public ArrayList<Invocation> team;
    public ArrayList<Invocation> pasture;
    public Codex codex;

    public SaveData() {}

    public SaveData(Player player) {
        this.name = player.getName();
        this.inventory = player.getSatchel().getItems();
        this.team = player.getTeam().getMembers();
        this.pasture = player.getPasture().getInvocations();
        this.codex = player.getCodex();
    }

    @Override
    public void write(Json json) {
        json.writeValue("name", name);
        json.writeValue("inventory", inventory);
        json.writeValue("team", team);
        json.writeValue("pasture", pasture);
        json.writeValue("codex", codex);
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        this.name = jsonData.getString("name");
        this.inventory = json.readValue("inventory", ArrayList.class, ItemStack.class, jsonData);
        this.codex = json.readValue("codex", Codex.class, Codex.class, jsonData);
        this.team = json.readValue("team", ArrayList.class, Invocation.class, jsonData);
        this.pasture = json.readValue("pasture", ArrayList.class, Invocation.class, jsonData);
    }
}
