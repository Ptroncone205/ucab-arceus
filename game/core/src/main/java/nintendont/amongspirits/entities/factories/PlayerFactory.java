package nintendont.amongspirits.entities.factories;

import com.badlogic.gdx.math.Vector3;
import nintendont.amongspirits.data.codex.Codex;
import nintendont.amongspirits.data.codex.FakeCodexLoader;
import nintendont.amongspirits.data.savedata.SaveData;
import nintendont.amongspirits.data.spirits.Pasture;
import nintendont.amongspirits.data.spirits.Team;
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
            Codex codex = new FakeCodexLoader().load();
            SaveData data = saveManager.loadGame(playerName, codex);
            if (data.codex != null) codex = data.codex;
            Satchel inventory = new Satchel();
            inventory.setItems(data.inventory);
            Team team = new Team(data.team);
            Pasture pasture = new Pasture(data.pasture);

            Player player = new Player(data.name, new Vector3(0,15,0), inventory, codex);
            player.setTeam(team);
            player.setPasture(pasture);
            return player;
        } catch(Exception e) {
            System.err.println("Error cargando datos: " + e.getLocalizedMessage());
            return createPlayer(playerName);
        }
    }
}
