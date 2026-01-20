package nintendont.amongspirits.ui.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import nintendont.amongspirits.Const;
import nintendont.amongspirits.Const.GameState;
import nintendont.amongspirits.data.codex.Codex;
import nintendont.amongspirits.data.codex.CodexPreviewAssets;
import nintendont.amongspirits.entities.Player;
import nintendont.amongspirits.managers.CraftManager;
import nintendont.amongspirits.ui.codex.CodexMainUI;
import nintendont.amongspirits.utils.AssetUtils;

import java.util.HashMap;
import java.util.Stack;

public class GUIManager implements Disposable{
    public Stage stage;
    private Player player;
    private Skin skin;
    private HashMap<String, MenuTable> tables = new HashMap<>();
    private Stack<MenuTable> stack = new Stack<>();
    private Codex codex;
    private AssetManager assets;
    private CodexMainUI codexUI;

    public GUIManager (AssetManager assets, SpriteBatch batch, CraftManager craftManager, Player player, Codex codex){
        stage =new Stage(new ScreenViewport(), batch){
            @Override
            public boolean keyDown(int keyCode) {
                super.keyDown(keyCode);
                return false;
            }
        };
        this.player = player;
        this.codex = codex;
        this.assets = assets;
        createSkin();

        TeamMenu teamMenu = new TeamMenu(skin,this, assets);
        PastureUI pastureUI = new PastureUI(skin, player.getPasture(),teamMenu, this, assets);
        codexUI = new CodexMainUI(assets, codex, skin, this);

        InventoryMenu inventoryMenu = new InventoryMenu(player.getSatchel(), craftManager, skin, this, assets);
        PauseMenu pauseMenu = new PauseMenu(skin, this);

        stage.addActor(codexUI);
        stage.addActor(pauseMenu);
        stage.addActor(inventoryMenu);
        stage.addActor(pastureUI);

        tables.put("codex", codexUI);
        tables.put("pause", pauseMenu);
        tables.put("satchel", inventoryMenu);
        tables.put("pasture", pastureUI);

        hideAll();

    }

    public void addMenu(String name, MenuTable menu){
        tables.put(name, menu);
    }

    public void render(float delta){
        stage.act(delta);
        stage.draw();
    }

    public void update(){
        tables.forEach((K,V)->V.update());
    }

    public void openMenu(String name){
        if (Const.currentState != GameState.INGAME) return;
        MenuTable menu = tables.get(name);
        stack.push(menu);
        menu.setVisible(true);
        updateState();
        update();
    }

    public void toggleMenu(String name) {
        if (!stack.isEmpty() && stack.peek() == tables.get(name)) {
            goBack();
            return;
        }
        openMenu(name);
    }

    public void goBack(){
        stage.unfocusAll();
        if (stack.isEmpty()){
            openMenu("pause");
            return;
        }
        stack.pop().setVisible(false);
        updateState();
    }

    private void updateState() {
        if (stack.isEmpty()) {
            Const.currentState = Const.GameState.INGAME;
            Gdx.input.setCursorCatched(true);
            return;
        }
        Const.currentState = Const.GameState.PAUSE;
        Gdx.input.setCursorCatched(false);
    }
    public void hideAll(){
        tables.forEach((K,V)->V.setVisible(false));
        stack.clear();
        Const.currentState = GameState.INGAME;
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

        Button.ButtonStyle buttonStyle = new Button.ButtonStyle();
        buttonStyle.up = skin.newDrawable("white", new Color(0.4f, 0.4f, 0.4f, 1));
        buttonStyle.down = skin.newDrawable("white", new Color(49f/255f, 142f/255f, 148f/255f, 1));
        buttonStyle.over = skin.newDrawable("white", new Color(0.2f, 0.8f, 0.9f, 1));
        skin.add("default", buttonStyle);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("white", new Color(0.4f, 0.4f, 0.4f, 1));
        textButtonStyle.down = skin.newDrawable("white", new Color(49f/255f, 142f/255f, 148f/255f, 1));
        textButtonStyle.over = skin.newDrawable("white", new Color(0.2f, 0.8f, 0.9f, 1));
        textButtonStyle.font = skin.getFont("default");
        skin.add("default", textButtonStyle);

        ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle();
        scrollPaneStyle.vScroll = skin.newDrawable("white",Color.GRAY);
        scrollPaneStyle.vScrollKnob = skin.newDrawable("white", Color.LIGHT_GRAY);
        skin.add("default", scrollPaneStyle);
    }

    @Override
    public void dispose() {
        skin.dispose();
        stage.dispose();
    }

    public boolean handleInput (int keycode) {
        // esc highest priority
        if (keycode == Input.Keys.ESCAPE) {
            goBack(); return true;
        }
        // then menu specific inputs
        if (!stack.isEmpty() && stack.peek().handleInput(keycode)){
            return true;
        }
        // then hotkeys
        switch (keycode){
            case Input.Keys.TAB:
                toggleMenu("satchel");
                return true;
            case Input.Keys.P:
                toggleMenu("pasture");
                return true;
            case Input.Keys.C:
                Actor cmenu = tables.get("codex").findActor("codex_menu");
                toggleMenu("codex");
                return true;
            default:
                return false;
        }
    }
    public <T extends MenuTable> T getMenu(String name){
        return (T) tables.get(name);
    }
    public Player getPlayer(){
        return player;
    }
}
