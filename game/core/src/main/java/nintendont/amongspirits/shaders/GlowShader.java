package nintendont.amongspirits.shaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;

public class GlowShader extends DefaultShader {
    public int u_glowColour;
    public int u_glowIntensity;

    public GlowShader(Renderable renderable) {
        super(renderable, createConfig());
        u_glowColour = register("u_glowColour");
        u_glowIntensity = register("u_glowIntensity");
    }

    private static Config createConfig() {
        Config config = new Config();
        config.vertexShader = null; 
        config.fragmentShader = Gdx.files.internal("shaders/glow.frag.glsl").readString();
        config.numBones = 80;
        config.ignoreUnimplemented = false;
        config.defaultCullFace = GL20.GL_BACK;
        return config;
    }

    @Override
    public boolean canRender(Renderable instance) {
        return instance.material.has(GlowAttribute.Glow);
    }

    @Override
    public void render(Renderable renderable) {
        context.setBlending(true, GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        
        if (renderable.material.has(GlowAttribute.Glow)) {
            GlowAttribute glow = (GlowAttribute) renderable.material.get(GlowAttribute.Glow);
            set(u_glowColour, glow.glowColor.r, glow.glowColor.g, glow.glowColor.b);
            set(u_glowIntensity, glow.glowIntensity);
        }

        super.render(renderable);
    }
}