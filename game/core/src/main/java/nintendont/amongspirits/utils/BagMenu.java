package nintendont.amongspirits.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import nintendont.amongspirits.data.satchel.ConsumableItem;
import nintendont.amongspirits.screens.BattleScreen;
import nintendont.amongspirits.data.spirits.Invocation;
import nintendont.amongspirits.data.satchel.ItemStack;
import java.util.ArrayList;

public class BagMenu extends Table {

    public BagMenu(TextButton.TextButtonStyle style, final BattleScreen game, ArrayList<ItemStack> items){
        this.center();
        Table scrollTable = new Table();
        if (items != null && !items.isEmpty()){
            for (final ItemStack stack : items){
                if (stack.getItem() instanceof ConsumableItem && stack.getCount() > 0){
                    TextButton btn = new TextButton(stack.getItem().getName() + " x" + stack.getCount(), style);
                    btn.addListener(new ClickListener(){
                        @Override
                        public void clicked(InputEvent e, float x, float y){
                            game.getGame().playSound("music and sounds/sounds/button_sel.mp3");
                            game.getStage().addActor(new HealMenu(style, game, stack));
                        }
                    });
                    scrollTable.add(btn).size(220, 45).pad(2);
                }
            }
        } else {
            scrollTable.add(new Label("BOLSA VACÍA", new Label.LabelStyle(style.font, Color.GRAY)));
        }

        ScrollPane scroll = new ScrollPane(scrollTable);
        this.add(scroll).height(130).row();

        TextButton back = new TextButton("VOLVER", style);
        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                game.getGame().playSound("music and sounds/sounds/button_sel.mp3");
                game.onBackSelected();
            }
        });
        this.add(back).size(220, 40).padBottom(50);
    }

    private class HealMenu extends Table {
        public HealMenu(TextButton.TextButtonStyle style, final BattleScreen game, final ItemStack stack) {
            this.setFillParent(true);
            this.setBackground(game.getColoredDrawable(1, 1, new Color(0, 0, 0, 0.9f)));
            this.center();

            this.add(new Label("¿A QUIÉN CURAR?", new Label.LabelStyle(style.font, Color.YELLOW))).padBottom(20).row();

            Table grid = new Table();
            for (final Invocation inv : game.getPlayer().getTeam().getMembers()) {
                Table card = new Table();
                card.setBackground(game.getColoredDrawable(1, 1, new Color(1, 1, 1, 0.1f)));
                card.pad(10);

                card.add(new Label(inv.getFullName(), new Label.LabelStyle(style.font, Color.CYAN))).width(120);

                float ratio = inv.getHealthRatio();
                card.add(new Image(game.getColoredDrawable((int)(100 * ratio), 10, Color.GREEN))).size(100 * ratio, 10).pad(10);

                TextButton bCurar = new TextButton("USAR", style);
                if (inv.isFullyHealthy() || inv.getHP() <= 0) bCurar.setColor(Color.GRAY);

                bCurar.addListener(new ClickListener(){
                    @Override
                    public void clicked(InputEvent e, float x, float y){
                        if (!inv.isFullyHealthy() && inv.getHP() > 0){
                            inv.heal(25);
                            stack.setCount(stack.getCount() - 1);
                            game.getGame().playSound("music and sounds/sounds/heal.mp3");

                            if (inv == game.getPlayerActiveInvocation()) {
                                game.playHealAnimation(game.getPlayerImage());
                            }

                            game.updateHealth();
                            HealMenu.this.remove();
                            game.startEnemyTurn();
                        }
                    }
                });
                card.add(bCurar).size(80, 40);
                grid.add(card).fillX().pad(2).row();
            }
            this.add(grid).row();

            TextButton btnCancel = new TextButton("CANCELAR", style);
            btnCancel.addListener(new ClickListener(){
                @Override public void clicked(InputEvent e, float x, float y) {
                    game.getGame().playSound("music and sounds/sounds/button_sel.mp3");
                    HealMenu.this.remove();
                }
            });
            this.add(btnCancel).size(150, 40).padTop(20);
        }
    }
}
