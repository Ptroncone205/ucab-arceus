package nintendont.amongspirits.entities.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import nintendont.amongspirits.entities.components.RigidbodyComponent;

public class BulletRigidbodyListener implements EntityListener {
    private final btDiscreteDynamicsWorld dynamicsWorld;
    private final ComponentMapper<RigidbodyComponent> modelMapper = ComponentMapper.getFor(RigidbodyComponent.class);

    public BulletRigidbodyListener(btDiscreteDynamicsWorld dynamicsWorld) {
        this.dynamicsWorld = dynamicsWorld;
    }

    @Override
    public void entityAdded(Entity entity) {
        RigidbodyComponent rigidbody = modelMapper.get(entity);
        if (rigidbody != null) {
            dynamicsWorld.addRigidBody(rigidbody.bulletBody);
        }
    }

    @Override
    public void entityRemoved(Entity entity) {
        RigidbodyComponent rigidbody = modelMapper.get(entity);
        if (rigidbody != null) {
            dynamicsWorld.removeRigidBody(rigidbody.bulletBody);
            rigidbody.bulletBody.dispose();
            rigidbody.motionState.dispose();
        }
    }
}
