package nintendont.amongspirits.ui.codex;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Pools;
import com.badlogic.gdx.utils.Scaling;

import java.util.ArrayList;

public class CodexEntryCard extends Table {
    // Colors
    private static final Color TEXT_COLOR_ACTIVE = Color.BLACK;
    private static final Color TEXT_COLOR_INACTIVE = Color.WHITE;
    private static final Color BG_COLOR_ACTIVE = Color.valueOf("e6e1c9");
    private static final Color BG_COLOR_INACTIVE = Color.valueOf("4a494e");

    // State
    private boolean active = false;

    // Widgets
    private final ArrayList<Label> mainLabels = new ArrayList<>();

    public CodexEntryCard(int index, String name, Drawable drawable, boolean known) {
        this.setTouchable(Touchable.enabled);

        Image avatar = new Image(drawable);
        avatar.setScaling(Scaling.fit);
        this.add(avatar).size(75).padLeft(10);

        Table infoTable = new Table();

        Label.LabelStyle style = new Label.LabelStyle();
        style.font = new BitmapFont();
        style.fontColor = Color.WHITE;

        String indexStr = String.format("No. %03d", index);
        Label indexLabel = new Label(indexStr, style);

        Label nameLabel = new Label(name, style);

        mainLabels.add(indexLabel);
        mainLabels.add(nameLabel);

        infoTable.add(indexLabel).left();
        infoTable.row();
        infoTable.add(nameLabel).left();
        this.add(infoTable).padLeft(20);

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        TextureRegionDrawable backgroundDrawable = new TextureRegionDrawable(new Texture(pixmap));
        pixmap.dispose();
        this.setBackground(backgroundDrawable);
        this.setColor(BG_COLOR_INACTIVE);

        this.left();
    }

    public void getActive(boolean active) {
        this.active = active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        Color bgColor = active ? BG_COLOR_ACTIVE : BG_COLOR_INACTIVE;
        Color textColor = active ? TEXT_COLOR_ACTIVE : TEXT_COLOR_INACTIVE;

        this.setColor(bgColor);
        for (Label label : mainLabels) {
            label.setColor(textColor);
        }
    }

    private void emitChange() {
        ChangeListener.ChangeEvent changeEvent = Pools.obtain(ChangeListener.ChangeEvent.class);
        fire(changeEvent);
        Pools.free(changeEvent);
    }
}
