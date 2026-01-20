package nintendont.amongspirits.data.codex;

import java.util.Arrays;

public class ResearchTaskSet {
    private final ResearchTask base;
    private final Milestone[] milestones;
    private final boolean boosted;
    private int currentCount;

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

    public void setCurrentCount(int currentCount) {
        this.currentCount = currentCount;
    }

    public void increaseCount() {
        setCurrentCount(getCurrentCount() + 1);
    }

    public int getResearchPoints() {
        int multiplier = boosted ? 2 : 1;
        int milestonesReached = (int)Arrays.stream(milestones).filter(m -> m.getTargetCount() <= currentCount).count();
        return milestonesReached * multiplier;
    }
}
