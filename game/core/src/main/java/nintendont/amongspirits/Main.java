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

    public void newGame(){
        newGame(false);
    }

    public void newGame(boolean load){
        Const.currentState = GameState.INGAME;
        gameScreen = new GameScreen(this, load);
        this.setScreen(gameScreen);
    }
    
    @Override
    public void resize(int width, int height) {
        // TODO Auto-generated method stub
        super.resize(width, height);
    }
    
    @Override
    public void dispose() {
        // TODO Auto-generated method stub
        super.dispose();
    }

}
