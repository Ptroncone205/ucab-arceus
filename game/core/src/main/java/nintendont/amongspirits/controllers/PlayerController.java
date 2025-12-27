package nintendont.amongspirits.controllers;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.InputAdapter;

import nintendont.amongspirits.Const;
import nintendont.amongspirits.entities.Player;

public class PlayerController extends InputAdapter{
    Player player;
    Camera camera;
    boolean jump, up, down, left, right;
    Vector3 moveDir = new Vector3();

    public PlayerController (Player player, Camera camera){
        super();
        up = down = left = right = false;
        this.player = player;
        this.camera = camera;

    }

    public void update(float dt){
        Vector3 camForward = camera.direction.cpy();
        camForward.y = 0;
        camForward.nor();

        Vector3 camRight = camera.direction.cpy().crs(camera.up);
        camRight.y = 0;
        camRight.nor();

        if (up == true){
            moveDir.add(camForward);
        }
        
        if (down == true){
            moveDir.sub(camForward);
        }
        
        if (left == true){
            moveDir.sub(camRight);
        }
        
        if (right == true){
            moveDir.add(camRight);
        }
        moveDir.nor();
        player.move(moveDir, dt);

        if (jump == true){
            player.jump();
        }

        moveDir.set(0,0,0);
    }

    @Override
    public boolean keyDown(int keycode) {
        System.out.println("reached");
        if (Const.currentState != Const.GameState.INGAME) return false;
        switch (keycode) {
            case Keys.W: up = true;  break;
            case Keys.S: down = true; break;
            case Keys.A: left = true; break;
            case Keys.D: right = true; break;
            case Keys.SPACE: jump = true; break;
            default: return false;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode){
        switch (keycode){
            case Keys.W: up = false;  break;
            case Keys.S: down = false; break;
            case Keys.A: left = false; break;
            case Keys.D: right = false; break;
            case Keys.SPACE: jump = false; break;
            default: return false;
        }
        return true;
    }
}

