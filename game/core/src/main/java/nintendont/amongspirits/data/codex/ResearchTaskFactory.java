package nintendont.amongspirits.data.codex;

public class ResearchTaskFactory {
    public ResearchTask createCatchTask() {
        String description = "Number caught";
        SolutionAlgorithm algorithm = new CatchSolutionAlgorithm();
        return new ResearchTask(description, algorithm);
    }

    public ResearchTask createDefeatTask() {
        String description = "Number defeated";
        SolutionAlgorithm algorithm = new DefeatSolutionAlgorithm();
        return new ResearchTask(description, algorithm);
    }
}
