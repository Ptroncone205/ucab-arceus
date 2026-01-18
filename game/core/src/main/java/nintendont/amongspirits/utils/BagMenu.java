package nintendont.amongspirits.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import nintendont.amongspirits.screens.BattleScreen;
import nintendont.amongspirits.data.spirits.Invocation;
import nintendont.amongspirits.data.spirits.Spirit;
import nintendont.amongspirits.entities.ItemStack;
import nintendont.amongspirits.entities.items.Consumable;
import java.util.ArrayList;

public class BagMenu extends Table {

    public BagMenu(TextButton.TextButtonStyle style, final BattleScreen game, ArrayList<ItemStack> items){

        this.center();

        Table scrollTable = new Table();
        if (items != null && !items.isEmpty()){
            for (final ItemStack stack : items){
                if (stack.getItem() instanceof Consumable){
                    TextButton btn = new TextButton(stack.getItem().getName() + " x" + stack.getCount(), style);
                    btn.addListener(new ClickListener(){
                        @Override
                        public void clicked(InputEvent e, float x, float y){

                            game.getStage().addActor(new HealMenu(style, game, stack));
                        }
                    });
                    scrollTable.add(btn).size(220, 45).pad(2);
                }
            }
        }else{
            scrollTable.add(new Label("VACÍO", new Label.LabelStyle(style.font, Color.GRAY)));
        }

        ScrollPane scroll = new ScrollPane(scrollTable);
        this.add(scroll).height(130).row();

        TextButton back = new TextButton("VOLVER", style);
        back.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent e, float x, float y) {
                game.onBackSelected();
            }
        });
        this.add(back).size(220, 40).padBottom(50);
    }

    private class HealMenu extends Table {
        public HealMenu(TextButton.TextButtonStyle style, final BattleScreen game, final ItemStack stack) {
            this.setFillParent(true);

            this.setBackground(game.getColoredDrawable(1, 1, new Color(0, 0, 0, 0.85f)));
            this.center();

            this.add(new Label("USAR " + stack.getItem().getName().toUpperCase(),
                new Label.LabelStyle(style.font, Color.YELLOW))).padBottom(20).row();

            Table grid = new Table();
            for (final Invocation inv : game.getPlayer().getTeam().getMembers()) {
                Table card = new Table();
                card.setBackground(game.getColoredDrawable(1, 1, new Color(1, 1, 1, 0.05f)));
                card.pad(10);

                Label nameLabel = new Label(inv.getFullName(), new Label.LabelStyle(style.font, Color.CYAN));
                Label hpText = new Label(inv.getHP() + "/" + inv.getMaxHP(), new Label.LabelStyle(style.font, Color.WHITE));

                card.add(nameLabel).width(120).left();

                float percent = inv.getHealthRatio();
                Stack hpStack = new Stack();
                hpStack.add(new Image(game.getColoredDrawable(1, 1, Color.BLACK)));
                Image bar = new Image(game.getColoredDrawable(1, 1,
                    percent < 0.2f ? Color.RED : (percent < 0.5f ? Color.YELLOW : Color.GREEN)));
                hpStack.add(new Container<>(bar).width(120 * percent).height(10).align(Align.left));

                card.add(hpStack).size(120, 10).padLeft(10).padRight(10);
                card.add(hpText).width(80).right();

                TextButton bCurar = new TextButton("CURAR", style);
                if (inv.isFullyHealthy()) bCurar.setColor(Color.DARK_GRAY);

                bCurar.addListener(new ClickListener(){
                    @Override
                    public void clicked(InputEvent e, float x, float y){
                        if (!inv.isFullyHealthy()){

                            int healPower = stack.getItem().getName().contains("Super") ? 50 : 20;
                            inv.heal(healPower);
                            stack.setCount(stack.getCount() - 1);

                            game.updateHealth();
                            HealMenu.this.remove();
                            game.startEnemyTurn();
                        }
                    }
                });
                card.add(bCurar).size(90, 40).padLeft(15);

                grid.add(card).fillX().pad(4).row();
            }
            this.add(grid).row();

            TextButton btnCancel = new TextButton("CANCELAR", style);
            btnCancel.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent e, float x, float y) {
                    HealMenu.this.remove();
                }
            });
            this.add(btnCancel).size(200, 45).padTop(20);
        }
    }
}
