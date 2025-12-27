package nintendont.amongspirits.data.codex;

public class FakeCodexLoader extends CodexLoader {
    @Override
    public Codex load() {
        Codex codex = new Codex();

        SpiritForm deer = new SpiritForm(1, "Deer", SpiritElement.ICE, CodexIconAssets.LION);
        SpiritForm wolf = new SpiritForm(2, "Wolf", SpiritElement.ICE, CodexIconAssets.LION);
        SpiritForm bunny = new SpiritForm(3, "Bunny", SpiritElement.ICE, CodexIconAssets.LION);
        SpiritForm bear = new SpiritForm(4, "Bear", SpiritElement.ICE, CodexIconAssets.LION);
        SpiritForm fox = new SpiritForm(5, "Fox", SpiritElement.ICE, CodexIconAssets.LION);
        SpiritForm lion = new SpiritForm(6, "Lion", SpiritElement.FIRE, CodexIconAssets.LION);
        SpiritForm horse = new SpiritForm(7, "Horse", SpiritElement.FIRE, CodexIconAssets.LION);
        SpiritForm basilisk = new SpiritForm(8, "Basilisk", SpiritElement.FIRE, CodexIconAssets.LION);
        SpiritForm frog = new SpiritForm(9, "Frog", SpiritElement.FIRE, CodexIconAssets.LION);
        SpiritForm monkey = new SpiritForm(10, "Monkey", SpiritElement.FIRE, CodexIconAssets.LION);
        SpiritForm owl = new SpiritForm(11, "Owl", SpiritElement.FIRE, CodexIconAssets.LION);
        SpiritForm armadillo = new SpiritForm(12, "Armadillo", SpiritElement.FIRE, CodexIconAssets.LION);

        codex.addForm(deer);
        codex.addForm(wolf);
        codex.addForm(bunny);
//        codex.addForm(bear);
//        codex.addForm(fox);
//        codex.addForm(lion);
//        codex.addForm(horse);
//        codex.addForm(basilisk);
//        codex.addForm(frog);
//        codex.addForm(monkey);
//        codex.addForm(owl);
//        codex.addForm(armadillo);

        return codex;
    }
}
