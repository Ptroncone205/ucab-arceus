package nintendont.amongspirits.data.satchel;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.Texture;

public class Item {
    private int id;
    private String name;
    private String description;
    private boolean isMaterial;
    private AssetDescriptor<Texture> iconAsset;

    public Item() {
    }

    public Item(int id, String name, String description, boolean isMaterial, AssetDescriptor<Texture> iconAsset) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.isMaterial = isMaterial;
        this.iconAsset = iconAsset;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isMaterial() {
        return isMaterial;
    }

    public AssetDescriptor<Texture> getIconAsset() {
        return iconAsset;
    }
}
