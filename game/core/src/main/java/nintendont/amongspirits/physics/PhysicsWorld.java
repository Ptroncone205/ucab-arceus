package nintendont.amongspirits.physics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.DebugDrawer;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver;
import com.badlogic.gdx.physics.bullet.linearmath.btIDebugDraw;
import com.badlogic.gdx.utils.Disposable;

import java.util.ArrayList;

public class PhysicsWorld implements Disposable {
    private static btDiscreteDynamicsWorld dynamicsWorld;
    private btDefaultCollisionConfiguration collisionConfig;
    private btCollisionDispatcher collDispatcher;
    private btBroadphaseInterface broadphase;
    private btSequentialImpulseConstraintSolver solver;
    private DebugDrawer  debugDrawer;
    private ArrayList<Disposable> disposables = new ArrayList<>();

    // Debug drawing ray casts
    private static final Vector3 lastRayFrom = new Vector3();
    private static final Vector3 lastRayTo = new Vector3();

    public void create() {
        Bullet.init();
        collisionConfig = new btDefaultCollisionConfiguration();
        collDispatcher = new btCollisionDispatcher(collisionConfig);
        broadphase = new btDbvtBroadphase();
        solver = new btSequentialImpulseConstraintSolver();
        dynamicsWorld = new btDiscreteDynamicsWorld(collDispatcher,broadphase,solver,collisionConfig);
        dynamicsWorld.setGravity(new Vector3(0,-98.1f,0));
        debugDrawer = new DebugDrawer();
        debugDrawer.setDebugMode(
            btIDebugDraw.DebugDrawModes.DBG_DrawWireframe
        );
        dynamicsWorld.setDebugDrawer(debugDrawer);
    }

    public btDiscreteDynamicsWorld getDynamicsWorld() {
        return dynamicsWorld;
    }

    public void createGround(){
        float mass = 0f;
        Vector3 piso = new Vector3(50,1,50);
        btBoxShape groundShape = new btBoxShape(piso);
        disposables.add(groundShape);
        Vector3 inertia = new Vector3(0,0,0);
        btRigidBody.btRigidBodyConstructionInfo info = new btRigidBody.btRigidBodyConstructionInfo(mass,null,groundShape,inertia);
        disposables.add(info);
        btRigidBody rb = new btRigidBody(info);
        rb.setFriction(0.5f);
        dynamicsWorld.addRigidBody(rb);
        info.dispose();
    }

    public void createDynamicBox(){
        float mass = 1f;
        Vector3 piso = new Vector3(1,1,1);
        btBoxShape boxShape = new btBoxShape(piso);
        Vector3 inertia = new Vector3();
        boxShape.calculateLocalInertia(mass,inertia);
        MotionState motionState = new MotionState(new Matrix4());
        disposables.add(motionState);
        btRigidBody.btRigidBodyConstructionInfo info = new btRigidBody.btRigidBodyConstructionInfo(mass,motionState,boxShape,inertia);
        info.dispose();
    }

    public static void raycast(Vector3 from, Vector3 to, RayResultCallback callback) {
        lastRayFrom.set(from).sub(0, 5f, 0f);

        dynamicsWorld.rayTest(from, to, callback);

        if (callback.hasHit() && callback instanceof ClosestRayResultCallback) {
            // Use interpolation to determine the hitpoint where the ray hit the object
            // This is what bullet does behind the scenes as well
            lastRayTo.set(from);
            lastRayTo.lerp(to, callback.getClosestHitFraction());
        } else {
            lastRayTo.set(to);
        }
    }

    public void update() {
        final float delta = Gdx.graphics.getDeltaTime();
        dynamicsWorld.stepSimulation(delta,5,1f/60f);
    }

    public void renderDebug(Camera camera) {
        debugDrawer.begin(camera);
        dynamicsWorld.debugDrawWorld();
        debugDrawer.end();
    }

    @Override
    public void dispose() {
        for (Disposable disposable : disposables) {
            disposable.dispose();
        }
        disposables.clear();
        dynamicsWorld.dispose();
        solver.dispose();
        broadphase.dispose();
        collDispatcher.dispose();
        collisionConfig.dispose();
    }
}
