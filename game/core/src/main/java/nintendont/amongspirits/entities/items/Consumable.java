package nintendont.amongspirits.entities.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.JsonValue;
import nintendont.amongspirits.entities.Entity;
import nintendont.amongspirits.entities.items.effects.HealEffect;
import nintendont.amongspirits.entities.items.effects.ItemEffect;

public class Consumable extends Item {
    private ItemEffect effect;

    public Consumable(int id, String name, String desc, boolean isMaterial, AssetManager assets) {
        super(id, name, desc, isMaterial, assets);
        Pixmap temp = new Pixmap(Gdx.files.internal("textures/oranberry.png"));
        this.icon = new Texture(temp);
        temp.dispose();
    }

    public Consumable (JsonValue data, AssetManager assets){
        super(data, assets);
        if (data.get("effect").getString("type").equals("HEAL")){
            this.effect = new HealEffect(data.get("effect").getInt("value"));
            System.out.println(((HealEffect)this.effect).value);
        }
    }

//    @Override
//    public void useItem(Entity target){
//        effect.apply((com.badlogic.ashley.core.Entity) target);
//    }

}
