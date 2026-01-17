package nintendont.amongspirits;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import nintendont.amongspirits.data.spirits.Spirit;
import nintendont.amongspirits.utils.*;

public class BattleMain extends ApplicationAdapter implements MenuListener{
    private Stage stage;
    private Table tableB;
    private SpriteBatch batch;
    private Sprite bgSprite;
    private Label messageLabel;

    private Spirit[] team;
    private int activeIndex = 0;
    private float hpEnemy = 100f, hpMaxEnemy = 100f;
    private String enemyName = "Tolon";

    private Image healthBarPlayer, healthBarEnemy;
    private AttackMenu attackMenu;
    private int potions = 3, superPotions = 3;

    public TextButton.TextButtonStyle styleRed, styleBlue, styleGreen, styleYellow;
    private BitmapFont font;

    @Override
    public void create(){
        batch = new SpriteBatch();
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        font = new BitmapFont();

        team = new Spirit[]{
            new Spirit("Ciervo", "THUNDER", 100f, new String[]{"Impactrueno", "Ataque Rapido", "Electro Bola", "Rugido"}, "gokuprueba.png"),
            new Spirit("Lobo", "ICE", 100f, new String[]{"Colmillo Hielo", "Mordisco", "Garra Hielo", "Aullido"}, "gokuprueba2.png"),
            new Spirit("Conejo", "ICE", 80f, new String[]{"Doble Patada", "Refuerzo", "Rayo Hielo", "Agilidad"}, "gokuprueba.png"),
            new Spirit("Leon", "FIRE", 120f, new String[]{"Llamarada", "Colmillo Igneo", "Intimidar", "Derribar"}, "gokuprueba2.png"),
            new Spirit("Ciervo 2", "THUNDER", 100f, new String[]{"Impactrueno", "Ataque Rapido", "Electro Bola", "Rugido"}, "gokuprueba.png"),
            new Spirit("Leon 2", "FIRE", 120f, new String[]{"Llamarada", "Colmillo Igneo", "Intimidar", "Derribar"}, "gokuprueba2.png")
        };

        setupBattleUI();
    }

    public void setupBattleUI(){
        stage.clear();
        Table tableRoot = new Table();
        tableRoot.setFillParent(true);
        stage.addActor(tableRoot);

        bgSprite = new Sprite(new Texture(Gdx.files.internal("fightbg.png")));
        bgSprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        styleRed = createButtonStyle(Color.RED);
        styleBlue = createButtonStyle(new Color(0, 0, 0.5f, 1));
        styleGreen = createButtonStyle(Color.GREEN);
        styleYellow = createButtonStyle(new Color(0.6f, 0.6f, 0, 1));

        healthBarPlayer = new Image();
        healthBarEnemy = new Image();

        // Enemigo
        Table enemyArea = new Table();
        enemyArea.add(new Image(new Texture("gokuprueba2.png"))).size(150).row();

        Table enemyStats = new Table();
        enemyStats.add(new Label(enemyName, new Label.LabelStyle(font, Color.BLACK))).left().row();
        Stack enemyStack = new Stack();
        enemyStack.add(new Image(getColoredDrawable(200, 15, Color.BLACK)));
        enemyStack.add(new Container<>(healthBarEnemy).align(Align.left));
        enemyStats.add(enemyStack).size(200, 15).left();

        enemyArea.add(enemyStats).left().padTop(5);
        tableRoot.add(enemyArea).expand().top().right().pad(40).row();

        // Jugador
        Table playerArea = new Table();
        playerArea.add(new Image(new Texture(team[activeIndex].texturePath))).size(150);

        Table pStats = new Table();
        pStats.add(new Label(team[activeIndex].name, new Label.LabelStyle(font, Color.BLACK))).left().row();
        Stack playerStack = new Stack();
        playerStack.add(new Image(getColoredDrawable(200, 15, Color.BLACK)));
        playerStack.add(new Container<>(healthBarPlayer).align(Align.left));
        pStats.add(playerStack).size(200, 15);

        playerArea.add(pStats).padLeft(20);
        tableRoot.add(playerArea).expand().bottom().left().pad(40).row();

        // Texto
        Table bottom = new Table();
        tableB = new Table();
        messageLabel = new Label("¿Qué debería hacer " + team[activeIndex].name + "?", new Label.LabelStyle(font, Color.WHITE));
        messageLabel.setWrap(true);
        messageLabel.setAlignment(Align.center);

        Table msgTable = new Table();
        msgTable.setBackground(getColoredDrawable(1,1, new Color(0.1f,0.1f,0.1f,0.8f)));
        msgTable.add(messageLabel).expand().fill().pad(10);

        setupMainButtons();
        bottom.add(msgTable).expand().fill().uniformX();
        bottom.add(tableB).expand().fill().uniformX();
        tableRoot.add(bottom).fillX().height(150).bottom();

        updateHealth();
    }

    private void setupMainButtons(){
        tableB.clearChildren();
        Table table = new Table();
        TextButton buttonFight = new TextButton("FIGHT", styleRed);
        TextButton buttonSpirits = new TextButton("SPIRITS", styleBlue);
        TextButton buttonBag = new TextButton("BAG", styleGreen);
        TextButton buttonRun = new TextButton("RUN", styleYellow);

        buttonFight.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent e, float x, float y){
                onFightSelected();
            }});

        buttonSpirits.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent e, float x, float y){
            stage.addActor(new SpiritsMenu(styleBlue, BattleMain.this, false));
        }});

        buttonBag.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent e, float x, float y){
            tableB.clearChildren(); tableB.add(new BagMenu(styleGreen, BattleMain.this)).expand().fill();
        }});

        buttonRun.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent e, float x, float y){ Gdx.app.exit();
        }});

        table.add(buttonFight).expand().fill().uniformX().pad(2);
        table.add(buttonSpirits).expand().fill().uniformX().pad(2).row();
        table.add(buttonBag).expand().fill().uniformX().pad(2);
        table.add(buttonRun).expand().fill().uniformX().pad(2);
        tableB.add(table).expand().fill();
        updateAttackMenu();
    }

    public void updateHealth(){
        float hpAlly = team[activeIndex].hp / team[activeIndex].hpMax;
        float hpEnemy = this.hpEnemy / hpMaxEnemy;

        Color colorAlly = hpAlly < 0.2f ? Color.RED : (hpAlly < 0.5f ? Color.YELLOW : Color.GREEN);
        Color colorEnemy = hpEnemy < 0.2f ? Color.RED : (hpEnemy < 0.5f ? Color.YELLOW : Color.GREEN);

        healthBarPlayer.setDrawable(getColoredDrawable((int)(200 * hpAlly), 15, colorAlly));
        healthBarEnemy.setDrawable(getColoredDrawable((int)(200 * hpEnemy), 15, colorEnemy));
    }

    public void dealDamage(boolean toEnemy, float amount){
        if (toEnemy){
            hpEnemy = Math.max(0, hpEnemy - amount);
            updateHealth();
        }else{
            team[activeIndex].hp = Math.max(0, team[activeIndex].hp - amount);
            updateHealth();

            if (team[activeIndex].hp <= 0){
                messageLabel.setText("¡" + team[activeIndex].name + " ha sido debilitado!");
                stage.addActor(new SpiritsMenu(styleBlue, this, true));
            }
        }
    }

    public void switchSpirit(int index){
        activeIndex = index;
        messageLabel.setText("¡Adelante " + team[activeIndex].name + "!");
        setupBattleUI();
        enemyTurnTimer();
    }

    public void applyPotion(int index, int amount){
        team[index].heal(amount);
        messageLabel.setText(team[index].name + " recuperó salud.");
        updateHealth();
        enemyTurnTimer();
    }

    private void enemyTurnTimer(){
        com.badlogic.gdx.utils.Timer.schedule(new com.badlogic.gdx.utils.Timer.Task(){
            @Override
            public void run() {
                if(hpEnemy > 0 && team[activeIndex].hp > 0) {
                    messageLabel.setText("¡" + enemyName + " lanza un ataque!");
                    dealDamage(false, 15);
                }
            }
        }, 1.2f);
    }


    public void updateAttackMenu(){
        attackMenu = new AttackMenu(styleRed, this, team[activeIndex].moves[0], team[activeIndex].moves[1], team[activeIndex].moves[2], team[activeIndex].moves[3]);
    }

    public TextureRegionDrawable getColoredDrawable(int w, int h, Color c){
        Pixmap p = new Pixmap(Math.max(1, w), h, Pixmap.Format.RGBA8888);
        p.setColor(c); p.fill();
        TextureRegionDrawable d = new TextureRegionDrawable(new TextureRegion(new Texture(p)));
        p.dispose();
        return d;
    }

    private TextButton.TextButtonStyle createButtonStyle(Color color){
        TextButton.TextButtonStyle text = new TextButton.TextButtonStyle();
        text.up = getColoredDrawable(1, 1, color);
        text.font = font;
        return text;
    }

    public Spirit[] getTeam(){
        return team;
    }

    public int getActiveIndex(){
        return activeIndex;
    }

    public int getPotions(){
        return potions;
    }

    public void setPotions(int p){
        potions = p;
    }

    public int getSuperPotions(){
        return superPotions;
    }

    public void setSuperPotions(int p){
        superPotions = p;
    }

    public Stage getStage(){
        return stage;
    }


    @Override
    public void onAttackSelected(String n){
        messageLabel.setText(team[activeIndex].name + " usó " + n + "!");
        dealDamage(true, 25);
        onBackSelected();
        if(hpEnemy > 0) enemyTurnTimer();
    }

    @Override
    public void onFightSelected() {
        tableB.clearChildren(); tableB.add(attackMenu).expand().fill();
    }

    @Override
    public void onBackSelected() {
        setupMainButtons();
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin(); bgSprite.draw(batch); batch.end();
        stage.act(); stage.draw();
    }
}
