package nintendont.amongspirits.engine.listeners;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.gdx.utils.ObjectSet;
import net.mgsx.gltf.scene3d.scene.SceneManager;
import nintendont.amongspirits.engine.components.ModelComponent;

public class SceneEntityListener implements EntityListener {
    private SceneManager sceneManager;
    private final ObjectSet<Entity> entitiesInScene = new ObjectSet<>();
    private final ComponentMapper<ModelComponent> mm = ComponentMapper.getFor(ModelComponent.class);

    public SceneEntityListener(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    @Override
    public void entityAdded(Entity entity) {
        if (!entitiesInScene.contains(entity)) {
            ModelComponent mc = mm.get(entity);
            sceneManager.addScene(mc.getScene());
            entitiesInScene.add(entity);
        }
    }

    @Override
    public void entityRemoved(Entity entity) {
        if (entitiesInScene.contains(entity)){
            ModelComponent mc = mm.get(entity);
            sceneManager.removeScene(mc.getScene());
            entitiesInScene.remove(entity);
        }
    }

}
