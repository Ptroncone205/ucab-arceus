package nintendont.amongspirits.data.spirits;

import nintendont.amongspirits.data.codex.SpiritForm;

public class Spirit {
    private final int id;
    private final String name, lastName;
    private final String biography;
    private final boolean gender;
    private final SpiritForm form;

    public Spirit(int id, String name, String lastName, String biography, boolean gender, SpiritForm form) {
        this.name = name;
        this.lastName = lastName;
        this.biography = biography;
        this.id = id;
        this.gender = gender;
        this.form = form;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getBiography() {
        return biography;
    }

    public boolean getGender() {
        return gender;
    }

    public SpiritForm getForm() {
        return form;
    }
}
