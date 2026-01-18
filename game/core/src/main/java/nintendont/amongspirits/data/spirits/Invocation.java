package nintendont.amongspirits.data.spirits;
import java.util.ArrayList;

public class Invocation {
    private Spirit spirit;
    private Stats stats;
    private ArrayList<SpiritMove> moves = new ArrayList<>();
    private int level;

    public Invocation(Spirit spirit, int level){
        this.spirit = spirit;
        this.level = level;
    }

    public Spirit getSpirit(){
        return spirit;
    }

    public ArrayList<SpiritMove> getMoves(){
        return moves;
    }

    public int getLevel(){
        return level;
    }
}
