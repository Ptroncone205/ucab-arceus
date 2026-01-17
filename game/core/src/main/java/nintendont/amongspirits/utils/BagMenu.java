package nintendont.amongspirits.utils;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import nintendont.amongspirits.BattleMain;
import nintendont.amongspirits.data.spirits.Spirit;

public class BagMenu extends Table{
    public BagMenu(TextButton.TextButtonStyle style, final BattleMain game){
        TextButton potion = new TextButton("POCIÓN (" + game.getPotions() + ")", style);
        TextButton superPotion = new TextButton("SUPER POCIÓN (" + game.getSuperPotions() + ")", style);
        TextButton back = new TextButton("VOLVER", style);

        potion.addListener(new ClickListener(){
            @Override public void clicked(InputEvent e, float x, float y){
            if(game.getPotions() > 0) {
                game.setPotions(game.getPotions() - 1);
                game.getStage().addActor(new HealMenu(style, game, 40));
                game.onBackSelected();
            }
        }});

        superPotion.addListener(new ClickListener(){
            @Override public void clicked(InputEvent e, float x, float y){
            if(game.getSuperPotions() > 0) {
                game.setSuperPotions(game.getSuperPotions() - 1);
                game.getStage().addActor(new HealMenu(style, game, 80));
                game.onBackSelected();
            }
        }});

        back.addListener(new ClickListener(){
            @Override public void clicked(InputEvent e, float x, float y){
                game.onBackSelected();
            }});

        this.add(potion).expand().fill().pad(2).row();
        this.add(superPotion).expand().fill().pad(2).row();
        this.add(back).expand().fill().pad(2);
    }

    private class HealMenu extends SpiritsMenu{
        public HealMenu(TextButton.TextButtonStyle style, final BattleMain game, final int amount){
            super(style, game, false);
            this.clearChildren();

            this.add(new Label("¿A QUÉ ESPÍRITU DESEAS CURAR?", new Label.LabelStyle(style.font, com.badlogic.gdx.graphics.Color.YELLOW))).pad(20).row();

            Table table = new Table();

            for(int i = 0; i < game.getTeam().length; i++){
                final int idx = i;
                final Spirit spirit = game.getTeam()[i];

                Table card = new Table().pad(10);
                card.setBackground(game.getColoredDrawable(1, 1, new com.badlogic.gdx.graphics.Color(0.1f, 0.1f, 0.1f, 0.8f)));
                card.add(new Image(new com.badlogic.gdx.graphics.Texture(spirit.texturePath))).size(50).padRight(10);

                Table info = new Table();
                info.add(new Label(spirit.name, new Label.LabelStyle(style.font, com.badlogic.gdx.graphics.Color.WHITE))).left().row();

                Stack hpBg = new Stack();
                hpBg.add(new Image(game.getColoredDrawable(120, 10, com.badlogic.gdx.graphics.Color.BLACK)));
                float actualHp = spirit.hp / spirit.hpMax;
                com.badlogic.gdx.graphics.Color barCol = actualHp < 0.2f ? com.badlogic.gdx.graphics.Color.RED : (actualHp < 0.5f ? com.badlogic.gdx.graphics.Color.YELLOW : com.badlogic.gdx.graphics.Color.GREEN);
                hpBg.add(new Container<>(new Image(game.getColoredDrawable(1, 1, spirit.hp <= 0 ? com.badlogic.gdx.graphics.Color.GRAY : barCol))).width(120 * actualHp).height(10).align(com.badlogic.gdx.utils.Align.left));
                info.add(hpBg).size(120, 10);
                card.add(info).pad(10);

                TextButton button = new TextButton("CURAR", style);

                button.addListener(new ClickListener(){
                    @Override public void clicked(InputEvent e, float x, float y){
                    game.applyPotion(idx, amount);
                    HealMenu.this.remove();
                }});
                card.add(button).size(100, 40);
                table.add(card).pad(5);
                if((i + 1) % 2 == 0) table.row();
            }
            this.add(table).row();

            TextButton buttonCancel = new TextButton("CANCELAR", style);

            buttonCancel.addListener(new ClickListener(){
                @Override public void clicked(InputEvent e, float x, float y){
                    HealMenu.this.remove();
                }});
            this.add(buttonCancel).size(200, 50).pad(20);
        }
    }
}
