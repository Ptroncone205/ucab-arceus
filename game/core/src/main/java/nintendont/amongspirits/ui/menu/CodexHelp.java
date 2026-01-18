package nintendont.amongspirits.ui.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;

public class CodexHelp extends MenuOverlay {

    public CodexHelp(Skin skin, MenuUI menu) {
        super(skin, menu);

        this.setFillParent(true);
        this.setBackground(skin.newDrawable("white", new Color(0, 0, 0, 0.85f)));

        Label title = new Label("CODEX", skin);
        title.setFontScale(2f);
        title.setColor(Color.WHITE);

        String textoLargo = "Para abrir el codex presiona la tecla C.\nAquí se mostrará información relevante de todos los espíritus que coexisten en este mundo como id, nombre, elemento, cantidad de espíritus atrapados, cantidad de espíritus derrotados,  nivel de búsqueda, y una descripción breve.\nTodo esto se mostrará junto a una imagen de la forma animal del espíritu.";

        Label bodyText = new Label(textoLargo, skin);
        bodyText.setWrap(true);
        bodyText.setAlignment(Align.center);

        Image infoImage = new Image(new Texture(Gdx.files.internal("codexHelp.png")));

        TextButton btnBack = createButton("Entendido", () -> menu.setMenu("help"));

        this.center();
        this.add(title).padTop(30).padBottom(20).row();

        this.add(bodyText).width(600).padBottom(30).row();

        this.add(infoImage).size(900, 400).padBottom(30).row();

        this.add(btnBack).width(200).height(50).padBottom(30).row();
    }
}
