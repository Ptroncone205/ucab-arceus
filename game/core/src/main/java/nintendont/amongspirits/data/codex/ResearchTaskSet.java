package nintendont.amongspirits.data.codex;

public class ResearchTaskSet {
    private final ResearchTask base;
    private final Milestone[] milestones;
    private final boolean boosted;
    private final int currentCount;

    public ResearchTaskSet(
        ResearchTask base,
        Milestone[] milestones,
        boolean boosted,
        int currentCount
    ) {
        this.base = base;
        this.milestones = milestones;
        this.boosted = boosted;
        this.currentCount = currentCount;
    }

    public ResearchTask getBase() {
        return base;
    }

    public String getDescription() {
        return base.getDescription();
    }

    public Milestone[] getMilestones() {
        return milestones;
    }

    public boolean isBoosted() {
        return boosted;
    }

    public int getCurrentCount() {
        return currentCount;
    }
}
