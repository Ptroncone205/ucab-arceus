package nintendont.amongspirits.data.codex;

import java.util.ArrayList;
import java.util.List;

public class Codex {
    private ArrayList<SpiritForm> forms;

    public Codex() {
        this.forms = new ArrayList<>();
    }

    public List<SpiritForm> getForms() {
        return forms;
    }

    public void addForm(SpiritForm spiritForm) {
        forms.add(spiritForm);
    }
}
