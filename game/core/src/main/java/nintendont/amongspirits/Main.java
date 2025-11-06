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
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture image;
    private SceneManager sceneManager;
    private SceneAsset sceneAsset;
    private Scene scene;
    private Camera camera;
    private Vector3 playerPos = new Vector3(0,2,5);
    private AnimationController animationController;

    @Override
    public void create() {
        batch = new SpriteBatch();
        image = new Texture("libgdx.png");
        camera = new PerspectiveCamera(70, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(0, 2, 5);
        camera.lookAt(0, 0, 0);
        camera.update();
        sceneManager = new SceneManager(256);
        sceneManager.setCamera(camera);
        sceneAsset = new GLTFLoader().load(Gdx.files.internal("lion/scene.gltf"));
        scene = new Scene(sceneAsset.scene);
        sceneManager.addScene(scene);
        animationController = new AnimationController(scene.modelInstance);

    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            playerPos.z -= delta;
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            playerPos.z += delta;
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            playerPos.x += delta;
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            playerPos.x -= delta;
        } else if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            playerPos.y += delta;
        } else  if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            playerPos.y -= delta;
        }

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
    }

    @Override
    public void dispose() {
        batch.dispose();
        image.dispose();
        sceneManager.dispose();
        sceneAsset.dispose();
    }
}
