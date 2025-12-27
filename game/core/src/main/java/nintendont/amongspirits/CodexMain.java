package nintendont.amongspirits;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import nintendont.amongspirits.data.codex.Codex;
import nintendont.amongspirits.data.codex.FakeCodexLoader;
import nintendont.amongspirits.ui.codex.CodexEntryMenu;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class CodexMain extends ApplicationAdapter {
    private Stage stage;
    private CodexEntryMenu codexMenuUI;
    private AssetManager assetManager;

    @Override
    public void create() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        assetManager = new AssetManager();
        assetManager.load("sprites/icons/lion.png", Texture.class);
        assetManager.finishLoading();

        Codex codex = new FakeCodexLoader().load();

        codexMenuUI = new CodexEntryMenu(codex, assetManager);
        stage.addActor(codexMenuUI);
        stage.setKeyboardFocus(codexMenuUI);

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
