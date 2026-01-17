package nintendont.amongspirits.data.spirits;

public class Spirit {
    public String name;
    public float hp;
    public float hpMax;
    public String type;
    public String[] moves;
    public String texturePath;

    public Spirit(String name, String type, float hpMax, String[] moves, String texturePath){
        this.name = name;
        this.type = type;
        this.hpMax = hpMax;
        this.hp = hpMax;
        this.moves = moves;
        this.texturePath = texturePath;
    }

    public boolean isFainted(){
        return hp <= 0;
    }

    public void heal(float amount){
        this.hp += amount;
        if (this.hp > this.hpMax) this.hp = this.hpMax;
    }
}
