package nintendont.amongspirits.entities.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector3;

public class ThrowableComponent implements Component {
    public Vector3 direction;
    public float forceMagnitude;
    public boolean triggered;
}
