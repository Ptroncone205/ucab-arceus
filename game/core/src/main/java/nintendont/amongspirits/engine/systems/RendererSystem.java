package nintendont.amongspirits.engine.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.utils.ObjectSet;
import net.mgsx.gltf.scene3d.scene.SceneManager;
import nintendont.amongspirits.engine.components.ModelComponent;

public class RendererSystem extends EntitySystem {
    private SceneManager sceneManager;



    public RendererSystem(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    @Override
    public void update(float delta) {
        sceneManager.update(delta);
        sceneManager.render();
    }


}
