package nintendont.amongspirits.entities.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.ContactResultCallback;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObjectWrapper;
import com.badlogic.gdx.physics.bullet.collision.btManifoldPoint;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import nintendont.amongspirits.entities.components.CatchableComponent;
import nintendont.amongspirits.entities.components.TransformComponent;
import nintendont.amongspirits.entities.components.TriggerComponent;

public class TriggerTransformSystem extends IteratingSystem {
    private final ComponentMapper<TransformComponent> transformMapper = ComponentMapper.getFor(TransformComponent.class);
    private final ComponentMapper<TriggerComponent> triggerMapper = ComponentMapper.getFor(TriggerComponent.class);

    public TriggerTransformSystem() {
        super(Family.all(TransformComponent.class, TriggerComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float v) {
        TransformComponent transform = transformMapper.get(entity);
        TriggerComponent trigger = triggerMapper.get(entity);
        Vector3 targetPosition = new Vector3();
        transform.matrix.getTranslation(targetPosition);
        Matrix4 targetMatrix = new Matrix4();
        targetMatrix.translate(targetPosition);

        trigger.bulletObject.setWorldTransform(targetMatrix);
    }
}
