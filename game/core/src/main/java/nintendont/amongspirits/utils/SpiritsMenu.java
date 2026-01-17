package nintendont.amongspirits.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import nintendont.amongspirits.BattleMain;
import nintendont.amongspirits.data.spirits.Spirit;

public class SpiritsMenu extends Table{
    public SpiritsMenu(TextButton.TextButtonStyle style, final BattleMain game, boolean flag){
        this.setFillParent(true);
        this.setBackground(game.getColoredDrawable(1, 1, new Color(0, 0, 0.4f, 0.95f)));
        this.center();

        String tituloTxt = flag ? "¡DEBILITADO! ELIGE OTRO" : "EQUIPO";
        this.add(new Label(tituloTxt, new Label.LabelStyle(style.font, Color.YELLOW))).colspan(2).pad(20).row();

        Table table = new Table();
        for (int i = 0; i < game.getTeam().length; i++){
            final int idx = i;
            final Spirit spirit = game.getTeam()[i];
            Table card = new Table().pad(10);
            card.setBackground(game.getColoredDrawable(1, 1, new Color(0.1f, 0.1f, 0.1f, 0.8f)));

            card.add(new Image(new Texture(spirit.texturePath))).size(50).padRight(10);

            Table info = new Table();
            info.add(new Label(spirit.name, new Label.LabelStyle(style.font, Color.WHITE))).left().row();

            Stack hpBg = new Stack();
            hpBg.add(new Image(game.getColoredDrawable(120, 10, Color.BLACK)));
            float actualHp = spirit.hp / spirit.hpMax;

            Color barCol = actualHp < 0.2f ? Color.RED : (actualHp < 0.5f ? Color.YELLOW : Color.GREEN);
            hpBg.add(new Container<>(new Image(game.getColoredDrawable(1, 1, spirit.hp <= 0 ? Color.GRAY : barCol))).width(120 * actualHp).height(10).align(Align.left));
            info.add(hpBg).size(120, 10);
            card.add(info).pad(10);

            String txt = spirit.hp <= 0 ? "CAÍDO" : (idx == game.getActiveIndex() ? "EN CAMPO" : "ELEGIR");
            TextButton button = new TextButton(txt, style);
            if(spirit.hp <= 0 || idx == game.getActiveIndex()) button.setDisabled(true);

            button.addListener(new ClickListener(){
                @Override public void clicked(InputEvent e, float x, float y){
                    if(!button.isDisabled()){ game.switchSpirit(idx); SpiritsMenu.this.remove(); }
                }
            });
            card.add(button).size(100, 40);

            table.add(card).pad(5);
            if((i + 1) % 2 == 0) table.row();
        }
        this.add(table).row();

        if(!flag) {
            TextButton back = new TextButton("CERRAR", style);
            back.addListener(new ClickListener(){ @Override public void clicked(InputEvent e, float x, float y){ SpiritsMenu.this.remove(); }});
            this.add(back).size(200, 50).pad(20);
        }
    }
}
