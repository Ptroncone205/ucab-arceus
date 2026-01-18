package nintendont.amongspirits.ui.menu;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
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
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import nintendont.amongspirits.Const;
import nintendont.amongspirits.Main;
import nintendont.amongspirits.data.savedata.BtnEventListener;

/**
 * literalmente una copia de gui manager + pausemenu
 */
public class MenuUI extends InputAdapter implements Disposable {

    private BtnEventListener saveListener;

    private Main game;
    public Stage stage;
    private Table root;
    private Skin skin;
    private HashMap<String, Table> tables = new HashMap<>();
    private Table currentMenu;
    private ArrayList<Actor> buttons = new ArrayList<>();
    private int selected = 0;
    private TextButton selButton;
    private String playerName = "";

    public MenuUI(Main game) {
        this.game = game;
        this.stage = new Stage(new ScreenViewport(), Const.get().spriteBatch){
            @Override
            public boolean keyDown(int key){
                switch (key) {
                    case Keys.ESCAPE:
                    case Keys.ENTER:
                        Actor a = getKeyboardFocus();
                        if (a instanceof TextField){
                            playerName = ((TextField)a).getText();
                            System.out.println(playerName);
                            unfocusAll();
                            return true;
                        }
                    default:
                        return super.keyDown(key);
                }
            }
        };
        createSkin();

        create();
    }

    public String getPlayerName(){
        return playerName;
    }

    public void render(float delta){
        stage.act(delta);
        stage.draw();
    }

    private void create() {
        root = new Table();
        root.setFillParent(true);
        root.setBackground(skin.newDrawable(new TextureRegionDrawable(new Texture(new Pixmap(Gdx.files.internal("textures/menu/arceus.png"))))));
        Label title = new Label("JUEGUITO", skin);
        title.setFontScale(2f);

        Options options =new Options(skin);
        addMenu("options", options);
        Account account = new Account(skin);
        addMenu("account", account);
        
        TextButton btnResume = createButton("New Game", this::newGame);
        TextButton btnSave = createButton("Load Game", this::loadGame);
        TextButton btnQuit = createButton("Options", this::options);
        
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
                selected = buttons.indexOf(username);
            }
        });
        username.setSize(200, 40);
        buttons.add(username);


        root.center();
        root.add(title).padBottom(50).row();
        root.add(username).row();

        root.add(btnResume).width(200).height(50).padBottom(15).row();
        root.add(btnSave).width(200).height(50).padBottom(15).row();
        root.add(btnQuit).width(200).height(50).padBottom(15).row();

        addMenu("root", root);
        setMenu("root");
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

        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.background = skin.newDrawable("white", new Color(1,1,1,0.6f));
        textFieldStyle.selection = skin.newDrawable("white", new Color(49f/255f, 142f/255f, 148f/255f, 1));
        textFieldStyle.focusedBackground = skin.newDrawable("white", new Color(1,1,1,0.8f));
        textFieldStyle.messageFont= skin.getFont("default");
        textFieldStyle.messageFontColor = new Color(49f/255f, 142f/255f, 148f/255f, 1);
        textFieldStyle.cursor = skin.newDrawable("white", new Color(0.2f, 0.8f, 0.9f, 1));
        textFieldStyle.font = skin.getFont("default");
        textFieldStyle.fontColor = new Color(0,0,0,1f);
        skin.add("default", textFieldStyle);
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
        game.newGame(playerName, false);
    }

    public void loadGame(){
        if (saveListener != null) saveListener.onLoadRequest();
    }

    public void options(){
        setMenu("options");
    }

    public void setMenu(String name){
        hideAll();
        tables.get(name).setVisible(true);
        System.out.println("changed to " + name);
    }
    public void onClick(){
        selButton = (TextButton)buttons.get(selected);
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
                Actor a = buttons.get(selected);
                if (a instanceof TextField){
                    stage.setKeyboardFocus(a);
                } else {
                    onClick();
                }
                return true;
            default:
                return false;
        }
    }

    public void setBtnListener(BtnEventListener listener){
        this.saveListener = listener;
    }

    public void addMenu (String name, Table t){
        stage.addActor(t);
        tables.put(name, t);
        t.setVisible(false);
    }

    public void hideAll(){
        tables.forEach((K,V)-> V.setVisible(false));
    }

    @Override
    public void dispose(){
        skin.dispose();
        stage.dispose();
    }
}