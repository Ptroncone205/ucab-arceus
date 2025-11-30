package nintendont.amongspirits.engine.components;
import com.badlogic.ashley.core.Component;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneAsset;

public class ModelComponent implements Component {
    private Scene scene;
    private SceneAsset sceneAsset;

    public Scene getScene() {
        return scene;
    }
}
