package nintendont.amongspirits.ui.codex;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import nintendont.amongspirits.data.codex.Codex;
import nintendont.amongspirits.data.codex.SpiritForm;

import java.util.ArrayList;

public class CodexEntryMenu extends Table {
    private int selectedIndex = 0;
    private final ArrayList<CodexEntryCard> entries = new ArrayList<>();

    public CodexEntryMenu(Codex codex, AssetManager assetManager) {
        this.setTouchable(Touchable.enabled);

        Table root = this;
        root.setFillParent(true);

        Table entriesTable = new Table();
        root.add(entriesTable);

        Texture lionTexture = assetManager.get("sprites/icons/lion.png", Texture.class);
        TextureRegion lionIcon = new TextureRegion(lionTexture);
        TextureRegionDrawable lionIconDrawable = new TextureRegionDrawable(lionIcon);

        for (SpiritForm form : codex.getForms()) {
            CodexEntryCard card = new CodexEntryCard(1, form.getAnimalName(), lionIconDrawable, true);
            entriesTable.add(card).width(275).height(85);
            entriesTable.row();
            entries.add(card);
            card.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    selectCard(card);
                }
            });
        }

        this.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.DOWN ||  keycode == Input.Keys.S) {
                    selectedIndex = (selectedIndex + 1) % entries.size();
                    CodexEntryCard card = entries.get(selectedIndex);
                    selectCard(card);
                }
                if (keycode == Input.Keys.UP ||  keycode == Input.Keys.W) {
                    selectedIndex = (selectedIndex - 1 + entries.size()) % entries.size();
                    CodexEntryCard card = entries.get(selectedIndex);
                    selectCard(card);
                }
                return true;
            }
        });

        selectCard(entries.get(selectedIndex));
    }

    private void selectCard(CodexEntryCard card) {
        for (CodexEntryCard other : entries) {
            other.setActive(false);
        }
        card.setActive(true);
    }
}
