package nintendont.amongspirits;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.BoxShapeBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.btPersistentManifold;

import net.mgsx.gltf.loaders.gltf.GLTFLoader;
import net.mgsx.gltf.scene3d.attributes.PBRColorAttribute;
import net.mgsx.gltf.scene3d.attributes.PBRCubemapAttribute;
import net.mgsx.gltf.scene3d.attributes.PBRTextureAttribute;
import net.mgsx.gltf.scene3d.lights.DirectionalLightEx;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneAsset;
import net.mgsx.gltf.scene3d.scene.SceneManager;
import net.mgsx.gltf.scene3d.scene.SceneSkybox;
import net.mgsx.gltf.scene3d.shaders.PBRShaderProvider;
import net.mgsx.gltf.scene3d.utils.IBLBuilder;

import nintendont.amongspirits.entities.Player;
import nintendont.amongspirits.entities.items.Consumable;
import nintendont.amongspirits.entities.items.Item;
import nintendont.amongspirits.entities.items.MaterialItem;
import nintendont.amongspirits.managers.CraftManager;
import nintendont.amongspirits.managers.InteractionScanner;
import nintendont.amongspirits.managers.InventoryManager;
import nintendont.amongspirits.physics.MyContactListener;
import nintendont.amongspirits.physics.PhysicsWorld;
import nintendont.amongspirits.shaders.CustomShaderProvider;
import nintendont.amongspirits.terrains.HeightMapTerrain;
import nintendont.amongspirits.ui.GUIManager;

public class Main extends ApplicationAdapter
{
	private SceneManager sceneManager;
	private SceneAsset sceneAsset;
	private Scene playerScene;

	private Cubemap diffuseCubemap;
	private Cubemap environmentCubemap;
	private Cubemap specularCubemap;
	private Texture brdfLUT;
	private SceneSkybox skybox;
	private DirectionalLightEx light;

	// movement
	private Player player;
	private Matrix4 playerTransform = new Matrix4();
	private Vector3 moveDirection = new Vector3();

	//camera
	private PerspectiveCamera camera;
	private float camPitch = Const.CAMERA_DEFAULT_PITCH;
	private float distanceFromPlayer = 15f;
	private float angleAroundPlayer = 0f;

	// terrain
	private HeightMapTerrain terrain;
	// private HeightMM terrain;
	private Scene terrainScene;

	// physics
	private PhysicsWorld physicsWorld;

	private SpriteBatch batch;
	private BitmapFont font;

	private GUIManager guiManager;
	private InventoryManager inventory;
	private CraftManager crafting;

	MyContactListener cl;
	private InteractionScanner iScan;
	private Item focusedItem;
	private ArrayList<Item> items = new ArrayList<>();

	@Override
	public void create() {
		Bullet.init();

		// create player scene
		sceneAsset = new GLTFLoader().load(Gdx.files.internal("models/mc/lukitm501.gltf"));
		playerScene = new Scene(sceneAsset.scene);
		float scale_factor = 0.2f;
		playerScene.modelInstance.transform.scale(scale_factor, scale_factor, scale_factor);
		sceneManager = new SceneManager(new CustomShaderProvider(), PBRShaderProvider.createDefaultDepth(24));
		sceneManager.addScene(playerScene);

		camera = new PerspectiveCamera(67f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.near = 0.1f;
		camera.far = 1000;
		sceneManager.setCamera(camera);
		Gdx.input.setCursorCatched(true);
		camera.position.set(0,0f, 4f);

		physicsWorld = new PhysicsWorld();
		physicsWorld.create();

		player  = new Player(playerScene, new Vector3(0,15,0));
		physicsWorld.getDynamicsWorld().addRigidBody(player.getRigidBody(), Const.PF_PLAYER, Const.PF_GROUND | Const.PF_ITEM);

		// setup light
		light = new DirectionalLightEx();
		light.direction.set(1, -3, 1).nor();
		light.color.set(Color.WHITE);
		sceneManager.environment.add(light);

		// setup quick IBL (image based lighting)
		// Texture skyTex = new Texture(Gdx.files.internal("textures/skyPan.png"));

		IBLBuilder iblBuilder = IBLBuilder.createOutdoor(light);
		environmentCubemap = iblBuilder.buildEnvMap(1024);
		diffuseCubemap = iblBuilder.buildIrradianceMap(256);
		specularCubemap = iblBuilder.buildRadianceMap(10);
		iblBuilder.dispose();

		// This texture is provided by the library, no need to have it in your assets.
		brdfLUT = new Texture(Gdx.files.classpath("net/mgsx/gltf/shaders/brdfLUT.png"));

		sceneManager.setAmbientLight(1f);
		sceneManager.environment.set(new PBRTextureAttribute(PBRTextureAttribute.BRDFLUTTexture, brdfLUT));
		sceneManager.environment.set(PBRCubemapAttribute.createSpecularEnv(specularCubemap));
		sceneManager.environment.set(PBRCubemapAttribute.createDiffuseEnv(diffuseCubemap));

		// setup skybox

		skybox = new SceneSkybox(environmentCubemap);
		sceneManager.setSkyBox(skybox);

		buildTerrain();

		// text
		batch = new SpriteBatch();
		font = new BitmapFont();

		inventory = new InventoryManager();
		crafting = new CraftManager();
		guiManager = new GUIManager(batch, inventory, crafting);


		// item build
		for (int i = 0; i<30; i++){
			Item testItem;
			if (i > 10){
				testItem = new Consumable(0, "Oran Berry", "Heals 20 HP", true);

			} else { testItem = new MaterialItem(1, "Tumblestone", "Mysterious red stone used to craft pokeballs");}
			testItem.create(new Vector3(2,-5,-5 * (i+1)), 2f, 2f, 2f);
			items.add(testItem);
			sceneManager.addScene(testItem.getScene());

		}

		cl = new MyContactListener();
		iScan = new InteractionScanner();
		focusedItem = null;

	}

	private void buildTerrain() {
		if (terrain != null){
			terrain.dispose();
			sceneManager.removeScene(terrainScene);
		}

		terrain = new HeightMapTerrain("textures/heightmap.png", 30f, 5f);
		physicsWorld.getDynamicsWorld().addRigidBody(terrain.getRigidBody(), Const.PF_GROUND, Const.PF_PLAYER | Const.PF_ITEM);
		terrainScene = new Scene(terrain.getModelInstance());
		sceneManager.addScene(terrainScene);
	}

	@Override
	public void resize(int width, int height) {
		sceneManager.updateViewport(width, height);
	}

	@Override
	public void render() {
		float deltaTime = Gdx.graphics.getDeltaTime();
		// updates
		physicsWorld.update();
		processInput(deltaTime);
		player.update();
		sceneManager.update(deltaTime);
		updateCamera();

		focusedItem = iScan.findTarget(player.playerPos, camera, items);


        if (focusedItem != null && Gdx.input.isKeyJustPressed(Input.Keys.F)) {
			if (inventory.addItem(focusedItem)){
				items.remove(focusedItem);
				guiManager.update();
				focusedItem.dispose();
				sceneManager.removeScene(focusedItem.getScene());
				focusedItem = null;
			} else {
				System.out.println("nuh uh");
			}
        }


		// render
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		sceneManager.render();
		// physicsWorld.renderDebug(camera);

		// UI
		batch.begin();
		font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond() + "\ndelta: " + deltaTime, 20, Gdx.graphics.getHeight() - 20);
		if (focusedItem != null){ // TODO
			Vector3 uiPos = camera.project(focusedItem.pos.cpy().add(0,2,0));
			font.draw(batch, "F: agarrar", uiPos.x, uiPos.y);

		}
		batch.end();
		guiManager.render(deltaTime);

		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            guiManager.togglePause();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.TAB)) {
            guiManager.toggleInventory();
        }
	}

	private void updateCamera() {
		float horDistance = calculateCamHorDistance(distanceFromPlayer);
		float verDistance = calculateCamVerDistance(distanceFromPlayer);

		calculatePitch();
		calculateAngleAroundPlayer();
		calculateCameraPos(player.playerPos, horDistance, verDistance);

		camera.up.set(Vector3.Y);
		camera.lookAt(player.playerPos);
		camera.update();
	}

	private void calculateCameraPos(Vector3 playerPosition, float horDistance, float verDistance) {
		float offsetX = (float) (horDistance * Math.sin(Math.toRadians(angleAroundPlayer)));
		float offsetZ = (float) (horDistance * Math.cos(Math.toRadians(angleAroundPlayer)));

		camera.position.x = playerPosition.x - offsetX;
		camera.position.z = playerPosition.z - offsetZ;
		camera.position.y = playerPosition.y + verDistance;
	}

	private void calculateAngleAroundPlayer() {
		float angleChange= Gdx.input.getDeltaX() * Const.CAMERA_ANGLE_AROUND_PLAYER_FACTOR;
		angleAroundPlayer -= angleChange;
	}

	private void calculatePitch() {
		float pitchChange = -Gdx.input.getDeltaY() * Const.CAMERA_PITCH_FACTOR;
		camPitch -= pitchChange;
		if (camPitch < Const.CAMERA_MIN_PITCH)
			camPitch = Const.CAMERA_MIN_PITCH;
		else if (camPitch > Const.CAMERA_MAX_PITCH)
			camPitch = Const.CAMERA_MAX_PITCH;
	}

	private float calculateCamVerDistance(float distanceFromPlayer) {
		return (float) (distanceFromPlayer * Math.sin(Math.toRadians(camPitch)));
	}

	private float calculateCamHorDistance(float distanceFromPlayer) {
		return (float) (distanceFromPlayer * Math.cos(Math.toRadians(camPitch)));
	}

	private void processInput (float deltaTime) {
		playerTransform.set(playerScene.modelInstance.transform);

        Vector3 camForward = camera.direction.cpy();
        camForward.y = 0;
        camForward.nor();

        Vector3 camRight = camera.direction.cpy().crs(camera.up);
        camRight.y = 0;
        camRight.nor();
		if (Gdx.input.isKeyPressed(Input.Keys.SPACE)){
			player.jump(physicsWorld);
		}

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            moveDirection.add(camForward);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            moveDirection.sub(camForward);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            moveDirection.sub(camRight);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            moveDirection.add(camRight);
        }

        if (moveDirection.len2() > 0) {
            moveDirection.nor();
        }

		player.move(moveDirection, deltaTime);

    moveDirection.set(0, 0, 0);
	}

	private void buildBoxes() {
		ModelBuilder modelBuilder = new ModelBuilder();
		modelBuilder.begin();

		for (int x = 0; x < 100; x+= 10) {
			for (int z = 0; z < 100; z+= 10) {
				Material material = new Material();
				material.set(PBRColorAttribute.createBaseColorFactor(Color.RED));
				MeshPartBuilder builder = modelBuilder.part(x + ", " + z, GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, material);
				BoxShapeBuilder.build(builder, x, 0, z, 1f,1f,1f);
			}
		}

		ModelInstance model = new ModelInstance(modelBuilder.end());
		sceneManager.addScene(new Scene(model));
	}


	@Override
	public void dispose() {
		terrain.dispose();
		sceneManager.dispose();
		sceneAsset.dispose();
		environmentCubemap.dispose();
		diffuseCubemap.dispose();
		specularCubemap.dispose();
		brdfLUT.dispose();
		skybox.dispose();
		batch.dispose();
		font.dispose();
		physicsWorld.getDynamicsWorld().dispose();
	}
}
