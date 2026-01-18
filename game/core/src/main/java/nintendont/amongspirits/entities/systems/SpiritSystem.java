package nintendont.amongspirits.entities.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import nintendont.amongspirits.entities.components.AnimationComponent;
import nintendont.amongspirits.entities.components.RigidbodyComponent;
import nintendont.amongspirits.entities.components.SpiritComponent;
import nintendont.amongspirits.entities.components.SpiritTagComponent;
import nintendont.amongspirits.entities.components.TransformComponent;

public class SpiritSystem extends IteratingSystem {
    private final int STATUS_MOVING = 1;
    private final int STATUS_IDLE = 0;

    private final ComponentMapper<TransformComponent> transformMapper = ComponentMapper.getFor(TransformComponent.class);
    private final ComponentMapper<SpiritComponent> spiritMapper = ComponentMapper.getFor(SpiritComponent.class);
    private final ComponentMapper<RigidbodyComponent> rigidbodyMapper = ComponentMapper.getFor(RigidbodyComponent.class);
    private final ComponentMapper<AnimationComponent> animationMapper = ComponentMapper.getFor(AnimationComponent.class);
    private final ComponentMapper<SpiritTagComponent> spiritTypeMapper = ComponentMapper.getFor(SpiritTagComponent.class);
    public SpiritSystem() {
        super(Family.all(SpiritComponent.class, RigidbodyComponent.class, AnimationComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float delta) {
        TransformComponent transform = transformMapper.get(entity);
        SpiritComponent spirit = spiritMapper.get(entity);
        RigidbodyComponent rigidbody = rigidbodyMapper.get(entity);
        AnimationComponent animation = animationMapper.get(entity);
        SpiritTagComponent spiritType = spiritTypeMapper.get(entity);

        if(spirit.status == STATUS_MOVING) {
            rigidbody.bulletBody.activate(true);
            Vector3 next = spirit.patrolPoints[spirit.currentTarget];
            Vector3 diff = next.cpy().sub(rigidbody.bulletBody.getWorldTransform().getTranslation(new Vector3()));
            Vector3 direction = diff.cpy().nor();

            Matrix4 transformMatrix = rigidbody.bulletBody.getWorldTransform();
            Vector3 position = new Vector3();
            transformMatrix.getTranslation(position);

            Quaternion targetRotation = new Quaternion();
            Matrix4 lookAtMatrix = new Matrix4();
            lookAtMatrix.setToLookAt(direction.cpy().scl(1, 0, 1), Vector3.Y);
            try {
                lookAtMatrix.inv();
            } catch (RuntimeException e) { }
            lookAtMatrix.rotate(Vector3.Y, 180f);
            lookAtMatrix.getRotation(targetRotation);

            transformMatrix.set(position, targetRotation);

            rigidbody.bulletBody.setWorldTransform(transformMatrix);
            rigidbody.bulletBody.setLinearVelocity(direction.cpy().scl(spirit.speed));

            if (diff.len() < 0.1f) {
                spirit.status = STATUS_IDLE;
                spirit.currentTarget = (spirit.currentTarget + 1) % spirit.patrolPoints.length;
            }
            animation.controller.paused = false;
        }else if(spirit.status == STATUS_IDLE){
            animation.controller.paused = true;
            spirit.stateTime += delta;
            if(spirit.stateTime >= 5) {
                spirit.status = STATUS_MOVING;
                spirit.stateTime = 0;
            }
        }
    }
}
