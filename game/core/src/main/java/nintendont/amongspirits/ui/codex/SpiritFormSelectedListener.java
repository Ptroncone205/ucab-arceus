package nintendont.amongspirits.ui.codex;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import nintendont.amongspirits.data.codex.SpiritForm;

public abstract class SpiritFormSelectedListener implements EventListener {
    @Override
    public boolean handle(Event event) {
        if (!(event instanceof SpiritFormSelectedEvent)) {
            return false;
        }

        SpiritFormSelectedEvent sfse = (SpiritFormSelectedEvent) event;
        onSpiritFormSelected(sfse, sfse.getSpiritForm());

        return true;
    }

    public abstract void onSpiritFormSelected(SpiritFormSelectedEvent event, SpiritForm spiritForm);
}
