package nintendont.amongspirits.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import nintendont.amongspirits.Main;

public class IntroScreen implements Screen {
    private Main game;
    private Stage stage;
    private BitmapFont font;
    private Music introMusic;

    public IntroScreen(Main game) {
        this.game = game;
        this.stage = new Stage();
        this.font = new BitmapFont();

        introMusic = Gdx.audio.newMusic(Gdx.files.internal("music and sounds/music/intro_theme.mp3"));
        introMusic.setLooping(true);
        introMusic.setVolume(0.5f);
        introMusic.play();

        setupIntroSequence();
    }

    private void setupIntroSequence() {

        Image imgUcab = new Image(new Texture(Gdx.files.internal("ucabLogo.jpg")));
        Image imgLibGdx = new Image(new Texture(Gdx.files.internal("libGdx-Bullet.jpg")));
        Image imgNintendont = new Image(new Texture(Gdx.files.internal("nintendontLogo.jpg")));
        Image imgBackground = new Image(new Texture(Gdx.files.internal("textures/menu/arceus.png")));
        imgUcab.setFillParent(true);
        imgLibGdx.setFillParent(true);
        imgNintendont.setFillParent(true);
        imgBackground.setFillParent(true);

        Label.LabelStyle style = new Label.LabelStyle(font, Color.WHITE);
        Label titleLabel = new Label("AMONG SPIRITS", style);
        Label authorLabel = new Label("NINTENDONT", style);
        Label clickStartLabel = new Label("¡Haz click para comenzar!", new Label.LabelStyle(font, Color.RED));

        // Alphas
        imgUcab.getColor().a = 0;
        imgLibGdx.getColor().a = 0;
        imgNintendont.getColor().a = 0;
        imgBackground.getColor().a = 0;
        titleLabel.getColor().a = 0;
        authorLabel.getColor().a = 0;
        clickStartLabel.getColor().a = 0;

        Table textTable = new Table();
        textTable.setFillParent(true);
        textTable.add(titleLabel).padBottom(10).row();
        textTable.add(authorLabel).padBottom(80).row();
        textTable.add(clickStartLabel);

        stage.addActor(imgBackground);
        stage.addActor(imgUcab);
        stage.addActor(imgLibGdx);
        stage.addActor(imgNintendont);
        stage.addActor(textTable);


        // Logo UCAB (0s a 5.5s)
        imgUcab.addAction(Actions.sequence(
            Actions.delay(1.0f),
            Actions.fadeIn(0.5f),
            Actions.delay(3.5f),
            Actions.fadeOut(0.5f)
        ));

        // Logo LibGDX y Bullet (6.5s a 11s)
        imgLibGdx.addAction(Actions.sequence(
            Actions.delay(6.5f),
            Actions.fadeIn(0.5f),
            Actions.delay(3.5f),
            Actions.fadeOut(0.5f)
        ));

        // Logo Nintendont (12.0s a 16.5s)
        imgNintendont.addAction(Actions.sequence(
            Actions.delay(12.0f),
            Actions.fadeIn(0.5f),
            Actions.delay(3.5f),
            Actions.fadeOut(0.5f)
        ));

        // Fondo Final (Aparición de 3.5s)
        imgBackground.addAction(Actions.sequence(
            Actions.delay(17.5f),
            Actions.fadeIn(3.5f)
        ));

        //  Texto que parpadea y final de imagenes
        textTable.addAction(Actions.sequence(
            Actions.delay(19.5f),
            Actions.fadeIn(1.0f),
            Actions.run(() -> {
                clickStartLabel.addAction(Actions.forever(Actions.sequence(
                    Actions.fadeOut(0.6f),
                    Actions.fadeIn(0.6f)
                )));

                Gdx.input.setInputProcessor(new com.badlogic.gdx.InputAdapter() {
                    @Override
                    public boolean keyDown(int keycode) {

                        goToMainMenu();
                        return true;
                    }
                    @Override
                    public boolean touchDown(int x, int y, int pointer, int button) {
                        goToMainMenu();
                        return true;
                    }
                });
            })
        ));
    }

    private void goToMainMenu() {
        game.playSound("music and sounds/sounds/button_pressed.mp3");
        introMusic.stop();
        game.setScreen(new MainMenu(game));
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            goToMainMenu();
        }

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override public void resize(int width, int height){
        stage.getViewport().update(width, height, true);
    }

    @Override public void show() {}

    @Override public void pause() {
        introMusic.pause();
    }

    @Override public void resume() {
        introMusic.play();
    }

    @Override public void hide() {}

    @Override public void dispose() {
        stage.dispose();
        font.dispose();
        introMusic.dispose();
    }
}
