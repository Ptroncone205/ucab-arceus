package nintendont.amongspirits.entities;

public class Pokemon extends Entity {
    protected String name;
    public String nick;
    //stats
    protected int atk;
    protected int def;
    protected int spAtk;
    protected int spDef;
    protected int speed;
    public Pokemon (){

        loadModel();
        buildBody();
    }

    public void loadModel(){
    }

    public void buildBody(){
    }

}
