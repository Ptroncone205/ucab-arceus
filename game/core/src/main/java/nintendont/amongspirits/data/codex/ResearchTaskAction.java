package nintendont.amongspirits.data.codex;

public class ResearchTaskAction {
    private ResearchTaskActionType type;

    public ResearchTaskAction(ResearchTaskActionType type) {
        this.type = type;
    }

    public ResearchTaskActionType getType() {
        return type;
    }
}
