package nintendont.amongspirits.data.codex;

import java.util.ArrayList;

public class Codex {
    private ArrayList<SpiritForm> forms;

    public Codex() {
        this.forms = new ArrayList<>();
    }

    public void addForm(SpiritForm spiritForm) {
        forms.add(spiritForm);
    }
}
