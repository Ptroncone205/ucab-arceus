package nintendont.amongspirits;

import com.badlogic.gdx.ApplicationAdapter;
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

public class PhysicsWorld extends ApplicationAdapter {
    private btDefaultCollisionConfiguration collisionConfig;
    private btCollisionDispatcher collDispatcher;
    private btBroadphaseInterface broadphase;
    private btSequentialImpulseConstraintSolver solver;
    private btDiscreteDynamicsWorld dynamicsWorld;
    private DebugDrawer  debugDrawer;
    private ArrayList<Disposable> disposables = new ArrayList<>();

    @Override
    public void create() {
        Bullet.init();
        collisionConfig = new btDefaultCollisionConfiguration();
        collDispatcher = new btCollisionDispatcher(collisionConfig);
        broadphase = new btDbvtBroadphase();
        solver = new btSequentialImpulseConstraintSolver();
        dynamicsWorld = new btDiscreteDynamicsWorld(collDispatcher,broadphase,solver,collisionConfig);
        dynamicsWorld.setGravity(new Vector3(0,-9.81f,0));
        createGround();
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
        dynamicsWorld.addRigidBody(rb);
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
        disposables.add(info);
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
