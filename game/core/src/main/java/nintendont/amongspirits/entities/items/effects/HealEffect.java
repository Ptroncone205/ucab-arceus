package nintendont.amongspirits.entities.items.effects;

import nintendont.amongspirits.entities.Pokemon;

public class HealEffect implements ItemEffect{
    public int value;
    public HealEffect (int value){
        this.value = value;
    }
    @Override
    public void apply(Pokemon pokemon) {
        System.out.println(pokemon.nick + "healed by" + value + "HP");
    }
}
