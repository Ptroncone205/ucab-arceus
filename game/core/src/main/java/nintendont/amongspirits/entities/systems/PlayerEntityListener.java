package nintendont.amongspirits.entities.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import nintendont.amongspirits.entities.Player;
import nintendont.amongspirits.entities.components.ModelComponent;
import nintendont.amongspirits.entities.components.PlayerTagComponent;
import nintendont.amongspirits.entities.components.RigidbodyComponent;
import nintendont.amongspirits.entities.components.TriggerComponent;

public class PlayerEntityListener implements EntityListener {
    private final ComponentMapper<PlayerTagComponent> playerTagMapper = ComponentMapper.getFor(PlayerTagComponent.class);
    private final ComponentMapper<ModelComponent> modelMapper = ComponentMapper.getFor(ModelComponent.class);
    private final ComponentMapper<RigidbodyComponent> rigidbodyMapper = ComponentMapper.getFor(RigidbodyComponent.class);

    private final Player player;

    public PlayerEntityListener(Player player) {
        this.player = player;
    }

    @Override
    public void entityAdded(Entity entity) {
        PlayerTagComponent playerTag = playerTagMapper.get(entity);
        ModelComponent model = modelMapper.get(entity);
        RigidbodyComponent rigidbody = rigidbodyMapper.get(entity);

        if (playerTag == null) {
            return;
        }

        player.setScene(model.gltfScene);
        player.setRigidBody(rigidbody.bulletBody);
    }

    @Override
    public void entityRemoved(Entity entity) {
        player.setScene(null);
        player.setRigidBody(null);
    }
}
