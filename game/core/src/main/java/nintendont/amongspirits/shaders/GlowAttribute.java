package nintendont.amongspirits.shaders;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Attribute;

public class GlowAttribute extends Attribute {
    public static final String Alias = "GlowAttribute";
    public static final long Glow = register(Alias);

    public Color glowColor = new Color(1, 1, 1, 1);
    public float glowIntensity = 1.0f;

    public GlowAttribute(Color color, float intensity) {
        super(Glow);
        if (color != null) this.glowColor.set(color);
        this.glowIntensity = intensity;
    }

    @Override
    public Attribute copy() {
        return new GlowAttribute(glowColor, glowIntensity);
    }

    @Override
    protected boolean equals(Attribute other) {
        GlowAttribute attr = (GlowAttribute) other;
        return glowColor.equals(attr.glowColor) &&
               glowIntensity == attr.glowIntensity;
    }

    @Override
    public int compareTo(Attribute o) {
        if (type != o.type) return (int) (type - o.type);
        return 0;
    }
}