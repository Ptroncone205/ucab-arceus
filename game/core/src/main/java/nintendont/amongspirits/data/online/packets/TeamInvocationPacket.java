package nintendont.amongspirits.data.online.packets;

public class TeamInvocationPacket {
    public String name;
    public String lastName;
    public boolean gender;
    public int hp;
    public int maxHP;
    public int attack;
    public int specialAttack;
    public int defense;
    public int specialDefense;
    public int speed;
    public int spiritFormId;

    public TeamInvocationPacket() {
    }

    public TeamInvocationPacket(String name, String lastName, boolean gender, int hp, int maxHP, int attack, int specialAttack, int defense, int specialDefense, int speed, int spiritFormId) {
        this.name = name;
        this.lastName = lastName;
        this.gender = gender;
        this.hp = hp;
        this.maxHP = maxHP;
        this.attack = attack;
        this.specialAttack = specialAttack;
        this.defense = defense;
        this.specialDefense = specialDefense;
        this.speed = speed;
        this.spiritFormId = spiritFormId;
    }
}
