package nintendont.amongspirits.entities.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.github.czyzby.websocket.WebSocket;
import nintendont.amongspirits.data.online.packets.ItemCollectedPacket;
import nintendont.amongspirits.entities.Player;
import nintendont.amongspirits.entities.components.ItemTagComponent;
import nintendont.amongspirits.entities.components.OnlineItemTagComponent;
import nintendont.amongspirits.entities.components.TransformComponent;
import nintendont.amongspirits.ui.game.GUIManager;

import java.util.Optional;

public class ItemSystem extends IteratingSystem {
    public static final float INTERACTION_DIST = 10f;
    private static final ComponentMapper<TransformComponent> transformMapper = ComponentMapper.getFor(TransformComponent.class);
    private static final ComponentMapper<ItemTagComponent> itemTagMapper = ComponentMapper.getFor(ItemTagComponent.class);
    private static final ComponentMapper<OnlineItemTagComponent> onlineItemTagMapper = ComponentMapper.getFor(OnlineItemTagComponent.class);

    private final Json json = new Json();
    private final Player player;
    private final Camera camera;
    private final WebSocket socket;
    private final Vector2 screenCenter = new Vector2();
    private Entity focusedItemEntity;
    private float currentBestDistance;

    public ItemSystem(Player player, Camera camera, GUIManager guiManager, InputMultiplexer input, WebSocket socket) {
        super(Family.all(TransformComponent.class, ItemTagComponent.class).get());
        this.player = player;
        this.camera = camera;
        this.socket = socket;
        json.setOutputType(JsonWriter.OutputType.json);
        json.setUsePrototypes(false);
        input.addProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.F && focusedItemEntity != null) {
                    ItemTagComponent itemTag = itemTagMapper.get(focusedItemEntity);
                    if (player.getSatchel().addItem(itemTag.item)){
                        guiManager.update();
                        if (socket.isOpen()) {
                            OnlineItemTagComponent onlineItemTag = onlineItemTagMapper.get(focusedItemEntity);
                            ItemCollectedPacket itemCollectedPacket = new ItemCollectedPacket("item_collected", onlineItemTag.itemId, onlineItemTag.spawnIndex);
                            socket.send(json.toJson(itemCollectedPacket));
                        };
                        getEngine().removeEntity(focusedItemEntity);
                    }
                }
                return super.keyDown(keycode);
            }
        });
    }

    @Override
    public void update(float deltaTime) {
        player.setFocusedItemPosition(Optional.empty());
        screenCenter.set(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);
        focusedItemEntity = null;
        currentBestDistance = Float.MAX_VALUE;
        super.update(deltaTime);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        TransformComponent transform = transformMapper.get(entity);
        Vector3 itemPos = new Vector3();
        transform.matrix.getTranslation(itemPos);

        float distSq = player.playerPos.dst2(itemPos);
        if (distSq > INTERACTION_DIST) return;

        Vector3 screenPos = new Vector3(itemPos);
        camera.project(screenPos);

        boolean onScreenX = screenPos.x > 0 && screenPos.x < Gdx.graphics.getWidth();
        boolean onScreenY = screenPos.y > 0 && screenPos.y < Gdx.graphics.getHeight();
        boolean inFront = screenPos.z < 1.0f;

        if (onScreenX && onScreenY && inFront) {

            float distFromCenter = Math.abs(screenPos.x - screenCenter.x) + Math.abs(screenPos.y - screenCenter.y);

            // prioritize item closest to the center of the screen
            if (distFromCenter < currentBestDistance) {
                currentBestDistance = distFromCenter;
                focusedItemEntity = entity;
                player.setFocusedItemPosition(Optional.of(itemPos));
            }
        }
    }
}
