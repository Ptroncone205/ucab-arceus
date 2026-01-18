package nintendont.amongspirits.ui.menu;

import java.util.ArrayList;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class Options extends MenuOverlay {
    private Skin skin;

    private ArrayList<Actor> actors = new ArrayList<>();
    private Actor selectedActor;
    private int selected;
    private TextButton selButton;

    public Options (Skin skin, MenuUI menu){
        super(skin, menu);

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

        this.center();
        this.add(title).padBottom(50).row();
        this.add(username).row();

        this.add(btnResume).width(200).height(50).padBottom(15).row();
        this.add(btnSave).width(200).height(50).padBottom(15).row();
        this.add(btnQuit).width(200).height(50).padBottom(15).row();

        TextButton btnBack = createButton("Back", this::back);
        this.add(btnBack).width(200).height(50).padBottom(15).row();

        actors.add(username);
        actors.add(btnResume);
        actors.add(btnSave);
        actors.add(btnQuit);
        actors.add(btnBack);

        this.setDebug(true);
    }

    public void onClick(){
        selButton = (TextButton)actors.get(selected);
        Runnable action = (Runnable)selButton.getUserObject();
        action.run();
    }

    @Override
    public boolean handleInput (int key){
        switch (key){
            case Keys.ENTER:
            case Keys.SPACE:
                Actor a = actors.get(selected);
                if (a instanceof TextButton) onClick();
                return true;
            default:
                return super.handleInput(key);
        }
    }
}
