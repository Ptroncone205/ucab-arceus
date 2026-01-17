package nintendont.amongspirits.ui.menu;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import nintendont.amongspirits.data.savedata.BtnEventListener;

public class Options extends Table {
    private Table root;
    private Skin skin;

    private ArrayList<Actor> actors = new ArrayList<>();
    private Actor selectedActor;
    private int selected;
    private TextButton selButton;

    public Options (Skin skin){
        // root = new Table();
        // this.add(root);
        this.skin = skin;
        this.setFillParent(true);
        this.setBackground(skin.newDrawable("white",new Color(Color.BLACK)));
        Label title = new Label("JUEGOTE", skin);
        title.setFontScale(2f);
        
        TextButton btnResume = createButton("New Game", () -> System.out.println("new game"));
        TextButton btnSave = createButton("Load Game", () -> System.out.println("laod game"));
        TextButton btnQuit = createButton("Options", () -> System.out.println("options"));
        
        TextField username = new TextField(null, skin){
            @Override
            protected void updateDisplayText() {
                int cursor = getCursorPosition();
                setText(this.text.replaceAll(" ", ""));
                setCursorPosition(cursor);
                System.out.println(this.text);
                super.updateDisplayText();
            }
        };
        username.addListener(new ClickListener(){
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                selected = actors.indexOf(username);
            }
        });
        username.setSize(200, 40);
        actors.add(username);


        this.center();
        this.add(title).padBottom(50).row();
        this.add(username).row();

        this.add(btnResume).width(200).height(50).padBottom(15).row();
        this.add(btnSave).width(200).height(50).padBottom(15).row();
        this.add(btnQuit).width(200).height(50).padBottom(15).row();
    }


    private TextButton createButton(String text, Runnable action) {
        TextButton btn = new TextButton(text, skin){
            @Override
            public boolean isOver(){
                return actors.indexOf(this) == selected;
            } 
        };
        btn.setUserObject(action);
        actors.add(btn);
        btn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                action.run();
            }
        });
        btn.addListener(new ClickListener(){
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                selected = actors.indexOf(btn);
            }
        });
        return btn;
    }

    public void onClick(){
        selButton = (TextButton)actors.get(selected);
        Runnable action = (Runnable)selButton.getUserObject();
        action.run();
    }

    public boolean handleInput (int key){
        switch (key){
            case Keys.W:
                selected -= 1;
                if (selected < 0) selected = actors.size() - 1;
                return true;
            case Keys.S:
                selected += 1;
                if (selected >= actors.size()) selected = 0;
                return true;
            case Keys.ENTER:
            case Keys.SPACE:
                Actor a = actors.get(selected);
                if (a instanceof TextButton) onClick();
                return true;
            default:
                return false;
        }
    }
}
