package nintendont.amongspirits.entities.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.math.Vector3;
import net.mgsx.gltf.scene3d.lights.DirectionalLightEx;
import net.mgsx.gltf.scene3d.scene.SceneManager;
import nintendont.amongspirits.entities.Player;
import nintendont.amongspirits.entities.spawners.SpiritSpawner;
import nintendont.amongspirits.ui.game.GUIManager;

public class EndgameSystem extends EntitySystem {
    private final Player player;
    private final SpiritSpawner spiritSpawner;
    private final GUIManager guiManager;
    private final SceneManager sceneManager;
    private boolean phoenixSpawned;

    public EndgameSystem(Player player, SpiritSpawner spiritSpawner, GUIManager guiManager, SceneManager sceneManager) {
        this.player = player;
        this.spiritSpawner = spiritSpawner;
        this.guiManager = guiManager;
        this.sceneManager = sceneManager;
    }

    @Override
    public void update(float delta) {
        if (player.getCodex().isComplete()) {
            if (!player.getCodex().isPhoenixDefeated()) {
                sceneManager.environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 1, 0, 0, 1));
            }

            if (!phoenixSpawned) {
                spiritSpawner.spawnPhoenix(new Vector3(68.99219f,4.0298457f,7.298912f), new Vector3[]{
                    new Vector3(68.99219f,4.0298457f,7.298912f),
                    new Vector3(86.905846f,-8.5201435f+20,-50.465534f),
                    new Vector3(54.958504f,-4.9462724f,-108.37495f),
                    new Vector3(-46.333305f,-6.3786345f+20,-99.84783f),
                    new Vector3(-85.02251f,5.214801f,-80.039276f),
                    new Vector3(-127.1247f,-3.9191184f,-36.69553f),
                    new Vector3(-83.34349f,-9.322952f+20,45.375275f)
                });
                phoenixSpawned = true;
            }
        } else {
            player.getCodex().calcComplete();
        }

        // For debugging reasons
        if (Gdx.input.isKeyPressed(Input.Keys.F8)) {
            player.getCodex().setComplete(true);
            guiManager.update();
        }
    }
}
