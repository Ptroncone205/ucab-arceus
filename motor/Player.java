package com.motor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.ClosestNotMeRayResultCallback;
import com.badlogic.gdx.physics.bullet.collision.ClosestRayResultCallback;
import com.badlogic.gdx.physics.bullet.collision.btCapsuleShape;
import com.badlogic.gdx.physics.bullet.collision.btManifoldPoint;
import com.badlogic.gdx.physics.bullet.collision.btPersistentManifold;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.utils.Disposable;
import com.motor.physics.MotionState;
import com.motor.physics.PhysicsWorld;
import com.motor.utils.Utils3D;


import net.mgsx.gltf.scene3d.scene.Scene;

public class Player implements Disposable{
    private final ClosestNotMeRayResultCallback callback;
    private final Vector3 tmpPosition = new Vector3();
    private Scene scene;
    private MotionState motionState;
    private btRigidBody rigidBody;
    private btCapsuleShape capsuleShape;

    private Matrix4 playerTransform = new Matrix4();
    public Vector3 playerPos;
    private Vector3 inertia;
    private Quaternion roation = new Quaternion();

    private float maxSpeed;
    private Vector3 tempVec;

    public Player (Scene scene, Vector3 position){
        callback = new ClosestNotMeRayResultCallback(rigidBody);
        this.scene = scene;
        this.scene.modelInstance.transform.scale(0.1f, 0.1f, 0.1f);
        this.playerPos = position;
        this.scene.modelInstance.transform.setTranslation(position);
        motionState = new MotionState(this.scene.modelInstance.transform);

        tempVec = new Vector3();
        inertia = new Vector3();
        capsuleShape = new btCapsuleShape(0.5f, 2f);
        capsuleShape.calculateLocalInertia(54f, inertia);

        btRigidBody.btRigidBodyConstructionInfo info = new btRigidBody.btRigidBodyConstructionInfo(1f,motionState,capsuleShape,inertia);
        rigidBody = new btRigidBody(info);
        info.dispose();
        rigidBody.setAngularFactor(0);
        // rigidBody.setCcdMotionThreshold(0.0001f);
        // rigidBody.setCcdSweptSphereRadius(0.2f);
    }

    public void update(){
        rigidBody.getWorldTransform(playerTransform);
        playerTransform.getTranslation(playerPos);
        this.scene.modelInstance.transform.setTranslation(playerPos);

    }

    public void move (Vector3 direction, float dt){
        rigidBody.activate();
        rigidBody.applyCentralImpulse(direction.cpy().scl(100f * dt));

        // run logic cap speed
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)){
            maxSpeed = 30f;
        }else { maxSpeed = 15f;}
        
        Vector3 vel = rigidBody.getLinearVelocity();
        if (vel.len() > maxSpeed){
            Vector3 max = vel.cpy().nor().scl(maxSpeed);
            max.y = (vel.y < -30f) ? -30f : vel.y;
            rigidBody.setLinearVelocity(max);
        }

        // TODO: ROTATION INTERPOLATION
        float angle = (float) Math.toDegrees(Math.atan2(direction.x,direction.z));
        scene.modelInstance.transform.rotate(Vector3.Y, angle);
        System.out.println(vel);
    }

    public void jump (PhysicsWorld pw){
        if (isGrounded(pw)){
            tempVec.set(rigidBody.getLinearVelocity());
            tempVec.y = 30f;
            rigidBody.setLinearVelocity(tempVec); }
    }

    private boolean isGrounded(PhysicsWorld pw) {
        // Reset out callback
        callback.setClosestHitFraction(1.0f);
        callback.setCollisionObject(null);

        System.out.println(playerPos);
        // The position we are casting a ray to, slightly below the players current position.
        tmpPosition.set(playerPos).sub(0, 1.6f, 0);
        pw.raycast(playerPos, tmpPosition, callback);
        return callback.hasHit();
    }

    public btRigidBody getRigidBody() {
        return rigidBody;
    }

    @Override
    public void dispose() {
        rigidBody.dispose();
    }
}