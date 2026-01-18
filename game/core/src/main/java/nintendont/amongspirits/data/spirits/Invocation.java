package nintendont.amongspirits.data.spirits;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;

public class Invocation {
    private Spirit spirit;
    private Stats stats;
    private ArrayList<SpiritMove> moves = new ArrayList<>();

    public Invocation(Spirit spirit) {
        this.spirit = spirit;
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

    public ArrayList<SpiritMove> getMoves(){
        return moves;
    }

    public boolean isFainted() {
        return stats.getHP().isEmpty();
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
}
