package nintendont.amongspirits.entities.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.JsonValue;
import nintendont.amongspirits.entities.Entity;
import nintendont.amongspirits.entities.Pokemon;
import nintendont.amongspirits.entities.items.effects.HealEffect;
import nintendont.amongspirits.entities.items.effects.ItemEffect;

public class Consumable extends Item {
    private ItemEffect effect;

    public Consumable(int id, String name, String desc, boolean isMaterial) {
        super(id, name, desc, isMaterial);
        Pixmap temp = new Pixmap(Gdx.files.internal("textures/oranberry.png"));
        this.icon = new Texture(temp);
        temp.dispose();
    }

    public Consumable (JsonValue data){
        super(data);
        if (data.get("effect").getString("type").equals("HEAL")){
            this.effect = new HealEffect(data.get("effect").getInt("value"));
            System.out.println(((HealEffect)this.effect).value);
        }
    }

    @Override
    public void useItem(Entity target){
        effect.apply((Pokemon) target);
    }

    // public String getValue(){
    //     return
    // }
}
