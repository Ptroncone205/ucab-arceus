package nintendont.amongspirits.data.codex;

public class FakeCodexLoader extends CodexLoader {
    @Override
    public Codex load() {
        Codex codex = new Codex();

        SpiritForm lion = new SpiritForm("123", "Lion", SpiritElement.FIRE);
        codex.addForm(lion);

        return codex;
    }
}
