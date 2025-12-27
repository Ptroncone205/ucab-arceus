package nintendont.amongspirits.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import nintendont.amongspirits.Const;
import nintendont.amongspirits.Const.GameState;
import nintendont.amongspirits.managers.CraftManager;
import nintendont.amongspirits.managers.InventoryManager;

public class GUIManager implements Disposable{
    public Stage stage;
    private InventoryMenu inventoryMenu;
    private Skin skin;
    // private PauseMenu pauseMenu
    private boolean isPaused = false;
    private boolean isInventoryOpen = false;

    // public enum UIState{ INGAME, INVENTORY, PAUSE, SELECT_ITEM, SELECT_PKMN }

    public GUIManager (SpriteBatch batch, InventoryManager inventoryManager, CraftManager craftManager){
        stage =new Stage(new ScreenViewport(), batch);

        createBasicSkin();
        inventoryMenu = new InventoryMenu(inventoryManager, craftManager, skin);

        stage.addActor(inventoryMenu);

        hideAll();

    }

    public void render(float delta){
        inventoryMenu.refresh();
        stage.act(delta);
        stage.draw();
    }

    public void update(){
        // inventoryMenu.update();
    }

    public void setState(GameState state){
        Const.currentState = state;
        System.out.println(state);
    }

    public void togglePause() {

        switch (Const.currentState){
            case INGAME:
                Const.currentState = GameState.PAUSE;
                //pauseMenu.setiVisible(true);
                Gdx.input.setCursorCatched(false);
                break;
            case PAUSE:
                Const.currentState = GameState.INGAME;
                //pauseMenu.setiVisible(false);
                
                Gdx.input.setCursorCatched(true);
                break;
            case INVENTORY:
                Const.currentState = GameState.INGAME;
                inventoryMenu.setVisible(false);
                Gdx.input.setCursorCatched(true);
                break;
            case SELECT_ITEM:
                Const.currentState = GameState.INVENTORY;
                break;
            case SELECT_PKMN:
                Const.currentState = GameState.INVENTORY;
                break;
        }
        update();
    }

    public void toggleInventory() {
        switch (Const.currentState) {
            case INGAME:
                Const.currentState = GameState.INVENTORY;
                inventoryMenu.setVisible(true);
                Gdx.input.setCursorCatched(false);
                break;
        
            case INVENTORY:
                Const.currentState = GameState.INGAME;
                inventoryMenu.setVisible(false);
                Gdx.input.setCursorCatched(true);
                break;
            case SELECT_ITEM:
                Const.currentState = GameState.INGAME;
                inventoryMenu.setVisible(false);
                Gdx.input.setCursorCatched(true);
                break;
            case SELECT_PKMN:
                Const.currentState = GameState.INGAME;
                inventoryMenu.setVisible(false);
                Gdx.input.setCursorCatched(true);
                break;
            case PAUSE:
                break;
        }
        update();
    }

    public void hideAll(){
        inventoryMenu.setVisible(false);
        Const.currentState = GameState.INGAME;
    }


    private void createBasicSkin() {
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
        textButtonStyle.up = skin.newDrawable("white", Color.GRAY);
        textButtonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.over = skin.newDrawable("white", Color.WHITE);
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

    public void handleInput() {
        
    }
}
