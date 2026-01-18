package nintendont.amongspirits.ui.menu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;

public class Extra extends MenuOverlay {

    public Extra(Skin skin, MenuUI menu) {
        super(skin, menu);

        this.setFillParent(true);
        this.setBackground(skin.newDrawable("white", new Color(0, 0, 0, 0.85f)));

        Label title = new Label("INFORMACIÓN EXTRA ", skin);
        title.setFontScale(2f);
        title.setColor(Color.WHITE);

        String textoLargo = "Lenguaje de Programación utilizado: Java.\n\nLibrerías externas utilizadas: LibGdx, Bullet.\n\nDesarrolladores: Alfonso Suarez, Daniela Casanova, Paulo Troncone.\n\nDiseñadora artística: Susan Silva.\n\nProfesora: Janelly Bello.\n\nVersión actualizada: 1.0";

        Label bodyText = new Label(textoLargo, skin);
        bodyText.setWrap(true);
        bodyText.setAlignment(Align.center);

        TextButton btnBack = createButton("Entendido", () -> menu.setMenu("help"));

        this.center();
        this.add(title).padBottom(50).row();

        this.add(bodyText).width(600).padBottom(30).row();

        this.add(btnBack).width(200).height(50).padBottom(30).padTop(250).row();
    }
}
