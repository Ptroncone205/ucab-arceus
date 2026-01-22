package nintendont.amongspirits.screens;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cubemap;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;

import com.github.czyzby.websocket.WebSocket;
import net.mgsx.gltf.scene3d.attributes.PBRCubemapAttribute;
import net.mgsx.gltf.scene3d.attributes.PBRTextureAttribute;
import net.mgsx.gltf.scene3d.lights.DirectionalLightEx;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneManager;
import net.mgsx.gltf.scene3d.scene.SceneSkybox;
import net.mgsx.gltf.scene3d.shaders.PBRShaderConfig;
import net.mgsx.gltf.scene3d.shaders.PBRShaderProvider;
import net.mgsx.gltf.scene3d.utils.IBLBuilder;
import nintendont.amongspirits.Const;
import nintendont.amongspirits.Main;
import nintendont.amongspirits.Const.GameState;
import nintendont.amongspirits.controllers.PlayerController;
import nintendont.amongspirits.data.assets.GameAssets;
import nintendont.amongspirits.data.config.MultiplayerConfig;
import nintendont.amongspirits.data.config.MultiplayerConfigLoader;
import nintendont.amongspirits.data.spirits.Invocation;
import nintendont.amongspirits.entities.Player;
import nintendont.amongspirits.entities.components.ModelComponent;
import nintendont.amongspirits.entities.components.PlayerTagComponent;
import nintendont.amongspirits.entities.components.RigidbodyComponent;
import nintendont.amongspirits.entities.components.TriggerComponent;
import nintendont.amongspirits.entities.factories.MultiplayerWSFactory;
import nintendont.amongspirits.entities.spawners.ItemSpawner;
import nintendont.amongspirits.entities.spawners.PlayerSpawner;
import nintendont.amongspirits.entities.spawners.SpiritSpawner;
import nintendont.amongspirits.entities.spawners.YumenjiangSpawner;
import nintendont.amongspirits.entities.systems.*;
import nintendont.amongspirits.managers.CraftManager;
import nintendont.amongspirits.physics.MyContactListener;
import nintendont.amongspirits.physics.PhysicsWorld;
import nintendont.amongspirits.shaders.CustomShaderProvider;
import nintendont.amongspirits.terrains.HeightMapTerrain;
import nintendont.amongspirits.ui.game.BtnEventListener;
import nintendont.amongspirits.ui.game.GUIManager;
import nintendont.amongspirits.ui.game.PauseMenu;

public class GameScreen implements Screen{
    private final Main game;
	private final AssetManager assets;
	private final Player player;

    private final Const context = Const.get();
	private SceneManager sceneManager;

    private Engine ecsEngine;

	private Cubemap diffuseCubemap;
	private Cubemap environmentCubemap;
	private Cubemap specularCubemap;
	private Texture brdfLUT;
	private SceneSkybox skybox;
	private DirectionalLightEx light;

	// input
	private InputMultiplexer multiplexer;
	private PlayerController playerController;
	// movement

	//camera
	private PerspectiveCamera camera;
	private float camPitch = Const.CAMERA_DEFAULT_PITCH;
	private float distanceFromPlayer = 15f;
	private float angleAroundPlayer = 0f;
    private boolean shouldDebugPhysics = false;

	// terrain
	private HeightMapTerrain terrain;
	// private HeightMM terrain;
	private Scene terrainScene;

	// physics
	private PhysicsWorld physicsWorld;

	private SpriteBatch batch;
	private BitmapFont font;

	private GUIManager guiManager;
	private CraftManager crafting;

	MyContactListener cl;
	private Texture catchMode;
	private Texture challengeMode;
	private Texture noPkmn;

    public GameScreen(Main game, AssetManager assets, Player player) {
        this.game = game;
        this.assets = assets;
        this.player = player;

        Bullet.init();
        context.init();

        PBRShaderConfig config = PBRShaderProvider.createDefaultConfig();
        config.numBones = 156;
		sceneManager = new SceneManager(new CustomShaderProvider(config), PBRShaderProvider.createDefaultDepth(156));

        game.playMusic("music and sounds/music/game.mp3", true);

		camera = new PerspectiveCamera(67f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.near = 0.1f;
		camera.far = 1000;
		sceneManager.setCamera(camera);
		Gdx.input.setCursorCatched(true);
		camera.position.set(0,0f, 4f);

		physicsWorld = context.createPhysicsWorld();

		multiplexer = new InputMultiplexer();
		Gdx.input.setInputProcessor(multiplexer);

		// text
		batch = context.spriteBatch;
		font = new BitmapFont();

		crafting = new CraftManager(game.getItems());

		guiManager = new GUIManager(assets, batch, crafting, player, player.getCodex());

        ((PauseMenu)guiManager.getMenu("pause")).setSaveListener(new BtnEventListener() {
			@Override
			public void onSaveRequest(){
                game.getSaveManager().saveGame(player);
			}
			@Override
			public void onQuitRequest(){
				game.quitGame();
			}
		});

        catchMode = assets.get(GameAssets.POKEBALL);
        noPkmn = assets.get(GameAssets.NO_POKEBALL);

		multiplexer.addProcessor(guiManager.stage);
		multiplexer.addProcessor(new InputAdapter(){
            @Override
            public boolean keyDown(int key){
                return guiManager.handleInput(key);
            }
        });

		playerController = new PlayerController(player, camera);
		multiplexer.addProcessor(playerController);

		// setup light
		light = new DirectionalLightEx();
		light.direction.set(1, -3, 1).nor();
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

		sceneManager.setAmbientLight(0.7f);
		sceneManager.environment.set(new PBRTextureAttribute(PBRTextureAttribute.BRDFLUTTexture, brdfLUT));
		sceneManager.environment.set(PBRCubemapAttribute.createSpecularEnv(specularCubemap));
		sceneManager.environment.set(PBRCubemapAttribute.createDiffuseEnv(diffuseCubemap));

		// setup skybox

		skybox = new SceneSkybox(environmentCubemap);
		sceneManager.setSkyBox(skybox);

		buildTerrain();

		cl = new MyContactListener();

        // Setup WebSocket connection
        MultiplayerConfig multiplayerConfig =  new MultiplayerConfigLoader().loadFromPropsFile();
        WebSocket socket = new MultiplayerWSFactory().createWebSocket(multiplayerConfig, "world");

        // Setup ECS engine
        ecsEngine = new Engine();

        ecsEngine.addEntityListener(Family.all(ModelComponent.class).get(), new SceneModelListener(sceneManager));
        ecsEngine.addEntityListener(Family.all(RigidbodyComponent.class).get(), new BulletRigidbodyListener(physicsWorld.getDynamicsWorld()));
        ecsEngine.addEntityListener(Family.all(TriggerComponent.class).get(), new BulletTriggerListener(physicsWorld.getDynamicsWorld()));
        ecsEngine.addEntityListener(Family.all(PlayerTagComponent.class).get(), new PlayerEntityListener(player));

        // Initialize spawners
        YumenjiangSpawner yumenjianSpawner = new YumenjiangSpawner(ecsEngine, assets);
        PlayerSpawner playerSpawner = new PlayerSpawner(ecsEngine, assets);
        ItemSpawner itemSpawner = new ItemSpawner(ecsEngine, assets, game.getItems());
        SpiritSpawner spiritSpawner = new SpiritSpawner(ecsEngine, assets, player.getCodex());

        // Initialize systems
        ecsEngine.addSystem(new BulletPhysicsSystem(physicsWorld));
        ecsEngine.addSystem(new PlayerSystem(player, playerController));
        ecsEngine.addSystem(new TriggerTransformSystem());
        ecsEngine.addSystem(new AnimationSystem());
        ecsEngine.addSystem(new SpiritSystem());
        ecsEngine.addSystem(new ThrowablePhysicsSystem());
        ecsEngine.addSystem(new CatchableSystem(player, physicsWorld));
        ecsEngine.addSystem(new ChallengeSystem(game, player, physicsWorld, socket));
        ecsEngine.addSystem(new ItemSystem(player, camera, guiManager, multiplexer, socket));
        ecsEngine.addSystem(new YumenjiangSystem(multiplexer, player, yumenjianSpawner, camera, guiManager));
        ecsEngine.addSystem(new MultiplayerSystem(game, socket, player, playerSpawner, itemSpawner));
        ecsEngine.addSystem(new EndgameSystem(player, spiritSpawner, guiManager, sceneManager));
        ecsEngine.addSystem(new SceneManagerSystem(sceneManager));

        playerSpawner.spawnPlayer(new Vector3(28.804615f,-9.616931f,-111.636635f));

        spiritSpawner.spawnLion(new Vector3(30.155998f,-5.723038f,17.230192f), new Vector3[] {
            new Vector3(30.155998f,-5.723038f,17.230192f),
            new Vector3(1.6152792f,-6.701237f,35.62867f),
            new Vector3(-17.962223f,-7.386892f,12.141544f),
        });
        spiritSpawner.spawnDeer(new Vector3(-8.658541f,-7.1790175f,-83.79534f), new Vector3[] {
            new Vector3(-8.658541f,-7.1790175f,-83.79534f),
            new Vector3(-67.99276f,-3.6083195f,-91.609474f),
            new Vector3(-124.330864f,-6.713242f,-110.35451f),
            new Vector3(-125.90662f,-3.1293454f,-41.220222f),
            new Vector3(-124.330864f,-6.713242f,-110.35451f),
            new Vector3(-67.99276f,-3.6083195f,-91.609474f),
        });
        spiritSpawner.spawnWolf(new Vector3(-118.479904f,-13.676473f,16.55237f), new Vector3[] {
            new Vector3(-118.479904f,-13.676473f,16.55237f),
            new Vector3(-56.469837f,-10.573059f,26.624063f),
            new Vector3(-103.06346f,-13.143304f,114.45455f),
        });
        spiritSpawner.spawnBunny(new Vector3(28.545568f,-11.491491f,-50.88826f), new Vector3[] {
            new Vector3(28.545568f,-11.491491f,-50.88826f),
            new Vector3(28.737074f,-11.382355f,-89.65038f),
            new Vector3(79.27631f,-11.176129f,-73.44786f),
        });
        spiritSpawner.spawnFox(new Vector3(42.199657f,-6.9087963f,65.2783f), new Vector3[] {
            new Vector3(42.199657f,-6.9087963f,65.2783f),
            new Vector3(12.966826f,-9.829789f,101.345924f),
            new Vector3(-13.61148f,-4.642546f,90.47211f),
            new Vector3(22.177233f,-7.333871f,62.20569f),
        });
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
        game.playMusic("music and sounds/music/game.mp3", true);
        Gdx.input.setCursorCatched(true);
        Gdx.input.setInputProcessor(multiplexer);

    }

    @Override
    public void render(float delta) {
        float deltaTime = Gdx.graphics.getDeltaTime();

        ecsEngine.update(deltaTime);

		if (Const.currentState == GameState.INGAME)
			updateCamera();

		// render
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		sceneManager.render();

        if (shouldDebugPhysics) {
            physicsWorld.renderDebug(camera);
        }

		// HUD
		batch.begin();
		font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond() + "\ndelta: " + deltaTime, 20, Gdx.graphics.getHeight() - 20);
		if (player.getFocusedItemPosition().isPresent()) {
			Vector3 uiPos = camera.project(player.getFocusedItemPosition().get().cpy().add(0,2,0));
			font.draw(batch, "F: agarrar", uiPos.x, uiPos.y);

		}
        int offset = 75;
		if ((player.getMode() == Player.ThrowingMode.TO_ENCOUNTER && player.getTeam().getMembers().isEmpty())
            || (player.getMode() == Player.ThrowingMode.TO_CATCH && !player.getSatchel().hasYumenjiang())) {
			batch.draw(noPkmn, offset, offset, 50,50);
		} else {
            if (player.getMode() == Player.ThrowingMode.TO_ENCOUNTER){
                Invocation active = player.getTeam().getMembers().get(player.getSelectedTeamMemberIndex());
                Texture battleTexture = assets.get(active.getBattleAsset());
                float scale = 1/8f;
                float imgWidth = battleTexture.getWidth() * scale;
                float imgHeight = battleTexture.getHeight() * scale;
                batch.draw(battleTexture, offset - imgWidth/2f, offset, imgWidth, imgHeight);
            }
            batch.draw(catchMode, offset, offset, 50,50);
        }
		batch.end();
		guiManager.render(deltaTime);

        if (Gdx.input.isKeyJustPressed(Input.Keys.F1)) {
            System.out.println(player.getPosition());
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.F2)) {
            shouldDebugPhysics = !shouldDebugPhysics;
        }
    }

    private void updateCamera() {
		float horDistance = calculateCamHorDistance(distanceFromPlayer);
		float verDistance = calculateCamVerDistance(distanceFromPlayer);

		calculatePitch();
		calculateAngleAroundPlayer();
		calculateCameraPos(player.getPosition(), horDistance, verDistance);

		camera.up.set(Vector3.Y);
		camera.lookAt(player.getPosition());
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
        game.stopMusic();

    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub
        game.playMusic("music and sounds/music/game.mp3", true);
    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub

    }

    @Override
    public void dispose() {
        terrain.dispose();
		sceneManager.dispose();
		environmentCubemap.dispose();
		diffuseCubemap.dispose();
		specularCubemap.dispose();
		brdfLUT.dispose();
		skybox.dispose();
		font.dispose();
		physicsWorld.dispose();
    }
}
