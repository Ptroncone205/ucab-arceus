package nintendont.amongspirits.ui;

import com.badlogic.gdx.Gdx;
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
import nintendont.amongspirits.managers.CraftManager;
import nintendont.amongspirits.managers.InventoryManager;

public class GUIManager implements Disposable{
    public Stage stage;
    private InventoryMenu inventoryMenu;
    private Skin skin;
    // private PauseMenu pauseMenu
    private boolean isPaused = false;
    private boolean isInventoryOpen = false;

    public enum UIState{ INGAME, INVENTORY, PAUSE, SELECT_ITEM, SELECT_PKMN }
    private UIState currentState = UIState.INGAME;

    public GUIManager (SpriteBatch batch, InventoryManager inventoryManager, CraftManager craftManager){
        stage =new Stage(new ScreenViewport(), batch);
        Gdx.input.setInputProcessor(stage);

        createBasicSkin();
        inventoryMenu = new InventoryMenu(inventoryManager, craftManager, skin);

        stage.addActor(inventoryMenu);

        hideAll();

    }

    public void render(float delta){
        stage.act(delta);
        stage.draw();
    }

    public void update(){
        inventoryMenu.update(skin);
    }

    public void setState(UIState state){
        currentState = state;
        System.out.println(state);
    }

    public void togglePause() {
        Gdx.input.setCursorCatched(!Gdx.input.isCursorCatched());
        if (isInventoryOpen) {
            isInventoryOpen = false;
            inventoryMenu.setVisible(false);
            return;
        }

        isPaused = !isPaused;
        // pauseMenu.setVisible(isPaused);

        // TODO: pausar juego
    }

    public void toggleInventory() {
        if (isPaused) return;

        isInventoryOpen = !isInventoryOpen;
        inventoryMenu.setVisible(isInventoryOpen);
        Gdx.input.setCursorCatched(!Gdx.input.isCursorCatched());
    }

    public void hideAll(){
        inventoryMenu.setVisible(false);
        currentState = UIState.INGAME;
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
}
