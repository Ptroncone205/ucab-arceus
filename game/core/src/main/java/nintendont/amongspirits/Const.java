package nintendont.amongspirits;

import com.badlogic.gdx.graphics.Color;
import nintendont.amongspirits.physics.PhysicsWorld;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.utils.Disposable;

import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneManager;

public class Const implements Disposable {
    //singleton jeje
    public static Const hola;
    // game states
    public static enum GameState{
        INGAME,         // 0
        PAUSE,          // 2
        BATTLE,
    }
    public static GameState currentState = GameState.INGAME;
    //todo
    public static float CAMERA_DEFAULT_PITCH = 0f;
    public static float CAMERA_MIN_PITCH = 1f;
    public static float CAMERA_MAX_PITCH = 89f;
    public static float CAMERA_ZOOM_LEVEL_FACTOR = 0.5f;
    public static float CAMERA_ANGLE_AROUND_PLAYER_FACTOR = 0.2f;
    public static float CAMERA_MIN_DISTANCE_FROM_PLAYER = 4;
    public static float CAMERA_PITCH_FACTOR = 0.3f;
    public static float MAX_STEP_HEIGHT = 30f;
    public static Color BLACK_1 = new Color(0,0,0,0.8f);
    public static short PF_PLAYER = 1 << 0;
    public static short PF_ITEM   = 1 << 1;
    public static short PF_GROUND = 1 << 2;

    // stuff
    public static SpriteBatch spriteBatch;
    public AssetManager assets;
    public PhysicsWorld physicsWorld;
    public SceneManager sceneManager;

    private Const(){}

    public static Const get(){
        if (hola == null){
            hola = new Const();
        }
        return hola;
    }

    public void init(){

        assets = new AssetManager();
        spriteBatch = new SpriteBatch();
        sceneManager = new SceneManager();
    }

    public void setState(GameState state){
        Const.currentState = state;
    }

    public void addRigidBody(btRigidBody body){
        physicsWorld.getDynamicsWorld().addRigidBody(body);
    }

    public void addScene(Scene scene){
        sceneManager.addScene(scene);
    }

    public <T> void loadAsset(String path, Class<T> type){
        assets.load(path, type);
    }

    public btDiscreteDynamicsWorld getDynamicsWorld(){
        return physicsWorld.getDynamicsWorld();
    }

    public PhysicsWorld createPhysicsWorld(){
        physicsWorld = new PhysicsWorld();
        physicsWorld.create();
        return physicsWorld;
    }

    public SpriteBatch createSpriteBatch(){
        spriteBatch = new SpriteBatch();
        return spriteBatch;
    }

    @Override
    public void dispose() {
        if (sceneManager != null) sceneManager.dispose();
        if (assets != null) assets.dispose();
        if (spriteBatch != null) spriteBatch.dispose();
        if (physicsWorld != null) physicsWorld.dispose();
    }
}
