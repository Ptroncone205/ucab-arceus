package nintendont.amongspirits.utils;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

public class AttackMenu extends Table{
    public AttackMenu(TextButton.TextButtonStyle style, final MenuListener listener, final String m1, final String m2, final String m3, final String m4){
        this.setFillParent(false);

        Table movesTable = new Table();
        String[] moves = {m1, m2, m3, m4};

        for (int i = 0; i < moves.length; i++){
            final String moveName = moves[i];
            TextButton button = new TextButton(moveName, style);

            button.addListener(new ClickListener() {
                @Override public void clicked(InputEvent event, float x, float y) { listener.onAttackSelected(moveName); }
            });

            movesTable.add(button).expand().fill().uniformX().pad(2);
            if ((i + 1) % 2 == 0) movesTable.row();
        }

        TextButton back = new TextButton("ATRÁS", style);
        back.addListener(new ClickListener() {
            @Override public void clicked(InputEvent event, float x, float y) { listener.onBackSelected(); }
        });

        this.add(movesTable).expand().fill().row();
        this.add(back).fillX().height(40).pad(2);
    }
}
