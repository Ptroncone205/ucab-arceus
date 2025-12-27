package nintendont.amongspirits.ui.codex;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Scaling;
import nintendont.amongspirits.data.codex.*;

public class CodexEntrySummary extends Table {
    private static final Color BG_COLOR = Color.valueOf("e6e1c9");
    private static final Color HEADER_BG_COLOR = Color.valueOf("273a4e");
    private static final Color HEADER_BG_COLOR_SECONDARY = Color.valueOf("394a5a");
    private static final Color HEADER_BG_COLOR_TERTIARY = Color.valueOf("1d2630");

    private AssetManager manager;
    private SpiritForm spiritForm;

    public CodexEntrySummary(AssetManager manager, SpiritForm spiritForm) {
        this.manager = manager;
        this.spiritForm = spiritForm;
        buildContent();
    }

    public SpiritForm getSpiritForm() {
        return spiritForm;
    }

    public void setSpiritForm(SpiritForm spiritForm) {
        this.spiritForm = spiritForm;
        this.clearChildren();
        buildContent();
    }

    public void buildContent() {
        Table contentTable = new Table();
        contentTable.padRight(120).padTop(200).top();

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        TextureRegionDrawable backgroundDrawable = new TextureRegionDrawable(new Texture(pixmap));
        pixmap.dispose();

        Table header = new Table();
        header.setBackground(backgroundDrawable);
        header.setColor(HEADER_BG_COLOR);
        header.left().padLeft(20);

        BitmapFont titleFont = manager.get("roboto_2xl.ttf", BitmapFont.class);
        BitmapFont mainFont = manager.get("roboto_lg.ttf", BitmapFont.class);
        BitmapFont levelFont = manager.get("chinese_8xl.ttf", BitmapFont.class);

        Label.LabelStyle titleStyle = new Label.LabelStyle();
        titleStyle.font = titleFont;
        titleStyle.fontColor = Color.WHITE;

        Label.LabelStyle headerStyle = new Label.LabelStyle();
        headerStyle.font = mainFont;
        headerStyle.fontColor = Color.WHITE;

        Label.LabelStyle bodyStyle = new Label.LabelStyle();
        bodyStyle.font = mainFont;
        bodyStyle.fontColor = Color.BLACK;

        Label.LabelStyle levelStyle = new Label.LabelStyle();
        levelStyle.font = levelFont;
        levelStyle.fontColor = Color.BLACK;

        String indexStr = String.format("No. %03d", spiritForm.getId());
        Label indexLabel = new Label(indexStr, headerStyle);
        Container<Label> indexContainer = new Container<>(indexLabel);
        indexContainer.setBackground(backgroundDrawable);
        indexContainer.setColor(HEADER_BG_COLOR_TERTIARY);
        indexContainer.padLeft(20).padRight(20);

        Label nameLabel = new Label(spiritForm.getAnimalName(), titleStyle);

        Table nameTable = new Table();
        nameTable.defaults().space(20);
        nameTable.add(indexContainer);
        nameTable.add(nameLabel);
        header.add(nameTable);
        header.add().expandX();

        Label descriptionLabel = new Label(spiritForm.getDescription(), headerStyle);
        Container<Label> descriptionContainer = new Container<>(descriptionLabel);
        descriptionContainer.setBackground(backgroundDrawable);
        descriptionContainer.setColor(HEADER_BG_COLOR_SECONDARY);

        Label elementLabel = new Label(spiritForm.getElement().name(), headerStyle);
        Container<Label> elementContainer = new Container<>(elementLabel);
        elementContainer.setBackground(backgroundDrawable);
        elementContainer.setColor(HEADER_BG_COLOR_TERTIARY);
        elementContainer.padLeft(10).padRight(10);

        Table descriptionTable = new Table();
        descriptionTable.defaults().space(10);
        descriptionTable.add(descriptionContainer).size(250, 24);
        descriptionTable.add(elementContainer).height(24);
        header.add(descriptionTable).padRight(140);

        contentTable.add(header).width(824).height(48);
        contentTable.row().padTop(20);

        Table bodyTable = new Table();
        bodyTable.defaults().space(20).top();

        Texture previewTexture = manager.get(spiritForm.getPreviewAsset());
        Image previewImage = new Image(previewTexture);
        previewImage.setScaling(Scaling.fit);
        bodyTable.add(previewImage).height(334);

        Table researchTasksTable = new Table();
        researchTasksTable.defaults().space(6).top().left();

        Label researchTasksLabel = new Label("Research Tasks", headerStyle);
        Container<Label> researchTasksLabelContainer = new Container<>(researchTasksLabel).left();
        researchTasksLabelContainer.setBackground(backgroundDrawable);
        researchTasksLabelContainer.setColor(HEADER_BG_COLOR);
        researchTasksLabelContainer.padLeft(20);
        researchTasksTable.add(researchTasksLabelContainer).height(30).width(300);
        researchTasksTable.row();

        for (ResearchTaskSet set : spiritForm.getTasks()) {
            Table setTable = new Table().padTop(15);

            Label setLabel = new Label(set.getDescription(), bodyStyle);
            setTable.add(setLabel).row();

            Table countsTable = new Table();

            Label currentCountLabel = new Label(String.valueOf(set.getCurrentCount()), bodyStyle);
            countsTable.add(currentCountLabel).padRight(15);

            Table milestoneTable = new Table();
            milestoneTable.defaults().space(8);
            for (Milestone milestone : set.getMilestones()) {
                Label milestoneLabel = new Label(String.valueOf(milestone.getTargetCount()), bodyStyle);
                Container<Label> milestoneContainer = new  Container<>(milestoneLabel);
                milestoneContainer.setBackground(backgroundDrawable);
                milestoneContainer.setColor(Color.valueOf("e7deb2"));
                milestoneTable.add(milestoneContainer).size(40, 24);
            }
            countsTable.add(milestoneTable);

            setTable.add(countsTable);
            researchTasksTable.add(setTable).width(280).height(30);
            researchTasksTable.row().padTop(20);
        }

        Table researchLevelTable = new Table();
        Label researchLevelLabel = new Label("Research Level", bodyStyle);
        Label levelLabel = new Label(String.valueOf(spiritForm.getResearchLevel()), levelStyle);
        researchLevelTable.add(researchLevelLabel).row();
        researchLevelTable.add(levelLabel);

        researchTasksTable.add().expandY().row();
        researchTasksTable.add(researchLevelTable).center();

        bodyTable.add(researchTasksTable).height(334);

        contentTable.add(bodyTable);
        contentTable.row().padTop(20);

        Table footerTable = new Table();

        Label longDescriptionLabel = new Label(spiritForm.getLongDescription(), bodyStyle);
        longDescriptionLabel.setWrap(true);
        longDescriptionLabel.setAlignment(Align.center);
        footerTable.add(longDescriptionLabel).width(500);

        contentTable.add(footerTable);

        Texture scrollTexture = manager.get("sprites/backgrounds/codex-scroll.png", Texture.class);
        Image scroll = new Image(scrollTexture);
        scroll.setScaling(Scaling.fit);
        Container<Image> scrollContainer = new Container<>(scroll);
        scrollContainer.setTransform(true);
        scrollContainer.setScale(1.65f);
        scrollContainer.setOrigin(scroll.getWidth() / 4 + 75, scroll.getHeight() / 4);

        Stack stack = new Stack();
        stack.add(scrollContainer);
        stack.add(contentTable);

        this.add(stack);
    }
}
