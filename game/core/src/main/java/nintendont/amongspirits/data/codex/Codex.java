package nintendont.amongspirits.data.codex;

import nintendont.amongspirits.data.spirits.Spirit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Codex {
    private ArrayList<SpiritForm> forms;

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
        // TODO actualizar la entrada si ya existe :v
        forms.add(spiritForm);
        System.out.println(forms);
    }
}
