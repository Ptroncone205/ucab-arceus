package nintendont.amongspirits.managers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;

import nintendont.amongspirits.entities.items.Item;
import nintendont.amongspirits.entities.items.Pokeball;

public final class CraftManager {
    private final HashMap<String, String> items = new HashMap<>();
    private final HashMap<String, String> recipes = new HashMap<>();
    public CraftManager(){
        ItemFactory.init();
        this.load();
    }

    public Item craft(Item itemA, Item itemB){
        String key = generateKey(itemA.name, itemB.name);
        String output = recipes.get(key);
        if (output == null) return null;
        return ItemFactory.createItem(output);
        // if (output.equals("Pokeball")){
        //     return new Pokeball(0, output, "Basic pokeballused to catch pokemon", true);
        // }
        // return null;
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
        public String output;
    }
}
