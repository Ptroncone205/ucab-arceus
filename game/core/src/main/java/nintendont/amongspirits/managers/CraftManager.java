package nintendont.amongspirits.managers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import nintendont.amongspirits.data.satchel.Item;
import nintendont.amongspirits.data.satchel.ItemDB;

public final class CraftManager {
    private final HashMap<String, Integer> recipes = new HashMap<>();
    private final ItemDB items;

    public CraftManager(ItemDB items){
        this.items = items;
        this.load();
    }

    public Item craft(Item itemA, Item itemB){
        String key = generateKey(itemA.getName(), itemB.getName());
        Integer itemId = recipes.get(key);
        if (itemId == null) return null;
        return items.getItemById(itemId);
    }

    public void load(){
        Json json = new Json();
        ArrayList<Recipe> dataR = json.fromJson(ArrayList.class, Recipe.class, Gdx.files.internal("data/recipes.json"));
        for (Recipe recipe : dataR){
            String key = generateKey(recipe.itemA, recipe.itemB);

            recipes.put(key, recipe.output);
        }
    }

    private String generateKey(String a, String b){
        String[] inputs = { a, b };
        Arrays.sort(inputs);

        return inputs[0] + "_" + inputs[1];
    }

    public static class Recipe{
        public String itemA;
        public String itemB;
        public Integer output;
    }
}
