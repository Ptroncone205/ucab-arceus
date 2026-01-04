package nintendont.amongspirits.screens;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cubemap;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;

import net.mgsx.gltf.scene3d.attributes.PBRCubemapAttribute;
import net.mgsx.gltf.scene3d.attributes.PBRTextureAttribute;
import net.mgsx.gltf.scene3d.lights.DirectionalLightEx;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneAsset;
import net.mgsx.gltf.scene3d.scene.SceneManager;
import net.mgsx.gltf.scene3d.scene.SceneSkybox;
import net.mgsx.gltf.scene3d.shaders.PBRShaderProvider;
import net.mgsx.gltf.scene3d.utils.IBLBuilder;
import nintendont.amongspirits.Const;
import nintendont.amongspirits.Main;
import nintendont.amongspirits.Const.GameState;
import nintendont.amongspirits.controllers.GUIController;
import nintendont.amongspirits.controllers.PlayerController;
import nintendont.amongspirits.data.savedata.SaveData;
import nintendont.amongspirits.data.savedata.SaveEventListener;
import nintendont.amongspirits.entities.Player;
import nintendont.amongspirits.entities.items.Item;
import nintendont.amongspirits.managers.CraftManager;
import nintendont.amongspirits.managers.InteractionScanner;
import nintendont.amongspirits.managers.ItemFactory;
import nintendont.amongspirits.managers.Satchel;
import nintendont.amongspirits.managers.SaveManager;
import nintendont.amongspirits.physics.MyContactListener;
import nintendont.amongspirits.physics.PhysicsWorld;
import nintendont.amongspirits.shaders.CustomShaderProvider;
import nintendont.amongspirits.terrains.HeightMapTerrain;
import nintendont.amongspirits.ui.GUIManager;
import nintendont.amongspirits.utils.AssetUtils;

public class GameScreen implements Screen{
    private Main game;
    private final Const context = Const.get();
	private SceneManager sceneManager;
	private SceneAsset sceneAsset;
	private Scene playerScene;

	private Cubemap diffuseCubemap;
	private Cubemap environmentCubemap;
	private Cubemap specularCubemap;
	private Texture brdfLUT;
	private SceneSkybox skybox;
	private DirectionalLightEx light;

	// input
	private InputMultiplexer multiplexer;
	private PlayerController playerController;
	private GUIController guiController;
	// movement
	private Player player;

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
	private Satchel inventory;
	private CraftManager crafting;

	MyContactListener cl;
	private InteractionScanner iScan;
	private Item focusedItem;
	private ArrayList<Item> items = new ArrayList<>();

    public GameScreen(Main game){
        this.game = game;

        Bullet.init();
        context.init();

        AssetUtils.loadGLTF(context.getAssetManager(), "models/mc/lukitm501.gltf");
		System.out.println("start loading");
        context.getAssetManager().finishLoading();
        System.out.println("finish loading");
		// create player scene
		sceneAsset = context.getAssetManager().get("models/mc/lukitm501.gltf", SceneAsset.class);
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

		multiplexer = new InputMultiplexer();
		Gdx.input.setInputProcessor(multiplexer);

		// text
		batch = new SpriteBatch();
		font = new BitmapFont();

		inventory = new Satchel();
		crafting = new CraftManager();
		guiManager = new GUIManager(batch, inventory, crafting);
		guiManager.getPauseMenu().setSaveListener(new SaveEventListener() {
			@Override
			public void onSaveRequest(){
				SaveManager.saveGame(player, items);
			}
			@Override
			public void onLoadRequest(){
				SaveData data = SaveManager.loadGame();
				inventory.setItems(data.inventory);
				items = data.items;
				player = new Player(data.name, playerScene, new Vector3(0,15,0), inventory);
			}
		});
		
		guiController = new GUIController(guiManager);
		multiplexer.addProcessor(guiController);
		multiplexer.addProcessor(guiManager.stage);

		if (player == null){
			player  = new Player("player", playerScene, new Vector3(0,15,0), inventory);
		}
		
		playerController = new PlayerController(player, camera);
		multiplexer.addProcessor(playerController);
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


		ItemFactory.init();
		// item build
		if (items.isEmpty()){
			for (int i = 0; i<30; i++){
				Item testItem;
				if (i > 10){
					testItem = ItemFactory.createItem(0);
				} else { testItem = ItemFactory.createItem(2); }
				testItem.create(new Vector3(2,-5,-5 * (i+1)), 2f, 2f, 2f);
				items.add(testItem);
				sceneManager.addScene(testItem.getScene());
			}
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
    public void show() {
        Const.currentState = GameState.INGAME;
        
    }
    
    @Override
    public void render(float delta) {
        float deltaTime = Gdx.graphics.getDeltaTime();
		// updates
		physicsWorld.update();
		playerController.update(deltaTime);
		// handleInput(deltaTime);
		// processInput(deltaTime);
		player.update();
		sceneManager.update(deltaTime);
		if (Const.currentState == GameState.INGAME)
			updateCamera();

		focusedItem = iScan.findTarget(player.playerPos, camera, items);


        if (focusedItem != null && Gdx.input.isKeyJustPressed(Input.Keys.F)) {
			if (inventory.addItem(focusedItem)){
				items.remove(focusedItem);
				guiManager.update();
				focusedItem.dispose();
				sceneManager.removeScene(focusedItem.getScene());
				focusedItem = null;
			}
        }


		// render
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		sceneManager.render();
		// physicsWorld.renderDebug(camera);

		// HUD
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

    @Override
    public void resize(int width, int height) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void pause() {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void resume() {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void hide() {
        // TODO Auto-generated method stub
        
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
		context.dispose();
        
    }
    
}
