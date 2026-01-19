package nintendont.amongspirits.ui.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class Account extends MenuOverlay {
    public Account (Skin skin, MenuUI menu){
        super(skin, menu);

        this.setFillParent(true);


        this.setBackground(skin.newDrawable(new TextureRegionDrawable(new Texture(new Pixmap(Gdx.files.internal("textures/menu/arceus.png"))))));
        Label title = new Label("Usuario", skin);
        title.setFontScale(2f);

        TextField username = creaTextField(skin);
        username.setSize(200, 40);

        TextButton btnResume = createButton("New Game", () ->{menu.getGame().playMusic("", true);

            menu.newGame();
        });

        TextButton btnSave = createButton("Load Game", menu::loadGame);
        TextButton btnQuit = createButton("Options", ()->menu.setMenu("options"));
        TextButton btnHelp = createButton("Help", ()->menu.setMenu("help"));


        Table topT = new Table();
        topT.add(title).padBottom(50).padRight(20);
        topT.add(username).padBottom(50);

        Table botT = new Table();
        botT.add(btnResume).width(200).height(50).padBottom(15).row();
        botT.add(btnSave).width(200).height(50).padBottom(15).row();
        botT.add(btnQuit).width(200).height(50).padBottom(15).row();
        botT.add(btnHelp).width(200).height(50).padBottom(15).row();

        this.add(topT).row();
        this.add(botT).row();

        actors.add(username);
        actors.add(btnResume);
        actors.add(btnSave);
        actors.add(btnQuit);
        actors.add(btnHelp);

        this.setDebug(true);
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
                Actor a = actors.get(selected);
                if (a instanceof TextButton) onClick((TextButton)a);
                return true;
            default:
                return super.handleInput(key);
        }
    }
}
