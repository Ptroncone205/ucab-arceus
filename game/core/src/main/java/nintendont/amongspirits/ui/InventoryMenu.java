package nintendont.amongspirits.ui;

import java.util.ArrayList;

import javax.swing.UIManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import nintendont.amongspirits.Const.GameState;
import nintendont.amongspirits.Const;
import nintendont.amongspirits.entities.ItemStack;
import nintendont.amongspirits.entities.items.Item;
import nintendont.amongspirits.managers.CraftManager;
import nintendont.amongspirits.managers.InventoryManager;

public class InventoryMenu extends Table{
    private InventoryManager invManager;
    private CraftManager craftManager;
    public GameState currentState;
    private Table grid;
    private Table team;
    private Label title;
    private Label desc;

    // selected items for crafting
    private ItemStack itemA;
    private ItemStack itemB;


    public InventoryMenu (InventoryManager invManager, CraftManager craftManager, Skin skin){
        this.invManager = invManager;
        this.craftManager = craftManager;
        this.setFillParent(true);

        this.setBackground(skin.newDrawable("white", 0, 0, 0, 0.85f));
        this.top();

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
        team = new Table();
        team.top().padTop(20);
        team.setBackground(skin.newDrawable("white", 0, 0, 0, 0.5f));

        body.add(leftPanel).width(Value.percentWidth(0.55f, body)).expandY().fillY();
        body.add(team).width(Value.percentWidth(0.45f, body)).expandY().fillY();

        this.add(body).expand().fill();

        update(skin);

        // this.setDebug(true);
    }

    public void update(Skin skin){
        grid.clear();
        // System.out.println(Const.currentState);

        int cols = 0;

        ArrayList<ItemStack> removeItems = new ArrayList<>();
        for ( ItemStack stack : invManager.getItems()){
            if (stack.count <= 0){
                removeItems.add(stack); // no se puede remover items mientras recorro el arreglo lol
                continue;
            }
            Actor slot = createSlot(skin, stack);

            grid.add(slot).size(70).pad(15);
            cols++;

            if ( cols > 4){
                grid.row();
                cols = 0;
            }
        }
        for (ItemStack item : removeItems){
            invManager.getItems().remove(item);
        }

        switch (Const.currentState) {
            case SELECT_ITEM:
                desc.setText(itemA.getItem().getName() + ": " + itemA.getCount() + "\n" + itemA.getItem().getDesc());
                break;
        
            default:
                desc.setText("select item");
                break;
        }
    }

    private Actor createSlot(Skin skin, final ItemStack stack) {
        Stack slotStack = new Stack();

        Image bg = new Image(skin.newDrawable("white", Color.DARK_GRAY));
        bg.setTouchable(Touchable.disabled);
        slotStack.add(bg);

        if (stack == null) return slotStack;

        Image icon;
        if (stack.getItem().icon != null){
            icon = new Image(stack.getItem().icon);
        } else{
            icon = new Image(skin.newDrawable("white", Color.CYAN)); // Placeholder Icon
        }

        Table iconContainer = new Table();
        iconContainer.add(icon).size(50);
        slotStack.add(iconContainer);

        if (stack.getCount() > 1) {
            Label countLabel = new Label(String.valueOf(stack.getCount()), skin);
            countLabel.setAlignment(Align.bottomRight);
            slotStack.add(countLabel);
        }

        slotStack.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (stack.getItem().isMaterial()){
                    if (Const.currentState == Const.GameState.INVENTORY){
                        itemA = stack;
                        Const.currentState = Const.GameState.SELECT_ITEM;
                    } else if (Const.currentState == Const.GameState.SELECT_ITEM) {
                        itemB = stack;
                        Item output = craftManager.craft(itemA.getItem(), itemB.getItem());
                        if (output != null){
                            invManager.addItem(output);
                            itemA.count--; itemB.count--;
                        }
                        Const.currentState = Const.GameState.INVENTORY;
                    }
                }

                update(skin);
                // System.out.println("Clicked: " + stack.getItem().getName());
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                desc.setText(stack.getItem().getName() + ": " + stack.getCount() + "\n" + stack.item.desc);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                System.out.println("exit from " + stack.item.name);
                update(skin);
                
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
}
