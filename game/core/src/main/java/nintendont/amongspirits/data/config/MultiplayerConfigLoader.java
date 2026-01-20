package nintendont.amongspirits.data.config;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.PropertiesUtils;

import java.io.IOException;

public class MultiplayerConfigLoader {
    public MultiplayerConfig loadFromPropsFile() {
        FileHandle file = Gdx.files.internal("multiplayer.properties");
        ObjectMap<String, String> properties = new ObjectMap<>();
        try {
            PropertiesUtils.load(properties, file.reader());
            String address = properties.get("address");
            int port = Integer.parseInt(properties.get("port"));
            return new MultiplayerConfig(address, port);
        } catch (IOException e) {
            return new MultiplayerConfig("localhost", 8080);
        }
    }
}
