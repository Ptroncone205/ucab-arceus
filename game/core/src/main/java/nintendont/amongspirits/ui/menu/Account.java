package nintendont.amongspirits.ui.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class Account extends  MenuOverlay{
    public Account(Skin skin, MenuUI menu) {
        super(skin, menu);

        this.setFillParent(true);


        this.setBackground(skin.newDrawable(new TextureRegionDrawable(new Texture(new Pixmap(Gdx.files.internal("textures/menu/arceus.png"))))));
        Label title = new Label("Usuario", skin);
        title.setFontScale(2f);

        TextField username = creaTextField(skin);
        username.setSize(200, 40);

        TextButton btnLogIn = createButton("Log In", ()->{if(!menu.getPlayerName().isBlank())menu.setMenu("play");});

        Table topT = new Table();
        topT.add(title).padBottom(50).padRight(20);
        topT.add(username).padBottom(50).row();

        Table botT = new Table();
        botT.add(btnLogIn).width(200).height(50).padBottom(15).row();

        this.add(topT).row();
        this.add(botT).row();
    }
}
