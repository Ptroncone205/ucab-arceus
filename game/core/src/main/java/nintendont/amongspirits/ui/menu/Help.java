package nintendont.amongspirits.ui.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class Help extends MenuOverlay{

    public Help (Skin skin, MenuUI menu){
        super(skin, menu);

        this.setFillParent(true);
        this.setBackground(skin.newDrawable(new TextureRegionDrawable(new Texture(new Pixmap(Gdx.files.internal("textures/menu/arceus.png"))))));
        Label title = new Label("AYUDA", skin);
        title.setFontScale(2f);
        
        TextButton btnBack = createButton("Back", this::back);
        TextArea h1 = new TextArea("Usa WASD para moverte", skin);
        h1.setSize(200, 200);

        this.center();
        this.add(title).padBottom(50).row();
        this.add(h1).row();

        this.add(btnBack).width(200).height(50).padBottom(15).row();
        this.setDebug(true);
    }

    @Override
    public boolean handleInput(int key) {
        return super.handleInput(key);
    }
}
