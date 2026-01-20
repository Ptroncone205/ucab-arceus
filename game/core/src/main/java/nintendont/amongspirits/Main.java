package nintendont.amongspirits;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import nintendont.amongspirits.Const.GameState;
import nintendont.amongspirits.data.satchel.ItemDB;
import nintendont.amongspirits.data.satchel.ItemDBLoader;
import nintendont.amongspirits.entities.Player;
import nintendont.amongspirits.entities.factories.PlayerFactory;
import nintendont.amongspirits.managers.SaveManager;
import nintendont.amongspirits.screens.GameScreen;
import nintendont.amongspirits.screens.IntroScreen;
import nintendont.amongspirits.screens.MainMenu;

public class Main extends Game {
    private final Const context = Const.get();
    private ItemDB items;
    private SaveManager saveManager;
    public AssetManager assets;
    public GameScreen gameScreen;
    private Music currentMusic;

    @Override
    public void create() {
        context.init();
        assets = context.assets;
        items = new ItemDBLoader().load();
        saveManager = new SaveManager(items);
        this.setScreen(new IntroScreen(this, assets));
    }

    @Override
    public void render() {
        super.render();
    }

    public void newGame(String playerName, boolean load){
        if (playerName == null || playerName.isBlank()) return;

        playMusic("", true);

        Const.currentState = GameState.INGAME;
        Player player = new PlayerFactory(saveManager).loadPlayerFromSaveData(playerName);
        gameScreen = new GameScreen(this, assets, player);
        this.setScreen(gameScreen);
    }

    public void quitGame(){
        if(gameScreen != null) {
            gameScreen.dispose();
            gameScreen = null;
        }
        playMusic("", true);
        this.setScreen(new MainMenu(this));
    }

    public void playMusic(String path, boolean loop) {
        if (currentMusic != null) {
            currentMusic.stop();
            currentMusic.dispose();
        }

        try {
            currentMusic = Gdx.audio.newMusic(Gdx.files.internal(path));
            currentMusic.setLooping(loop);
            currentMusic.setVolume(0.2f);
            currentMusic.play();
        } catch (Exception e) {
            Gdx.app.error("Music", "No se pudo cargar el archivo: " + path);
        }
    }

    public void stopMusic() {
        if (currentMusic != null) {
            currentMusic.stop();
        }
    }

    public void playSound(String path) {
        try {
            Sound sound = Gdx.audio.newSound(Gdx.files.internal(path));
            sound.play(0.3f);

        } catch (Exception e) {
            Gdx.app.error("Sound", "No se pudo reproducir el sonido: " + path);
        }
    }

    public ItemDB getItems() {
        return items;
    }

    public SaveManager getSaveManager() {
        return saveManager;
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void dispose() {
        super.dispose();
        if (currentMusic != null) currentMusic.dispose();
        if (gameScreen != null) gameScreen.dispose();
        context.dispose();
    }
}
