package nintendont.amongspirits.ui.codex;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import nintendont.amongspirits.data.codex.Codex;
import nintendont.amongspirits.data.codex.SpiritForm;
import nintendont.amongspirits.ui.game.GUIManager;
import nintendont.amongspirits.ui.game.MenuTable;

public class CodexMainUI extends MenuTable {
    AssetManager manager; Codex codex;
    public CodexMainUI(AssetManager manager, Codex codex, Skin skin, GUIManager gui) {
        super(gui);
        this.manager = manager;
        this.codex=codex;
        this.setFillParent(true);
        this.setBackground(skin.newDrawable("white", 0, 0, 0, 0.85f));

        
    }
    
    @Override
    public void update() {
        this.clear();
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
        
                gui.stage.setKeyboardFocus(menu);
    }

    @Override
    public boolean handleInput(int key) {
        return false;
    }
}
