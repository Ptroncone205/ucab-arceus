package nintendont.amongspirits;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;
import nintendont.amongspirits.data.spirits.*;
import nintendont.amongspirits.utils.*;
import nintendont.amongspirits.entities.ItemStack;
import nintendont.amongspirits.entities.items.Consumable;

import java.util.ArrayList;

public class BattleMain extends ApplicationAdapter implements MenuListener{
    private Stage stage;
    private Table tableB;
    private SpriteBatch batch;
    private Sprite bgSprite;
    private Label messageLabel;

    private Team playerTeam;
    private int activeIndex = 0;
    private String enemyName = "Tolon";
    private float hpEnemy = 150f, hpMaxEnemy = 150f;

    private Image healthBarPlayer, healthBarEnemy;
    private BitmapFont font;
    private TextButton.TextButtonStyle styleRed, styleBlue, styleGreen, styleYellow;

    private ArrayList<ItemStack> inventory;

    @Override
    public void create() {
        batch = new SpriteBatch();
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        font = new BitmapFont();

        // Styles
        styleRed = createButtonStyle(Color.RED);
        styleBlue = createButtonStyle(Color.BLUE);
        styleGreen = createButtonStyle(Color.GREEN);
        styleYellow = createButtonStyle(Color.YELLOW);

        // Sprites
        bgSprite = new Sprite(new Texture(Gdx.files.internal("fightbg.png")));
        bgSprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Movimientos
        SpiritMove placaje = new SpiritMove("mv01", "Placaje", "Golpe físico", SpiritMoveCategory.PHYSICAL, 40, 100, 35, 0);
        SpiritMove llamarada = new SpiritMove("mv02", "Llamarada", "Fuego intenso", SpiritMoveCategory.ESPECIAL, 90, 85, 10, 10);
        SpiritMove ventisca = new SpiritMove("mv03", "Ventisca", "Tormenta de hielo", SpiritMoveCategory.ESPECIAL, 110, 70, 5, 10);
        SpiritMove chispa = new SpiritMove("mv04", "Chispa", "Descarga eléctrica", SpiritMoveCategory.ESPECIAL, 65, 100, 20, 30);

        // Equipo
        playerTeam = new Team();
        playerTeam.getMembers().add(crearInvocacion("Ciervo 1", "THUNDER", 100, "gokuprueba.png", chispa, placaje,llamarada,ventisca));
        playerTeam.getMembers().add(crearInvocacion("Ciervo 2", "THUNDER", 100, "gokuprueba.png", chispa, placaje,llamarada,ventisca));
        playerTeam.getMembers().add(crearInvocacion("Lobo", "ICE", 120, "gokuprueba.png", chispa, placaje,llamarada,ventisca));
        playerTeam.getMembers().add(crearInvocacion("Conejo", "ICE", 80, "gokuprueba2.png", chispa, placaje,llamarada,ventisca));
        playerTeam.getMembers().add(crearInvocacion("Fénix", "FIRE", 90, "gokuprueba2.png", chispa, placaje,llamarada,ventisca));
        playerTeam.getMembers().add(crearInvocacion("León", "FIRE", 130, "gokuprueba2.png", chispa, placaje,llamarada,ventisca));

        // Inventario
        inventory = new ArrayList<>();

        Consumable pocion = new Consumable(1, "Pocion", "Restaura 20 HP", false);
        inventory.add(new ItemStack(pocion, 1));
        Consumable superPocion = new Consumable(2, "Super Pocion", "Restaura 50 HP", false);
        inventory.add(new ItemStack(superPocion, 2));

        setupBattleUI();
    }

    private Invocation crearInvocacion(String nombre, String tipo, int hp, String tex, SpiritMove m1, SpiritMove m2, SpiritMove m3, SpiritMove m4){
        Spirit s = new Spirit(nombre, tipo, hp, tex);
        Invocation inv = new Invocation(s, 10);
        inv.getMoves().add(m1);
        inv.getMoves().add(m2);
        inv.getMoves().add(m3);
        inv.getMoves().add(m4);
        return inv;
    }

    public void setupBattleUI(){
        stage.clear();
        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        Spirit currentSpirit = playerTeam.getMembers().get(activeIndex).getSpirit();

        // UI ENEMIGO
        healthBarEnemy = new Image();
        Table enemyGroup = new Table();
        enemyGroup.add(new Image(new Texture(Gdx.files.internal("gokuprueba2.png")))).size(180).row();
        Table enemyInfo = new Table();
        enemyInfo.add(healthBarEnemy).size(200, 20).row();
        enemyInfo.add(new Label(enemyName, new Label.LabelStyle(font, Color.RED)));
        enemyGroup.add(enemyInfo).left().padTop(10);
        root.add(enemyGroup).expand().top().right().padTop(60).padRight(180).row();

        // UI JUGADOR
        healthBarPlayer = new Image();
        Table playerGroup = new Table();
        playerGroup.add(new Image(new Texture(Gdx.files.internal(currentSpirit.texturePath)))).size(180).row();
        playerGroup.add(healthBarPlayer).size(200, 20).padTop(5).row();
        playerGroup.add(new Label(currentSpirit.name, new Label.LabelStyle(font, Color.CYAN)));
        root.add(playerGroup).expand().bottom().left().pad(40).row();

        float panelHeight = 190;
        Table bottomPanel = new Table();
        bottomPanel.setBackground(getColoredDrawable(1, 1, new Color(0, 0, 0, 0.85f)));

        messageLabel = new Label("¿Qué hará " + currentSpirit.name + "?", new Label.LabelStyle(font, Color.WHITE));
        messageLabel.setWrap(true);
        messageLabel.setAlignment(Align.center);
        bottomPanel.add(messageLabel).width(Gdx.graphics.getWidth() * 0.5f).height(panelHeight).pad(20);

        tableB = new Table();
        setupMainButtons(panelHeight);
        bottomPanel.add(tableB).width(Gdx.graphics.getWidth() * 0.5f).height(panelHeight);

        root.add(bottomPanel).fillX().height(panelHeight);
        updateHealth();
    }

    public void setupMainButtons(float panelH){
        tableB.clearChildren();

        float btnW = (Gdx.graphics.getWidth() * 0.5f) / 2.1f;
        float btnH = panelH / 2.2f;

        TextButton btnFight = new TextButton("LUCHAR", styleRed);
        TextButton btnBag = new TextButton("BOLSA", styleBlue);
        TextButton btnTeam = new TextButton("EQUIPO", styleGreen);
        TextButton btnRun = new TextButton("HUIR", styleYellow);

        btnFight.addListener(new ClickListener(){
            @Override public void clicked(InputEvent e, float x, float y){
                onFightSelected();
            }});

        btnBag.addListener(new ClickListener(){
            @Override public void clicked(InputEvent e, float x, float y){
            tableB.clearChildren();
            tableB.add(new BagMenu(styleBlue, BattleMain.this, inventory)).fill();
        }});

        btnTeam.addListener(new ClickListener(){
            @Override public void clicked(InputEvent e, float x, float y){
            stage.addActor(new SpiritsMenu(styleGreen, BattleMain.this, false));
        }});

        btnRun.addListener(new ClickListener(){
            @Override public void clicked(InputEvent e, float x, float y){
                Gdx.app.exit();
            }});

        // Anadir botones principales
        tableB.add(btnFight).size(btnW, btnH).pad(2);
        tableB.add(btnBag).size(btnW, btnH).pad(2).row();
        tableB.add(btnTeam).size(btnW, btnH).pad(2);
        tableB.add(btnRun).size(btnW, btnH).pad(2);
    }

    @Override
    public void onAttackSelected(String attackName){
        Invocation currentInv = playerTeam.getMembers().get(activeIndex);
        SpiritMove moveUsed = null;

        for(SpiritMove m : currentInv.getMoves()){
            if(m.getName().equals(attackName)){
                moveUsed = m; break;
            }
        }

        if (moveUsed != null){
            hpEnemy -= (moveUsed.getBasePower() / 2f);
            messageLabel.setText("¡" + currentInv.getSpirit().name + " usó " + attackName + "!");
        }

        updateHealth();
        startEnemyTurn();
    }

    public void startEnemyTurn(){
        tableB.clearChildren();
        Timer.schedule(new Timer.Task(){
            @Override public void run(){
                if (hpEnemy <= 0){
                    messageLabel.setText("¡El enemigo ha sido derrotado!");
                }else{
                    messageLabel.setText("¡El enemigo contraataca!");
                    playerTeam.getMembers().get(activeIndex).getSpirit().hp -= 15;
                    updateHealth();
                    Timer.schedule(new Timer.Task(){
                        @Override
                        public void run(){
                            if (playerTeam.getMembers().get(activeIndex).getSpirit().isFainted()){
                                playerTeam.getMembers().get(activeIndex).getSpirit().hp = 0f;
                                stage.addActor(new SpiritsMenu(styleGreen, BattleMain.this, true));
                            }else{
                                messageLabel.setText("¿Qué hará " + playerTeam.getMembers().get(activeIndex).getSpirit().name + "?");
                                setupMainButtons(190);
                            }
                        }
                    }, 1.5f);
                }
            }
        }, 1.5f);
    }

    public void updateHealth(){
        Spirit s = playerTeam.getMembers().get(activeIndex).getSpirit();
        healthBarPlayer.setDrawable(createBarDrawable(s.hp / s.hpMax));
        healthBarEnemy.setDrawable(createBarDrawable(hpEnemy / hpMaxEnemy));
    }

    private TextureRegionDrawable createBarDrawable(float percent){
        int w = 200, h = 20;
        Pixmap p = new Pixmap(w, h, Pixmap.Format.RGBA8888);
        p.setColor(Color.BLACK); p.fill();
        Color colorVida = percent < 0.2f ? Color.RED : (percent < 0.5f ? Color.YELLOW : Color.GREEN);
        p.setColor(colorVida);
        p.fillRectangle(0, 0, (int)(w * Math.max(0, percent)), h);
        TextureRegionDrawable d = new TextureRegionDrawable(new TextureRegion(new Texture(p)));
        p.dispose();
        return d;
    }

    public TextureRegionDrawable getColoredDrawable(int w, int h, Color c) {
        Pixmap p = new Pixmap(w > 0 ? w : 1, h, Pixmap.Format.RGBA8888);
        p.setColor(c); p.fill();
        TextureRegionDrawable d = new TextureRegionDrawable(new TextureRegion(new Texture(p)));
        p.dispose();
        return d;
    }

    private TextButton.TextButtonStyle createButtonStyle(Color c) {
        TextButton.TextButtonStyle s = new TextButton.TextButtonStyle();
        s.up = getColoredDrawable(1, 1, c);
        s.font = font;
        return s;
    }

    public Table getTableB(){
        return tableB;
    }

    public Stage getStage(){
        return stage;
    }

    public Team getPlayerTeam(){
        return playerTeam;
    }

    public int getActiveIndex(){
        return activeIndex;
    }

    public ArrayList<ItemStack> getInventory(){
        return inventory;
    }

    public void switchSpirit(int idx){
        activeIndex = idx; setupBattleUI(); startEnemyTurn();
    }

    public void lose(){
        if (dead(playerTeam.getMembers())){

        }
    }

    public boolean dead(ArrayList<Invocation> members){
        int dead = 0;
        int i;
        for(i = 0; i <= members.size(); i++){
            if (members.get(i).getSpirit().isFainted()){
                dead++;
            }
        }
        if (dead < i){
            return false;
        }else{
            return true;
        }
    }

    @Override
    public void onBackSelected(){
        setupMainButtons(190);
    }

    @Override
    public void onFightSelected(){
        tableB.clearChildren();
        tableB.add(new AttackMenu(styleRed, this, playerTeam.getMembers().get(activeIndex).getMoves())).fill();
    }

    @Override
    public void render(){
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin(); bgSprite.draw(batch); batch.end();
        stage.act(); stage.draw();
    }
}
