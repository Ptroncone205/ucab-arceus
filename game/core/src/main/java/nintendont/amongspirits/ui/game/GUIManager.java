package nintendont.amongspirits.ui.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import nintendont.amongspirits.Const;
import nintendont.amongspirits.Const.GameState;
import nintendont.amongspirits.data.codex.Codex;
import nintendont.amongspirits.data.codex.CodexPreviewAssets;
import nintendont.amongspirits.data.spirits.Pasture;
import nintendont.amongspirits.entities.Player;
import nintendont.amongspirits.managers.CraftManager;
import nintendont.amongspirits.managers.Satchel;
import nintendont.amongspirits.ui.codex.CodexMainUI;
import nintendont.amongspirits.ui.menu.MenuOverlay;
import nintendont.amongspirits.utils.AssetUtils;

import java.util.HashMap;

public class GUIManager implements Disposable{
    private AssetManager assetManager = Const.get().assetManager;
    public Stage stage;
    private CodexMainUI codexUI;
    private Codex codex;
    private InventoryMenu inventoryMenu;
    private Skin skin;
    private PauseMenu pauseMenu;
    private PastureUI pastureUI;


    public GUIManager (SpriteBatch batch, CraftManager craftManager, Player player, Codex codex){
        stage =new Stage(new ScreenViewport(), batch);
        this.codex = codex;
        assetManager.load("sfx/ui/open_page_foley.ogg", Sound.class);
        assetManager.load("sprites/icons/lion.png", Texture.class);
        assetManager.load("sprites/backgrounds/codex-scroll.png", Texture.class);
        assetManager.load(CodexPreviewAssets.DEER);
        assetManager.load(CodexPreviewAssets.WOLF);
        assetManager.load(CodexPreviewAssets.BUNNY);
        assetManager.load(CodexPreviewAssets.FOX);
        assetManager.load(CodexPreviewAssets.LION);

        AssetUtils.setTrueTypeFontLoaders(assetManager);
        AssetUtils.loadFont(assetManager, "roboto_xs.ttf", "fonts/roboto.ttf", 12);
        AssetUtils.loadFont(assetManager, "roboto_sm.ttf", "fonts/roboto.ttf", 14);
        AssetUtils.loadFont(assetManager, "roboto_base.ttf", "fonts/roboto.ttf", 16);
        AssetUtils.loadFont(assetManager, "roboto_lg.ttf", "fonts/roboto.ttf", 18);
        AssetUtils.loadFont(assetManager, "roboto_xl.ttf", "fonts/roboto.ttf", 20);
        AssetUtils.loadFont(assetManager, "roboto_2xl.ttf", "fonts/roboto.ttf", 24);
        AssetUtils.loadFont(assetManager, "chinese_8xl.ttf", "fonts/chinese_takeaway.ttf", 96);
        AssetUtils.loadFont(assetManager, "chinese_9xl.ttf", "fonts/chinese_takeaway.ttf", 128);
        assetManager.finishLoading();

        createSkin();

        pastureUI = new PastureUI(skin, player.getPasture());
        codexUI = new CodexMainUI(assetManager, codex, skin);

        inventoryMenu = new InventoryMenu(player.getSatchel(), craftManager, skin);
        pauseMenu = new PauseMenu(skin);

        stage.addActor(codexUI);
        stage.addActor(pauseMenu);
        stage.addActor(inventoryMenu);

        hideAll();

    }

    public void render(float delta){
        stage.act(delta);
        stage.draw();
    }

    public void update(){
        inventoryMenu.refresh();
    }

    public void togglePasture(){

    }

    public void togglePause() {

        switch (Const.currentState){
            case INGAME:
                Const.currentState = GameState.PAUSE;
                pauseMenu.setVisible(true);
                Gdx.input.setCursorCatched(false);
                break;
            case PAUSE:
                Const.currentState = GameState.INGAME;
                pauseMenu.setVisible(false);

                Gdx.input.setCursorCatched(true);
                break;
            case INVENTORY:
                toggleInventory();
                break;
            case SELECT_ITEM:
            case SELECT_PKMN:
                Const.currentState = GameState.INVENTORY;
                break;
            case CODEX:
                toggleCodex();
            default:
                return;
        }
        update();
    }

    public void toggleInventory() {
        switch (Const.currentState) {
            case INGAME:
            case INVENTORY:
                Const.currentState = Const.currentState==GameState.INGAME? GameState.INVENTORY: GameState.INGAME;
                inventoryMenu.setVisible(!inventoryMenu.isVisible());
                Gdx.input.setCursorCatched(!Gdx.input.isCursorCatched());
                break;
            case SELECT_ITEM:
            case SELECT_PKMN:
                Const.currentState = GameState.INVENTORY;
                inventoryMenu.setVisible(false);
                Gdx.input.setCursorCatched(false);
                break;
            default:
                return;
        }
        update();
    }

    public void toggleCodex(){
        if (Const.currentState != GameState.INGAME && Const.currentState != GameState.CODEX) return;
        if (codexUI.isVisible()){
            codexUI.setVisible(false);
        } else{
            codexUI = new CodexMainUI(assetManager, codex, skin);
            stage.addActor(codexUI);
            codexUI.validate();
            codexUI.setVisible(true);
        }
        Gdx.input.setCursorCatched(!Gdx.input.isCursorCatched());
        Const.currentState = Const.currentState == GameState.INGAME ? GameState.CODEX : GameState.INGAME;
    }

    public void hideAll(){
        inventoryMenu.setVisible(false);
        pauseMenu.setVisible(false);
        codexUI.setVisible(false);
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
        switch (Const.currentState){
            case INVENTORY:
            case SELECT_ITEM:
            case SELECT_PKMN:
                return inventoryMenu.handleInput(keycode);
            case PAUSE:
                return pauseMenu.handleInput(keycode);
            case CODEX:
//                return codexUI.handleInput(keycode);
            default: return false;
        }
    }

    public PauseMenu getPauseMenu(){
        return pauseMenu;
    }
}
