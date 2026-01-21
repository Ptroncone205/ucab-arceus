package nintendont.amongspirits.entities.spawners;

import java.util.Random;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Material;
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
import nintendont.amongspirits.data.assets.GameAssets;
import nintendont.amongspirits.data.codex.Codex;
import nintendont.amongspirits.data.codex.CodexCommons;
import nintendont.amongspirits.data.codex.SpiritForm;
import nintendont.amongspirits.data.spirits.Spirit;
import nintendont.amongspirits.data.spirits.SpiritElement;
import nintendont.amongspirits.data.spirits.SpiritGenders;
import nintendont.amongspirits.entities.components.*;
import nintendont.amongspirits.physics.MotionState;
import nintendont.amongspirits.physics.PhysicsLayers;
import nintendont.amongspirits.shaders.GlowAttribute;
import nintendont.amongspirits.utils.SpiritDataGenerator;

public class SpiritSpawner {
    private final Engine engine;
    private final AssetManager assetManager;
    private final Codex codex;
    private final SpiritDataGenerator dataGenerator = new SpiritDataGenerator();

    public SpiritSpawner(Engine engine, AssetManager assetManager, Codex codex) {
        this.engine = engine;
        this.assetManager = assetManager;
        this.codex = codex;
    }

    public Spirit generateSpirit(SpiritForm form) {
        Spirit spirit = new Spirit(
            dataGenerator.getNextId(),
            dataGenerator.getName(),
            dataGenerator.getLastName(),
            dataGenerator.getBio(),
            dataGenerator.getGender(),
            form);
        return spirit;
    }

    public Entity spawnPhoenix(Vector3 spawnPoint, Vector3[] patrolPoints) {
        SceneAsset modelAsset = assetManager.get(GameAssets.PHOENIX_SCENE);
        Spirit spirit = generateSpirit(codex.getFormById(CodexCommons.PHOENIX_ID));
        return spawnSpirit(spirit, modelAsset, "Take 001", 2f, spawnPoint, patrolPoints, 1f, new Vector3(0, 0, 0));
    }

    public Entity spawnLion(Vector3 spawnPoint, Vector3[] patrolPoints) {
        SceneAsset modelAsset = assetManager.get(GameAssets.LION_SCENE);
        Spirit spirit = generateSpirit(codex.getFormById(CodexCommons.LION_ID));
        return spawnSpirit(spirit, modelAsset, null, 2f, spawnPoint, patrolPoints, 1f, null);
    }

    public Entity spawnDeer(Vector3 spawnPoint, Vector3[] patrolPoints) {
        SceneAsset modelAsset = assetManager.get(GameAssets.DEER_SCENE);
        Spirit spirit = generateSpirit(codex.getFormById(CodexCommons.DEER_ID));
        return spawnSpirit(spirit, modelAsset, "Armature|walk", 2.5f, spawnPoint, patrolPoints, 1f, null);
    }

    public Entity spawnWolf(Vector3 spawnPoint, Vector3[] patrolPoints) {
        SceneAsset modelAsset = assetManager.get(GameAssets.WOLF_SCENE);
        Spirit spirit = generateSpirit(codex.getFormById(CodexCommons.WOLF_ID));
        return spawnSpirit(spirit, modelAsset, "Take 001", 2.5f, spawnPoint, patrolPoints, 1f, null);
    }

    public Entity spawnBunny(Vector3 spawnPoint, Vector3[] patrolPoints) {
        SceneAsset modelAsset = assetManager.get(GameAssets.BUNNY_SCENE);
        Spirit spirit = generateSpirit(codex.getFormById(CodexCommons.BUNNY_ID));
        return spawnSpirit(spirit, modelAsset, "Take 001", 0.05f, spawnPoint, patrolPoints, 1f, null);
    }

    public Entity spawnFox(Vector3 spawnPoint, Vector3[] patrolPoints) {
        SceneAsset modelAsset = assetManager.get(GameAssets.FOX_SCENE);
        Spirit spirit = generateSpirit(codex.getFormById(CodexCommons.FOX_ID));
        return spawnSpirit(spirit, modelAsset, "redfox|red_fox_walk_fwd_01", .75f, spawnPoint, patrolPoints, 1f, null);
    }

    public Entity spawnSpirit(Spirit spiritData, SceneAsset modelAsset, String animationName, float modelScale, Vector3 spawnPoint, Vector3[] patrolPoints, float mass, Vector3 gravity) {
        Entity entity = engine.createEntity();

        SpiritTagComponent spiritType = engine.createComponent(SpiritTagComponent.class);
        spiritType.spirit = spiritData;

        ModelComponent model = engine.createComponent(ModelComponent.class);
        model.gltfScene = new Scene(modelAsset.scene);
        Random rand = new Random();
        GlowAttribute glow = new GlowAttribute(new Color(rand.nextFloat(0.5f,0.8f), rand.nextFloat(0.5f,0.8f), rand.nextFloat(0.5f,0.8f),1), 1f);
        for( Material mat: model.gltfScene.modelInstance.materials){
            mat.set(glow);
        }


        TransformComponent transform = engine.createComponent(TransformComponent.class);
        model.gltfScene.modelInstance.transform = transform.matrix;
        transform.matrix.setTranslation(spawnPoint.x, spawnPoint.y, spawnPoint.z);
        transform.matrix.scale(modelScale, modelScale, modelScale);

        AnimationComponent animation = engine.createComponent(AnimationComponent.class);
        animation.controller = new AnimationController(model.gltfScene.modelInstance);
        animation.controller.animate(animationName,-1,0.5f,null,0);

        RigidbodyComponent rigidbody = engine.createComponent(RigidbodyComponent.class);

        Matrix4 offset = new Matrix4().translate(Vector3.Y.cpy().scl(2f));

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
        if (gravity != null) {
            rigidbody.bulletBody.setGravity(gravity);
        }

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
        entity.add(spiritType);

        engine.addEntity(entity);
        return entity;
    }
}
