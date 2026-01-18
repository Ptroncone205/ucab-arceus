package nintendont.amongspirits.entities.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import net.mgsx.gltf.scene3d.scene.SceneManager;
import nintendont.amongspirits.entities.components.ModelComponent;

public class SceneModelListener implements EntityListener {
    private final SceneManager sceneManager;
    private final ComponentMapper<ModelComponent> modelMapper = ComponentMapper.getFor(ModelComponent.class);

    public SceneModelListener(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    @Override
    public void entityAdded(Entity entity) {
        ModelComponent model = modelMapper.get(entity);
        if (model != null) {
            sceneManager.addScene(model.gltfScene);
        }
    }

    @Override
    public void entityRemoved(Entity entity) {
        ModelComponent model = modelMapper.get(entity);
        if (model != null) {
            sceneManager.removeScene(model.gltfScene);
        }
    }
}
