package nintendont.amongspirits.data.spirits;

import java.util.ArrayList;

public class Team{
    private ArrayList<Invocation> members = new ArrayList<>();

    public Team(){}
    public Team (ArrayList<Invocation> members){
        this.members = members;
    }
    
    public ArrayList<Invocation> getMembers(){
        return members;
    }

    public boolean areAllMembersDefeated() {
        return getMembers().stream().allMatch(Invocation::isFainted);
    }

    public boolean isAnyMemberActive() {
        return getMembers().stream().anyMatch(Invocation::isActive);
    }
}
