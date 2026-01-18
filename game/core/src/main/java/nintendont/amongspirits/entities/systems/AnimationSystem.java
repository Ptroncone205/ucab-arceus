package nintendont.amongspirits.entities.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import nintendont.amongspirits.entities.components.AnimationComponent;

public class AnimationSystem extends IteratingSystem {
    private final ComponentMapper<AnimationComponent> animationMapper = ComponentMapper.getFor(AnimationComponent.class);

    public AnimationSystem() {
        super(Family.all(AnimationComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float delta) {
        AnimationComponent animation =  animationMapper.get(entity);
        animation.controller.update(delta);
    }
}
