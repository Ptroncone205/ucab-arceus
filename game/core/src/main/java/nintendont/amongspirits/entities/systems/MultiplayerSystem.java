package nintendont.amongspirits.entities.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
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
import nintendont.amongspirits.entities.spawners.PlayerSpawner;
import nintendont.amongspirits.screens.BattleScreen;

import java.util.HashMap;

public class MultiplayerSystem extends EntitySystem {
    private final Json json = new Json();
    private final Main game;
    private final Player player;
    private final PlayerSpawner playerSpawner;
    private final WebSocket socket;
    private final HashMap<Integer, Entity> onlinePlayers = new HashMap<>();
    private Enemy challenger = null;

    public MultiplayerSystem(
        Main game,
        WebSocket socket,
        Player player,
        PlayerSpawner playerSpawner
    ) {
        this.game = game;
        this.socket = socket;
        this.player = player;
        this.playerSpawner = playerSpawner;

        json.setOutputType(JsonWriter.OutputType.json);

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
        socket.send(json.toJson(packet));
    }

    private void handleWorldSetup(JsonValue root) {
        for (JsonValue player : root.get("players")) {
            PlayerCoordinatesPacket coords = json.readValue(PlayerCoordinatesPacket.class, player);
            Entity companion = playerSpawner.spawnCompanion(coords.id, new Vector3(coords.x, coords.y, coords.z));
            onlinePlayers.put(coords.id, companion);
        }
    }

    private void handlePlayerSignedIn(JsonValue root) {
        int signedInID = root.getInt("id");
        Gdx.app.log("WS", "Player " + signedInID + " signed in!");
    }

    private void handlePlayerConnected(JsonValue root) {
        PlayerCoordinatesPacket coords = json.readValue(PlayerCoordinatesPacket.class, root.get("player"));
        Entity companion = playerSpawner.spawnCompanion(coords.id, new Vector3(coords.x, coords.y, coords.z));
        onlinePlayers.put(coords.id, companion);
    }

    private void handlePlayerUpdate(JsonValue root) {
        PlayerCoordinatesPacket coords = json.readValue(PlayerCoordinatesPacket.class, root.get("player"));
        Entity target = onlinePlayers.get(coords.id);
        if (target != null) {
            TransformComponent transform = target.getComponent(TransformComponent.class);
            transform.matrix.setTranslation(coords.x, coords.y, coords.z);
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
        packet.x = player.playerPos.x;
        packet.y = player.playerPos.y;
        packet.z = player.playerPos.z;
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
}
