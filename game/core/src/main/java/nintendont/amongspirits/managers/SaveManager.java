package nintendont.amongspirits.managers;

import java.util.ArrayList;
import java.util.logging.FileHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonWriter;

import nintendont.amongspirits.data.savedata.ItemSerializer;
import nintendont.amongspirits.data.savedata.SaveData;
import nintendont.amongspirits.data.savedata.StackSerializer;
import nintendont.amongspirits.entities.ItemStack;
import nintendont.amongspirits.entities.Player;
import nintendont.amongspirits.entities.items.Consumable;
import nintendont.amongspirits.entities.items.Item;
import nintendont.amongspirits.entities.items.MaterialItem;
import nintendont.amongspirits.entities.items.Pokeball;

public class SaveManager {
    public static FileHandle file = Gdx.files.local("data/saves/save.json");
    
    public static void saveGame(Player player, ArrayList<Item> items){
        SaveData data = new SaveData(player, items);

        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        json.setUsePrototypes(false);

        json.setSerializer(ItemStack.class, new StackSerializer());

        String jsonString = json.prettyPrint(data);

        
        try{
            file.writeString(jsonString, false);
            System.out.println("saved succesfully to: " + file.file().getAbsolutePath());
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public static SaveData loadGame(){
        Json json = new Json();
        json.setUsePrototypes(false);

        json.setSerializer(ItemStack.class, new StackSerializer());
        
        try{
            SaveData data = json.fromJson(SaveData.class, file);
            System.out.println("loaded succesfully");
            return data;
        } catch (Exception e){
            e.printStackTrace();
        } return null;
    }
}
