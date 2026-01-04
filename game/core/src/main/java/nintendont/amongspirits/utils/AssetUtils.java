package nintendont.amongspirits.utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;

import net.mgsx.gltf.loaders.gltf.GLTFAssetLoader;
import net.mgsx.gltf.scene3d.scene.SceneAsset;

public class AssetUtils {

    public static void setTrueTypeFontLoaders(AssetManager manager) {
        FileHandleResolver resolver = new InternalFileHandleResolver();
        manager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        manager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
    }

    public static void loadFont(AssetManager manager, String assetKey, String fontFileName, int fontSize) {
        FreetypeFontLoader.FreeTypeFontLoaderParameter params = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        params.fontFileName = fontFileName;
        params.fontParameters.size = fontSize;
        manager.load(assetKey, BitmapFont.class, params);
    }

    public static void loadGLTF(AssetManager manager, String path){
        manager.setLoader(SceneAsset.class, ".gltf", new GLTFAssetLoader(new InternalFileHandleResolver()));
        manager.load(path, SceneAsset.class);
    }
}
