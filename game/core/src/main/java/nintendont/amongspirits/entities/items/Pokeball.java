package nintendont.amongspirits.entities.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.JsonValue;
import nintendont.amongspirits.entities.Entity;

public class Pokeball extends Item{
    public Pokeball(int id, String name, String desc, boolean isMaterial) {
        super(id, name, desc, isMaterial);
        Pixmap temp = new Pixmap(Gdx.files.internal("textures/pokeball.png"));
        this.icon = new Texture(temp);
        temp.dispose();
    }
    public Pokeball (JsonValue data){
        super(data);
    }
    @Override
    public void useItem(Entity target){
        // TODO debe contnener la logica de atrapar pokemon
    }
}
