package nintendont.amongspirits.entities.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Affine2;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import nintendont.amongspirits.physics.MotionState;

public class RigidbodyComponent implements Component {
    public btRigidBody bulletBody;
    public MotionState motionState;

    public Affine2 getWorldTransform() {
        return null;
    }
}
