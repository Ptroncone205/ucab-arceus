package nintendont.amongspirits.ui.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;

public class Introduccion extends MenuOverlay {

    public Introduccion(Skin skin, MenuUI menu) {
        super(skin, menu);

        this.setFillParent(true);
        this.setBackground(skin.newDrawable("white", new Color(0, 0, 0, 0.85f)));

        Label title = new Label("INTRODUCCION AL MUNDO", skin);
        title.setFontScale(2f);
        title.setColor(Color.WHITE);

        String textoLargo = "¡Bienvenido a Among Spirits fantástico jugador!\n En este mundo podrás interactuar con espíritus elementales que vagan por el mapa en forma de animales.\n Puedes moverte con W A S D y saltar con el espacio.\nLos atrapa espiritus se tiran con el click izquierdo, debes de tener en cuenta que si lo lanzas mientras está vacio atraparás el espíritu salvaje y si lo lanzas con un espíritu dentro entrarás en un combate.\nLos objetos se recogen con la tecla F, ten en cuenta que estos seran necesarios para fabricar objetos consumibles.\nTodo el equipo de Nintendont espera que tengas una experiencia maravillosa y esperamos que puedes disfrutar este juego <3.";

        Label bodyText = new Label(textoLargo, skin);
        bodyText.setWrap(true);
        bodyText.setAlignment(Align.center);

        Image infoImage = new Image(new Texture(Gdx.files.internal("textures/menu/arceus.png")));

        TextButton btnBack = createButton("Entendido", () -> menu.setMenu("help"));

        this.center();
        this.add(title).padTop(30).padBottom(20).row();

        this.add(bodyText).width(600).padBottom(30).row();

        this.add(infoImage).size(250, 250).padBottom(30).row();

        this.add(btnBack).width(200).height(50).padBottom(30).row();
    }
}
