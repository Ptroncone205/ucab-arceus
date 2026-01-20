package nintendont.amongspirits.data.satchel.effects;

import nintendont.amongspirits.data.spirits.Invocation;

public class HealEffect implements ConsumableEffect {
    public int value;

    public HealEffect (int value){
        this.value = value;
    }

    @Override
    public void apply(Invocation invocation) {
        invocation.heal(value);
    }
}
