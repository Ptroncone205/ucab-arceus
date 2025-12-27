package nintendont.amongspirits.data.codex;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;

public class SpiritForm {
    private int id;
    private String animalName;
    private SpiritElement element;
    private ArrayList<SpiritMove> moves;
    private int researchLevel;
    private AssetDescriptor<Texture> iconAsset;

    public SpiritForm(int id, String animalName, SpiritElement element, AssetDescriptor<Texture> iconAsset) {
        this.id = id;
        this.animalName = animalName;
        this.element = element;
        this.iconAsset = iconAsset;
        this.moves = new ArrayList<>();
        this.researchLevel = 0;
    }

    public String getAnimalName() {
        return animalName;
    }

    public void addMove(SpiritMove move) {
        moves.add(move);
    }
}
