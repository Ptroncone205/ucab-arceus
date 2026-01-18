package nintendont.amongspirits.entities.systems;

import com.badlogic.ashley.core.EntitySystem;
import net.mgsx.gltf.scene3d.scene.SceneManager;
import nintendont.amongspirits.physics.PhysicsWorld;

public class SceneManagerSystem extends EntitySystem {
    private final SceneManager sceneManager;

    public SceneManagerSystem(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    @Override
    public void update(float delta) {
        sceneManager.update(delta);
    }
}
