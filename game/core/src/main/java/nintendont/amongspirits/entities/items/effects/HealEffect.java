package nintendont.amongspirits.entities.items.effects;


import nintendont.amongspirits.data.spirits.Invocation;
import nintendont.amongspirits.data.spirits.Spirit;

public class HealEffect implements ItemEffect{
    public int value;
    public HealEffect (int value){
        this.value = value;
    }
    @Override
    public void apply(Invocation pokemon) {
        pokemon.heal(value);
    }
}
