package nintendont.amongspirits.entities.systems;

import com.badlogic.ashley.core.EntitySystem;
import net.mgsx.gltf.scene3d.scene.SceneManager;
import nintendont.amongspirits.controllers.PlayerController;
import nintendont.amongspirits.entities.Player;

public class PlayerSystem extends EntitySystem {
    private final Player player;
    private final PlayerController playerController;

    public PlayerSystem(Player player, PlayerController playerController) {
        this.player = player;
        this.playerController = playerController;
    }

    @Override
    public void update(float delta) {
        player.update();
        playerController.update(delta);
    }
}
