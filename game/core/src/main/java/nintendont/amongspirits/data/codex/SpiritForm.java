package nintendont.amongspirits.data.codex;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;
import java.util.List;

public class SpiritForm {
    private final int id;
    private final String animalName;
    private final String description;
    private final String longDescription;
    private final SpiritElement element;
    private final ArrayList<SpiritMove> moves;
    private final ArrayList<ResearchTaskSet> tasks;
    private int researchLevel;
    private final AssetDescriptor<Texture> iconAsset;
    private final AssetDescriptor<Texture> previewAsset;
    private final AssetDescriptor<Texture> battleMaleAsset;
    private final AssetDescriptor<Texture> battleFemaleAsset;

    public SpiritForm(
        int id,
        String animalName,
        String description,
        String longDescription,
        SpiritElement element,
        AssetDescriptor<Texture> iconAsset,
        AssetDescriptor<Texture> previewAsset,
        AssetDescriptor<Texture> battleMaleAsset,
        AssetDescriptor<Texture> battleFemaleAsset
    ) {
        this.id = id;
        this.animalName = animalName;
        this.description = description;
        this.longDescription = longDescription;
        this.element = element;
        this.iconAsset = iconAsset;
        this.previewAsset = previewAsset;
        this.battleMaleAsset = battleMaleAsset;
        this.battleFemaleAsset = battleFemaleAsset;
        this.moves = new ArrayList<>();
        this.tasks = new ArrayList<>();
        this.researchLevel = 0;
    }

    public int getId() {
        return id;
    }

    public String getAnimalName() {
        return animalName;
    }

    public String getDescription() {
        return description;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public SpiritElement getElement() {
        return element;
    }

    public AssetDescriptor<Texture> getIconAsset() {
        return iconAsset;
    }

    public AssetDescriptor<Texture> getPreviewAsset() {
        return previewAsset;
    }

    public AssetDescriptor<Texture> getBattleMaleAsset() {
        return battleMaleAsset;
    }

    public AssetDescriptor<Texture> getBattleFemaleAsset() {
        return battleFemaleAsset;
    }

    public int getResearchLevel() {
        return researchLevel;
    }

    public void setResearchLevel(int researchLevel) {
        this.researchLevel = researchLevel;
    }

    public List<ResearchTaskSet> getTasks() {
        return tasks;
    }

    public void addMove(SpiritMove move) {
        moves.add(move);
    }

    public void addTaskSet(ResearchTaskSet taskSet) {
        tasks.add(taskSet);
    }
}
