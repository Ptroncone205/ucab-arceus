package nintendont.amongspirits.entities.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.JsonValue;

public class MaterialItem extends Item{
    public MaterialItem(int id, String name, String desc, AssetManager assets) {
        super(id, name, desc, true, assets);
        Pixmap temp = new Pixmap(Gdx.files.internal("textures/tumblestone.png"));
        this.icon = new Texture(temp);
        temp.dispose();
    }
    public MaterialItem(JsonValue data, AssetManager assets){
        super(data, assets);
    }

}
