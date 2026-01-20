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
            CodexIconAssets.DEER,
            CodexPreviewAssets.DEER,
            BattleSpiritAssets.MALE_DEER,
            BattleSpiritAssets.FEMALE_DEER);
        SpiritForm wolf = new SpiritForm(
            CodexCommons.WOLF_ID,
            "Wolf",
            "Moon-howling guardian",
            "A shadow with yellow eyes, it sings to the pale moon and guards the snowy mountain passes from those who carry hate.",
            SpiritElement.ICE,
            CodexIconAssets.WOLF,
            CodexPreviewAssets.WOLF,
            BattleSpiritAssets.MALE_WOLF,
            BattleSpiritAssets.FEMALE_WOLF);
        SpiritForm bunny = new SpiritForm(
            CodexCommons.BUNNY_ID,
            "Bunny",
            "Meadow luck bringer",
            "Small and soft, it hides luck within its twitching nose and burrows deep to whisper secrets to the roots of the world.",
            SpiritElement.ICE,
            CodexIconAssets.BUNNY,
            CodexPreviewAssets.BUNNY,
            BattleSpiritAssets.MALE_BUNNY,
            BattleSpiritAssets.FEMALE_BUNNY);
        SpiritForm fox = new SpiritForm(
            CodexCommons.FOX_ID,
            "Fox",
            "Cunning fire tail",
            "With a tail of flickering flame, it weaves through the tall grass, outsmarting dark spirits with a clever, toothy grin.",
            SpiritElement.ICE,
            CodexIconAssets.FOX,
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
        SpiritForm phoenix = new SpiritForm(
            CodexCommons.PHOENIX_ID,
            "Phoenix",
            "Eternal flame sentinel",
            "The celestial sentinel of the South, embodying the eternal element of fire. Its shimmering crimson wings bring omens of peace, prosperity, and the sun’s rebirth.",
            SpiritElement.FIRE,
            CodexIconAssets.PHOENIX,
            CodexPreviewAssets.PHOENIX,
            BattleSpiritAssets.WU_ZETIAN,
            BattleSpiritAssets.WU_ZETIAN);

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
        SpiritMove superFlare = new SpiritMove(
            9,
            "Super Llamarada",
            "",
            SpiritMoveCategory.PHYSICAL,
            14,
            70,
            70,
            90);
        SpiritMove hotRay = new SpiritMove(
            10,
            "Rayo Ardiente",
            "",
            SpiritMoveCategory.PHYSICAL,
            10,
            70,
            70,
            90);
        SpiritMove solarWave = new SpiritMove(
            11,
            "Onda Solar",
            "",
            SpiritMoveCategory.ESPECIAL,
            25,
            70,
            70,
            90);
        SpiritMove rebirth = new SpiritMove(
            12,
            "Renacer",
            "",
            SpiritMoveCategory.STATUS,
            6,
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
        phoenix.addMove(superFlare);
        phoenix.addMove(hotRay);
        phoenix.addMove(solarWave);
        phoenix.addMove(rebirth);

        ResearchTaskFactory researchTaskFactory = new ResearchTaskFactory();
        ResearchTask catchTask = researchTaskFactory.createCatchTask();
        ResearchTask defeatTask = researchTaskFactory.createDefeatTask();
        ResearchTask winTask = researchTaskFactory.createWinTask();

        Milestone[] easyMilestones = new Milestone[] {
            new Milestone(1),
            new Milestone(2),
            new Milestone(4),
            new Milestone(10),
            new Milestone(15),
        };

        Milestone[] normalMilestones = new Milestone[] {
            new Milestone(1),
            new Milestone(3),
            new Milestone(6),
            new Milestone(12),
            new Milestone(25),
        };

        deer.addTaskSet(new ResearchTaskSet(catchTask, easyMilestones, true, 0));
        deer.addTaskSet(new ResearchTaskSet(winTask, easyMilestones, false, 0));

        wolf.addTaskSet(new ResearchTaskSet(catchTask, normalMilestones, true, 0));
        wolf.addTaskSet(new ResearchTaskSet(defeatTask, normalMilestones, true, 0));
        wolf.addTaskSet(new ResearchTaskSet(winTask, easyMilestones, false, 0));

        bunny.addTaskSet(new ResearchTaskSet(catchTask, easyMilestones, true, 0));
        bunny.addTaskSet(new ResearchTaskSet(winTask, easyMilestones, false, 0));

        fox.addTaskSet(new ResearchTaskSet(catchTask, normalMilestones, true, 0));
        fox.addTaskSet(new ResearchTaskSet(defeatTask, easyMilestones, false, 0));
        fox.addTaskSet(new ResearchTaskSet(winTask, easyMilestones, false, 0));

        lion.addTaskSet(new ResearchTaskSet(catchTask, normalMilestones, false, 0));
        lion.addTaskSet(new ResearchTaskSet(defeatTask, easyMilestones, true, 0));
        lion.addTaskSet(new ResearchTaskSet(winTask, easyMilestones, false, 0));

        phoenix.addTaskSet(new ResearchTaskSet(defeatTask, new Milestone[] { new Milestone(1) }, false, 0));

        codex.addForm(deer);
        codex.addForm(wolf);
        codex.addForm(bunny);
        codex.addForm(fox);
        codex.addForm(lion);
        codex.addForm(phoenix);

        return codex;
    }
}
