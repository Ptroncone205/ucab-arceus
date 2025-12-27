package nintendont.amongspirits.ui.codex;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import nintendont.amongspirits.data.codex.Codex;
import nintendont.amongspirits.data.codex.SpiritForm;

import java.util.ArrayList;
import java.util.Random;

public class CodexEntryMenu extends Table {
    private int selectedIndex = 0;
    private final ArrayList<CodexEntryCard> entries = new ArrayList<>();
    private final Sound pageFoleySfx;

    public CodexEntryMenu(AssetManager manager, Codex codex) {
        this.setTouchable(Touchable.enabled);
        this.setName("codex_menu");

        pageFoleySfx = manager.get("sfx/ui/open_page_foley.ogg", Sound.class);

        Table root = this;

        Table entriesTable = new Table();
        root.add(entriesTable);

        int index = 0;
        for (SpiritForm form : codex.getForms()) {
            Texture iconTexture = manager.get(form.getIconAsset());
            TextureRegion iconRegion = new TextureRegion(iconTexture);
            TextureRegionDrawable icon = new TextureRegionDrawable(iconRegion);

            CodexEntryCard card = new CodexEntryCard(form.getId(), form.getAnimalName(), icon, true);
            entriesTable.add(card).width(275).height(85);
            entriesTable.row();
            entries.add(card);
            int currentIndex = index;
            card.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    selectedIndex = currentIndex;
                    selectSpiritForm(codex, currentIndex);
                }
            });
            index++;
        }

        this.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.DOWN ||  keycode == Input.Keys.S) {
                    selectedIndex = (selectedIndex + 1) % entries.size();
                    selectSpiritForm(codex, selectedIndex);
                }
                if (keycode == Input.Keys.UP ||  keycode == Input.Keys.W) {
                    selectedIndex = (selectedIndex - 1 + entries.size()) % entries.size();
                    selectSpiritForm(codex, selectedIndex);
                }
                return true;
            }
        });

        selectSpiritForm(codex, selectedIndex);
    }

    private void selectSpiritForm(Codex codex, int selectedIndex) {
        CodexEntryCard card = entries.get(selectedIndex);
        activateCard(card);
        emitSpiritFormSelect(codex, selectedIndex);
        playChangeSfx();
    }

    private void activateCard(CodexEntryCard card) {
        for (CodexEntryCard other : entries) {
            other.setActive(false);
        }
        card.setActive(true);
    }

    private void emitSpiritFormSelect(Codex codex, int selectedIndex) {
        SpiritForm form = codex.getForms().get(selectedIndex);
        SpiritFormSelectedEvent event = SpiritFormSelectedEvent.pool.obtain();
        event.setSpiritForm(form);
        fire(event);
        SpiritFormSelectedEvent.pool.free(event);
    }

    private void playChangeSfx() {
        float randomPitch = MathUtils.random(0.8f, 1.2f);
        pageFoleySfx.play(1f, randomPitch, 0);
    }
}
