package nintendont.amongspirits.ui.codex;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import nintendont.amongspirits.data.codex.Codex;
import nintendont.amongspirits.data.codex.SpiritForm;

public class CodexMainUI extends Table {
    public CodexMainUI(AssetManager manager, Codex codex) {
        this.setFillParent(true);

        CodexEntrySummary summary = new CodexEntrySummary(manager, codex.getForms().get(0));
        CodexEntryMenu menu = new CodexEntryMenu(manager, codex);

        this.add(summary);
        this.add(menu);

        menu.addListener(new SpiritFormSelectedListener() {
            @Override
            public void onSpiritFormSelected(SpiritFormSelectedEvent event, SpiritForm spiritForm) {
                summary.setSpiritForm(spiritForm);
            }
        });
    }
}
