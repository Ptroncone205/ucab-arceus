package nintendont.amongspirits.data.codex;

public class WinSolutionAlgorithm extends SolutionAlgorithm {
    @Override
    public boolean validate(ResearchTaskValidationContext context) {
        return context.getAction().getType() == ResearchTaskActionType.WIN;
    }
}
