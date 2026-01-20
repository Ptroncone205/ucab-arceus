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
import nintendont.amongspirits.data.spirits.Pasture;
import nintendont.amongspirits.utils.styleUtils;

import java.util.Collections;
import java.util.List;

public class PastureUI extends MenuTable {

    public enum SlotType { PASTURE, TEAM }
    private AssetManager assets;
    private SlotType selectedType = null;
    private int selectedIndex = -1;

    private boolean swapMode = false;
    private SlotType swapType = null;
    private int swapIndex = -1;

    private Pasture pasture;
    private TeamMenu teamTable;
    private int pasturePage = 0;
    private final int ROWS = 4;
    private final int COLS = 5;
    private final int PAGE_SIZE = ROWS * COLS;

    private Skin skin;
    private Table pastureGrid;
    private Table teamStrip;
    private Table infoPanel;
    private Label pageLabel;

    public PastureUI(Skin skin, Pasture pasture, TeamMenu teamTable, GUIManager gui, AssetManager assets) {
        super(gui);
        this.assets = assets;
        this.skin = skin;
        this.pasture = pasture;
        this.teamTable = teamTable;
        this.setFillParent(true);
        this.setDebug(true);

        Table root = new Table();
        root.setFillParent(true);

        // team root
        Table teamRoot = new Table();
        teamRoot.setBackground(skin.newDrawable("white", Const.BLACK_1));
        Label tTitle = new Label("PARTY", skin);
        tTitle.setFontScale(1.2f);
        teamRoot.add(tTitle).pad(10).top().row();

        teamStrip = new Table();
        teamRoot.add(teamStrip).width(100).expandY().top();

        // pasture root
        Table pastureRoot = new Table();
        pastureRoot.setBackground(skin.newDrawable("white", Const.BLACK_1));
        Label pTitle = new Label("PASTURE", skin);
        pTitle.setFontScale(1.5f);
        pastureRoot.add(pTitle).pad(10).top().row();

        pastureGrid = new Table();
        pastureRoot.add(pastureGrid).size(350, 300).align(Align.topLeft).row();

        Table pageControls = new Table();
        TextButton prevBtn = new TextButton("<", skin);
        prevBtn.addListener(new ClickListener(){ @Override public void clicked(InputEvent e, float x, float y){ changePage(-1); }});

        TextButton nextBtn = new TextButton(">", skin);
        nextBtn.addListener(new ClickListener(){ @Override public void clicked(InputEvent e, float x, float y){ changePage(1); }});

        pageLabel = new Label("PAG 1", skin);

        pageControls.add(prevBtn).size(30,30).pad(10);
        pageControls.add(pageLabel).pad(10);
        pageControls.add(nextBtn).size(30,30).pad(10);
        pastureRoot.add(pageControls).bottom();

        // info root
        infoPanel = new Table();
        infoPanel.setBackground(skin.newDrawable("white", new Color(0f, 0f, 0f, 0.8f)));

        //
        root.add(teamRoot).width(120).height(500).pad(10).align(Align.top);
        root.add(pastureRoot).width(500).height(500).pad(10).align(Align.top);
        root.add(infoPanel).width(250).height(500).pad(10).align(Align.top);

        this.add(root).expand().fill();
    }

    public void update() {
        updatePastureTable();
        updateTeamTable();
        updateInfoTable();
    }

    private void updatePastureTable() {
        pastureGrid.clear();
        List<Invocation> box = pasture.getInvocations();

        int start = pasturePage * PAGE_SIZE;
        int end = Math.min(start + PAGE_SIZE, box.size());

        int cellCounter = 0;

        for (int i = start; i < start + PAGE_SIZE; i++) {
            if (i >= box.size()) break;
            Invocation spirit = box.get(i);

            Table slot = createSlot(spirit, SlotType.PASTURE, i, 60);
            pastureGrid.add(slot).size(60).pad(5);

            cellCounter++;
            if (cellCounter % COLS == 0) pastureGrid.row();
        }
        pageLabel.setText("PAG " + (pasturePage + 1));
        pastureGrid.align(Align.topLeft);
    }

    private void updateTeamTable() {
        teamStrip.clear();

        List<Invocation> team = gui.getPlayer().getTeam().getMembers();

        for (int i = 0; i < team.size(); i++) {
            Table slot = createSlot(team.get(i), SlotType.TEAM, i, 65);
            teamStrip.add(slot).size(65).padBottom(10).row();
        }
    }

    private void updateInfoTable() {
        infoPanel.clear();

        Invocation spirit = getSelectedSpirit();
        if (spirit == null) {
            infoPanel.add(new Label("Select a Spirit", skin));
            return;
        }

        Texture tex = Const.get().assets.get(spirit.getBattleAsset());

        Label name = new Label(spirit.getFullName(), skin);
        name.setFontScale(1.5f);

        float hpRatio = spirit.getHealthRatio();
        Color barCol = hpRatio < 0.2f ? Color.RED : (hpRatio < 0.5f ? Color.YELLOW : Color.GREEN);

        Table bar = new Table();
        bar.setBackground(styleUtils.getColoredDrawable(1,1, Color.BLACK));
        bar.add(new Image(styleUtils.getColoredDrawable(1,1, barCol))).width(200 * hpRatio).height(15).left();
        bar.add().expandX();

        Table bio = new Table();
        Label bioLabel = new Label(spirit.getSpirit().getBiography(), skin);
        bioLabel.setAlignment(Align.center);
        bioLabel.setWrap(true);
        bio.add(bioLabel).width(200);

        infoPanel.setDebug(true);
        infoPanel.add(new Image(tex)).size(150).pad(20).row();
        infoPanel.add(name).padBottom(10).row();
        infoPanel.add(bar).size(200, 15).padBottom(5).row();
        infoPanel.add(new Label((int)spirit.getHP() + "/" + (int)spirit.getMaxHP(), skin)).row();
        infoPanel.add(bio);
        infoPanel.align(Align.top);
    }


    private Table createSlot(Invocation spirit, SlotType type, int index, int size) {
        Table slot = new Table();

        boolean isSelected = (selectedType == type && selectedIndex == index);
        boolean isSwitchSource = (swapMode && swapType == type && swapIndex == index);

        Color bgCol = Const.BLACK_1;
        if (isSwitchSource) bgCol = Color.ROYAL;
        else if (isSelected) bgCol = Color.GOLD;

        slot.setBackground(skin.newDrawable("white", bgCol));

        if (spirit != null) {
            Texture tex = assets.get(spirit.getBattleAsset());
            slot.add(new Image(tex)).size(size - 10);

            if (spirit.getHP() <= 0) slot.setColor(1, 0.5f, 0.5f, 1);
        }

        slot.setTouchable(Touchable.enabled);
        slot.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                onClick(type, index, spirit);
            }
        });

        return slot;
    }

    private void onClick(SlotType type, int index, Invocation clickedMon) {
        if (swapMode) {
            if (swapType == type && swapIndex == index) {
                swapMode = false;
                swapIndex = -1;
            } else {
                performSwap(swapType, swapIndex, type, index);
                swapMode = false;
                swapIndex = -1;
            }
            update();
            return;
        }

        if (selectedType == type && selectedIndex == index) {
            if (clickedMon != null) {
                swapMode = true;
                swapType = type;
                swapIndex = index;
            }
        } else {

            selectedType = type;
            selectedIndex = index;
        }
        update();
    }

    private void performSwap(SlotType srcType, int srcIdx, SlotType dstType, int dstIdx) {
        List<Invocation> srcList = getListByType(srcType);
        List<Invocation> dstList = getListByType(dstType);

        Invocation objA = (srcIdx < srcList.size()) ? srcList.get(srcIdx) : null;
        Invocation objB = (dstIdx < dstList.size()) ? dstList.get(dstIdx) : null;

        if (srcList == dstList) {
            Collections.swap(srcList, srcIdx, dstIdx);
        } else {
            if (srcIdx < srcList.size()) srcList.set(srcIdx, objB);
            if (dstIdx < dstList.size()) dstList.set(dstIdx, objA);
        }

        System.out.println("Swapped " + srcType + " " + srcIdx + " with " + dstType + " " + dstIdx);
    }

    private List<Invocation> getListByType(SlotType type) {
        if (type == SlotType.TEAM) {
            return teamTable.gui.getPlayer().getTeam().getMembers();
        } else {
            return pasture.getInvocations();
        }
    }

    private Invocation getSelectedSpirit() {
        if (selectedIndex == -1 || selectedType == null) return null;
        List<Invocation> list = getListByType(selectedType);
        if (selectedIndex >= list.size()) return null;
        return list.get(selectedIndex);
    }

    private void changePage(int dir) {
        if (pasture.getInvocations().size() < pasturePage * PAGE_SIZE) return;
        pasturePage += dir;
        if (pasturePage < 0) pasturePage = 0;
        update();
    }

    @Override
    public boolean handleInput(int keycode) {
        return false;
    }
}
