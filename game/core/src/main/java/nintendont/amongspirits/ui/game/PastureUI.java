package nintendont.amongspirits.ui.game;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import nintendont.amongspirits.data.spirits.Invocation;
import nintendont.amongspirits.data.spirits.Pasture;

public class PastureUI extends Table {
    private  Skin skin;
    private Pasture pasture;
    public PastureUI (Skin skin, Pasture pasture){
        this.skin = skin;
        this.pasture = pasture;
    }

    public void update(){
        for (Invocation inv : pasture.getInvocations()){

        }
    }

    public Table createInvTable(){
        Table invTable = new Table();
        Image healthBar = new Image();
        return invTable;
    }
}
