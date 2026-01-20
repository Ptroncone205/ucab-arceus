package nintendont.amongspirits.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.ClosestNotMeRayResultCallback;
import com.badlogic.gdx.physics.bullet.collision.btCapsuleShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import nintendont.amongspirits.Const;
import nintendont.amongspirits.data.codex.Codex;
import nintendont.amongspirits.data.online.packets.BattlePlayerPacket;
import nintendont.amongspirits.data.online.packets.TeamInvocationPacket;
import nintendont.amongspirits.data.spirits.Invocation;
import nintendont.amongspirits.data.spirits.Pasture;
import nintendont.amongspirits.data.spirits.Spirit;
import nintendont.amongspirits.data.spirits.Team;
import nintendont.amongspirits.entities.systems.YumenjiangSystem;
import nintendont.amongspirits.managers.Satchel;
import nintendont.amongspirits.physics.MotionState;
import nintendont.amongspirits.physics.PhysicsWorld;
import net.mgsx.gltf.scene3d.scene.Scene;

public class Player implements Disposable{
    private final String name;
    private final Vector3 tmpPosition = new Vector3();

    private Codex codex;
    private Satchel satchel;
    private Team team = new Team();
    private Pasture pasture = new Pasture();


    private ThrowingMode mode = ThrowingMode.TO_CATCH;
    private int selectedTeamMemberIndex = 0;

    private Scene scene;
    private MotionState motionState;
    private btRigidBody rigidBody;
    private ClosestNotMeRayResultCallback callback;
    private btCapsuleShape shape;
    private float angle;

    private Matrix4 playerTransform = new Matrix4();
    public Vector3 playerPos;
    private Vector3 inertia;
    private Quaternion roation = new Quaternion(); // la rotacion no funciona, pero eventualmente

    private float maxSpeed;
    private Vector3 tempVec;

    public Player (String name, Vector3 position, Satchel satchel, Codex codex){
        this.name = name;
        this.satchel = satchel;
        this.codex = codex;
        this.playerPos = position;
    }

    public void setupScene(Scene scene) {
        this.scene = scene;
        this.scene.modelInstance.transform.scale(0.1f, 0.1f, 0.1f);
        this.scene.modelInstance.transform.setTranslation(this.playerPos);
        motionState = new MotionState(this.scene.modelInstance.transform);

        tempVec = new Vector3();
        inertia = new Vector3();
        shape = new btCapsuleShape(0.5f, 2f);
        shape.calculateLocalInertia(54f, inertia);

        btRigidBody.btRigidBodyConstructionInfo info = new btRigidBody.btRigidBodyConstructionInfo(1f,motionState,shape,inertia);
        rigidBody = new btRigidBody(info);
        info.dispose();
        rigidBody.setAngularFactor(0);
        rigidBody.setUserValue(Const.PF_PLAYER);
        callback = new ClosestNotMeRayResultCallback(rigidBody);
        // rigidBody.setCcdMotionThreshold(0.0001f);
        // rigidBody.setCcdSweptSphereRadius(0.2f);
    }

    public void update(){
        rigidBody.getWorldTransform(playerTransform);
        playerTransform.getTranslation(playerPos);
        this.scene.modelInstance.transform.setTranslation(playerPos);
        // scene.modelInstance.transform.rotateTowardDirection(Vector3.Y, angle);
//
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

        angle = (float) Math.toDegrees(Math.atan2(direction.x,direction.z));
        scene.modelInstance.transform.rotate(Vector3.Y, angle);
        // System.out.println(vel);
    }

    public void jump (){
        if (isGrounded()){
            tempVec.set(rigidBody.getLinearVelocity());
            tempVec.y = 30f;
            rigidBody.setLinearVelocity(tempVec); }
    }

    private boolean isGrounded() {
        // Reset out callback
        callback.setClosestHitFraction(1.0f);
        callback.setCollisionObject(null);

        // System.out.println(playerPos);
        // The position we are casting a ray to, slightly below the players current position.
        tmpPosition.set(playerPos).sub(0, 1.6f, 0);
        PhysicsWorld.raycast(playerPos, tmpPosition, callback);
        return callback.hasHit();
    }

    public btRigidBody getRigidBody() {
        return rigidBody;
    }

    public String getName(){
        return name;
    }

    public Satchel getSatchel(){
        return satchel;
    }

    public Team getTeam(){
        return team;
    }

    public Pasture getPasture(){
        return pasture;
    }

    public Codex getCodex(){
        return codex;
    }

    public ThrowingMode getMode() {
        return mode;
    }

    public void setMode(ThrowingMode mode) {
        this.mode = mode;
    }

    public int getSelectedTeamMemberIndex() {
        return selectedTeamMemberIndex;
    }

    public void setSelectedTeamMemberIndex(int selectedTeamMemberIndex) {
        this.selectedTeamMemberIndex = selectedTeamMemberIndex;
    }

    public BattlePlayerPacket getAsChallenger() {
        TeamInvocationPacket[] teamPacket = team.getMembers().stream()
            .map(m -> createInvocationPacketFrom(m))
            .toArray(TeamInvocationPacket[]::new);

        return new BattlePlayerPacket(name, selectedTeamMemberIndex, teamPacket);
    }

    private TeamInvocationPacket createInvocationPacketFrom(Invocation m) {
        return new TeamInvocationPacket(m.getSpirit().getName(), m.getSpirit().getLastName(), m.getSpirit().getGender(), m.getHP(), m.getMaxHP(), m.getAttack(), m.getSpecialAttack(), m.getDefense(), m.getSpecialDefense(), m.getSpeed(), m.getSpirit().getForm().getId());
    }

    @Override
    public void dispose() {
        rigidBody.dispose();
        shape.dispose();
        scene.modelInstance.model.dispose();
    }

    public enum ThrowingMode {
        TO_CATCH,
        TO_ENCOUNTER,
    }
}
