package nintendont.amongspirits.entities.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.WebSocketListener;
import nintendont.amongspirits.Main;
import nintendont.amongspirits.data.online.packets.*;
import nintendont.amongspirits.data.spirits.Invocation;
import nintendont.amongspirits.data.spirits.Spirit;
import nintendont.amongspirits.data.spirits.Team;
import nintendont.amongspirits.entities.Enemy;
import nintendont.amongspirits.entities.Player;
import nintendont.amongspirits.entities.components.TransformComponent;
import nintendont.amongspirits.entities.spawners.ItemSpawner;
import nintendont.amongspirits.entities.spawners.PlayerSpawner;
import nintendont.amongspirits.screens.BattleScreen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MultiplayerSystem extends EntitySystem {
    public static final double EPSILON = 0.0001;

    private final Json json = new Json();
    private final Main game;
    private final WebSocket socket;
    private final Player player;
    private final PlayerSpawner playerSpawner;
    private final ItemSpawner itemSpawner;
    private final HashMap<Integer, Entity> onlinePlayers = new HashMap<>();
    private final HashMap<Integer, OnlineItem> onlineItems = new HashMap<>();
    private PlayerCoordinatesPacket lastPlayerPacket;
    private Enemy challenger = null;
    private boolean offlineItemsSpawned = false;

    public MultiplayerSystem(
        Main game,
        WebSocket socket,
        Player player,
        PlayerSpawner playerSpawner,
        ItemSpawner itemSpawner
    ) {
        this.game = game;
        this.socket = socket;
        this.player = player;
        this.playerSpawner = playerSpawner;
        this.itemSpawner = itemSpawner;

        json.setOutputType(JsonWriter.OutputType.json);
        json.setUsePrototypes(false);

        socket.addListener(new WebSocketListener() {
            @Override
            public boolean onOpen(WebSocket webSocket) {
                Gdx.app.log("WS", "Connected!");
                PlayerCoordinatesPacket packet = createPlayerPacketFrom("player_signin");
                webSocket.send(json.toJson(packet));
                return FULLY_HANDLED;
            }

            @Override
            public boolean onMessage(WebSocket webSocket, String packet) {
                Gdx.app.log("WS", "Received: " + packet);

                JsonValue root = new JsonReader().parse(packet);
                String type = root.getString("type");
                switch (type) {
                    case "world_setup":
                        handleWorldSetup(root);
                        break;
                    case "player_signedin":
                        handlePlayerSignedIn(root);
                        break;
                    case "player_connected":
                        handlePlayerConnected(root);
                        break;
                    case "player_moved":
                        handlePlayerUpdate(root);
                        break;
                    case "player_challenged":
                        handlePlayerChallenged(root);
                        break;
                    case "player_disconnected":
                        handlePlayerDisconnected(root);
                        break;
                    case "item_collected_broadcast":
                        handleItemCollectedBroadcast(root);
                        break;
                    case "item_respawned":
                        handleItemRespawned(root);
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

                if (!offlineItemsSpawned) {
                    itemSpawner.spawnTumblestone(new Vector3(-17.838638f,-4.3560739f,-47.24584f));
                    itemSpawner.spawnOranBerry(new Vector3(1.8027654f,-2.9660032f,-41.948856f));
                    itemSpawner.spawnOranBerry(new Vector3(48.982178f,1.8653733f,-9.645581f));
                    itemSpawner.spawnOranBerry(new Vector3(54.165665f,1.508391f,0.024594655f));
                    itemSpawner.spawnOranBerry(new Vector3(67.40762f,4.045167f,5.6782846f));
                    itemSpawner.spawnTumblestone(new Vector3(54.230034f,-3.6913362f,-20.758377f));
                    itemSpawner.spawnTumblestone(new Vector3(83.13638f,-2.0633545f,-0.5703411f));
                    itemSpawner.spawnTumblestone(new Vector3(102.18394f,1.1988071f,7.637454f));
                    itemSpawner.spawnTumblestone(new Vector3(127.85025f,1.4623288f,-19.181307f));
                    itemSpawner.spawnTumblestone(new Vector3(133.04187f,-3.0535147f,-52.89993f));
                    itemSpawner.spawnTumblestone(new Vector3(142.18419f,-4.625522f,-78.41611f));
                    offlineItemsSpawned = true;
                }

                return FULLY_HANDLED;
            }
        });
    }

    @Override
    public void update(float delta) {
        if (socket.isClosed()) {
            return;
        }

        if (challenger != null) {
            socket.close();
            game.setScreen(new BattleScreen(game, player, challenger, player.getSelectedTeamMemberIndex(), game.assets));
            return;
        }

        PlayerCoordinatesPacket packet = createPlayerPacketFrom("player_update");
        if (lastPlayerPacket == null || packet.x - lastPlayerPacket.x > EPSILON || packet.y - lastPlayerPacket.y > EPSILON || packet.z - lastPlayerPacket.z > EPSILON || packet.angle - player.getAngle() > EPSILON) {
            socket.send(json.toJson(packet));
        }
        lastPlayerPacket = packet;
    }

    private void handleWorldSetup(JsonValue root) {
        for (JsonValue player : root.get("players")) {
            PlayerCoordinatesPacket coords = json.readValue(PlayerCoordinatesPacket.class, player);
            Entity companion = playerSpawner.spawnCompanion(coords.id, new Vector3(coords.x, coords.y, coords.z));
            onlinePlayers.put(coords.id, companion);
        }

        for (JsonValue item : root.get("items")) {
            int itemId = item.getInt("id");
            OnlineItem onlineItem = new OnlineItem();
            onlineItems.put(itemId, onlineItem);

            for (int i = 0; i < item.get("spawns").size; i++) {
                JsonValue spawn = item.get("spawns").get(i);
                int x = spawn.getInt(1);
                int y = spawn.getInt(2);
                int z = spawn.getInt(3);

                OnlineItemSpawn onlineItemSpawn = new OnlineItemSpawn();
                onlineItemSpawn.available = spawn.getBoolean(0);
                onlineItemSpawn.position = new Vector3(x, y, z);
                if (onlineItemSpawn.available) {
                    onlineItemSpawn.entity = itemSpawner.spawnItemByIdForOnline(itemId, i, new Vector3(x, y, z));
                }

                if (onlineItemSpawn.entity == null) {
                    Gdx.app.log("WS", "Unsupported item ID for spawning: " + itemId);
                }

                onlineItem.spawns.add(onlineItemSpawn);
            }
        }
    }

    private void handlePlayerSignedIn(JsonValue root) {
        int signedInID = root.getInt("id");
        Gdx.app.log("WS", "Player " + signedInID + " signed in!");
    }

    private void handlePlayerConnected(JsonValue root) {
        PlayerCoordinatesPacket coords = json.readValue(PlayerCoordinatesPacket.class, root.get("player"));
        Entity companion = playerSpawner.spawnCompanion(coords.id, new Vector3(coords.x, coords.y, coords.z));
        TransformComponent transform = companion.getComponent(TransformComponent.class);
        transform.matrix.rotate(Vector3.Y, coords.angle);
        onlinePlayers.put(coords.id, companion);
    }

    private void handlePlayerUpdate(JsonValue root) {
        PlayerCoordinatesPacket coords = json.readValue(PlayerCoordinatesPacket.class, root.get("player"));
        Entity target = onlinePlayers.get(coords.id);
        if (target != null) {
            Matrix4 rotationMatrix = new Matrix4();
            rotationMatrix.rotate(Vector3.Y, coords.angle);

            TransformComponent transform = target.getComponent(TransformComponent.class);
            Vector3 position = new  Vector3(coords.x, coords.y, coords.z);
            Vector3 scale = transform.matrix.getScale(new Vector3());
            Quaternion rotation = rotationMatrix.getRotation(new Quaternion());

            transform.matrix.set(position, rotation, scale);
        }
    }

    private void handlePlayerChallenged(JsonValue root) {
        int challengerID = root.getInt("challengerID");
        BattlePlayerPacket challengerPacket = json.readValue(BattlePlayerPacket.class, root.get("challenger"));
        challenger = createEnemyFromPacket(challengerPacket);

        PlayerChallengeRequestPacket challengeBack = new PlayerChallengeRequestPacket("player_challenge", challengerID, player.getAsChallenger());
        socket.send(json.toJson(challengeBack));
    }

    private void handlePlayerDisconnected(JsonValue root) {
        int disconnectedID = root.getInt("id");
        Entity removed = onlinePlayers.remove(disconnectedID);
        getEngine().removeEntity(removed);
        if (removed != null) {
            Gdx.app.log("WS", "Player " + disconnectedID + " disconnected!");
        }
    }

    private void handleItemCollectedBroadcast(JsonValue root) {
        int itemId = root.getInt("itemId");
        int spawnIndex = root.getInt("spawnIndex");

        OnlineItem targetItem = onlineItems.get(itemId);
        OnlineItemSpawn targetSpawn = targetItem.spawns.get(spawnIndex);

        targetSpawn.available = false;
        if (targetSpawn.entity != null) {
            getEngine().removeEntity(targetSpawn.entity);
        }
    }

    private void handleItemRespawned(JsonValue root) {
        int itemId = root.getInt("itemId");
        int spawnIndex = root.getInt("spawnIndex");

        OnlineItem targetItem = onlineItems.get(itemId);
        OnlineItemSpawn targetSpawn = targetItem.spawns.get(spawnIndex);

        targetSpawn.available = true;
        targetSpawn.entity = itemSpawner.spawnItemByIdForOnline(itemId, spawnIndex, new Vector3(targetSpawn.position));
    }

    private Enemy createEnemyFromPacket(BattlePlayerPacket packet) {
        Team enemyTeam = new Team();
        for (TeamInvocationPacket p : packet.team) {
            enemyTeam.getMembers().add(new Invocation(new Spirit(0, p.name, p.lastName, "", p.gender, player.getCodex().getFormById(p.spiritFormId))));
        }
        return new Enemy(packet.name, enemyTeam, false);
    }

    private PlayerCoordinatesPacket createPlayerPacketFrom(String type) {
        PlayerCoordinatesPacket packet = new PlayerCoordinatesPacket();
        packet.type = type;
        packet.x = player.getPosition().x;
        packet.y = player.getPosition().y;
        packet.z = player.getPosition().z;
        packet.angle = player.getAngle();
        return packet;
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        if (socket.isClosed()) {
            socket.connect();
        }
    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
        if (socket.isOpen()) {
            socket.close();
        }
    }

    class OnlineItem {
        public List<OnlineItemSpawn> spawns = new ArrayList<>();
    }

    class OnlineItemSpawn {
        public boolean available;
        public Vector3 position;
        public Entity entity;
    }
}
