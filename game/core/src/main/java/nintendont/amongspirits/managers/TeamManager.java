package nintendont.amongspirits.managers;

import java.util.ArrayList;

import nintendont.amongspirits.entities.ItemStack;
import nintendont.amongspirits.entities.Pokemon;
import nintendont.amongspirits.entities.items.Item;

public class TeamManager {
    private ArrayList<Pokemon> team;
    private final int SLOTS = 6;

    public TeamManager (){
        this.team = new ArrayList<>();
    }

    public boolean addPkmn(Pokemon pkmn){
        if (team.size() >= SLOTS){
            return false;
        }

        team.add(new Pokemon());
        return true;
    }

    public void removePkmn(Pokemon pkmn){
        team.remove(pkmn);
    }

    public ArrayList<Pokemon> getTeam(){
        return team;
    }

    // public void useItem(Pokemon item){
    //     item.useItem();
    //     if (item.count == 0){
    //         team.remove(item);
    //     }
    // }
}
