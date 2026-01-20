package nintendont.amongspirits.ui.game;

import java.util.Iterator;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import nintendont.amongspirits.data.satchel.ConsumableItem;
import nintendont.amongspirits.data.satchel.Item;
import nintendont.amongspirits.data.satchel.ItemStack;
import nintendont.amongspirits.managers.CraftManager;
import nintendont.amongspirits.data.satchel.Satchel;

public class InventoryMenu extends MenuTable{
    private final AssetManager assets;
    private Satchel invManager;
    private CraftManager craftManager;
    private Table grid;
    private MenuTable teamTable;
    private Label title;
    private Label desc;
    private Skin skin;
    private Image cursor;
    Vector2 cursorPos = new Vector2();

    private enum InvState{
        NONE,
        SELECT_ITEM,
        SELECT_PKMN
    }
    private InvState invState = InvState.NONE;
    private final int ROWS = 5;
    private final int COLS = 4;

    // selected items for crafting
    private ItemStack itemA;
    private ItemStack itemB;

    private int selected = 0;


    public InventoryMenu (Satchel invManager, CraftManager craftManager, Skin skin, GUIManager gui, AssetManager assets){
        super(gui);
        this.assets = assets;
        this.invManager = invManager;
        this.craftManager = craftManager;
        this.skin = skin;
        this.setFillParent(true);

        this.setBackground(skin.newDrawable("white", 0, 0, 0, 0.85f));
        this.top();

        cursor = new Image(skin.newDrawable("white", new Color(1, 1, 1, 0.5f)));
        cursor.setSize(70, 70);
        this.addActor(cursor);
        cursor.toFront();
        cursor.setTouchable(Touchable.disabled);

        // top bar
        Table header = new Table();
        header.setBackground(skin.newDrawable("white", new Color(0.2f, 0.2f, 0.2f, 1f)));

        Label title = new Label("Satchel", skin);
        title.setFontScale(1.5f);

        header.add(new Label("E: Recetas", skin)).left().pad(15);
        header.add(title).expandX().center().pad(15);
        header.add(new Label("Q: Team", skin)).right().pad(15);
        // header.setDebug(true);
        this.add(header).growX().height(60).top();
        this.row();

        // cuerpo
        Table body = new Table();

        //  contenedor de items
        Table leftPanel = new Table();
        // leftPanel.setDebug(true);

        grid = new Table();
        grid.align(Align.topLeft);

        // cntenedor de descripcion
        Table descTable = createDescriptionPanel(skin);

        leftPanel.add(grid).expand().fill().pad(20).top().left();
        leftPanel.row();
        leftPanel.add(descTable).growX().height(120).pad(20).bottom();


        // contenedor de equipos (aqui salen los pokemon o lo q sea)
        this.teamTable = new TeamMenu(skin, gui, assets){
            @Override
            public void onClick(int index){
                if (invState != InvState.NONE){
                    ((Consumable)itemA.getItem()).useItem(this.getSelSpirit());
                    update();
                    return;
                }
                super.onClick(index);
            }
        };
        Table team = new Table();
        team.center().top().pad(20);
        team.setBackground(skin.newDrawable("white", 0, 0, 0, 0.2f));
        team.add(teamTable);
//        team.setDebug(true);

        body.add(leftPanel).width(Value.percentWidth(0.55f, body)).expandY().fillY();
        body.add(team).width(Value.percentWidth(0.45f, body)).expandY().fillY();

        this.add(body).expand().fill();
        update();
        updateCursor();
    }
    private ItemStack selItem;
    public void updateDesc(){
        // solo porque me molestabaa tener que actualioxar todo
        // actualizar descripcion
        switch (invState) {
            case SELECT_ITEM:
                desc.setText(itemA.getItem().getName() + ": " + itemA.getCount() + "\n" + itemA.getItem().getDescription() + "\n selected");
                break;

            default:
                if (invManager.getItems().isEmpty()){
                    desc.setText("No items :(");
                } else {
                    selected = selected >= invManager.getItems().size() ? invManager.getItems().size()-1 : selected;
                    selItem = invManager.getItems().get(selected);
                    desc.setText( selItem.getItem().getName() + ": "+ selItem.getCount() + "\n" + selItem.getItem().getDescription());
                }
                break;
        }
    }

    public void updateCursor(){
        if (invManager.getItems().isEmpty()) {
            cursorPos.set(0,0);
            cursor.setVisible(false);
            return;
        } else cursor.setVisible(true);

        if (selected < invManager.getItems().size()){
            grid.getChildren().get(selected).localToActorCoordinates(cursor, cursorPos);
        } else {
            grid.getChildren().get(invManager.getItems().size()-1).localToActorCoordinates(cursor, cursorPos);
        }
        cursor.setPosition(cursorPos.x,cursorPos.y);
        cursor.toFront();
    }
    @Override
    public void update(){ // construye el menu, se llama al agregar o eliminar un item
        grid.clear();
        int cols = 0;
        int sel = 0;
        Actor slot;

        Iterator<ItemStack> it = invManager.getItems().iterator();
        ItemStack stack;
        while (it.hasNext()){
            stack = it.next();

            if (stack.getCount() <= 0){
                it.remove();
                continue;
            }

            slot = createSlot(skin, stack, sel);
            grid.add(slot).size(70).pad(15);

            cols++;

            if ( cols > COLS){
                grid.row();
                cols = 0;
            }
            sel++;
        }

        this.validate();
        updateDesc();
        updateCursor();
        teamTable.update();
    }

    private Actor createSlot(Skin skin, ItemStack stack, int sel) {
        Stack slotStack = new Stack();
        Image bg;

        bg = new Image(skin.newDrawable("white", Color.DARK_GRAY));

        bg.setTouchable(Touchable.disabled);
        slotStack.add(bg);

        if (stack == null) return slotStack;

        Image icon;
        if (stack.getItem().getIconAsset() != null){
            Texture iconTexture = assets.get(stack.getItem().getIconAsset());
            icon = new Image(iconTexture);
        } else{
            icon = new Image(skin.newDrawable("white", Color.CYAN)); // Placeholder Icon
        }

        Table iconContainer = new Table();
        iconContainer.add(icon).size(50);

        iconContainer.setTouchable(Touchable.disabled);
        icon.setTouchable(Touchable.disabled);

        slotStack.add(iconContainer);

        if (stack.getCount() > 1) {
            Label countLabel = new Label(String.valueOf(stack.getCount()), skin);
            countLabel.setAlignment(Align.bottomRight);
            slotStack.add(countLabel);
            countLabel.setTouchable(Touchable.disabled);
        }

        int index = invManager.getItems().indexOf(stack);

        slotStack.setTouchable(Touchable.enabled);
        slotStack.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("helpp");
                onClick(stack);
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                selected = index;
                updateDesc();
                updateCursor();
            }
        });

        return slotStack;
    }
    private Table createDescriptionPanel(Skin skin) {
        Table t = new Table();
        t.setBackground(skin.newDrawable("white", new Color(0.1f, 0.1f, 0.1f, 0.6f))); // Darker box
        t.pad(15);

        desc = new Label("Select an item", skin);
        desc.setColor(Color.YELLOW);
        desc.setFontScale(1.2f);

        title = new Label("", skin);
        title.setWrap(true);

        t.add(desc).left().expandX().row();
        t.add(title).left().grow().padTop(10);

        return t;
    }

    public Table contextMenu(boolean isMaterial){
        return null;
    }

    public void onClick(ItemStack stack){
        // if (stack.getItem() instanceof Consumable){
        //     if (invState == InvState.NONE){
        //         itemA = stack;
        //         invState = InvState.SELECT_PKMN;
        //         update();
        //     }
        // }
        if (stack.getItem().isMaterial()){
            if (invState == InvState.NONE){
                    itemA = stack;
                    invState = InvState.SELECT_ITEM;
                    updateDesc();
            } else if (invState == InvState.SELECT_ITEM) {
                itemB = stack;
                Item output = craftManager.craft(itemA.getItem(), itemB.getItem());
                if (output != null){
                    invManager.addItem(output);
                    selected = selected <= 0 ? 0 : selected - 1;
                    itemA.decrease();
                    itemB.decrease();
                    update();
                }
                invState = InvState.NONE;
            }
        }
        
    }

    @Override
    public boolean handleInput (int key){
        int size = invManager.getItems().size();
//        if (size == 0 ) return false;
        switch (key) {
            case Keys.W:
                if (selected - ROWS >= 0 ) selected -= ROWS;
                break;
            case Keys.S:
                if (selected + ROWS < size ) selected += ROWS;
                break;
            case Keys.A:
                if (selected - 1 >= 0 ) selected -= 1;
                break;
            case Keys.D:
                if (selected + 1 < size ) selected += 1;
                break;
            case Keys.ENTER:
                System.out.println("clicked");
                onClick(invManager.getItems().get(selected));
                break;
            default:
                return false;
        }
        updateDesc();
        updateCursor();
        return true;
    }
}
