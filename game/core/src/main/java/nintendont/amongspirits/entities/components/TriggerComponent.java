package nintendont.amongspirits.entities.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import nintendont.amongspirits.physics.PhysicsLayers;

public class TriggerComponent implements Component {
    public btCollisionObject bulletObject;
    public int group;
    public int mask = PhysicsLayers.ALL;
}
