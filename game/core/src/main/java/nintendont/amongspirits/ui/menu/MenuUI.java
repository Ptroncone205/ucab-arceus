package nintendont.amongspirits.ui.menu;

import java.util.ArrayList;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import nintendont.amongspirits.Const;
import nintendont.amongspirits.Main;
import nintendont.amongspirits.Const.GameState;
import nintendont.amongspirits.data.savedata.BtnEventListener;

/**
 * literalmente una copia de gui manager + pausemenu
 */
public class MenuUI implements Disposable {

    private BtnEventListener saveListener;

    private Main game;
    public Stage stage;
    private Table root;
    private Table options;
    private Table account;
    private Skin skin;
    private ArrayList<Table> tables = new ArrayList<>();
    private ArrayList<TextButton> buttons = new ArrayList<>();
    private int selected = 0;
    private TextButton selButton;
    private String text;

    public MenuUI(Main game) {
        this.text = "";
        this.game = game;
        this.stage = new Stage(new ScreenViewport(), Const.get().spriteBatch);
        createSkin();

        create();
    }

    public void render(float delta){
        stage.act(delta);
        stage.draw();
    }

    private void create() {
        root = new Table();
        root.setFillParent(true);
        root.setBackground(skin.newDrawable("white", 0,0,0, 1f));
        
        Label title = new Label(text, skin);
        title.setFontScale(2f);
        
        TextButton btnResume = createButton("New Game", this::newGame);
        TextButton btnSave = createButton("Load Game", this::loadGame);
        TextButton btnQuit = createButton("Options", this::options);

        root.center();
        root.add(title).padBottom(50).row();
        
        root.add(btnResume).width(200).height(50).padBottom(15).row();
        root.add(btnSave).width(200).height(50).padBottom(15).row();
        root.add(btnQuit).width(200).height(50).padBottom(15).row();

        stage.addActor(root);
    }

    private void createSkin() {
        skin = new Skin();

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("white", new Texture(pixmap));

        skin.add("default", new BitmapFont());

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = skin.getFont("default");
        skin.add("default", labelStyle);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("white", new Color(0.4f, 0.4f, 0.4f, 1));
        textButtonStyle.down = skin.newDrawable("white", new Color(49f/255f, 142f/255f, 148f/255f, 1));
        textButtonStyle.over = skin.newDrawable("white", new Color(0.2f, 0.8f, 0.9f, 1));
        textButtonStyle.font = skin.getFont("default");
        skin.add("default", textButtonStyle);
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


    public void newGame(){
        game.newGame();
    }

    public void loadGame(){
        if (saveListener != null) saveListener.onLoadRequest();
    }

    public void options(){
        // TODO me doy cuenta que debo separar menuui del menu prinicpal
        // y el menu de opciones
        // es como guimanager 2 en todos los sentidos
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

    public void setBtnListener(BtnEventListener listener){
        this.saveListener = listener;
    }

    @Override
    public void dispose(){
        skin.dispose();
        stage.dispose();
    }
}