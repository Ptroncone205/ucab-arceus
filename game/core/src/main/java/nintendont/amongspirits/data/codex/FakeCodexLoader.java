package nintendont.amongspirits.data.codex;

public class FakeCodexLoader extends CodexLoader {
    @Override
    public Codex load() {
        Codex codex = new Codex();

        SpiritForm deer = new SpiritForm(
            1,
            "Deer",
            "Forest spirit guide",
            "Crowned with branches, it steps through silver mist, guiding lost travelers back to the safety of ancient forest trails.",
            SpiritElement.ICE,
            CodexIconAssets.LION,
            CodexPreviewAssets.DEER,
            BattleSpiritAssets.MALE_DEER,
            BattleSpiritAssets.FEMALE_DEER);
        SpiritForm wolf = new SpiritForm(
            2,
            "Wolf",
            "Moon-howling guardian",
            "A shadow with yellow eyes, it sings to the pale moon and guards the snowy mountain passes from those who carry hate.",
            SpiritElement.ICE,
            CodexIconAssets.LION,
            CodexPreviewAssets.WOLF,
            BattleSpiritAssets.MALE_WOLF,
            BattleSpiritAssets.FEMALE_WOLF);
        SpiritForm bunny = new SpiritForm(
            3,
            "Bunny",
            "Meadow luck bringer",
            "Small and soft, it hides luck within its twitching nose and burrows deep to whisper secrets to the roots of the world.",
            SpiritElement.ICE,
            CodexIconAssets.LION,
            CodexPreviewAssets.BUNNY,
            BattleSpiritAssets.MALE_BUNNY,
            BattleSpiritAssets.FEMALE_BUNNY);
        SpiritForm fox = new SpiritForm(
            4,
            "Fox",
            "Cunning fire tail",
            "With a tail of flickering flame, it weaves through the tall grass, outsmarting dark spirits with a clever, toothy grin.",
            SpiritElement.ICE,
            CodexIconAssets.LION,
            CodexPreviewAssets.FOX,
            BattleSpiritAssets.MALE_FOX,
            BattleSpiritAssets.FEMALE_FOX);
        SpiritForm lion = new SpiritForm(
            5,
            "Lion",
            "Golden sun monarch",
            "Wearing a mane of sunlight, the king of the plains roars a command that makes the stars tremble and the golden grass bow.",
            SpiritElement.FIRE,
            CodexIconAssets.LION,
            CodexPreviewAssets.LION,
            BattleSpiritAssets.MALE_LION,
            BattleSpiritAssets.FEMALE_LION);

        ResearchTaskFactory researchTaskFactory = new ResearchTaskFactory();
        ResearchTask catchTask = researchTaskFactory.createCatchTask();
        ResearchTask defeatTask = researchTaskFactory.createDefeatTask();

        deer.addTaskSet(new ResearchTaskSet(
            catchTask,
            new Milestone[]{
                new Milestone(1, false),
                new Milestone(2, false),
                new Milestone(4, false),
                new Milestone(10, false),
                new Milestone(15, false),
            },
            true,
            0
        ));

        wolf.addTaskSet(new ResearchTaskSet(
            catchTask,
            new Milestone[]{
                new Milestone(1, false),
                new Milestone(3, false),
                new Milestone(6, false),
                new Milestone(12, false),
                new Milestone(25, false),
            },
            true,
            0
        ));
        wolf.addTaskSet(new ResearchTaskSet(
            defeatTask,
            new Milestone[]{
                new Milestone(1, false),
                new Milestone(3, false),
                new Milestone(6, false),
                new Milestone(12, false),
                new Milestone(25, false),
            },
            true,
            0
        ));

        bunny.addTaskSet(new ResearchTaskSet(
            catchTask,
            new Milestone[]{
                new Milestone(1, false),
                new Milestone(3, false),
                new Milestone(6, false),
                new Milestone(12, false),
                new Milestone(25, false),
            },
            true,
            0
        ));

        fox.addTaskSet(new ResearchTaskSet(
            catchTask,
            new Milestone[]{
                new Milestone(1, false),
                new Milestone(3, false),
                new Milestone(6, false),
                new Milestone(12, false),
                new Milestone(25, false),
            },
            true,
            0
        ));
        fox.addTaskSet(new ResearchTaskSet(
            defeatTask,
            new Milestone[]{
                new Milestone(1, false),
                new Milestone(2, false),
                new Milestone(4, false),
                new Milestone(10, false),
                new Milestone(15, false),
            },
            false,
            0
        ));

        lion.addTaskSet(new ResearchTaskSet(
            catchTask,
            new Milestone[]{
                new Milestone(1, false),
                new Milestone(3, false),
                new Milestone(6, false),
                new Milestone(12, false),
                new Milestone(25, false),
            },
            false,
            0
        ));
        lion.addTaskSet(new ResearchTaskSet(
            defeatTask,
            new Milestone[]{
                new Milestone(1, false),
                new Milestone(2, false),
                new Milestone(4, false),
                new Milestone(10, false),
                new Milestone(15, false),
            },
            true,
            0
        ));

        codex.addForm(deer);
        codex.addForm(wolf);
        codex.addForm(bunny);
        codex.addForm(fox);
        codex.addForm(lion);

        return codex;
    }
}
