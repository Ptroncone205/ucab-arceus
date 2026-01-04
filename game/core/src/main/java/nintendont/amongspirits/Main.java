package nintendont.amongspirits;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
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
        gameScreen = new GameScreen(this);
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
