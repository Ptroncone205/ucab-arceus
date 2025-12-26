package nintendont.amongspirits.entities;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.particles.renderers.ModelInstanceRenderer;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.utils.Disposable;

public class Entity implements Disposable{
    protected btRigidBody body;
    protected ModelInstance modelInstance;


    @Override
    public void dispose() {
        body.dispose();
        modelInstance.model.dispose();
    }
}
