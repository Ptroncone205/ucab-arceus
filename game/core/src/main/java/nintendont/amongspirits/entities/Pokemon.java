package nintendont.amongspirits.entities;

public class Pokemon extends Entity {
    public String name;
    public String nick;
    //stats
    public int atk;
    public int def;
    public int spAtk;
    public int spDef;
    public int speed;
    public Pokemon (){

        loadModel();
        buildBody();
    }

    public void loadModel(){
    }

    public void buildBody(){
    }

}
