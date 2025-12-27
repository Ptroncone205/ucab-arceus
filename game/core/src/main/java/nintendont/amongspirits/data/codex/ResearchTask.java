package nintendont.amongspirits.data.codex;

public class ResearchTask {
    private final String description;
    private final SolutionAlgorithm solutionAlgorithm;

    public ResearchTask(String description, SolutionAlgorithm solutionAlgorithm) {
        this.description = description;
        this.solutionAlgorithm = solutionAlgorithm;
    }

    public String getDescription() {
        return description;
    }

    public SolutionAlgorithm getSolutionAlgorithm() {
        return solutionAlgorithm;
    }
}
