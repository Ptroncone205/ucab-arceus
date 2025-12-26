package nintendont.amongspirits.managers;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import nintendont.amongspirits.entities.items.Item;

public class InteractionScanner {
    public static final float INTERACTION_DIST = 10f;
    public static final float hola = 0f;

    private final Vector3 screenPos = new Vector3();



    public Item findTarget(Vector3 playerPos, Camera camera, List<Item> items) {
        float centerX = Gdx.graphics.getWidth() / 2f;
        float centerY = Gdx.graphics.getHeight() / 2f;
        Item bestItem = null;
        float bestDist = Float.MAX_VALUE;

        for (Item item : items) {

            float distSq = playerPos.dst2(item.pos);
            if (distSq > INTERACTION_DIST) continue;

            screenPos.set(item.pos);
            camera.project(screenPos);

            boolean onScreenX = screenPos.x > 0 && screenPos.x < Gdx.graphics.getWidth();
            boolean onScreenY = screenPos.y > 0 && screenPos.y < Gdx.graphics.getHeight();
            boolean inFront = screenPos.z < 1.0f;

            if (onScreenX && onScreenY && inFront) {

                float distFromCenter = Math.abs(screenPos.x - centerX) + Math.abs(screenPos.y - centerY);

                // prioritize item closest to the center of the screen
                if (distFromCenter < bestDist) {
                    bestDist = distFromCenter;
                    bestItem = item;
                }
            }
        }
        return bestItem;
    }
}
