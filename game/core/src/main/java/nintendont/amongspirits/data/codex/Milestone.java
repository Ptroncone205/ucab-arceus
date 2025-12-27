package nintendont.amongspirits.data.codex;

public class Milestone {
    private final int targetCount;
    private boolean isDone;

    public Milestone(int targetCount, boolean isDone) {
        this.targetCount = targetCount;
        this.isDone = isDone;
    }

    public int getTargetCount() {
        return targetCount;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }
}
