package nintendont.amongspirits.ui.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class PlayMenu extends MenuOverlay {
    public PlayMenu(Skin skin, MenuUI menu){
        super(skin, menu);

        this.setFillParent(true);


        this.setBackground(skin.newDrawable(new TextureRegionDrawable(new Texture(new Pixmap(Gdx.files.internal("textures/menu/arceus.png"))))));

        TextButton btnSave = createButton("Play", menu::loadGame);
        TextButton btnQuit = createButton("Options", ()->menu.setMenu("options"));
        TextButton btnHelp = createButton("Help", ()->menu.setMenu("help"));
        TextButton btnBack = createButton("Back", ()->menu.setMenu("account"));


        Table botT = new Table();
        botT.add(btnSave).width(200).height(50).padBottom(15).row();
        botT.add(btnQuit).width(200).height(50).padBottom(15).row();
        botT.add(btnHelp).width(200).height(50).padBottom(15).row();
        botT.add(btnBack).width(200).height(50).padBottom(15).row();

        this.add(botT).row();

        actores.add(btnSave);
        actores.add(btnQuit);
        actores.add(btnHelp);
        actores.add(btnBack);

    }

    public void onClick(TextButton a){
        Runnable action = (Runnable)a.getUserObject();
        action.run();
    }

    @Override
    public boolean handleInput (int key){
        switch (key){
            case Keys.ENTER:
            case Keys.SPACE:
                Actor a = actores.get(selected);
                if (a instanceof TextButton) onClick((TextButton)a);
                return true;
            default:
                return super.handleInput(key);
        }
    }
}
