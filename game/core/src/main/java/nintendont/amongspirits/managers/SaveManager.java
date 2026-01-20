package nintendont.amongspirits.managers;

import java.util.ArrayList;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

import nintendont.amongspirits.data.satchel.ItemDB;
import nintendont.amongspirits.data.savedata.SaveData;
import nintendont.amongspirits.data.savedata.StackSerializer;
import nintendont.amongspirits.data.satchel.ItemStack;
import nintendont.amongspirits.entities.Player;

public class SaveManager {
    public static FileHandle file = Gdx.files.local("data/saves/save.json");
    private final ItemDB items;

    public SaveManager(ItemDB items) {
        this.items = items;
    }

    public void saveGame(Player player){
        SaveData data = new SaveData(player);

        file = Gdx.files.local(String.format("data/saves/save_%s.json", player.getName()));

        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        json.setUsePrototypes(false);

        json.setSerializer(ItemStack.class, new StackSerializer(items));

        String jsonString = json.prettyPrint(data);

        try{
            file.writeString(jsonString, false);
            System.out.println("saved to: " + file.file().getAbsolutePath());
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public SaveData loadGame(String playerName){
        Json json = new Json();
        json.setUsePrototypes(false);

        json.setSerializer(ItemStack.class, new StackSerializer(items));

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
