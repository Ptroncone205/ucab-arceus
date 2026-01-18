package nintendont.amongspirits.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import nintendont.amongspirits.Main;
import nintendont.amongspirits.data.savedata.BtnEventListener;
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
                loadGame(menuUI.getPlayerName(), true);
            }
        });

        InputAdapter adapter = new InputAdapter(){
            @Override
            public boolean keyDown(int key){
                return menuUI.handleInput(key);
            }
        };

        multiplexer = new InputMultiplexer();
        Gdx.input.setInputProcessor(multiplexer);
        multiplexer.addProcessor(menuUI.stage);
        multiplexer.addProcessor(adapter);

    }
        @Override
    public void show() {
        // TODO Auto-generated method stub

    }

    @Override
    public void render(float delta) {
        menuUI.render(delta);

    }

    public void loadGame(String playerName, boolean load){
        game.newGame(playerName, load);
        // TODO debe enviar el archivo especifico o en su defecto el nombre del jugador
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
