package nintendont.amongspirits.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class styleUtils {
    private static TextureRegionDrawable createBarDrawable(float percent){
        int w = 200, h = 20;
        Pixmap p = new Pixmap(w, h, Pixmap.Format.RGBA8888);
        p.setColor(Color.BLACK); p.fill();
        Color colorVida = percent < 0.2f ? Color.RED : (percent < 0.5f ? Color.YELLOW : Color.GREEN);
        p.setColor(colorVida);
        p.fillRectangle(0, 0, (int)(w * Math.max(0, percent)), h);
        TextureRegionDrawable d = new TextureRegionDrawable(new TextureRegion(new Texture(p)));
        p.dispose();
        return d;
    }

    public static TextureRegionDrawable getColoredDrawable(int w, int h, Color c) {
        Pixmap p = new Pixmap(w > 0 ? w : 1, h, Pixmap.Format.RGBA8888);
        p.setColor(c); p.fill();
        TextureRegionDrawable d = new TextureRegionDrawable(new TextureRegion(new Texture(p)));
        p.dispose();
        return d;
    }
}
