package nintendont.amongspirits;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import net.mgsx.gltf.loaders.gltf.GLTFLoader;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneAsset;
import net.mgsx.gltf.scene3d.scene.SceneManager;

import java.util.Vector;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class SpiritMain extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture image;
    private SceneManager sceneManager;
    private SceneAsset sceneAsset;
    private Scene scene;
    private Camera camera;
    private Vector3 playerPos = new Vector3(0,5, 20);
    private AnimationController animationController;
    private Spirit alfonso;
    private PhysicsWorld physicsWorld;

    @Override
    public void create() {
        batch = new SpriteBatch();
        image = new Texture("libgdx.png");
        camera = new PerspectiveCamera(70, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.lookAt(0, 0, 0);
        camera.update();
        sceneManager = new SceneManager(256);
        sceneManager.setCamera(camera);
        sceneAsset = new GLTFLoader().load(Gdx.files.internal("lion/scene.gltf"));
        scene = new Scene(sceneAsset.scene);
        sceneManager.addScene(scene);
        animationController = new AnimationController(scene.modelInstance);
        physicsWorld = new PhysicsWorld();
        physicsWorld.create();
        physicsWorld.createGround();
        alfonso = new Spirit(scene, new Vector3[]{
            new Vector3(0,3,0),
            new Vector3(10,3,10),
            new Vector3(0,3,10),
        });
        physicsWorld.getDynamicsWorld().addRigidBody(alfonso.getRigidBody());
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();
        physicsWorld.update();
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            playerPos.z -= delta;
        } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            playerPos.z += delta;
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            playerPos.x += delta;
        } else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            playerPos.x -= delta;
        } else if (Gdx.input.isKeyPressed(Input.Keys.E)) {
            playerPos.y += delta;
        } else  if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            playerPos.y -= delta;
        }
        alfonso.update(delta);
        camera.position.set(playerPos.x, playerPos.y, playerPos.z);
        camera.lookAt(0,0,0);
        camera.up.set(Vector3.Y);
        animationController.animate("Take 001",-1,0.25f,null,0); ///lion walk
        animationController.update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        camera.update();
        sceneManager.update(delta);
        sceneManager.render();
        physicsWorld.renderDebug(camera);
    }

    @Override
    public void dispose() {
        batch.dispose();
        image.dispose();
        sceneManager.dispose();
        sceneAsset.dispose();
        physicsWorld.dispose();
    }
}
