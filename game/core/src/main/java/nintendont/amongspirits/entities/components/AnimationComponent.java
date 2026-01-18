package nintendont.amongspirits.entities.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;

public class AnimationComponent implements Component {
    public AnimationController controller;
    public boolean autoPlay;
}
