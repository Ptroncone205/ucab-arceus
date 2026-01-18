package nintendont.amongspirits;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

import nintendont.amongspirits.Const.GameState;
import nintendont.amongspirits.data.savedata.SaveData;
import nintendont.amongspirits.managers.ItemFactory;
import nintendont.amongspirits.screens.GameScreen;
import nintendont.amongspirits.screens.MainMenu;

public class Main extends Game {
    private final Const context = Const.get();
    private GameScreen gameScreen;
    // private Screen currentScreen;

    // render
    private float deltaTime;

    @Override
    public void create() {
        context.init();

        this.setScreen(new MainMenu(this));
    }

    @Override
    public void render() {
        deltaTime = Gdx.graphics.getDeltaTime();
        screen.render(deltaTime);
    }

    public void newGame(String playerName, boolean load){
        if (playerName.isBlank()) return;
        Const.currentState = GameState.INGAME;
        gameScreen = new GameScreen(this, playerName, load);
        this.setScreen(gameScreen);
    }

    public void quitGame(){
        Const.currentState = GameState.MENU;
        gameScreen.dispose();
        gameScreen = null;
        this.setScreen(new MainMenu(this));
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void dispose() {
        super.dispose();
        context.dispose();
    }

}
