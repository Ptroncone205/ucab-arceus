package nintendont.amongspirits.entities.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import nintendont.amongspirits.Const;
import nintendont.amongspirits.entities.ItemStack;
import nintendont.amongspirits.entities.Player;
import nintendont.amongspirits.entities.items.Pokeball;
import nintendont.amongspirits.entities.spawners.YumenjiangSpawner;

import java.util.Optional;

public class YumenjiangSystem extends EntitySystem {
    private final Player player;
    private final YumenjiangSpawner spawner;
    private final Camera camera;

    public YumenjiangSystem(
        InputMultiplexer multiplexer,
        Player player,
        YumenjiangSpawner spawner,
        Camera camera
    ) {
        this.player = player;
        this.spawner = spawner;
        this.camera = camera;
        multiplexer.addProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (Const.currentState != Const.GameState.INGAME) {
                    return false;
                }

                if (keycode == Input.Keys.R) {
                    if (player.getMode() == Player.ThrowingMode.TO_CATCH) {
                        player.setMode(Player.ThrowingMode.TO_ENCOUNTER);
                    } else {
                        player.setMode(Player.ThrowingMode.TO_CATCH);
                    }
                }
                return super.keyDown(keycode);
            }

            @Override
            public boolean scrolled(float amountX, float amountY) {
                if (Const.currentState != Const.GameState.INGAME) {
                    return false;
                }

                if (player.getMode() != Player.ThrowingMode.TO_ENCOUNTER) {
                    return false;
                }

                int memberCount = player.getTeam().getMembers().size();
                if (amountY > 0) {
                    player.setSelectedTeamMemberIndex((memberCount + player.getSelectedTeamMemberIndex() - 1) % memberCount);
                } else {
                    player.setSelectedTeamMemberIndex((player.getSelectedTeamMemberIndex() + 1) % memberCount);
                }

                return super.scrolled(amountX, amountY);
            }
        });
    }

    @Override
    public void update(float delta) {
        if (Const.currentState != Const.GameState.INGAME) {
            return;
        }

        if (Gdx.input.justTouched()) {
            if (player.getMode() == Player.ThrowingMode.TO_CATCH) {
                Optional<ItemStack> stack = player.getSatchel().getItems().stream()
                    .filter(i -> i.getItem() instanceof Pokeball).findFirst();

                if (stack.isPresent()) {
                    if (stack.get().count > 0) {
                        stack.get().count -= 1;
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            }

            Vector3 spawnPoint = new Vector3(player.playerPos).add(Vector3.Y.cpy().scl(2f));
            Vector3 throwDirection = camera.direction.cpy();
            throwDirection.add(new Vector3(0, 0.5f, 0));

            if (player.getMode() == Player.ThrowingMode.TO_CATCH) {
                spawner.spawnThrowableYumenjiangToCatch(spawnPoint, throwDirection, 50);
            } else {
                spawner.spawnThrowableYumenjiangToChallenge(spawnPoint, throwDirection, 50, player.getSelectedTeamMemberIndex());
            }
        }
    }
}
