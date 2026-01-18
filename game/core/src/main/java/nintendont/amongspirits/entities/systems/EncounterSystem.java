package nintendont.amongspirits.entities.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.physics.bullet.collision.ContactResultCallback;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObjectWrapper;
import com.badlogic.gdx.physics.bullet.collision.btManifoldPoint;
import nintendont.amongspirits.data.spirits.Invocation;
import nintendont.amongspirits.data.spirits.Spirit;
import nintendont.amongspirits.data.spirits.SpiritGenders;
import nintendont.amongspirits.data.spirits.Team;
import nintendont.amongspirits.entities.Enemy;
import nintendont.amongspirits.entities.Player;
import nintendont.amongspirits.entities.components.SpiritTagComponent;
import nintendont.amongspirits.screens.BattleScreen;
import nintendont.amongspirits.Main;
import nintendont.amongspirits.entities.components.ChallengerComponent;
import nintendont.amongspirits.entities.components.TriggerComponent;
import nintendont.amongspirits.physics.PhysicsWorld;

public class EncounterSystem extends IteratingSystem {
    private Engine engine;

    private final Main game;
    private final Player player;
    private final PhysicsWorld world;

    private final ComponentMapper<ChallengerComponent> challengerMapper = ComponentMapper.getFor(ChallengerComponent.class);
    private final ComponentMapper<SpiritTagComponent> spiritTagMapper = ComponentMapper.getFor(SpiritTagComponent.class);
    private final ComponentMapper<TriggerComponent> triggerMapper = ComponentMapper.getFor(TriggerComponent.class);

    public EncounterSystem(Main game, Player player, PhysicsWorld world) {
        super(Family.all(ChallengerComponent.class, TriggerComponent.class).get());
        this.game = game;
        this.player = player;
        this.world = world;
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        this.engine = engine;
    }

    @Override
    protected void processEntity(Entity entity, float v) {
        TriggerComponent trigger = triggerMapper.get(entity);

        ContactResultCallback callback = new ContactResultCallback() {
            @Override
            public float addSingleResult(btManifoldPoint cp,
                                         btCollisionObjectWrapper colObj0Wrap, int partId0, int index0,
                                         btCollisionObjectWrapper colObj1Wrap, int partId1, int index1) {
                btCollisionObject other = colObj0Wrap.getCollisionObject() == trigger.bulletObject
                    ? colObj1Wrap.getCollisionObject()
                    : colObj0Wrap.getCollisionObject();

                if (other.userData instanceof Entity) {
                    Entity otherEntity = (Entity) other.userData;
                    handleTrigger(entity, otherEntity);
                }
                return 0;
            }
        };
        callback.setCollisionFilterGroup(trigger.group);
        callback.setCollisionFilterMask(trigger.mask);

        world.getDynamicsWorld().contactTest(trigger.bulletObject, callback);
    }

    public void handleTrigger(Entity entity, Entity otherEntity) {
        ChallengerComponent challenger = challengerMapper.get(entity);

        SpiritTagComponent spiritTag = spiritTagMapper.get(otherEntity);

        if (spiritTag == null) {
            return;
        }

        Team enemyTeam = new Team();
        enemyTeam.getMembers().add(new Invocation(spiritTag.spirit));
        Enemy enemy = new Enemy("Wild Spirit", enemyTeam);

        game.setScreen(new BattleScreen(game, player, enemy, challenger.teamMemberId));
    }
}
