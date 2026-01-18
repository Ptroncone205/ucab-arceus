package nintendont.amongspirits.data.codex;

import nintendont.amongspirits.data.spirits.SpiritMoveCategory;

public class FakeCodexLoader extends CodexLoader {
    @Override
    public Codex load() {
        Codex codex = new Codex();

        SpiritForm deer = new SpiritForm(
            CodexCommons.DEER_ID,
            "Deer",
            "Forest spirit guide",
            "Crowned with branches, it steps through silver mist, guiding lost travelers back to the safety of ancient forest trails.",
            SpiritElement.ICE,
            CodexIconAssets.LION,
            CodexPreviewAssets.DEER,
            BattleSpiritAssets.MALE_DEER,
            BattleSpiritAssets.FEMALE_DEER);
        SpiritForm wolf = new SpiritForm(
            CodexCommons.WOLF_ID,
            "Wolf",
            "Moon-howling guardian",
            "A shadow with yellow eyes, it sings to the pale moon and guards the snowy mountain passes from those who carry hate.",
            SpiritElement.ICE,
            CodexIconAssets.LION,
            CodexPreviewAssets.WOLF,
            BattleSpiritAssets.MALE_WOLF,
            BattleSpiritAssets.FEMALE_WOLF);
        SpiritForm bunny = new SpiritForm(
            CodexCommons.BUNNY_ID,
            "Bunny",
            "Meadow luck bringer",
            "Small and soft, it hides luck within its twitching nose and burrows deep to whisper secrets to the roots of the world.",
            SpiritElement.ICE,
            CodexIconAssets.LION,
            CodexPreviewAssets.BUNNY,
            BattleSpiritAssets.MALE_BUNNY,
            BattleSpiritAssets.FEMALE_BUNNY);
        SpiritForm fox = new SpiritForm(
            CodexCommons.FOX_ID,
            "Fox",
            "Cunning fire tail",
            "With a tail of flickering flame, it weaves through the tall grass, outsmarting dark spirits with a clever, toothy grin.",
            SpiritElement.ICE,
            CodexIconAssets.LION,
            CodexPreviewAssets.FOX,
            BattleSpiritAssets.MALE_FOX,
            BattleSpiritAssets.FEMALE_FOX);
        SpiritForm lion = new SpiritForm(
            CodexCommons.LION_ID,
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

        SpiritMove thunderstruck = new SpiritMove(
            1,
            "Impactrueno",
            "",
            SpiritMoveCategory.PHYSICAL,
            10,
            70,
            70,
            90);
        SpiritMove quickAttack = new SpiritMove(
            2,
            "Ataque Rapido",
            "",
            SpiritMoveCategory.PHYSICAL,
            5,
            70,
            70,
            90);
        SpiritMove electricBall = new SpiritMove(
            3,
            "Electro Bola",
            "",
            SpiritMoveCategory.ESPECIAL,
            15,
            70,
            70,
            90);
        SpiritMove roar = new SpiritMove(
            4,
            "Rugido",
            "",
            SpiritMoveCategory.STATUS,
            2,
            70,
            70,
            90);
        SpiritMove iceFang = new SpiritMove(
            5,
            "Colmillo de Hielo",
            "",
            SpiritMoveCategory.PHYSICAL,
            8,
            70,
            70,
            90);
        SpiritMove nibble = new SpiritMove(
            6,
            "Mordisco",
            "",
            SpiritMoveCategory.PHYSICAL,
            4,
            70,
            70,
            90);
        SpiritMove iceClaw = new SpiritMove(
            7,
            "Garra de Hielo",
            "",
            SpiritMoveCategory.ESPECIAL,
            15,
            70,
            70,
            90);
        SpiritMove howl = new SpiritMove(
            8,
            "Aullido",
            "",
            SpiritMoveCategory.STATUS,
            2,
            70,
            70,
            90);
        SpiritMove doubleKick = new SpiritMove(
            5,
            "Doble Patada",
            "",
            SpiritMoveCategory.PHYSICAL,
            8,
            70,
            70,
            90);
        SpiritMove booster = new SpiritMove(
            6,
            "Refuerzo",
            "",
            SpiritMoveCategory.STATUS,
            4,
            70,
            70,
            90);
        SpiritMove iceRay = new SpiritMove(
            7,
            "Rayo Hielo",
            "",
            SpiritMoveCategory.ESPECIAL,
            15,
            70,
            70,
            90);
        SpiritMove agile = new SpiritMove(
            8,
            "Agilidad",
            "",
            SpiritMoveCategory.STATUS,
            2,
            70,
            70,
            90);
        SpiritMove flare = new SpiritMove(
            5,
            "Llamarada",
            "",
            SpiritMoveCategory.PHYSICAL,
            8,
            70,
            70,
            90);
        SpiritMove fireFang = new SpiritMove(
            6,
            "Colmillo Igneo",
            "",
            SpiritMoveCategory.PHYSICAL,
            4,
            70,
            70,
            90);
        SpiritMove scare = new SpiritMove(
            7,
            "Intimidar",
            "",
            SpiritMoveCategory.ESPECIAL,
            15,
            70,
            70,
            90);
        SpiritMove tackle = new SpiritMove(
            8,
            "Derribar",
            "",
            SpiritMoveCategory.STATUS,
            2,
            70,
            70,
            90);

        deer.addMove(thunderstruck);
        deer.addMove(quickAttack);
        deer.addMove(electricBall);
        deer.addMove(roar);
        wolf.addMove(iceClaw);
        wolf.addMove(iceFang);
        wolf.addMove(nibble);
        wolf.addMove(howl);
        bunny.addMove(doubleKick);
        bunny.addMove(booster);
        bunny.addMove(iceRay);
        bunny.addMove(agile);
        fox.addMove(flare);
        fox.addMove(electricBall);
        fox.addMove(howl);
        fox.addMove(tackle);
        lion.addMove(flare);
        lion.addMove(fireFang);
        lion.addMove(scare);
        lion.addMove(tackle);

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
