package nintendont.amongspirits.data.codex;

import java.util.ArrayList;
import java.util.List;

public class Codex {
    private ArrayList<SpiritForm> forms;
    private boolean complete;

    public Codex() {
        this.forms = new ArrayList<>();
    }

    public List<SpiritForm> getForms() {
        return forms;
    }

    public SpiritForm getFormById(int id) {
        return forms.stream().filter(f -> f.getId() == id).findFirst().orElse(null);
    }

    public void addForm(SpiritForm spiritForm) {
        forms.add(spiritForm);
    }

    public void calcComplete() {
        if (forms.stream().map(SpiritForm::getResearchLevel).allMatch(l -> l >= 10)) {
            complete = true;
        } else {
            complete = false;
        }
    }
    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }
}
