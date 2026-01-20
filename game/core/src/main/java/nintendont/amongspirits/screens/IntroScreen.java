package nintendont.amongspirits.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
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
import nintendont.amongspirits.data.assets.GameAssets;
import nintendont.amongspirits.data.codex.BattleSpiritAssets;
import nintendont.amongspirits.data.codex.CodexIconAssets;
import nintendont.amongspirits.data.codex.CodexPreviewAssets;
import nintendont.amongspirits.utils.AssetUtils;

public class IntroScreen implements Screen {
    private Main game;
    private Stage stage;
    private BitmapFont font;
    private Music introMusic;
    private AssetManager assets;

    public IntroScreen(Main game, AssetManager assets) {
        this.game = game;
        this.stage = new Stage();
        this.font = new BitmapFont();

        // LOAD ASSETS
        this.assets = assets;

        AssetUtils.loadGLTF(assets, "models/mc/lukitm501.gltf");
        assets.load(GameAssets.ORAN_BERRY);
        assets.load(GameAssets.POKEBALL);
        assets.load(GameAssets.NO_POKEBALL);
        assets.load(GameAssets.TUMBLESTONE);
        assets.load(GameAssets.YUMENJIANG_SCENE);
        assets.load(GameAssets.PHOENIX_SCENE);
        assets.load(GameAssets.LION_SCENE);
        assets.load(GameAssets.WOLF_SCENE);
        assets.load(GameAssets.DEER_SCENE);
        assets.load(GameAssets.BUNNY_SCENE);
        assets.load(GameAssets.FOX_SCENE);
        assets.load(GameAssets.ORAN_BERRY_SCENE);
        assets.load(GameAssets.TUMBLESTONE_SCENE);
        assets.load(BattleSpiritAssets.WU_ZETIAN);
        assets.load(BattleSpiritAssets.MALE_DEER);
        assets.load(BattleSpiritAssets.FEMALE_DEER);
        assets.load(BattleSpiritAssets.MALE_WOLF);
        assets.load(BattleSpiritAssets.FEMALE_WOLF);
        assets.load(BattleSpiritAssets.MALE_BUNNY);
        assets.load(BattleSpiritAssets.FEMALE_BUNNY);
        assets.load(BattleSpiritAssets.MALE_FOX);
        assets.load(BattleSpiritAssets.FEMALE_FOX);
        assets.load(BattleSpiritAssets.MALE_LION);
        assets.load(BattleSpiritAssets.FEMALE_LION);

        assets.load("sfx/ui/open_page_foley.ogg", Sound.class);
        assets.load("sprites/icons/lion.png", Texture.class);
        assets.load("sprites/backgrounds/codex-scroll.png", Texture.class);
        assets.load(CodexPreviewAssets.DEER);
        assets.load(CodexPreviewAssets.WOLF);
        assets.load(CodexPreviewAssets.BUNNY);
        assets.load(CodexPreviewAssets.FOX);
        assets.load(CodexPreviewAssets.LION);
        assets.load(CodexPreviewAssets.PHOENIX);
        assets.load(CodexIconAssets.DEER);
        assets.load(CodexIconAssets.WOLF);
        assets.load(CodexIconAssets.BUNNY);
        assets.load(CodexIconAssets.FOX);
        assets.load(CodexIconAssets.LION);
        assets.load(CodexIconAssets.PHOENIX);

        AssetUtils.setTrueTypeFontLoaders(assets);
        AssetUtils.loadFont(assets, "roboto_xs.ttf", "fonts/roboto.ttf", 12);
        AssetUtils.loadFont(assets, "roboto_sm.ttf", "fonts/roboto.ttf", 14);
        AssetUtils.loadFont(assets, "roboto_base.ttf", "fonts/roboto.ttf", 16);
        AssetUtils.loadFont(assets, "roboto_lg.ttf", "fonts/roboto.ttf", 18);
        AssetUtils.loadFont(assets, "roboto_xl.ttf", "fonts/roboto.ttf", 20);
        AssetUtils.loadFont(assets, "roboto_2xl.ttf", "fonts/roboto.ttf", 24);
        AssetUtils.loadFont(assets, "chinese_8xl.ttf", "fonts/chinese_takeaway.ttf", 96);
        AssetUtils.loadFont(assets, "chinese_9xl.ttf", "fonts/chinese_takeaway.ttf", 128);

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
        assets.finishLoading();
        game.playSound("music and sounds/sounds/button_pressed.mp3");
        introMusic.stop();
        game.setScreen(new MainMenu(game));
    }

    @Override
    public void render(float delta) {
        assets.update();
        if (!assets.isFinished()) System.out.println("LOADING: " + assets.getLoadedAssets() * 100 / 34 + "%");
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT_BRACKET) || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
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
