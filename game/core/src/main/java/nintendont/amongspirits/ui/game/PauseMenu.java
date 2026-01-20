package nintendont.amongspirits.ui.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import nintendont.amongspirits.Const;
import nintendont.amongspirits.Const.GameState;

public class PauseMenu extends MenuTable {

    private BtnEventListener saveListener;

    private Skin skin;
    private ArrayList<TextButton> buttons = new ArrayList<>();
    private int selected = 0;
    private Button selButton;

    public PauseMenu(Skin skin, GUIManager gui) {
        super(gui);
        this.skin = skin;

        this.setFillParent(true);

        this.setBackground(skin.newDrawable("white", 0,0,0, 0.85f));

        create();
    }

    private void create() {
        // Title
        Label title = new Label("PAUSED", skin);
        title.setFontScale(2f);

        // Buttons
        TextButton btnResume = createButton("Resume", this::onResume);
        TextButton btnSave = createButton("Save Game", this::onSave);
        TextButton btnQuit = createButton("Quit to Title", this::onQuit);

        // Layout
        this.center();
        this.add(title).padBottom(50).row();

        this.add(btnResume).width(200).height(50).padBottom(15).row();
        this.add(btnSave).width(200).height(50).padBottom(15).row();
        this.add(btnQuit).width(200).height(50).padBottom(15).row();

    }

    private TextButton createButton(String text, Runnable action) {
        TextButton btn = new TextButton(text, skin){
            @Override
            public boolean isOver(){
                return buttons.indexOf(this) == selected;
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
                selected = buttons.indexOf(btn);
            }
        });
        buttons.add(btn);
        return btn;
    }


    public void onResume(){
        gui.goBack();
    }

    public void onSave(){
        if (saveListener != null) saveListener.onSaveRequest();
    }

    public void onQuit(){
        if (saveListener != null) saveListener.onQuitRequest();
    }

    public void onClick(){
        selButton = buttons.get(selected);
        Runnable action = (Runnable)selButton.getUserObject();
        action.run();
    }

    public boolean handleInput (int key){
        switch (key){
            case Keys.W:
                selected -= 1;
                if (selected < 0) selected = buttons.size() - 1;
                return true;
            case Keys.S:
                selected += 1;
                if (selected >= buttons.size()) selected = 0;
                return true;
            case Keys.ENTER:
            case Keys.SPACE:
                onClick();
                return true;
            default:
                return false;
        }
    }

    public void setSaveListener(BtnEventListener lsitener){
        this.saveListener = lsitener;
    }
}
