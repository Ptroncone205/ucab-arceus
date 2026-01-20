package nintendont.amongspirits.entities.spawners;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btCompoundShape;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneAsset;
import nintendont.amongspirits.data.assets.GameAssets;
import nintendont.amongspirits.entities.components.*;
import nintendont.amongspirits.physics.MotionState;
import nintendont.amongspirits.physics.PhysicsLayers;

public class YumenjiangSpawner {
    private final Engine engine;
    private final AssetManager assets;

    public YumenjiangSpawner(Engine engine, AssetManager assets) {
        this.engine = engine;
        this.assets = assets;
    }

    public Entity spawnYumenjiang(Vector3 spawnPoint) {
        Entity entity = engine.createEntity();

        SceneAsset modelAsset = assets.get(GameAssets.YUMENJIANG_SCENE);
        ModelComponent model = engine.createComponent(ModelComponent.class);
        model.gltfScene = new Scene(modelAsset.scene);

        TransformComponent transform = engine.createComponent(TransformComponent.class);
        model.gltfScene.modelInstance.transform = transform.matrix;
        transform.matrix.setTranslation(spawnPoint.x, spawnPoint.y, spawnPoint.z);
        float modelScale = 0.01f;
        transform.matrix.scale(modelScale, modelScale, modelScale);

        RigidbodyComponent rigidbody = engine.createComponent(RigidbodyComponent.class);

        float mass = 1f;
        Vector3 inertia = new Vector3();
        btCollisionShape collision = new btSphereShape(0.25f);
        btCompoundShape collisionContainer = new btCompoundShape();
        Matrix4 collisionOffset = new Matrix4().translate(0, .12f, 0);
        collisionContainer.addChildShape(collisionOffset, collision);
        collisionContainer.calculateLocalInertia(mass, inertia);
        MotionState motionState = new MotionState(transform.matrix);

        btRigidBody.btRigidBodyConstructionInfo info = new btRigidBody.btRigidBodyConstructionInfo(mass, motionState, collisionContainer, inertia);
        rigidbody.bulletBody = new btRigidBody(info);
        rigidbody.bulletBody.setAngularFactor(0.1f);
        rigidbody.motionState = motionState;

        entity.add(transform);
        entity.add(model);
        entity.add(rigidbody);

        engine.addEntity(entity);
        return entity;
    }

    public Entity spawnThrowableYumenjiangToCatch(Vector3 spawnPoint, Vector3 direction, float forceMagnitude) {
        Entity entity = spawnYumenjiang(spawnPoint);

        ThrowableComponent throwable = engine.createComponent(ThrowableComponent.class);
        throwable.direction = direction.cpy();
        throwable.forceMagnitude = forceMagnitude;
        throwable.triggered = false;
        entity.add(throwable);

        TriggerComponent trigger = engine.createComponent(TriggerComponent.class);
        btCollisionShape triggerShape = new btSphereShape(3f);
        trigger.group = PhysicsLayers.HITBOX;
        trigger.mask = PhysicsLayers.HITBOX;
        trigger.bulletObject = new btCollisionObject();
        trigger.bulletObject.setCollisionFlags(trigger.bulletObject.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_NO_CONTACT_RESPONSE);
        trigger.bulletObject.setCollisionShape(triggerShape);
        trigger.bulletObject.userData = entity;
        entity.add(trigger);

        CatcherComponent catcher = engine.createComponent(CatcherComponent.class);
        entity.add(catcher);

        return entity;
    }

    public Entity spawnThrowableYumenjiangToChallenge(Vector3 spawnPoint, Vector3 direction, float forceMagnitude, int teamMemberId) {
        Entity entity = spawnYumenjiang(spawnPoint);

        ThrowableComponent throwable = engine.createComponent(ThrowableComponent.class);
        throwable.direction = direction.cpy();
        throwable.forceMagnitude = forceMagnitude;
        throwable.triggered = false;
        entity.add(throwable);

        TriggerComponent trigger = engine.createComponent(TriggerComponent.class);
        btCollisionShape triggerShape = new btSphereShape(5f);
        trigger.group = PhysicsLayers.HITBOX;
        trigger.mask = PhysicsLayers.HITBOX;
        trigger.bulletObject = new btCollisionObject();
        trigger.bulletObject.setCollisionFlags(trigger.bulletObject.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_NO_CONTACT_RESPONSE);
        trigger.bulletObject.setCollisionShape(triggerShape);
        trigger.bulletObject.userData = entity;
        entity.add(trigger);

        ChallengerComponent challenger = engine.createComponent(ChallengerComponent.class);
        challenger.teamMemberId = teamMemberId;
        entity.add(challenger);

        return entity;
    }
}
