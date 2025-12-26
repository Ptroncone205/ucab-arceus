package nintendont.amongspirits.physics;

import com.badlogic.gdx.physics.bullet.collision.ContactListener;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import nintendont.amongspirits.Const;

public class MyContactListener extends ContactListener{
    @Override
    public void onContactStarted(btCollisionObject colObj0, btCollisionObject colObj1) {
        if ((colObj0.getUserValue() == Const.PF_PLAYER) && (colObj1.getUserValue() == Const.PF_ITEM)) {
            System.out.println("picked up");
        } else if ((colObj0.getUserValue() == Const.PF_ITEM) && (colObj1.getUserValue() == Const.PF_PLAYER)) {
            System.out.println("picked up");
        }
    }
}
