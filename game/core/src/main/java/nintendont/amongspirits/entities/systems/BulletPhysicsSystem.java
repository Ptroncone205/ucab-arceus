package nintendont.amongspirits.entities.systems;

import com.badlogic.ashley.core.EntitySystem;
import nintendont.amongspirits.physics.PhysicsWorld;

public class BulletPhysicsSystem extends EntitySystem {
    private final PhysicsWorld world;

    public BulletPhysicsSystem(PhysicsWorld world) {
        this.world = world;
    }

    @Override
    public void update(float delta) {
        world.update(delta);
    }
}
