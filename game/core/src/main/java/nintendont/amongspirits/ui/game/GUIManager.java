package nintendont.amongspirits.ui.game;

import com.badlogic.gdx.Gdx;
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
import nintendont.amongspirits.Const;
import nintendont.amongspirits.Const.GameState;
import nintendont.amongspirits.managers.CraftManager;
import nintendont.amongspirits.managers.Satchel;

public class GUIManager implements Disposable{
    public Stage stage;
    private InventoryMenu inventoryMenu;
    private Skin skin;
    private PauseMenu pauseMenu;

    public GUIManager (SpriteBatch batch, Satchel satchel, CraftManager craftManager){
        stage =new Stage(new ScreenViewport(), batch);

        createSkin();
        inventoryMenu = new InventoryMenu(satchel, craftManager, skin);
        pauseMenu = new PauseMenu(skin);

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
                Const.currentState = GameState.INGAME;
                inventoryMenu.setVisible(false);
                Gdx.input.setCursorCatched(true);
                break;
            case SELECT_ITEM:
            case SELECT_PKMN:
                Const.currentState = GameState.INVENTORY;
                break;
            default:
                return;
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
            case SELECT_PKMN:
                Const.currentState = GameState.INGAME;
                inventoryMenu.setVisible(false);
                Gdx.input.setCursorCatched(true);
                break;
            default:
                return;
        }
        update();
    }

    public void hideAll(){
        inventoryMenu.setVisible(false);
        pauseMenu.setVisible(false);
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
            default: return false;
        }
    }

    public PauseMenu getPauseMenu(){
        return pauseMenu;
    }
}
