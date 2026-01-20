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

    protected ArrayList<Actor> actores = new ArrayList<>();
    protected int selected;

    public MenuOverlay (Skin skin, MenuUI menu){
        this.skin = skin;
        this.menu = menu;
    }

    public boolean handleInput (int key){
        switch (key){
            case Keys.W:
                selected -= 1;
                if (selected < 0) selected = actores.size() - 1;
                menu.getGame().playSound("music and sounds/sounds/button_sel.mp3");
                return true;
            case Keys.S:
                selected += 1;
                if (selected >= actores.size()) selected = 0;
                menu.getGame().playSound("music and sounds/sounds/button_sel.mp3");
                return true;
            default:
                return false;
        }
    }

    protected TextButton createButton(String text, Runnable action) {
        TextButton btn = new TextButton(text, skin){
            @Override
            public boolean isOver(){
                return actores.indexOf(this) == selected;
            }
        };
        btn.setUserObject(action);

        btn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                menu.getGame().playSound("music and sounds/sounds/button_pressed.mp3");
                action.run();
            }
        });

        btn.addListener(new ClickListener(){
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1 && selected != actores.indexOf(btn)) {
                    selected = actores.indexOf(btn);
                    menu.getGame().playSound("music and sounds/sounds/button_sel.mp3");
                }
            }
        });
        actores.add(btn);
        return btn;
    }

    protected void back(){
        menu.setMenu("play");
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
                selected = actores.indexOf(textField);
            }
        });
        return textField;
    }
}
