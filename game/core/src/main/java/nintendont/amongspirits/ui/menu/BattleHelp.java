package nintendont.amongspirits.ui.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;

public class BattleHelp extends MenuOverlay {

    public BattleHelp(Skin skin, MenuUI menu) {
        super(skin, menu);

        this.setFillParent(true);
        this.setBackground(skin.newDrawable("white", new Color(0, 0, 0, 0.85f)));

        Label title = new Label("BATTLE", skin);
        title.setFontScale(2f);
        title.setColor(Color.WHITE);

        String textoLargo = "Cuando entres a una batalla se mostraran ambos espíritus, tu aliado y el rival, también se mostrará un menu debajo donde tendrás 4 opciones para elegir en la batalla.\n1-Luchar: Se mostrará otro menu en el cual tu espíritu tendrá 4 ataques para utilizar.\n2-Bolsa:Aquí podras usar los consumibles disponibles en tu inventario para curar a tus espíritus, ¡Recuerda que ellos te están protegiendo, tienes que cuidarlos!.\n3-Equipo: Podrás ver el equipo de espíritus que tengas disponible en ese momento junto a su vida actual, puedes cambiarlos si así lo deseas.\n4-Huir: Tal vez no seas muy fan de la pelea y prefieres simplemente huir.\nRecuerda que después de cada acción tuya el espíritu enemigo va a atacarte, ¡Planea bien tu estrategia para ganar los combates!.";

        Label bodyText = new Label(textoLargo, skin);
        bodyText.setWrap(true);
        bodyText.setAlignment(Align.center);

        Image infoImage = new Image(new Texture(Gdx.files.internal("battleHelp.png")));

        TextButton btnBack = createButton("Entendido", () -> menu.setMenu("help"));

        this.center();
        this.add(title).padTop(30).padBottom(20).row();

        this.add(bodyText).width(600).padBottom(30).row();

        this.add(infoImage).size(720, 320).padBottom(30).row();

        this.add(btnBack).width(200).height(50).padBottom(30).row();
    }
}
