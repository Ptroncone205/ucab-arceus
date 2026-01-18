package nintendont.amongspirits.ui.menu;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import nintendont.amongspirits.Const;
import nintendont.amongspirits.Main;
import nintendont.amongspirits.data.savedata.BtnEventListener;

public class MenuUI extends InputAdapter implements Disposable {

    private BtnEventListener saveListener;

    private Main game;
    public Stage stage;

    private Skin skin;
    private HashMap<String, MenuOverlay> tables = new HashMap<>();
    private MenuOverlay currentMenu;
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

        // MainMenuUI menu = new MainMenuUI(skin, this);
        Help help = new Help(skin, this);
        Options options =new Options(skin, this);
        Account account = new Account(skin, this);

        // addMenu("menu", menu);
        addMenu("help", help);
        addMenu("account", account);
        addMenu("options", options);
        setMenu("account");
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
        textFieldStyle.messageFontColor = new Color(0,0,0, 1);
        textFieldStyle.cursor = skin.newDrawable("white", new Color(0.2f, 0.8f, 0.9f, 1));
        textFieldStyle.font = skin.getFont("default");
        textFieldStyle.fontColor = new Color(0,0,0,1f);
        skin.add("default", textFieldStyle);
    }


    public void newGame(){
        game.newGame(playerName, false);
    }

    public void loadGame(){
        if (saveListener != null) saveListener.onLoadRequest();
    }

    public void setMenu(String name){
        hideAll();
        currentMenu = tables.get(name);
        currentMenu.setVisible(true);
        System.out.println("changed to " + name);
    }

    public void onClick(){
        selButton = (TextButton)buttons.get(selected);
        Runnable action = (Runnable)selButton.getUserObject();
        action.run();
    }

    public boolean handleInput (int key){
        return currentMenu.handleInput(key);
    }

    public void setBtnListener(BtnEventListener listener){
        this.saveListener = listener;
    }

    public void addMenu (String name, MenuOverlay t){
        stage.addActor(t);
        tables.put(name, t);
        t.setVisible(false);
    }

    public void hideAll(){
        tables.forEach((K,V)-> V.setVisible(false)); //RAAAAAAAAAAAAAAAAAAAHHHHHHHHHHHHHHHHHHHHH
    }

    @Override
    public void dispose(){
        skin.dispose();
        stage.dispose();
    }
}
