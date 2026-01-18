package nintendont.amongspirits.entities.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import nintendont.amongspirits.entities.components.RigidbodyComponent;
import nintendont.amongspirits.entities.components.TriggerComponent;

public class BulletTriggerListener implements EntityListener {
    private final btDiscreteDynamicsWorld dynamicsWorld;
    private final ComponentMapper<TriggerComponent> modelMapper = ComponentMapper.getFor(TriggerComponent.class);

    public BulletTriggerListener(btDiscreteDynamicsWorld dynamicsWorld) {
        this.dynamicsWorld = dynamicsWorld;
    }

    @Override
    public void entityAdded(Entity entity) {
        TriggerComponent trigger = modelMapper.get(entity);
        if (trigger != null) {
            dynamicsWorld.addCollisionObject(trigger.bulletObject,  trigger.group, trigger.mask);
        }
    }

    @Override
    public void entityRemoved(Entity entity) {
        TriggerComponent trigger = modelMapper.get(entity);
        if (trigger != null) {
            dynamicsWorld.removeCollisionObject(trigger.bulletObject);
            trigger.bulletObject.dispose();
        }
    }
}
