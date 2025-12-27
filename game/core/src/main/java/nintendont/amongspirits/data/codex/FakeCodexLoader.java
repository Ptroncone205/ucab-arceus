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
            CodexPreviewAssets.DEER);
        SpiritForm wolf = new SpiritForm(
            2,
            "Wolf",
            "Moon-howling guardian",
            "A shadow with yellow eyes, it sings to the pale moon and guards the snowy mountain passes from those who carry hate.",
            SpiritElement.ICE,
            CodexIconAssets.LION,
            CodexPreviewAssets.WOLF);
        SpiritForm bunny = new SpiritForm(
            3,
            "Bunny",
            "Meadow luck bringer",
            "Small and soft, it hides luck within its twitching nose and burrows deep to whisper secrets to the roots of the world.",
            SpiritElement.ICE,
            CodexIconAssets.LION,
            CodexPreviewAssets.BUNNY);
        SpiritForm bear = new SpiritForm(
            4,
            "Bear",
            "Sleeping earth giant",
            "A mountain of fur, it dreams of old magic during the long frost and wakes to protect the honey-filled woods with a roar.",
            SpiritElement.ICE,
            CodexIconAssets.LION,
            CodexPreviewAssets.LION);
        SpiritForm fox = new SpiritForm(
            5,
            "Fox",
            "Cunning fire tail",
            "With a tail of flickering flame, it weaves through the tall grass, outsmarting dark spirits with a clever, toothy grin.",
            SpiritElement.ICE,
            CodexIconAssets.LION,
            CodexPreviewAssets.FOX);
        SpiritForm lion = new SpiritForm(
            6,
            "Lion",
            "Golden sun monarch",
            "Wearing a mane of sunlight, the king of the plains roars a command that makes the stars tremble and the golden grass bow.",
            SpiritElement.FIRE,
            CodexIconAssets.LION,
            CodexPreviewAssets.LION);
        SpiritForm horse = new SpiritForm(
            7,
            "Horse",
            "Wind-galloping steed",
            "Born of the north wind, its hooves strike sparks against the stones as it carries heroes toward the edge of the world.",
            SpiritElement.FIRE,
            CodexIconAssets.LION,
            CodexPreviewAssets.LION);
        SpiritForm basilisk = new SpiritForm(
            8,
            "Basilisk",
            "Stone-gazing serpent",
            "A scaled king with a crown of bone, whose emerald gaze turns the bravest knights into silent statues of cold, grey stone.",
            SpiritElement.FIRE,
            CodexIconAssets.LION,
            CodexPreviewAssets.LION);
        SpiritForm frog = new SpiritForm(
            9,
            "Frog",
            "Swamp potion singer",
            "Seated on a mossy throne, this emerald singer bubbles spells that turn swamp water into liquid starlight at midnight.",
            SpiritElement.FIRE,
            CodexIconAssets.LION,
            CodexPreviewAssets.LION);
        SpiritForm monkey = new SpiritForm(
            10,
            "Monkey",
            "Mischievous vine acrobat",
            "A spirit of the canopy, it steals golden fruit and swings on silver vines, mocking the giants who walk on the forest floor.",
            SpiritElement.FIRE,
            CodexIconAssets.LION,
            CodexPreviewAssets.LION);
        SpiritForm owl = new SpiritForm(
            11,
            "Owl",
            "Wise night watcher",
            "Silent wings of feathered starlight carry the forest's oldest scholar, whose amber eyes see every truth in the shadows.",
            SpiritElement.FIRE,
            CodexIconAssets.LION,
            CodexPreviewAssets.LION);
        SpiritForm armadillo = new SpiritForm(
            12,
            "Armadillo",
            "Rolling shield knight",
            "A knight in plates of living iron, it curls into a ball to defy the sharpest claws and waits for danger to pass by.",
            SpiritElement.FIRE,
            CodexIconAssets.LION,
            CodexIconAssets.LION);

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
//        codex.addForm(bear);
        codex.addForm(fox);
        codex.addForm(lion);
//        codex.addForm(horse);
//        codex.addForm(basilisk);
//        codex.addForm(frog);
//        codex.addForm(monkey);
//        codex.addForm(owl);
//        codex.addForm(armadillo);

        return codex;
    }
}
