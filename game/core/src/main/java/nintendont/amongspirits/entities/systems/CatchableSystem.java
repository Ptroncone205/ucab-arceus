package nintendont.amongspirits.entities.systems;

import javax.swing.Spring;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.physics.bullet.collision.ContactResultCallback;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObjectWrapper;
import com.badlogic.gdx.physics.bullet.collision.btManifoldPoint;
import com.badlogic.gdx.utils.Array;

import nintendont.amongspirits.data.spirits.Spirit;
import nintendont.amongspirits.entities.components.CatchableComponent;
import nintendont.amongspirits.entities.components.SpiritTypeComponent;
import nintendont.amongspirits.entities.components.TriggerComponent;
import nintendont.amongspirits.physics.PhysicsWorld;

public class CatchableSystem extends IteratingSystem {
    private Engine engine;
    private Array<Spirit> team;
    private final PhysicsWorld world;
    private final ComponentMapper<CatchableComponent> catchableMapper = ComponentMapper.getFor(CatchableComponent.class);
    private final ComponentMapper<TriggerComponent> triggerMapper = ComponentMapper.getFor(TriggerComponent.class);

    public CatchableSystem(PhysicsWorld world, Array<Spirit> team) {
        super(Family.all(CatchableComponent.class, TriggerComponent.class).get());
        this.world = world;
        this.team = team;
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        this.engine = engine;
    }

    @Override
    protected void processEntity(Entity entity, float v) {
        TriggerComponent trigger = triggerMapper.get(entity);

        ContactResultCallback callback = new ContactResultCallback() {
            @Override
            public float addSingleResult(btManifoldPoint cp,
                                         btCollisionObjectWrapper colObj0Wrap, int partId0, int index0,
                                         btCollisionObjectWrapper colObj1Wrap, int partId1, int index1) {
                btCollisionObject other = colObj0Wrap.getCollisionObject() == trigger.bulletObject
                    ? colObj1Wrap.getCollisionObject()
                    : colObj0Wrap.getCollisionObject();

                if (other.userData instanceof Entity) {
                    Entity otherEntity = (Entity) other.userData;
                    handleTrigger(entity, otherEntity);
                }
                return 0;
            }
        };
        callback.setCollisionFilterGroup(trigger.group);
        callback.setCollisionFilterMask(trigger.mask);

        world.getDynamicsWorld().contactTest(trigger.bulletObject, callback);
    }

    public void handleTrigger(Entity entity, Entity otherEntity) {
        
        SpiritTypeComponent s1 = entity.getComponent(SpiritTypeComponent.class);
        if (s1 == null) s1 = entity.getComponent(SpiritTypeComponent.class);
        
        team.add(s1.spirit);
        
        engine.removeEntity(entity);
        engine.removeEntity(otherEntity);
        
    }
}
