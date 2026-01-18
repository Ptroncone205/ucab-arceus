package nintendont.amongspirits.utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import nintendont.amongspirits.screens.BattleScreen;
import nintendont.amongspirits.data.spirits.Invocation;
import nintendont.amongspirits.data.spirits.Spirit;
import java.util.ArrayList;

public class SpiritsMenu extends Table {
    public SpiritsMenu(TextButton.TextButtonStyle style, final BattleScreen game, boolean flag, AssetManager assets) {
        this.setFillParent(true);
        this.setBackground(game.getColoredDrawable(1, 1, new Color(0, 0, 0.4f, 0.95f)));
        this.center();

        String tituloTxt = flag ? "¡DEBILITADO! ELIGE OTRO" : "EQUIPO";
        this.add(new Label(tituloTxt, new Label.LabelStyle(style.font, Color.YELLOW))).colspan(2).pad(20).row();

        Table table = new Table();

        ArrayList<Invocation> members = game.getPlayer().getTeam().getMembers();

        for (int i = 0; i < members.size(); i++) {
            final int idx = i;
            final Invocation invocation = members.get(i);
            final Spirit spirit = invocation.getSpirit();

            Table card = new Table().pad(10);
            card.setBackground(game.getColoredDrawable(1, 1, new Color(0.1f, 0.1f, 0.1f, 0.8f)));

            Texture battleGraphic = assets.get(invocation.getBattleAsset());
            card.add(new Image(battleGraphic)).size(50).padRight(10);

            Table info = new Table();
            info.add(new Label(invocation.getFullName(), new Label.LabelStyle(style.font, Color.WHITE))).left().row();

            Stack hpBg = new Stack();
            hpBg.add(new Image(game.getColoredDrawable(120, 10, Color.BLACK)));
            float actualHp = invocation.getHealthRatio();

            Color barCol = actualHp < 0.2f ? Color.RED : (actualHp < 0.5f ? Color.YELLOW : Color.GREEN);
            hpBg.add(new Container<>(new Image(game.getColoredDrawable(1, 1, invocation.getHP() <= 0 ? Color.GRAY : barCol)))
                .width(120 * actualHp).height(10).align(Align.left));

            info.add(hpBg).size(120, 10);
            card.add(info).pad(10);

            String txt = invocation.getStats().getHP().getCurrent() <= 0 ? "CAÍDO" : (idx == game.getPlayerActiveIndex() ? "EN CAMPO" : "ELEGIR");
            TextButton button = new TextButton(txt, style);
            if (invocation.getHP() <= 0 || idx == game.getPlayerActiveIndex()) button.setDisabled(true);

            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent e, float x, float y) {
                    if (!button.isDisabled()) {
                        game.switchSpirit(idx);
                        SpiritsMenu.this.remove();
                    }
                }
            });

            card.add(button).size(100, 40);
            table.add(card).pad(5);
            if ((i + 1) % 2 == 0) table.row();
        }

        this.add(table).row();

        if (!flag) {
            TextButton back = new TextButton("CERRAR", style);
            back.addListener(new ClickListener() {
                @Override public void clicked(InputEvent e, float x, float y) { SpiritsMenu.this.remove(); }
            });
            this.add(back).size(200, 50).pad(20);
        }
    }
}
