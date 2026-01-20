package nintendont.amongspirits.ui.game;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import java.util.ArrayList;

public abstract class MenuTable extends Table {
    protected ArrayList<Actor> actores= new ArrayList<>();
    protected int selected;
    protected GUIManager gui;
    public MenuTable(GUIManager gui){
        this.gui = gui;
    }
    public abstract boolean handleInput(int key);
    public void update(){}
}
