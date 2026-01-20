package nintendont.amongspirits.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;
import nintendont.amongspirits.Const;
import nintendont.amongspirits.Main;
import nintendont.amongspirits.data.codex.*;
import nintendont.amongspirits.data.spirits.*;
import nintendont.amongspirits.entities.Enemy;
import nintendont.amongspirits.entities.ItemStack;
import nintendont.amongspirits.entities.Player;
import nintendont.amongspirits.utils.*;

public class BattleScreen implements Screen, MenuListener {
    private AssetManager assets;
    private Stage stage;
    private Table tableB;
    private SpriteBatch batch;
    private Sprite bgSprite;
    private Label messageLabel;

    private Main game;
    private Player player;
    private Enemy enemy;
    private int playerActiveIndex = 0;
    private int enemyActiveIndex = 0;
    private boolean shouldGoBackToGame = false;

    private Image healthBarPlayer, healthBarEnemy;
    private BitmapFont font;
    private TextButton.TextButtonStyle styleRed, styleBlue, styleGreen, styleYellow;

    public BattleScreen(Main game, Player player, Enemy enemy, int initialPlayerActiveIndex, AssetManager assets) {
        this.game = game;
        this.player = player;
        this.enemy = enemy;
        this.playerActiveIndex = initialPlayerActiveIndex;
        game.playMusic("", true);

        Gdx.input.setCursorCatched(false);

        this.assets = assets;
//        assets.load(BattleSpiritAssets.MALE_DEER);
//        assets.load(BattleSpiritAssets.FEMALE_DEER);
//        assets.load(BattleSpiritAssets.MALE_WOLF);
//        assets.load(BattleSpiritAssets.FEMALE_WOLF);
//        assets.load(BattleSpiritAssets.MALE_BUNNY);
//        assets.load(BattleSpiritAssets.FEMALE_BUNNY);
//        assets.load(BattleSpiritAssets.MALE_FOX);
//        assets.load(BattleSpiritAssets.FEMALE_FOX);
//        assets.load(BattleSpiritAssets.MALE_LION);
//        assets.load(BattleSpiritAssets.FEMALE_LION);

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

        setupBattleUI();
    }

    public void setupBattleUI(){
        stage.clear();
        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);


        // UI ENEMIGO
        Invocation enemyInvocation = getEnemyActiveInvocation();
        Texture enemyGraphic = assets.get(enemyInvocation.getBattleAsset());
        healthBarEnemy = new Image();
        Table enemyGroup = new Table();
        enemyGroup.add(new Image(enemyGraphic)).size(180).row();
        Table enemyInfo = new Table();
        enemyInfo.add(healthBarEnemy).size(200, 20).row();
        enemyInfo.add(new Label(enemyInvocation.getFullName(), new Label.LabelStyle(font, Color.RED)));
        enemyGroup.add(enemyInfo).left().padTop(10);
        root.add(enemyGroup).expand().top().right().padTop(60).padRight(180).row();

        // UI JUGADOR
        Invocation playerInvocation = getPlayerActiveInvocation();
        Texture playerGraphic = assets.get(playerInvocation.getBattleAsset());
        healthBarPlayer = new Image();
        Table playerGroup = new Table();
        playerGroup.add(new Image(playerGraphic)).size(180).row();
        playerGroup.add(healthBarPlayer).size(200, 20).padTop(5).row();
        playerGroup.add(new Label(playerInvocation.getFullName(), new Label.LabelStyle(font, Color.CYAN)));
        root.add(playerGroup).expand().bottom().left().pad(40).row();

        float panelHeight = 190;
        Table bottomPanel = new Table();
        bottomPanel.setBackground(getColoredDrawable(1, 1, new Color(0, 0, 0, 0.85f)));

        messageLabel = new Label("¿Qué hará " + playerInvocation.getFullName() + "?", new Label.LabelStyle(font, Color.WHITE));
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
            @Override public void clicked(InputEvent e, float x, float y) {
                onFightSelected();
            }});

        btnBag.addListener(new ClickListener(){
            @Override public void clicked(InputEvent e, float x, float y) {
                game.playSound("");
            tableB.clearChildren();
            tableB.add(new BagMenu(styleBlue, BattleScreen.this, player.getSatchel().getItems())).fill();
        }});

        btnTeam.addListener(new ClickListener(){
            @Override public void clicked(InputEvent e, float x, float y) {
                game.playSound("");
            stage.addActor(new SpiritsMenu(styleGreen, BattleScreen.this, false, assets));
        }});

        btnRun.addListener(new ClickListener(){
            @Override public void clicked(InputEvent e, float x, float y) {
                game.playSound("");
                game.setScreen(game.gameScreen);
            }
        });

        // Anadir botones principales
        tableB.add(btnFight).size(btnW, btnH).pad(2);
        tableB.add(btnBag).size(btnW, btnH).pad(2).row();
        tableB.add(btnTeam).size(btnW, btnH).pad(2);
        tableB.add(btnRun).size(btnW, btnH).pad(2);
    }

    @Override
    public void onAttackSelected(String attackName) {
        game.playSound("");
        Invocation playerInvocation = getPlayerActiveInvocation();
        Invocation enemyInvocation = getEnemyActiveInvocation();
        SpiritMove moveUsed = playerInvocation.getMoves().stream().filter(m -> m.getName().equals(attackName)).findFirst().orElse(null);

        if (moveUsed != null) {
            int damageToGive = moveUsed.getBasePower();
            enemyInvocation.takeDamage(damageToGive);
            game.playSound("");
            messageLabel.setText("¡" + enemyInvocation.getFullName() + " usó " + attackName + "!");
        }

        if (enemyInvocation.isFainted()) {
            for (int i = 0; i < enemy.getTeam().getMembers().size(); i++) {
                if (enemy.getTeam().getMembers().get(i).isActive()) {
                    enemyActiveIndex = i;
                }
            }
        }

        checkEndgame();
        updateHealth();
        startEnemyTurn();
    }

    public void startEnemyTurn() {
        tableB.clearChildren();
        Timer.schedule(new Timer.Task(){
            @Override public void run(){
                Invocation playerInvocation = getPlayerActiveInvocation();
                Invocation enemyInvocation = getEnemyActiveInvocation();

                if (enemyInvocation.isFainted()){
                    game.playSound("");
                    messageLabel.setText("¡El enemigo ha sido derrotado!");
                } else {
                    SpiritMove move = enemyInvocation.getRandomMove();
                    playerInvocation.takeDamage(move.getBasePower());

                    messageLabel.setText("¡El enemigo contraataca!");
                    updateHealth();
                    Timer.schedule(new Timer.Task(){
                        @Override
                        public void run(){
                            if (playerInvocation.isFainted() && player.getTeam().isAnyMemberActive()){
                                stage.addActor(new SpiritsMenu(styleGreen, BattleScreen.this, true, assets));
                            }else{
                                messageLabel.setText("¿Qué hará " + playerInvocation.getFullName() + "?");
                                setupMainButtons(190);
                            }
                            checkEndgame();
                        }
                    }, 1.5f);
                }
            }
        }, 1.5f);
    }

    public void updateHealth(){
        healthBarPlayer.setDrawable(createBarDrawable(getPlayerActiveInvocation().getHealthRatio()));
        healthBarEnemy.setDrawable(createBarDrawable(getEnemyActiveInvocation().getHealthRatio()));
    }

    private void checkEndgame() {
        if (player.getTeam().areAllMembersDefeated()) {
            messageLabel.setText("El jugador ha perdido!");
            tableB.clearChildren();

            ItemStack itemStack = player.getSatchel().getRandomItem();
            itemStack.decrease();

            requestGoBackToGameAfter();
        } else if (enemy.getTeam().areAllMembersDefeated()) {
            messageLabel.setText("El jugador ha ganado!");
            tableB.clearChildren();

            if (enemy.isWild()) {
                for (Invocation invocation : enemy.getTeam().getMembers()) {
                    SpiritForm spiritForm = invocation.getSpirit().getForm();
                    spiritForm.validateTask(new ResearchTaskAction(ResearchTaskActionType.DEFEAT));
                }
            } else {
                for (Invocation invocation : player.getTeam().getMembers()) {
                    SpiritForm spiritForm = invocation.getSpirit().getForm();
                    spiritForm.validateTask(new ResearchTaskAction(ResearchTaskActionType.WIN));
                }
            }

            requestGoBackToGameAfter();
        }
    }

    private void requestGoBackToGameAfter() {
        Timer.schedule(new Timer.Task(){
            @Override
            public void run(){
                shouldGoBackToGame = true;
            }
        }, 3f);
    }

    private Invocation getPlayerActiveInvocation() {
        return player.getTeam().getMembers().get(playerActiveIndex);
    }

    private Invocation getEnemyActiveInvocation() {
        return enemy.getTeam().getMembers().get(enemyActiveIndex);
    }

    public Main getGame() {
        return game;
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

    public int getPlayerActiveIndex(){
        return playerActiveIndex;
    }

    public Player getPlayer(){
        return player;
    }

    public void switchSpirit(int idx){
        playerActiveIndex = idx; setupBattleUI(); startEnemyTurn();
    }

    @Override
    public void onBackSelected(){
        game.playSound("");
        setupMainButtons(190);
    }

    @Override
    public void onFightSelected(){
        game.playSound("");
        tableB.clearChildren();
        tableB.add(new AttackMenu(styleRed, this, getPlayerActiveInvocation().getMoves())).fill();
    }

    @Override
    public void render(float delta){
        if (shouldGoBackToGame){
            goBackToGame();
        }

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin(); bgSprite.draw(batch); batch.end();
        stage.act(); stage.draw();
    }

    private void goBackToGame() {
        game.setScreen(new GameScreen(game, assets, player));
    }

    @Override
    public void show() {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
    }
}
