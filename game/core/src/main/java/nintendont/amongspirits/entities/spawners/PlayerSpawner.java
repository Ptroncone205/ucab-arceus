package nintendont.amongspirits.entities.spawners;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneAsset;
import nintendont.amongspirits.Const;
import nintendont.amongspirits.data.assets.GameAssets;
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

    public Entity spawnPlayer(Vector3 spawnPoint) {
        Entity entity = spawnPlayerBase(spawnPoint);
        TransformComponent transform = entity.getComponent(TransformComponent.class);

        RigidbodyComponent rigidbody = engine.createComponent(RigidbodyComponent.class);

        Vector3 inertia = new Vector3();
        btCollisionShape collision = new btCapsuleShape(1f, 2.05f);
        btCompoundShape collisonBase = new btCompoundShape();
        collisonBase.addChildShape(new Matrix4().translate(Vector3.Y.cpy().scl(2f)), collision);
        collision.calculateLocalInertia(54f, inertia);

        MotionState motionState = new MotionState(transform.matrix);
        btRigidBody.btRigidBodyConstructionInfo info = new btRigidBody.btRigidBodyConstructionInfo(1f, motionState, collisonBase, inertia);
        rigidbody.bulletBody = new btRigidBody(info);
        rigidbody.bulletBody.setAngularFactor(0);
        rigidbody.bulletBody.setUserValue(Const.PF_PLAYER);
        rigidbody.motionState = motionState;

        PlayerTagComponent playerTag = engine.createComponent(PlayerTagComponent.class);

        entity.add(rigidbody);
        entity.add(playerTag);

        return entity;
    }

    public Entity spawnCompanion(int onlineId, Vector3 spawnPoint) {
        Entity entity = spawnPlayerBase(spawnPoint);
        TransformComponent transform = entity.getComponent(TransformComponent.class);

        TriggerComponent trigger = engine.createComponent(TriggerComponent.class);
        btCollisionShape triggerShape = new btSphereShape(3f);
        trigger.group = PhysicsLayers.HITBOX;
        trigger.mask = PhysicsLayers.HITBOX;
        trigger.bulletObject = new btCollisionObject();
        trigger.bulletObject.setCollisionFlags(trigger.bulletObject.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_NO_CONTACT_RESPONSE);
        trigger.bulletObject.setCollisionShape(triggerShape);
        trigger.bulletObject.setWorldTransform(transform.matrix);
        trigger.bulletObject.userData = entity;

        OnlinePlayerTagComponent playerTag = engine.createComponent(OnlinePlayerTagComponent.class);
        playerTag.onlineID = onlineId;

        entity.add(trigger);
        entity.add(playerTag);

        return entity;
    }

    public Entity spawnPlayerBase(Vector3 spawnPoint) {
        Entity entity = engine.createEntity();

        SceneAsset modelAsset = assetManager.get(GameAssets.GUINEVERE_SCENE);

        ModelComponent model = engine.createComponent(ModelComponent.class);
        model.gltfScene = new Scene(modelAsset.scene);

        TransformComponent transform = engine.createComponent(TransformComponent.class);
        model.gltfScene.modelInstance.transform = transform.matrix;
        transform.matrix.setTranslation(spawnPoint.x, spawnPoint.y, spawnPoint.z);
        float modelScale = 250f;
        transform.matrix.scale(modelScale, modelScale, modelScale);

        AnimationComponent animation = engine.createComponent(AnimationComponent.class);
        animation.controller = new AnimationController(model.gltfScene.modelInstance);
        animation.controller.animate("city_idle",-1,0.5f,null,0);

        entity.add(transform);
        entity.add(model);
        entity.add(animation);

        engine.addEntity(entity);
        return entity;
    }
}
