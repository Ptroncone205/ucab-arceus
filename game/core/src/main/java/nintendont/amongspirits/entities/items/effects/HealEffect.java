package nintendont.amongspirits.entities.items.effects;


import nintendont.amongspirits.data.spirits.Spirit;

public class HealEffect implements ItemEffect{
    public int value;
    public HealEffect (int value){
        this.value = value;
    }
    @Override
    public void apply(Spirit pokemon) {
        System.out.println(pokemon.getName() + "healed by" + value + "HP");
    }
}
