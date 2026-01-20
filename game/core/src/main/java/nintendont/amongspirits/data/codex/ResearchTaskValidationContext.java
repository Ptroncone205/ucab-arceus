package nintendont.amongspirits.data.codex;

public class ResearchTaskValidationContext {
    private SpiritForm spiritForm;
    private ResearchTaskAction action;
    private ResearchTaskSet targetSet;

    public ResearchTaskValidationContext(SpiritForm spiritForm, ResearchTaskAction action, ResearchTaskSet targetSet) {
        this.spiritForm = spiritForm;
        this.action = action;
        this.targetSet = targetSet;
    }

    public SpiritForm getSpiritForm() {
        return spiritForm;
    }

    public ResearchTaskAction getAction() {
        return action;
    }

    public ResearchTaskSet getTargetSet() {
        return targetSet;
    }
}
