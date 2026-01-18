package nintendont.amongspirits.entities.factories;

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

public class SpiritSpawner {
    private final Engine engine;
    private final AssetManager assetManager;

    public SpiritSpawner(Engine engine, AssetManager assetManager) {
        this.engine = engine;
        this.assetManager = assetManager;
    }

    public Entity spawnLion(Vector3 spawnPoint, Vector3[] patrolPoints) {
        SceneAsset modelAsset = assetManager.get("models/lion/scene.gltf", SceneAsset.class);
        return spawnSpirit(modelAsset, null, 2f, spawnPoint, patrolPoints);
    }

    public Entity spawnDeer(Vector3 spawnPoint, Vector3[] patrolPoints) {
        SceneAsset modelAsset = assetManager.get("models/deer/scene.gltf", SceneAsset.class);
        return spawnSpirit(modelAsset, "Armature|walk", 2.5f, spawnPoint, patrolPoints);
    }

    public Entity spawnWolf(Vector3 spawnPoint, Vector3[] patrolPoints) {
        SceneAsset modelAsset = assetManager.get("models/wolf/scene.gltf", SceneAsset.class);
        return spawnSpirit(modelAsset, "Take 001", 2.5f, spawnPoint, patrolPoints);
    }

    public Entity spawnBunny(Vector3 spawnPoint, Vector3[] patrolPoints) {
        SceneAsset modelAsset = assetManager.get("models/bunny/scene.gltf", SceneAsset.class);
        return spawnSpirit(modelAsset, "Take 001", 0.05f, spawnPoint, patrolPoints);
    }

    public Entity spawnFox(Vector3 spawnPoint, Vector3[] patrolPoints) {
        SceneAsset modelAsset = assetManager.get("models/fox/scene.gltf", SceneAsset.class);
        return spawnSpirit(modelAsset, "redfox|red_fox_walk_fwd_01", .75f, spawnPoint, patrolPoints);
    }

    public Entity spawnSpirit(SceneAsset modelAsset, String animationName, float modelScale, Vector3 spawnPoint, Vector3[] patrolPoints) {
        Entity entity = engine.createEntity();

        ModelComponent model = engine.createComponent(ModelComponent.class);
        model.gltfScene = new Scene(modelAsset.scene);

        TransformComponent transform = engine.createComponent(TransformComponent.class);
        model.gltfScene.modelInstance.transform = transform.matrix;
        transform.matrix.setTranslation(spawnPoint.x, spawnPoint.y, spawnPoint.z);
        transform.matrix.scale(modelScale, modelScale, modelScale);

        AnimationComponent animation = engine.createComponent(AnimationComponent.class);
        animation.controller = new AnimationController(model.gltfScene.modelInstance);
        animation.controller.animate(animationName,-1,0.5f,null,0);

        RigidbodyComponent rigidbody = engine.createComponent(RigidbodyComponent.class);

        Matrix4 offset = new Matrix4().translate(Vector3.Y.cpy().scl(2f));

        float mass = 1f;
        Vector3 inertia = new Vector3();
        btCollisionShape collision = new btSphereShape(2f);
        collision.calculateLocalInertia(mass, inertia);
        btCompoundShape baseCollision = new btCompoundShape();
        baseCollision.addChildShape(offset, collision);

        MotionState motionState = new MotionState(transform.matrix);
        btRigidBody.btRigidBodyConstructionInfo info = new btRigidBody.btRigidBodyConstructionInfo(mass, motionState, baseCollision, inertia);
        rigidbody.bulletBody = new btRigidBody(info);
        rigidbody.bulletBody.setAngularFactor(0);
        rigidbody.motionState = motionState;

        SpiritComponent spirit = new SpiritComponent();
        spirit.patrolPoints = patrolPoints;
        spirit.speed = 4f;

        TriggerComponent trigger = engine.createComponent(TriggerComponent.class);
        btCollisionShape triggerShape = new btSphereShape(3f);
        btCompoundShape triggerContainer = new btCompoundShape();
        triggerContainer.addChildShape(offset, triggerShape);
        trigger.group = PhysicsLayers.HITBOX;
        trigger.mask = PhysicsLayers.HITBOX;
        trigger.bulletObject = new btCollisionObject();
        trigger.bulletObject.setCollisionFlags(trigger.bulletObject.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_NO_CONTACT_RESPONSE);
        trigger.bulletObject.setCollisionShape(triggerContainer);
        trigger.bulletObject.setWorldTransform(transform.matrix);
        trigger.bulletObject.userData = entity;

        CatchableComponent catchable = engine.createComponent(CatchableComponent.class);

        entity.add(transform);
        entity.add(model);
        entity.add(animation);
        entity.add(rigidbody);
        entity.add(trigger);
        entity.add(spirit);
        entity.add(catchable);

        engine.addEntity(entity);
        return entity;
    }
}
