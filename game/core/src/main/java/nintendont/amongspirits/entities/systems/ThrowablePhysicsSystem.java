package nintendont.amongspirits.entities.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector3;
import nintendont.amongspirits.entities.components.RigidbodyComponent;
import nintendont.amongspirits.entities.components.ThrowableComponent;

public class ThrowablePhysicsSystem extends IteratingSystem {
    private final ComponentMapper<ThrowableComponent> throwableMapper = ComponentMapper.getFor(ThrowableComponent.class);
    private final ComponentMapper<RigidbodyComponent> rigidbodyMapper = ComponentMapper.getFor(RigidbodyComponent.class);

    public ThrowablePhysicsSystem() {
        super(Family.all(ThrowableComponent.class, RigidbodyComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        ThrowableComponent throwable = throwableMapper.get(entity);
        RigidbodyComponent rigidbody = rigidbodyMapper.get(entity);

        if (!throwable.triggered) {
            Vector3 force = throwable.direction.cpy().scl(throwable.forceMagnitude);
            rigidbody.bulletBody.applyCentralImpulse(force);
            throwable.triggered = true;
        }
    }
}
