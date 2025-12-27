package nintendont.amongspirits;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import nintendont.amongspirits.data.codex.Codex;
import nintendont.amongspirits.data.codex.CodexPreviewAssets;
import nintendont.amongspirits.data.codex.FakeCodexLoader;
import nintendont.amongspirits.ui.codex.CodexMainUI;
import nintendont.amongspirits.utils.AssetUtils;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class CodexMain extends ApplicationAdapter {
    private Stage stage;
    private AssetManager assetManager;

    @Override
    public void create() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        assetManager = new AssetManager();
        assetManager.load("sfx/ui/open_page_foley.ogg", Sound.class);
        assetManager.load("sprites/icons/lion.png", Texture.class);
        assetManager.load("sprites/backgrounds/codex-scroll.png", Texture.class);
        assetManager.load(CodexPreviewAssets.DEER);
        assetManager.load(CodexPreviewAssets.WOLF);
        assetManager.load(CodexPreviewAssets.BUNNY);
        assetManager.load(CodexPreviewAssets.FOX);
        assetManager.load(CodexPreviewAssets.LION);

        AssetUtils.setTrueTypeFontLoaders(assetManager);
        AssetUtils.loadFont(assetManager, "roboto_xs.ttf", "fonts/roboto.ttf", 12);
        AssetUtils.loadFont(assetManager, "roboto_sm.ttf", "fonts/roboto.ttf", 14);
        AssetUtils.loadFont(assetManager, "roboto_base.ttf", "fonts/roboto.ttf", 16);
        AssetUtils.loadFont(assetManager, "roboto_lg.ttf", "fonts/roboto.ttf", 18);
        AssetUtils.loadFont(assetManager, "roboto_xl.ttf", "fonts/roboto.ttf", 20);
        AssetUtils.loadFont(assetManager, "roboto_2xl.ttf", "fonts/roboto.ttf", 24);
        AssetUtils.loadFont(assetManager, "chinese_8xl.ttf", "fonts/chinese_takeaway.ttf", 96);
        AssetUtils.loadFont(assetManager, "chinese_9xl.ttf", "fonts/chinese_takeaway.ttf", 128);

        assetManager.finishLoading();

        Codex codex = new FakeCodexLoader().load();

        CodexMainUI main = new CodexMainUI(assetManager, codex);
        stage.addActor(main);

        Actor menu = main.findActor("codex_menu");
        if (menu != null) {
            stage.setKeyboardFocus(menu);
        }

//        stage.setDebugAll(true);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void render() {
        float dt = Gdx.graphics.getDeltaTime();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(dt);
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        assetManager.dispose();
    }
}
