package nintendont.amongspirits.entities.spawners;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Vector3;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneAsset;
import nintendont.amongspirits.data.satchel.Item;
import nintendont.amongspirits.data.satchel.ItemDB;
import nintendont.amongspirits.entities.components.*;

import static nintendont.amongspirits.data.assets.GameAssets.ORAN_BERRY_SCENE;
import static nintendont.amongspirits.data.assets.GameAssets.TUMBLESTONE_SCENE;

public class ItemSpawner {
    private final Engine engine;
    private final AssetManager assets;
    private final ItemDB items;

    public ItemSpawner(Engine engine, AssetManager assetManager, ItemDB items) {
        this.engine = engine;
        this.assets = assetManager;
        this.items = items;
    }

    public Entity spawnItemByIdForOnline(int itemId, int spawnIndex, Vector3 spawnPoint) {
        Entity entity = null;
        switch (itemId) {
            case ItemDB.TUMBLESTONE_ID:
                entity = spawnTumblestone(spawnPoint);
                break;
            case ItemDB.ORAN_BERRY_ID:
                entity = spawnOranBerry(spawnPoint);
                break;
        }

        if (entity == null) return null;

        OnlineItemTagComponent onlineItemTag = engine.createComponent(OnlineItemTagComponent.class);
        onlineItemTag.itemId = itemId;
        onlineItemTag.spawnIndex = spawnIndex;
        entity.add(onlineItemTag);

        return entity;
    }

    public Entity spawnOranBerry(Vector3 spawnPoint) {
        SceneAsset modelAsset = assets.get(ORAN_BERRY_SCENE);
        Item item = items.getItemById(ItemDB.ORAN_BERRY_ID);

        return spawnItem(item, spawnPoint, modelAsset, 0.05f);
    }

    public Entity spawnTumblestone(Vector3 spawnPoint) {
        SceneAsset modelAsset = assets.get(TUMBLESTONE_SCENE);
        Item item = items.getItemById(ItemDB.TUMBLESTONE_ID);

        return spawnItem(item, spawnPoint, modelAsset, 2f);
    }

    public Entity spawnItem(Item item, Vector3 spawnPoint, SceneAsset modelAsset, float modelScale) {
        Entity entity = engine.createEntity();

        ModelComponent model = engine.createComponent(ModelComponent.class);
        model.gltfScene = new Scene(modelAsset.scene);

        TransformComponent transform = engine.createComponent(TransformComponent.class);
        model.gltfScene.modelInstance.transform = transform.matrix;
        transform.matrix.setTranslation(spawnPoint.x, spawnPoint.y, spawnPoint.z);
        transform.matrix.scale(modelScale, modelScale, modelScale);

        ItemTagComponent itemTag = engine.createComponent(ItemTagComponent.class);
        itemTag.item = item;

        entity.add(transform);
        entity.add(model);
        entity.add(itemTag);

        engine.addEntity(entity);
        return entity;
    }
}
