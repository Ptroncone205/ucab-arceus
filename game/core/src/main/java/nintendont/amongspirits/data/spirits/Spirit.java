package nintendont.amongspirits.data.spirits;

import nintendont.amongspirits.data.codex.SpiritForm;

public class Spirit{
    public String name, lastName;
    public String biography;
    public int id;
    public boolean gender;
    public SpiritForm form;
    public float hp;
    public float hpMax;
    public String type;
    public String texturePath;

    public Spirit(String name, String type, float hpMax, String texturePath){
        this.name = name;
        this.type = type;
        this.hpMax = hpMax;
        this.hp = hpMax;
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
