package nintendont.amongspirits.ui.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import nintendont.amongspirits.Const;
import nintendont.amongspirits.data.spirits.Invocation;
import nintendont.amongspirits.utils.styleUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TeamMenu extends MenuTable {
    private AssetManager assets;
    private int selectedIndex = -1;
    private int switchIndex = -1;
    private boolean swapMode = false;

    private Table grid;
    private Skin skin;

    public TeamMenu(Skin skin, GUIManager gui, AssetManager assets) {
        super(gui);
        this.skin = skin;
        this.assets = assets;
        Table root = new Table();

        Label title = new Label("EQUIPO", skin);
        title.setFontScale(1.2f);
        root.add(title).pad(20).top().row();

        grid = new Table();
        grid.top();

        root.add(grid).expand().fill().pad(20).row();

        this.add(root).expand().fill();
        update();
    }

    @Override
    public void update() {
        grid.clear();
        ArrayList<Invocation> team = gui.getPlayer().getTeam().getMembers();

        for (int i = 0; i < team.size(); i++) {
            Invocation member = team.get(i);
            Table card = createSpiritCard(member, i);

            grid.add(card).size(225, 80).pad(10); // Made slightly wider for button
            if (i % 2 == 1) grid.row();
        }
    }

    private Table createSpiritCard(Invocation spirit, int index) {
        boolean isSelected = (index == selectedIndex);
        boolean isSwitchIndex = (index == switchIndex);
        boolean isFainted = spirit.getHP() <= 0;


        Color bgCol;
        if (isSwitchIndex) bgCol = Color.ROYAL;
        else if (isSelected) bgCol = Color.GOLD;
        else if (isFainted) bgCol = new Color(0.6f, 0f, 0f, 1f);
        else bgCol = new Color(0.2f, 0.2f, 0.3f, 1f);


        Table card = new Table(); // container
        card.setBackground(skin.newDrawable("white", bgCol));
        card.setTouchable(Touchable.enabled);

        Texture iconTex = assets.get(spirit.getBattleAsset());
        card.add(new Image(iconTex)).size(50).pad(5);

        Table stats = new Table(); // name & health
        stats.align(Align.left);
        stats.add(new Label(spirit.getFullName(), skin)).left().row();

        float hpRatio = spirit.getHealthRatio();
        Color barColor = hpRatio < 0.2f ? Color.RED : (hpRatio < 0.5f ? Color.YELLOW : Color.GREEN);

        Table barContainer = new Table();
        barContainer.setBackground(styleUtils.getColoredDrawable(1, 1, Color.BLACK));
        barContainer.add(new Image(styleUtils.getColoredDrawable(1, 1, barColor))).width(80 * hpRatio).height(8).left();
        barContainer.add().expandX();

        stats.add(barContainer).size(80, 8).padTop(5).left().row();
        stats.add(new Label((int)spirit.getHP() + "/" + (int)spirit.getMaxHP(), skin)).left();

        card.add(stats).expandX().fillX().pad(5);

        card.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                onClick(index);
            }
        });

        return card;
    }

    public void onClick(int index) {
        if (swapMode) {
            if (index == switchIndex) {
                swapMode = false;
                switchIndex = -1;
            } else {
                List<Invocation> team = gui.getPlayer().getTeam().getMembers();
                Collections.swap(team, switchIndex, index);

                swapMode = false;
                switchIndex = -1;
            }
            update();
            return;
        }
        if (selectedIndex == index){
            swapMode = true;
            switchIndex = index;
        } else{
            selectedIndex = index; }
        update();
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            switchIndex = -1;
            selectedIndex = -1;
            update();
        }
    }
    public Invocation getSelSpirit(){
        List<Invocation> team = gui.getPlayer().getTeam().getMembers();
        return team.get(selectedIndex);
    }
    @Override
    public boolean handleInput(int key) {
        return false;
    }
}
