package nintendont.amongspirits.physics;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.linearmath.btMotionState;

public class MotionState extends btMotionState{
    Matrix4 transform;
    private final Vector3 tempPosition = new Vector3();
    private final Quaternion tempQuaternion = new Quaternion();
    private final Vector3 tempScale = new Vector3();

    public MotionState (Matrix4 transform) {
        this.transform = transform;
    }

    @Override
    public void getWorldTransform (Matrix4 worldTrans){
        worldTrans.set(transform);
    }

    @Override
    public void setWorldTransform (Matrix4 worldTrans){
        worldTrans.getTranslation(tempPosition);
        worldTrans.getRotation(tempQuaternion);
        transform.getScale(tempScale);
        transform.set(tempPosition, tempQuaternion, tempScale);
    }
}
