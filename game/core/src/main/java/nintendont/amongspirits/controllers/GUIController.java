package nintendont.amongspirits.controllers;

import com.badlogic.gdx.InputAdapter;

import nintendont.amongspirits.Const;
import nintendont.amongspirits.Const.GameState;
import nintendont.amongspirits.ui.GUIManager;

public class GUIController extends InputAdapter {
    GUIManager gui;

    public GUIController (GUIManager gui){
        this.gui = gui;
    }

    @Override
    public boolean keyDown(int keycode){
        if (Const.currentState == GameState.INGAME) return false;
        return gui.handleInput(keycode);
    }
}
