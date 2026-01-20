package nintendont.amongspirits;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector3;
import nintendont.amongspirits.Const.GameState;
import nintendont.amongspirits.data.codex.Codex;
import nintendont.amongspirits.data.codex.FakeCodexLoader;
import nintendont.amongspirits.data.savedata.SaveData;
import nintendont.amongspirits.entities.Player;
import nintendont.amongspirits.entities.factories.PlayerFactory;
import nintendont.amongspirits.managers.Satchel;
import nintendont.amongspirits.managers.SaveManager;
import nintendont.amongspirits.screens.GameScreen;
import nintendont.amongspirits.screens.IntroScreen;
import nintendont.amongspirits.screens.MainMenu;

public class Main extends Game {
    private final Const context = Const.get();
    public AssetManager assets;
    public GameScreen gameScreen;
    private Music currentMusic;

    @Override
    public void create() {
        context.init();
        assets = context.assets;
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
        Player player = new PlayerFactory().loadPlayerFromSaveData(playerName);
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
            currentMusic.setVolume(0.5f);
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
            sound.play(0.7f);

        } catch (Exception e) {
            Gdx.app.error("Sound", "No se pudo reproducir el sonido: " + path);
        }
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
