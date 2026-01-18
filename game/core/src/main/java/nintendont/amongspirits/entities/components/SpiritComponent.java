package nintendont.amongspirits.entities.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector3;

public class SpiritComponent implements Component {
    public Vector3[] patrolPoints;
    public float stateTime;
    public float speed = 2f;
    public int currentTarget = 1;
    public int status;
}
