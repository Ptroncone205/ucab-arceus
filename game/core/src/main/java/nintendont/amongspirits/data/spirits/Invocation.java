package nintendont.amongspirits.data.spirits;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import nintendont.amongspirits.data.codex.SpiritMove;

import java.util.ArrayList;

public class Invocation {
    private Spirit spirit;
    private Stats stats;

    public Invocation(Spirit spirit) {
        this.spirit = spirit;
        this.stats = new Stats();
    }

    public Stats getStats() {
        return stats;
    }

    public Spirit getSpirit() {
        return spirit;
    }

    public int getHP() {
        return stats.getHP().getCurrent();
    }

    public int getMaxHP() {
        return stats.getHP().getCurrent();
    }

    public int getAttack() {
        return stats.getAttack().getCurrent();
    }

    public int getSpecialAttack() {
        return stats.getSpecialAttack().getCurrent();
    }

    public int getDefense() {
        return stats.getDefense().getCurrent();
    }

    public int getSpecialDefense() {
        return stats.getSpecialDefense().getCurrent();
    }

    public int getSpeed() {
        return stats.getSpeed().getCurrent();
    }

    public ArrayList<SpiritMove> getMoves(){
        return spirit.getForm().getMoves();
    }


    public SpiritMove getRandomMove(){
        if (getMoves().isEmpty()) return null;
        return getMoves().get(MathUtils.random(0, getMoves().size() - 1));
    }

    public boolean isFainted() {
        return stats.getHP().isEmpty();
    }

    public boolean isActive() {
        return stats.getHP().getCurrent() > 0;
    }

    public boolean isFullyHealthy() {
        return stats.getHP().isFull();
    }

    public float getHealthRatio() {
        return stats.getHP().getRatio();
    }

    public void heal(int points) {
        stats.getHP().setCurrent(stats.getHP().getCurrent() + points);
    }

    public void takeDamage(int damage) {
        playSound("music and sounds/sounds/dmg.mp3");
        stats.getHP().setCurrent(stats.getHP().getCurrent() - damage);
    }

    public AssetDescriptor<Texture> getBattleAsset() {
        return spirit.getGender() == SpiritGenders.FEMALE
            ? spirit.getForm().getBattleFemaleAsset()
            : spirit.getForm().getBattleMaleAsset();
    }

    public String getFullName() {
        return spirit.getName() + " " + spirit.getLastName();
    }

    public void playSound(String path) {
        try {
            Sound sound = Gdx.audio.newSound(Gdx.files.internal(path));
            sound.play(0.3f);

        } catch (Exception e) {
            Gdx.app.error("Sound", "No se pudo reproducir el sonido: " + path);
        }
    }
}
