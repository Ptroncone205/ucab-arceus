package nintendont.amongspirits.entities.factories;

import com.badlogic.gdx.math.Vector3;
import nintendont.amongspirits.data.codex.Codex;
import nintendont.amongspirits.data.codex.FakeCodexLoader;
import nintendont.amongspirits.data.savedata.SaveData;
import nintendont.amongspirits.entities.Player;
import nintendont.amongspirits.data.satchel.Satchel;
import nintendont.amongspirits.managers.SaveManager;

public class PlayerFactory {
    private final SaveManager saveManager;

    public PlayerFactory(SaveManager saveManager) {
        this.saveManager = saveManager;
    }

    public Player createPlayer(String playerName) {
        Satchel inventory = new Satchel();
        Codex codex = new FakeCodexLoader().load();
        return new Player(playerName, new Vector3(0,15,0), inventory, codex);
    }

    public Player loadPlayerFromSaveData(String playerName) {
        try {
            SaveData data = saveManager.loadGame(playerName);
            Satchel inventory = new Satchel();
            inventory.setItems(data.inventory);
            Codex codex = new FakeCodexLoader().load();
            Player player = new Player(data.name, new Vector3(0,15,0), inventory, codex);
            return player;
        } catch(Exception e) {
            System.err.println("Error cargando datos: " + e.getLocalizedMessage());
            return createPlayer(playerName);
        }
    }
}
