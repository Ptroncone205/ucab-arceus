package nintendont.amongspirits.data.codex;

import java.util.ArrayList;

public class SpiritForm {
    private String id;
    private String animalName;
    private SpiritElement element;
    private ArrayList<SpiritMove> moves;
    private int researchLevel;

    public SpiritForm(String id, String animalName, SpiritElement element) {
        this.id = id;
        this.animalName = animalName;
        this.element = element;
        this.moves = new ArrayList<>();
        this.researchLevel = 0;
    }

    public void addMove(SpiritMove move) {
        moves.add(move);
    }
}
