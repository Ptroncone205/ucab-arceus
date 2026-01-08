package nintendont.amongspirits.ui.menu;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class TextInputBox extends Table{
    private String text;
    private Skin skin;
    private Label labe;

    public TextInputBox (Skin skin){
        this.skin = skin;
        text = "";

    }

    public boolean handleInput(int key){
        text = Character.toString(key);
        return false;
    }

}
