package nintendont.amongspirits.ui.codex;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pools;
import com.badlogic.gdx.utils.Scaling;
import nintendont.amongspirits.data.codex.Codex;

public class CodexMenuHome extends Table implements Disposable {
    private Codex codex;

    // Assets
    private Texture scrollTexture;

    public CodexMenuHome(Codex codex) {
        this.codex = codex;

        this.setFillParent(true);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/chinese_takeaway.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 128;
        parameter.color = Color.WHITE;
        parameter.borderColor = Color.BLACK;
        BitmapFont customFont = generator.generateFont(parameter);
        generator.dispose();

        scrollTexture = new Texture("scroll.png");
        Image scroll = new Image(scrollTexture);
        scroll.setScaling(Scaling.fit);

        Label.LabelStyle style = new Label.LabelStyle();
        style.font = customFont;
        style.fontColor = Color.BLACK;
        Label title = new Label("Codex", style);
        title.setAlignment(Align.center);

        Stack stack = new Stack();
        stack.add(scroll);
        stack.add(title);

        this.add(stack);
    }

    @Override
    public void dispose() {
        scrollTexture.dispose();
    }
}
