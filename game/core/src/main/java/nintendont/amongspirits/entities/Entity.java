package nintendont.amongspirits.entities;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.particles.renderers.ModelInstanceRenderer;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.utils.Disposable;

public class Entity implements Disposable{
    protected btRigidBody body;
    protected btCollisionShape shape;
    protected ModelInstance modelInstance;


    @Override
    public void dispose() {
        if (body != null) body.dispose();
        if (shape != null) shape.dispose();
        modelInstance.model.dispose();
        System.err.println("entity disposed succesfully");
    }
}
