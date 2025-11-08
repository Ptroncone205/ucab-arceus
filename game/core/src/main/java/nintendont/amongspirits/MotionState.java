package nintendont.amongspirits;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.bullet.linearmath.btMotionState;
import com.badlogic.gdx.utils.Disposable;

public class MotionState extends btMotionState implements Disposable {
    private Matrix4 transform;

    public MotionState(Matrix4 transform) {
        this.transform = transform;
    }
    @Override
    public void getWorldTransform(Matrix4 worldTransform) {
        worldTransform.set(transform);
    }

    public void setWorldTransform(Matrix4 worldTransform) {
        transform.set(worldTransform);
    }

    public void dispose() {}
}
