package nintendont.amongspirits.ui.menu;

import java.util.ArrayList;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MenuOverlay extends Table{
    protected MenuUI menu;
    protected Skin skin;

    protected ArrayList<Actor> actors = new ArrayList<>();
    protected int selected;

    public MenuOverlay (Skin skin, MenuUI menu){
        this.skin = skin;
        this.menu = menu;
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
            default:
                return false;
        }
    }

    protected TextButton createButton(String text, Runnable action) {
        TextButton btn = new TextButton(text, skin){
            @Override
            public boolean isOver(){
                return actors.indexOf(this) == selected;
            }
        };
        btn.setUserObject(action);
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
        actors.add(btn);
        return btn;
    }

    protected void back(){
        menu.setMenu("account");
    }

    protected TextField creaTextField(Skin skin){
        TextField textField = new TextField(null, skin){
            @Override
            protected void updateDisplayText() {
                int cursor = getCursorPosition();
                setText(this.text.replaceAll(" ", ""));
                setCursorPosition(cursor);
                super.updateDisplayText();
            }
        };
        textField.addListener(new ClickListener(){
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                selected = actors.indexOf(textField);
            }
        });
        return textField;
    }
}
