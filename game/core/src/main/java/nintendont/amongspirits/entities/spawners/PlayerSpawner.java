package nintendont.amongspirits.entities.spawners;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btCompoundShape;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneAsset;
import nintendont.amongspirits.entities.components.*;
import nintendont.amongspirits.physics.MotionState;
import nintendont.amongspirits.physics.PhysicsLayers;

public class PlayerSpawner {
    private final Engine engine;
    private final AssetManager assetManager;

    public PlayerSpawner(Engine engine, AssetManager assetManager) {
        this.engine = engine;
        this.assetManager = assetManager;
    }

    public Entity spawnCompanion(int onlineId, Vector3 spawnPoint) {
        Entity entity = engine.createEntity();

        SceneAsset modelAsset = assetManager.get("models/mc/lukitm501.gltf", SceneAsset.class);

        ModelComponent model = engine.createComponent(ModelComponent.class);
        model.gltfScene = new Scene(modelAsset.scene);

        TransformComponent transform = engine.createComponent(TransformComponent.class);
        model.gltfScene.modelInstance.transform = transform.matrix;
        transform.matrix.setTranslation(spawnPoint.x, spawnPoint.y, spawnPoint.z);
        float modelScale = 1f;
        transform.matrix.scale(modelScale, modelScale, modelScale);

        PlayerTagComponent playerTag = engine.createComponent(PlayerTagComponent.class);
        playerTag.onlineID = onlineId;

        entity.add(transform);
        entity.add(model);
        entity.add(playerTag);

        engine.addEntity(entity);
        return entity;
    }
}
