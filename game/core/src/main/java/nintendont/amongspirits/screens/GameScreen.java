package nintendont.amongspirits.screens;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
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

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.WebSocketListener;
import com.github.czyzby.websocket.WebSockets;
import net.mgsx.gltf.scene3d.attributes.PBRCubemapAttribute;
import net.mgsx.gltf.scene3d.attributes.PBRTextureAttribute;
import net.mgsx.gltf.scene3d.lights.DirectionalLightEx;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneAsset;
import net.mgsx.gltf.scene3d.scene.SceneManager;
import net.mgsx.gltf.scene3d.scene.SceneSkybox;
import net.mgsx.gltf.scene3d.shaders.PBRShaderConfig;
import net.mgsx.gltf.scene3d.shaders.PBRShaderProvider;
import net.mgsx.gltf.scene3d.utils.IBLBuilder;
import nintendont.amongspirits.Const;
import nintendont.amongspirits.Main;
import nintendont.amongspirits.Const.GameState;
import nintendont.amongspirits.controllers.PlayerController;
import nintendont.amongspirits.data.online.packets.PlayerCoordinates;
import nintendont.amongspirits.data.savedata.SaveData;
import nintendont.amongspirits.entities.ItemStack;
import nintendont.amongspirits.entities.Player;
import nintendont.amongspirits.entities.components.ModelComponent;
import nintendont.amongspirits.entities.components.RigidbodyComponent;
import nintendont.amongspirits.entities.components.TransformComponent;
import nintendont.amongspirits.entities.components.TriggerComponent;
import nintendont.amongspirits.entities.spawners.PlayerSpawner;
import nintendont.amongspirits.entities.spawners.SpiritSpawner;
import nintendont.amongspirits.entities.spawners.YumenjiangSpawner;
import nintendont.amongspirits.entities.items.Item;
import nintendont.amongspirits.entities.items.Pokeball;
import nintendont.amongspirits.entities.systems.*;
import nintendont.amongspirits.managers.CraftManager;
import nintendont.amongspirits.managers.InteractionScanner;
import nintendont.amongspirits.managers.ItemFactory;
import nintendont.amongspirits.managers.Satchel;
import nintendont.amongspirits.managers.SaveManager;
import nintendont.amongspirits.managers.TextInput;
import nintendont.amongspirits.physics.MyContactListener;
import nintendont.amongspirits.physics.PhysicsWorld;
import nintendont.amongspirits.shaders.CustomShaderProvider;
import nintendont.amongspirits.terrains.HeightMapTerrain;
import nintendont.amongspirits.ui.game.BtnEventListener;
import nintendont.amongspirits.ui.game.GUIManager;
import nintendont.amongspirits.utils.AssetUtils;

import static com.github.czyzby.websocket.WebSocketListener.FULLY_HANDLED;

public class GameScreen implements Screen{
    private Main game;
    private WebSocket socket;
    private final Const context = Const.get();
	private SceneManager sceneManager;
	private SceneAsset sceneAsset;
	public static AssetManager assets;
	private Scene playerScene;
    private HashMap<Integer, Entity> onlinePlayers = new  HashMap<>();

    private Engine ecsEngine;
    private PlayerSpawner playerSpawner;
    private YumenjiangSpawner yumenjiangFactory;

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
	TextInput textlistener;
	private InteractionScanner iScan;
	private Item focusedItem;
	private ArrayList<Item> items = new ArrayList<>();

    public GameScreen(Main game, String playerName, boolean load){

        this.game = game;

        Bullet.init();
        context.init();
		ItemFactory.init();
		assets = new AssetManager();
		System.out.println("init just called");

		// cargar todos los assets usados por el juego incluyendo texturas y modelos
        AssetUtils.loadGLTF(assets, "models/mc/lukitm501.gltf");
		assets.load("textures/oranberry.png", Texture.class);
		assets.load("textures/pokeball.png", Texture.class);
		assets.load("textures/tumblestone.png", Texture.class);
        assets.load("models/yumenjiang/scene.gltf", SceneAsset.class);
        assets.load("models/lion/scene.gltf", SceneAsset.class);
        assets.load("models/wolf/scene.gltf", SceneAsset.class);
        assets.load("models/deer/scene.gltf", SceneAsset.class);
        assets.load("models/bunny/scene.gltf", SceneAsset.class);
        assets.load("models/fox/scene.gltf", SceneAsset.class);

		assets.finishLoading();

        PBRShaderConfig config = PBRShaderProvider.createDefaultConfig();
        config.numBones = 90;
		sceneManager = new SceneManager(new CustomShaderProvider(config), PBRShaderProvider.createDefaultDepth(90));

		// create player scene
		sceneAsset = assets.get("models/mc/lukitm501.gltf", SceneAsset.class);
		playerScene = new Scene(sceneAsset.scene);
		float scale_factor = 10f;
		playerScene.modelInstance.transform.scale(scale_factor, scale_factor, scale_factor);
		sceneManager.addScene(playerScene);

		textlistener = new TextInput();

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
		inventory = new Satchel();

		crafting = new CraftManager();
		guiManager = new GUIManager(batch, inventory, crafting);
		guiManager.getPauseMenu().setSaveListener(new BtnEventListener() {
			@Override
			public void onSaveRequest(){
				SaveManager.saveGame(player, items);
			}
			@Override
			public void onQuitRequest(){
				game.quitGame();
			}
		});

		if (load){
			loadData(playerName);
		} else {
			createData(playerName);
		}

		InputAdapter adapter = new InputAdapter(){
			@Override
			public boolean keyDown(int key){
				if (Const.currentState == GameState.INGAME) return false;
				return guiManager.handleInput(key);
			}
		};

		multiplexer.addProcessor(adapter);
		multiplexer.addProcessor(guiManager.stage);

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

		cl = new MyContactListener();
		iScan = new InteractionScanner();
		focusedItem = null;

        // Setup ECS engine
        ecsEngine = new Engine();

        ecsEngine.addEntityListener(Family.all(ModelComponent.class).get(), new SceneModelListener(sceneManager));
        ecsEngine.addEntityListener(Family.all(RigidbodyComponent.class).get(), new BulletRigidbodyListener(physicsWorld.getDynamicsWorld()));
        ecsEngine.addEntityListener(Family.all(TriggerComponent.class).get(), new BulletTriggerListener(physicsWorld.getDynamicsWorld()));

        ecsEngine.addSystem(new BulletPhysicsSystem(physicsWorld));
        ecsEngine.addSystem(new PlayerSystem(player, playerController));
        ecsEngine.addSystem(new TriggerTransformSystem());
        ecsEngine.addSystem(new AnimationSystem());
        ecsEngine.addSystem(new SpiritSystem());
        ecsEngine.addSystem(new ThrowablePhysicsSystem());
        ecsEngine.addSystem(new CatchableSystem(physicsWorld, player.getTeam()));
        ecsEngine.addSystem(new SceneManagerSystem(sceneManager));

        // Setup player factory
        playerSpawner = new PlayerSpawner(ecsEngine, assets);

        // Setup yumenjiang factory
        SceneAsset yumenjiangAsset = assets.get("models/yumenjiang/scene.gltf");
        yumenjiangFactory = new YumenjiangSpawner(ecsEngine, yumenjiangAsset);

        SpiritSpawner spiritSpawner = new SpiritSpawner(ecsEngine, assets);
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

        String address = "localhost";
        int port = 8080;

        socket = WebSockets.newSocket(WebSockets.toWebSocketUrl(address, port));

        socket.addListener(new WebSocketListener() {
            @Override
            public boolean onOpen(WebSocket webSocket) {
                Gdx.app.log("WS", "Connected!");
                Json json = new Json();
                json.setOutputType(JsonWriter.OutputType.json);
                PlayerCoordinates packet = new PlayerCoordinates();
                packet.type = "player_signin";
                packet.x = player.playerPos.x;
                packet.y = player.playerPos.y;
                packet.z = player.playerPos.z;
                webSocket.send(json.toJson(packet));
                return FULLY_HANDLED;
            }

            @Override
            public boolean onMessage(WebSocket webSocket, String packet) {
                Gdx.app.log("WS", "Received: " + packet);

                Json json = new Json();
                JsonValue root = new JsonReader().parse(packet);
                String type = root.getString("type");
                PlayerCoordinates coords;
                switch (type) {
                    case "player_signedin":
                        int signedInID = root.getInt("id");
                        Gdx.app.log("WS", "Player " + signedInID + " signed in!");
                        break;
                    case "all_players":
                        for (JsonValue player : root.get("players")) {
                            coords = json.readValue(PlayerCoordinates.class, player);
                            Entity companion = playerSpawner.spawnCompanion(coords.id, new Vector3(coords.x, coords.y, coords.z));
                            onlinePlayers.put(coords.id, companion);
                        }
                        break;
                    case "player_connected":
                        coords = json.readValue(PlayerCoordinates.class, root.get("player"));
                        Entity companion = playerSpawner.spawnCompanion(coords.id, new Vector3(coords.x, coords.y, coords.z));
                        onlinePlayers.put(coords.id, companion);
                        break;
                    case "player_moved":
                        coords = json.readValue(PlayerCoordinates.class, root.get("player"));
                        Entity target = onlinePlayers.get(coords.id);
                        if (target != null) {
                            TransformComponent transform = target.getComponent(TransformComponent.class);
                            transform.matrix.setTranslation(coords.x, coords.y, coords.z);
//                            transform.matrix.setFromEulerAnglesRad(coords.rotX, coords.rotY, coords.rotZ);
                        }
                        break;
                    case "player_disconnected":
                        int disconnectedID = root.getInt("id");
                        Entity removed = onlinePlayers.remove(disconnectedID);
                        if (removed != null) {
                            Gdx.app.log("WS", "Player " + disconnectedID + " disconnected!");
                        }
                        break;
                    default:
                        Gdx.app.log("WS", "Unknown message type received: " + type);
                }

                return FULLY_HANDLED;
            }

            @Override
            public boolean onMessage(WebSocket webSocket, byte[] bytes) {
                return false;
            }

            @Override
            public boolean onClose(WebSocket webSocket, int closeCode, String reason) {
                Gdx.app.log("WS", "Closed: " + reason);
                return FULLY_HANDLED;
            }

            @Override
            public boolean onError(WebSocket webSocket, Throwable error) {
                Gdx.app.error("WS", "Error!", error);
                return FULLY_HANDLED;
            }

            // Note: You may also need to override onMessage(WebSocket, byte[])
            // if sending binary data.
        });

        socket.connect();
    }

    private void loadData(String playerName) {
		// load assets
		try{
			SaveData data = SaveManager.loadGame(playerName);
			inventory.setItems(data.inventory);
			items = data.items;
			for (Item item : items){
				item.create(item.pos, 2f, 2f, 2f);
				sceneManager.addScene(item.getScene());
			}
			player = new Player(data.name, playerScene, new Vector3(0,15,0), inventory);
		} catch(Exception e){
			System.err.println("Error cargando datos: " + e.getLocalizedMessage());
			createData(playerName);
		}
	}

	private void createData(String playerName) {
		// load assets
		player = new Player(playerName, playerScene, new Vector3(0,15,0), inventory);
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

        ecsEngine.update(deltaTime);

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


        if (Const.currentState == GameState.INGAME && Gdx.input.justTouched()) {
			boolean flag = false;
			for (ItemStack iS : player.getSatchel().getItems()){
				if (iS.getItem() instanceof Pokeball){
					iS.count--;
					flag = true;
					break;
				}
			}
			if (!flag) return;

            Vector3 spawnPoint = new Vector3(player.playerPos).add(Vector3.Y.cpy().scl(2f));
            Vector3 throwDirection = camera.direction.cpy();
            throwDirection.add(new Vector3(0, 0.5f, 0));
            yumenjiangFactory.spawnThrowableYumenjiang(spawnPoint, throwDirection, 50);
        }

		if (Gdx.input.isKeyJustPressed(Input.Keys.F9)){
			System.out.println(player.getTeam());
		}

        if (socket.isOpen()) {
            Json json = new Json();
            json.setOutputType(JsonWriter.OutputType.json);
            PlayerCoordinates packet = new PlayerCoordinates();
            packet.type = "player_update";
            packet.x = player.playerPos.x;
            packet.y = player.playerPos.y;
            packet.z = player.playerPos.z;
            socket.send(json.toJson(packet));
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

        // FOR DEBUGGING: Remember to delete
        if (Gdx.input.isKeyJustPressed(Input.Keys.F1)) {
            System.out.println(player.playerPos);
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
		assets.dispose();
		environmentCubemap.dispose();
		diffuseCubemap.dispose();
		specularCubemap.dispose();
		brdfLUT.dispose();
		skybox.dispose();
		font.dispose();
		physicsWorld.dispose();
        if (socket.isOpen()) {
            socket.close();
        }
    }
}
