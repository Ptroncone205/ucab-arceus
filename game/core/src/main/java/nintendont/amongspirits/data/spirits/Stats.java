package nintendont.amongspirits.data.spirits;

public class Stats {
    private final RangedStatValue hp;
    private final StatValue attack, defense, specialAttack, specialDefense, speed;
    
    public Stats() {
        hp = new RangedStatValue(100, 100);
        attack =  new StatValue(15);
        defense =  new StatValue(15);
        specialAttack =  new StatValue(20);
        specialDefense =  new StatValue(20);
        speed =  new StatValue(7);
    }

    public RangedStatValue getHP() {
        return hp;
    }

    public StatValue getAttack() {
        return attack;
    }

    public StatValue getDefense() {
        return defense;
    }

    public StatValue getSpecialAttack() {
        return specialAttack;
    }

    public StatValue getSpecialDefense() {
        return specialDefense;
    }

    public StatValue getSpeed() {
        return speed;
    }
}
