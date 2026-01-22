package nintendont.amongspirits.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.Timer;
import nintendont.amongspirits.Main;
import nintendont.amongspirits.data.assets.GameAssets;
import nintendont.amongspirits.data.codex.*;
import nintendont.amongspirits.data.spirits.*;
import nintendont.amongspirits.entities.Enemy;
import nintendont.amongspirits.data.satchel.ItemStack;
import nintendont.amongspirits.entities.Player;
import nintendont.amongspirits.utils.*;

public class BattleScreen implements Screen, MenuListener {
    private AssetManager assets;
    private Stage stage;
    private Table tableB;
    private SpriteBatch batch;
    private Sprite bgSprite;
    private Sprite priestSprite;
    private Label messageLabel;

    private Main game;
    private Player player;
    private Enemy enemy;
    private int playerActiveIndex = 0;
    private int enemyActiveIndex = 0;
    private boolean shouldGoBackToGame = false;

    private Image healthBarPlayer, healthBarEnemy;
    private Image playerImage;
    private Image enemyImage;
    private BitmapFont font;
    private TextButton.TextButtonStyle styleRed, styleBlue, styleGreen, styleYellow;

    public BattleScreen(Main game, Player player, Enemy enemy, int initialPlayerActiveIndex, AssetManager assets) {
        this.game = game;
        this.player = player;
        this.enemy = enemy;
        this.playerActiveIndex = initialPlayerActiveIndex;
        this.assets = assets;

        batch = new SpriteBatch();
        stage = new Stage();
        Gdx.input.setCursorCatched(false);
        Gdx.input.setInputProcessor(stage);
        font = new BitmapFont();

        // Styles
        styleRed = createButtonStyle(Color.RED);
        styleBlue = createButtonStyle(Color.BLUE);
        styleGreen = createButtonStyle(Color.GREEN);
        styleYellow = createButtonStyle(Color.YELLOW);

        // Sprites
        bgSprite = new Sprite(assets.get(GameAssets.FIGHT_BACKGROUND));
        bgSprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        Texture priestTexture = assets.get(GameAssets.PRIEST);
        priestSprite = new Sprite(priestTexture);
        priestSprite.setSize(priestTexture.getWidth()/6f, priestTexture.getHeight()/6f);

        game.playMusic("music and sounds/music/wild-battle.mp3", true);
        setupBattleUI();
    }

    public void setupBattleUI(){
        stage.clear();

        Stack mainStack = new Stack();
        mainStack.setFillParent(true);
        stage.addActor(mainStack);

        Table gameLayer = new Table();

        // JUGADOR
        Invocation playerInvocation = getPlayerActiveInvocation();
        Table playerGroup = new Table();
        healthBarPlayer = new Image();

        playerGroup.add(new Label(playerInvocation.getFullName(), new Label.LabelStyle(font, Color.CYAN))).row();
        playerGroup.add(healthBarPlayer).size(220, 22).padTop(5).row();

        playerImage = new Image(assets.get(playerInvocation.getBattleAsset()));
        playerImage.setScaling(Scaling.contain);
        playerImage.setOrigin(Align.center);

        playerImage.setScale(0);
        game.playSound("music and sounds/sounds/change.mp3");
        playerImage.addAction(Actions.sequence(
            Actions.scaleTo(1, 1, 0.5f, Interpolation.bounceOut),
            Actions.run(() -> applyIdleAnimation(playerImage))
        ));

        playerGroup.add(playerImage).size(350).padBottom(20);
        gameLayer.add(playerGroup).expand().bottom().left().padLeft(60);

        // ENEMIGO
        Invocation enemyInvocation = getEnemyActiveInvocation();
        Table enemyGroup = new Table();
        healthBarEnemy = new Image();

        TextureRegion enemyTextureRegion = new TextureRegion(assets.get(enemyInvocation.getBattleAsset()));
        enemyTextureRegion.flip(true, false);
        enemyImage = new Image(enemyTextureRegion);
        enemyImage.setScaling(Scaling.contain);
        enemyImage.setOrigin(Align.center);

        // Entrada enemigo
        enemyImage.setScale(0);
        enemyImage.addAction(Actions.sequence(
            Actions.scaleTo(1, 1, 0.5f, Interpolation.bounceOut),
            Actions.run(() -> applyIdleAnimation(enemyImage))
        ));

        enemyGroup.add(new Label(enemyInvocation.getFullName(), new Label.LabelStyle(font, Color.RED))).row();
        enemyGroup.add(healthBarEnemy).size(220, 22).padBottom(5).row();
        enemyGroup.add(enemyImage).size(350).padBottom(20);

        gameLayer.add(enemyGroup).expandX().right().padTop(300).padRight(60).row();
        mainStack.add(gameLayer.padBottom(550));

        // PANEL UI
        Table uiLayer = new Table();
        Table bottomPanel = new Table();
        bottomPanel.setBackground(getColoredDrawable(1, 1, new Color(0, 0, 0, 0.85f)));

        messageLabel = new Label("¿Qué hará " + playerInvocation.getFullName() + "?", new Label.LabelStyle(font, Color.WHITE));
        messageLabel.setWrap(true);
        messageLabel.setAlignment(Align.center);
        bottomPanel.add(messageLabel).width(Gdx.graphics.getWidth() * 0.5f).height(190).pad(20);

        tableB = new Table();
        setupMainButtons(190);
        bottomPanel.add(tableB.padRight(10)).width(Gdx.graphics.getWidth() * 0.5f).height(190);

        uiLayer.add(bottomPanel).expand().bottom().fillX();
        mainStack.add(uiLayer);

        updateHealth();
    }

    private void applyIdleAnimation(Image image) {
        image.addAction(Actions.forever(Actions.sequence(
            Actions.scaleTo(1.01f, 0.99f, 2.0f, Interpolation.sine),
            Actions.scaleTo(1f, 1f, 2.0f, Interpolation.sine)
        )));
    }

    public void playHealAnimation(Image targetActor) {
        if (targetActor == null) return;
        targetActor.addAction(Actions.sequence(
            Actions.color(Color.GREEN, 0.15f),
            Actions.moveBy(0, 25, 0.1f, Interpolation.exp5Out),
            Actions.moveBy(0, -25, 0.15f, Interpolation.bounceOut),
            Actions.color(Color.WHITE, 0.15f)
        ));
    }

    private void playDamageAnimation(Image targetActor) {
        if (targetActor == null) return;
        targetActor.addAction(Actions.sequence(
            Actions.color(Color.RED, 0.1f),
            Actions.moveBy(15, 0, 0.05f),
            Actions.moveBy(-30, 0, 0.05f),
            Actions.moveBy(15, 0, 0.05f),
            Actions.color(Color.WHITE, 0.1f)
        ));
    }

    private void playFaintAnimation(Image targetActor) {
        if (targetActor == null) return;
        targetActor.clearActions();
        targetActor.addAction(Actions.parallel(
            Actions.moveBy(0, -200, 0.6f, Interpolation.exp5In),
            Actions.fadeOut(0.5f)
        ));
    }

    public void applyDamage(Invocation target, int damage, boolean isPlayerReceiving) {
        target.takeDamage(damage);
        updateHealth();

        Image targetActor = isPlayerReceiving ? playerImage : enemyImage;

        if (target.isFainted()){
            game.playSound("music and sounds/sounds/fainted.mp3");
            playFaintAnimation(targetActor);
        } else {
            playDamageAnimation(targetActor);
        }
    }

    public void updateHealth(){
        healthBarPlayer.setDrawable(createBarDrawable(getPlayerActiveInvocation().getHealthRatio()));
        healthBarEnemy.setDrawable(createBarDrawable(getEnemyActiveInvocation().getHealthRatio()));
    }

    @Override
    public void onAttackSelected(String attackName) {
        Invocation playerInv = getPlayerActiveInvocation();
        Invocation enemyInv = getEnemyActiveInvocation();
        SpiritMove move = playerInv.getMoves().stream().filter(m -> m.getName().equals(attackName)).findFirst().orElse(null);

        if (move != null) {
            messageLabel.setText("¡" + playerInv.getFullName() + " usó " + attackName + "!");
            applyDamage(enemyInv, move.getBasePower(), false);
        }

        checkEndgame();

        if (!enemyInv.isFainted()) startEnemyTurn();
        else Timer.schedule(new Timer.Task(){
            @Override
            public void run(){
                for (int i = 0; i < enemy.getTeam().getMembers().size(); i++) {
                    if (!enemy.getTeam().getMembers().get(i).isFainted()) {
                        switchEnemySpirit(i);
                    }
                }
                startEnemyTurn();
            }}, 1f);
    }

    public void startEnemyTurn() {
        tableB.clearChildren();
        Timer.schedule(new Timer.Task(){
            @Override public void run(){
                Invocation pInv = getPlayerActiveInvocation();
                Invocation eInv = getEnemyActiveInvocation();

                if (eInv.isFainted()){
                    messageLabel.setText("¡El enemigo ha sido derrotado!");
                } else {
                    SpiritMove move = eInv.getRandomMove();
                    messageLabel.setText("¡" + eInv.getFullName() + " usó " + move.getName() + "!");
                    applyDamage(pInv, move.getBasePower(), true);

                    Timer.schedule(new Timer.Task(){
                        @Override public void run(){
                            if (pInv.isFainted() && player.getTeam().isAnyMemberActive()){
                                stage.addActor(new SpiritsMenu(styleGreen, BattleScreen.this, true, assets));
                            } else {
                                messageLabel.setText("¿Qué hará " + pInv.getFullName() + "?");
                                setupMainButtons(190);
                            }
                            checkEndgame();
                        }
                    }, 1.5f);
                }
            }
        }, 1.5f);
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
            @Override
            public void clicked(InputEvent e, float x, float y) {
                game.playSound("music and sounds/sounds/button_sel.mp3");
                onFightSelected();
            }});

        btnBag.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent e, float x, float y) {
                game.playSound("music and sounds/sounds/button_sel.mp3");
                tableB.clearChildren();
                tableB.add(new BagMenu(styleBlue, BattleScreen.this, player.getSatchel().getItems())).fill();
            }});

        btnTeam.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent e, float x, float y) {
                game.playSound("music and sounds/sounds/button_sel.mp3");
                stage.addActor(new SpiritsMenu(styleGreen, BattleScreen.this, false, assets));
            }});

        btnRun.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent e, float x, float y) {
                game.playSound("music and sounds/sounds/button_sel.mp3");
                game.setScreen(new GameScreen(game, assets, player));
            }
        });
        tableB.add(btnFight).size(btnW, btnH).pad(2);
        tableB.add(btnBag).size(btnW, btnH).pad(2).row();
        tableB.add(btnTeam).size(btnW, btnH).pad(2);
        tableB.add(btnRun).size(btnW, btnH).pad(2);
    }

    private void checkEndgame() {
        if (player.getTeam().areAllMembersDefeated() || enemy.getTeam().areAllMembersDefeated()) {
            game.stopMusic();
            messageLabel.setText(player.getTeam().areAllMembersDefeated() ? "¡Has perdido!" : "¡Victoria!");
            game.playSound(player.getTeam().areAllMembersDefeated() ? "music and sounds/sounds/defeat.mp3" : "music and sounds/sounds/win.mp3");
            tableB.clearChildren();
            Timer.schedule(new Timer.Task(){ @Override public void run(){ shouldGoBackToGame = true; }}, 3f);
        }
    }

    private TextureRegionDrawable createBarDrawable(float percent){
        Pixmap p = new Pixmap(200, 20, Pixmap.Format.RGBA8888);
        p.setColor(Color.BLACK); p.fill();
        p.setColor(percent < 0.2f ? Color.RED : (percent < 0.5f ? Color.YELLOW : Color.GREEN));
        p.fillRectangle(0, 0, (int)(200 * Math.max(0, percent)), 20);
        TextureRegionDrawable d = new TextureRegionDrawable(new TextureRegion(new Texture(p)));
        p.dispose();
        return d;
    }

    public TextureRegionDrawable getColoredDrawable(int w, int h, Color c) {
        Pixmap p = new Pixmap(w, h, Pixmap.Format.RGBA8888);
        p.setColor(c); p.fill();
        TextureRegionDrawable d = new TextureRegionDrawable(new TextureRegion(new Texture(p)));
        p.dispose();
        return d;
    }

    private TextButton.TextButtonStyle createButtonStyle(Color c) {
        TextButton.TextButtonStyle s = new TextButton.TextButtonStyle();
        s.up = getColoredDrawable(1, 1, c); s.font = font; return s;
    }

    public Invocation getPlayerActiveInvocation() {
        return player.getTeam().getMembers().get(playerActiveIndex);
    }

    public Invocation getEnemyActiveInvocation() {
        return enemy.getTeam().getMembers().get(enemyActiveIndex);
    }

    public Main getGame() {
        return game;
    }
    public Stage getStage() {
        return stage;
    }

    public Player getPlayer() {
        return player;
    }

    public int getPlayerActiveIndex() {
        return playerActiveIndex;
    }

    public void onBackSelected() {
        setupMainButtons(190);
    }

    public void onFightSelected() {
        tableB.clearChildren(); tableB.add(new AttackMenu(styleRed, this, getPlayerActiveInvocation().getMoves())).fill();
    }

    public void switchSpirit(int idx) {
        playerActiveIndex = idx; setupBattleUI(); startEnemyTurn();
    }

    public void switchEnemySpirit(int idx) {
        enemyActiveIndex = idx; setupBattleUI();
    }

    public Image getPlayerImage() {
        return playerImage;
    }

    public Image getEnemyImage() {
        return enemyImage;
    }

    @Override public void render(float delta){
        if (shouldGoBackToGame) game.setScreen(new GameScreen(game, assets, player));
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        bgSprite.draw(batch);
        batch.end();
        stage.act();
        stage.draw();
        batch.begin();
        priestSprite.draw(batch);
        batch.end();
    }

    @Override public void show() { Gdx.input.setCursorCatched(false); }
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {}
}
