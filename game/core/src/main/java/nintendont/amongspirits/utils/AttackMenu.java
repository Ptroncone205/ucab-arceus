package nintendont.amongspirits.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import nintendont.amongspirits.data.spirits.SpiritMove;
import java.util.ArrayList;

public class AttackMenu extends Table{
    public AttackMenu(TextButton.TextButtonStyle style, final MenuListener listener, ArrayList<SpiritMove> moves){
        this.setFillParent(false);
        this.left();

        TextButton back = new TextButton("VOLVER", style);
        back.addListener(new ClickListener(){
            @Override public void clicked(InputEvent event, float x, float y){
                listener.onBackSelected();
            }
        });

        this.add(back).size(70, 100).padRight(30);

        Table grid = new Table();
        if (moves != null){
            for (int i = 0; i < moves.size(); i++){
                final SpiritMove move = moves.get(i);
                TextButton btn = new TextButton(move.getName(), style);
                btn.addListener(new ClickListener(){
                    @Override public void clicked(InputEvent event, float x, float y){
                        listener.onAttackSelected(move.getName());
                    }
                });

                grid.add(btn).size(Gdx.graphics.getWidth()*0.5f/2.5f, 170/2.2f).pad(2);
                if ((i + 1) % 2 == 0) grid.row();
            }
        }

        this.add(grid).padRight(50).padBottom(20);
    }
}
