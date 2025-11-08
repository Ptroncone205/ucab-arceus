package nintendont.amongspirits;

import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCapsuleShape;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.sun.org.apache.bcel.internal.generic.PUSH;
import net.mgsx.gltf.scene3d.scene.Scene;

public class Spirit {
    private Scene scene;
    private AnimationController animationController;
    private Vector3 [] position;
    private float stateTime;
    private float speed = 2f;
    private int i = 1;
    private int status;
    private final int STATUS_MOVING = 1;
    private final int STATUS_IDLE = 0;
    private btRigidBody rigidBody;
    private btCapsuleShape capsuleShape;
    private Vector3 inertia;

    public Spirit(Scene scene,Vector3 [] position ){
        animationController = new AnimationController(scene.modelInstance);
        this.scene = scene;
        this.position = position;
        Vector3 hitbox = new Vector3(0,0,0);
        inertia = new Vector3();
        btSphereShape capsuleShape = new btSphereShape(2f);
        capsuleShape.calculateLocalInertia(1f, inertia);
        this.scene.modelInstance.transform.setTranslation(new Vector3(0,10,0));
        MotionState motionState = new MotionState(this.scene.modelInstance.transform);
        btRigidBody.btRigidBodyConstructionInfo info = new btRigidBody.btRigidBodyConstructionInfo(1f,motionState,capsuleShape,inertia);
        rigidBody = new btRigidBody(info);
        rigidBody.setAngularFactor(0);
    }

    public btRigidBody getRigidBody() {
        return rigidBody;
    }

    public void update(float delta){
        if(status == STATUS_MOVING) {
            rigidBody.activate(true);
            Vector3 next = position[i];
            Vector3 diff = next.cpy().sub(rigidBody.getWorldTransform().getTranslation(new Vector3()));
            Vector3 direction = diff.cpy().nor();
            rigidBody.setLinearVelocity(direction.cpy().scl(speed));
            if (diff.len() < 0.1f) {
                status = STATUS_IDLE;
                i = (i + 1) % position.length;
            }
            animationController.update(delta);
        }else if(status == STATUS_IDLE){
            stateTime += delta;
            if(stateTime >= 5) {
                status = STATUS_MOVING;
                stateTime = 0;
            }
        }
    }
}
