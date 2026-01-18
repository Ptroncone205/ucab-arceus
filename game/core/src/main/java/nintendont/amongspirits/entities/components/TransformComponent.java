package nintendont.amongspirits.entities.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Matrix4;

public class TransformComponent implements Component {
    public final Matrix4 matrix = new Matrix4();
}
