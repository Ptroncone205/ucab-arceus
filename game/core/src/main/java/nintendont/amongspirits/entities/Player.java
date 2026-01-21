package nintendont.amongspirits.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.ClosestNotMeRayResultCallback;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.utils.Disposable;
import nintendont.amongspirits.data.codex.Codex;
import nintendont.amongspirits.data.online.packets.BattlePlayerPacket;
import nintendont.amongspirits.data.online.packets.TeamInvocationPacket;
import nintendont.amongspirits.data.spirits.Invocation;
import nintendont.amongspirits.data.spirits.Pasture;
import nintendont.amongspirits.data.spirits.Team;
import nintendont.amongspirits.data.satchel.Satchel;
import nintendont.amongspirits.physics.PhysicsWorld;
import net.mgsx.gltf.scene3d.scene.Scene;

import java.util.Optional;

public class Player implements Disposable{
    private final String name;
    private final Vector3 tmpPosition = new Vector3();

    private Codex codex;
    private Satchel satchel;
    private Team team = new Team();
    private Pasture pasture = new Pasture();
    private Optional<Vector3> focusedItemPosition = Optional.empty();

    private ThrowingMode mode = ThrowingMode.TO_CATCH;
    private int selectedTeamMemberIndex = 0;

    private Scene scene;
    private btRigidBody rigidBody;
    private ClosestNotMeRayResultCallback callback;

    private Matrix4 playerTransform = new Matrix4();
    private Vector3 position;
    private float angle;

    private float maxSpeed;
    private Vector3 tempVec = new Vector3();

    public Player (String name, Vector3 position, Satchel satchel, Codex codex){
        this.name = name;
        this.satchel = satchel;
        this.codex = codex;
        this.position = position;
    }

    public void update(){
        rigidBody.getWorldTransform(playerTransform);
        playerTransform.getTranslation(position);
        this.scene.modelInstance.transform.setTranslation(position);
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

        if (new Vector2(direction.x, direction.z).len2() > 0) {
            angle = (float) Math.toDegrees(Math.atan2(direction.x,direction.z));
        }
        scene.modelInstance.transform.rotate(Vector3.Y, angle);
    }

    public void jump (){
        if (isGrounded()){
            tempVec.set(rigidBody.getLinearVelocity());
            tempVec.y = 30f;
            rigidBody.setLinearVelocity(tempVec); }
    }

    private boolean isGrounded() {
        callback.setClosestHitFraction(1.0f);
        callback.setCollisionObject(null);

        tmpPosition.set(position).sub(0, 1.6f, 0);
        PhysicsWorld.raycast(position, tmpPosition, callback);
        return callback.hasHit();
    }

    public Vector3 getPosition() {
        return position;
    }

    public float getAngle() {
        return angle;
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public btRigidBody getRigidBody() {
        return rigidBody;
    }

    public void setRigidBody(btRigidBody btRigidBody) {
        this.rigidBody = btRigidBody;
        this.callback = new ClosestNotMeRayResultCallback(rigidBody);
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

    public Optional<Vector3> getFocusedItemPosition() {
        return focusedItemPosition;
    }

    public void setFocusedItemPosition(Optional<Vector3> focusedItemPosition) {
        this.focusedItemPosition = focusedItemPosition;
    }

    public int getSelectedTeamMemberIndex() {
        return selectedTeamMemberIndex;
    }

    public void setSelectedTeamMemberIndex(int selectedTeamMemberIndex) {
        this.selectedTeamMemberIndex = selectedTeamMemberIndex;
    }

    public BattlePlayerPacket getAsChallenger() {
        TeamInvocationPacket[] teamPacket = team.getMembers().stream()
            .map(this::createInvocationPacketFrom)
            .toArray(TeamInvocationPacket[]::new);

        return new BattlePlayerPacket(name, selectedTeamMemberIndex, teamPacket);
    }

    private TeamInvocationPacket createInvocationPacketFrom(Invocation m) {
        return new TeamInvocationPacket(m.getSpirit().getName(), m.getSpirit().getLastName(), m.getSpirit().getGender(), m.getHP(), m.getMaxHP(), m.getAttack(), m.getSpecialAttack(), m.getDefense(), m.getSpecialDefense(), m.getSpeed(), m.getSpirit().getForm().getId());
    }

    @Override
    public void dispose() {

    }

    public enum ThrowingMode {
        TO_CATCH,
        TO_ENCOUNTER,
    }

    public void setTeam(Team team){
        this.team = team;
    }
    public void setPasture(Pasture pasture){
        this.pasture = pasture;
    }
}
