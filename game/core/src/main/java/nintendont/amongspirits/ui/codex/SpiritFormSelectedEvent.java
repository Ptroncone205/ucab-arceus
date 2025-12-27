package nintendont.amongspirits.ui.codex;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.utils.DefaultPool;
import com.badlogic.gdx.utils.Pool;
import nintendont.amongspirits.data.codex.SpiritForm;

public class SpiritFormSelectedEvent extends Event implements Pool.Poolable {
    public static final DefaultPool<SpiritFormSelectedEvent> pool = new DefaultPool<>(SpiritFormSelectedEvent::new);
    private SpiritForm spiritForm;

    public SpiritForm getSpiritForm() {
        return spiritForm;
    }

    public void setSpiritForm(SpiritForm spiritForm) {
        this.spiritForm = spiritForm;
    }

    @Override
    public void reset() {
        super.reset();
        spiritForm = null;
    }
}
