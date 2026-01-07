package nintendont.amongspirits.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import nintendont.amongspirits.Main;
import nintendont.amongspirits.data.savedata.SaveData;
import nintendont.amongspirits.data.savedata.BtnEventListener;
import nintendont.amongspirits.managers.SaveManager;
import nintendont.amongspirits.ui.menu.MenuUI;

public class MainMenu implements Screen{
    private InputMultiplexer multiplexer;
    private MenuUI menuUI;
    private Main game;
    // private SaveData data;

    public MainMenu(Main game){
        this.game = game;
        menuUI = new MenuUI(game);
        menuUI.setBtnListener(new BtnEventListener(){
            @Override
            public void onLoadRequest(){
                loadGame();
            }
        });

        multiplexer = new InputMultiplexer();
        Gdx.input.setInputProcessor(multiplexer);
        multiplexer.addProcessor(menuUI.stage);

    }
        @Override
    public void show() {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void render(float delta) {
        menuUI.render(delta);
        
    }

    public void loadGame(){
        game.newGame(true);
    }

    @Override
    public void resize(int width, int height) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void pause() {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void resume() {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void hide() {
    }
    
    @Override
    public void dispose() {
        menuUI.dispose();
        
    }
}
