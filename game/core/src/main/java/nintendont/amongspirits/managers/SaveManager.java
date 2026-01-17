package nintendont.amongspirits.managers;

import java.util.ArrayList;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

import nintendont.amongspirits.data.savedata.SaveData;
import nintendont.amongspirits.data.savedata.StackSerializer;
import nintendont.amongspirits.entities.ItemStack;
import nintendont.amongspirits.entities.Player;
import nintendont.amongspirits.entities.items.Item;

public class SaveManager {
    public static FileHandle file = Gdx.files.local("data/saves/save.json");
    
    public static void saveGame(Player player, ArrayList<Item> items){
        SaveData data = new SaveData(player, items);

        file = Gdx.files.local(String.format("data/saves/save_%s.json", player.getName()));

        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        json.setUsePrototypes(false);

        json.setSerializer(ItemStack.class, new StackSerializer());

        String jsonString = json.prettyPrint(data);

        
        try{
            file.writeString(jsonString, false);
            System.out.println("saved to: " + file.file().getAbsolutePath());
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public static SaveData loadGame(String playerName){
        Json json = new Json();
        json.setUsePrototypes(false);

        json.setSerializer(ItemStack.class, new StackSerializer());
        
        try{
            file = Gdx.files.local(String.format("data/saves/save_%s.json", playerName));
            SaveData data = json.fromJson(SaveData.class, file);
            System.out.println("loaded from: " + file.file().getAbsolutePath());
            return data;
        } catch (Exception e){
            e.printStackTrace();
        } return null;
    }
}
