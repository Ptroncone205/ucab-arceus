package nintendont.amongspirits.data.spirits;

import java.util.ArrayList;

public class Pasture {
    private ArrayList<Invocation> invocations = new ArrayList<>(50);
    public Pasture(){}
    public Pasture (ArrayList<Invocation> invocations){
        this.invocations = invocations;
    }
    public ArrayList<Invocation> getInvocations() {
        return invocations;
    }
}
