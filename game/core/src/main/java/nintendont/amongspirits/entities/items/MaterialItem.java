package nintendont.amongspirits.entities.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

public class MaterialItem extends Item{
    public MaterialItem(int id, String name, String desc) {
        super(id, name, desc, true);
        Pixmap temp = new Pixmap(Gdx.files.internal("textures/tumblestone.png"));
        this.icon = new Texture(temp);
        temp.dispose();
    }

}
