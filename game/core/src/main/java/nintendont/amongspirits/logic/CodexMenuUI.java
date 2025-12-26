package nintendont.amongspirits.logic;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import nintendont.amongspirits.data.codex.Codex;

public class CodexMenuUI extends Table implements Disposable {
    private Codex codex;

    // Assets
    private Texture scrollTexture;

    // Widgets
    private Image scroll;
    private Label title;

    public CodexMenuUI(Codex codex) {
        this.codex = codex;

        scrollTexture = new Texture("scroll.png");
        scroll = new Image(scrollTexture);
        this.add(scroll);

        Label.LabelStyle style = new Label.LabelStyle();
        style.font = new BitmapFont();
        style.fontColor = Color.BLACK;
        title = new Label("Codex", style);
        this.add(title);
    }

    @Override
    public void dispose() {
        scrollTexture.dispose();
    }
}
