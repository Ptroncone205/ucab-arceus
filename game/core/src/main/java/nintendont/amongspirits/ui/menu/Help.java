package nintendont.amongspirits.ui.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class Help extends MenuOverlay {

    public Help(Skin skin, MenuUI menu) {
        super(skin, menu);

        this.setFillParent(true);
        this.setBackground(skin.newDrawable(new TextureRegionDrawable(new Texture(new Pixmap(Gdx.files.internal("textures/menu/arceus_notitle.png"))))));

        Label title = new Label("AYUDA", skin);
        title.setFontScale(2.5f);

        Table titleTable = new Table();

        titleTable.add(title).pad(10, 50, 20, 50);

        // Sub Menus
        Introduccion intro = new Introduccion(skin,menu);
        menu.addMenu("Introduccion", intro);

        InventarioHelp inv = new InventarioHelp(skin, menu);
        menu.addMenu("Inventario", inv);

        CodexHelp codex = new CodexHelp(skin, menu);
        menu.addMenu("Codex", codex);

        BattleHelp battle = new BattleHelp(skin, menu);
        menu.addMenu("Battle", battle);

        Extra extra = new Extra(skin, menu);
        menu.addMenu("Extra", extra);




        TextButton btnIntro = createButton("Introduccion", ()-> menu.setMenu("Introduccion"));
        TextButton btnInv = createButton("Inventario", () -> menu.setMenu("Inventario"));
        TextButton btnBattle = createButton("Batalla", () -> menu.setMenu("Battle"));
        TextButton btnCodex = createButton("Codex", () -> menu.setMenu("Codex"));
        TextButton btnExtra = createButton("Extra", () -> menu.setMenu("Extra"));
        TextButton btnBack = createButton("Volver", this::back);

        this.center();
        this.add(titleTable).padBottom(50).row();

        this.add(btnIntro).width(150).height(50).padBottom(10).row();
        this.add(btnInv).width(150).height(50).padBottom(10).row();
        this.add(btnBattle).width(150).height(50).padBottom(10).row();
        this.add(btnCodex).width(150).height(50).padBottom(10).row();
        this.add(btnExtra).width(150).height(50).padBottom(10).row();

        this.add(btnBack).width(100).height(50).padTop(40).row();
    }
}
