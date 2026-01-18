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
        this.setBackground(skin.newDrawable(new TextureRegionDrawable(new Texture(new Pixmap(Gdx.files.internal("textures/menu/arceus.png"))))));

        Label title = new Label("MENU DE AYUDA", skin);
        title.setFontScale(2.5f);

        Table titleTable = new Table();

        titleTable.setBackground(skin.newDrawable("white", Color.BLACK));
        titleTable.add(title).pad(20, 50, 20, 50);

        // Sub Menus
        Introduccion intro = new Introduccion(skin,menu);
        menu.addMenu("Introduccion", intro);

        InventarioHelp inv = new InventarioHelp(skin, menu);
        menu.addMenu("Inventario",inv);


        TextButton btnIntro = createButton("Introduccion al Mundo", ()-> menu.setMenu("Introduccion"));
        TextButton btnInv = createButton("Inventario", () -> menu.setMenu("Inventario"));
        TextButton btnBattle = createButton("Batalla", () -> System.out.println("Battle"));
        TextButton btnCodex = createButton("Codex", () -> System.out.println("Codex"));
        TextButton btnBack = createButton("Volver", this::back);

        this.center();
        this.add(titleTable).padBottom(50).row();

        this.add(btnIntro).width(400).height(60).padBottom(10).row();
        this.add(btnInv).width(400).height(60).padBottom(10).row();
        this.add(btnBattle).width(400).height(60).padBottom(10).row();
        this.add(btnCodex).width(400).height(60).padBottom(10).row();

        this.add(btnBack).width(200).height(50).padTop(40).row();
    }
}
