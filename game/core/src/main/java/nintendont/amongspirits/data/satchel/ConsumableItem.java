package nintendont.amongspirits.data.satchel;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.Texture;
import nintendont.amongspirits.data.satchel.effects.ConsumableEffect;

public class ConsumableItem extends Item {
    private ConsumableEffect effect;

    public ConsumableItem(int id, String name, String description, boolean isMaterial, AssetDescriptor<Texture> iconAsset, ConsumableEffect effect) {
        super(id, name, description, isMaterial, iconAsset);
        this.effect = effect;
    }

    public ConsumableEffect getEffect() {
        return effect;
    }
}
