package nintendont.amongspirits.data.codex;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.Texture;

public class CodexPreviewAssets {
    public static final AssetDescriptor<Texture> DEER = createDescriptor("deer");
    public static final AssetDescriptor<Texture> WOLF = createDescriptor("wolf");
    public static final AssetDescriptor<Texture> BUNNY = createDescriptor("bunny");
    public static final AssetDescriptor<Texture> FOX = createDescriptor("fox");
    public static final AssetDescriptor<Texture> LION = createDescriptor("lion");

    public static AssetDescriptor<Texture> createDescriptor(String animalName) {
        return  new AssetDescriptor<>(String.format("sprites/previews/%s.png", animalName), Texture.class);
    }
}
