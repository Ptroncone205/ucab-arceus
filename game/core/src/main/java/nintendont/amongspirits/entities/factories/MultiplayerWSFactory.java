package nintendont.amongspirits.entities.factories;

import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.WebSockets;
import nintendont.amongspirits.data.config.MultiplayerConfig;

public class MultiplayerWSFactory {
    public WebSocket createWebSocket(MultiplayerConfig config, String path) {
        String url = WebSockets.toWebSocketUrl(config.address, config.port) + path;
        return WebSockets.newSocket(url);
    }
}
