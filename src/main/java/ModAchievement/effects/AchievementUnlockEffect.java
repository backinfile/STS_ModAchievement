package ModAchievement.effects;

import ModAchievement.Log;
import ModAchievement.ModPath;
import ModAchievement.TextureLoader;
import ModAchievement.achievement.AchievementConfig;
import ModAchievement.achievement.AchievementItemActor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class AchievementUnlockEffect extends AbstractGameEffect {
    public static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ModPath.makeID("AchievementUIText"));

    static {
        Log.logger.info("===================== uiStrings:{}", uiStrings != null);
    }


    private final AchievementConfig config;
    public int state = 0;  // 0-enter 1-wait 2-leave
    private static final float DUR_ENTER = 0.3f;
    private static final float DUR_WAIT = 2f;
    private static final float DUR_LEAVE = 0.3f;


    private float curPercent = 0;
    private TextureRegion texture;

    public AchievementUnlockEffect(AchievementConfig config) {
        this.config = config;
        this.duration = this.startingDuration = DUR_ENTER;
        Log.logger.info("AchievementUnlockEffect {} ==================", config.key);

        texture = TextureLoader.getTextureRegionOrNull(config.imgPath);
        if (texture == null) texture = AchievementItemActor.defaultTex;
        CardCrawlGame.sound.playA("TINGSHA", 0.5f);
    }

    @Override
    public void update() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        this.duration -= deltaTime;
        if (this.duration < 0.0F) {
            if (state == 0) {
                this.state = 1;
                this.duration = this.startingDuration = DUR_WAIT;
            } else if (state == 1) {
                this.state = 2;
                this.duration = this.startingDuration = DUR_LEAVE;
            } else {
                this.isDone = true;
                return;
            }
        }
        curPercent = 1 - this.duration / this.startingDuration;
    }

    @Override
    public void render(SpriteBatch sb) {
        float x = ACHIEVEMENT_X;
        float y = 0f;

        switch (state) {
            case 0:
                y = MathUtils.lerp(ACHIEVEMENT_Y_START, ACHIEVEMENT_Y_STOP, curPercent);
                break;
            case 1:
                y = ACHIEVEMENT_Y_STOP;
                break;
            case 2:
                y = MathUtils.lerp(ACHIEVEMENT_Y_STOP, ACHIEVEMENT_Y_START, curPercent);
                break;
        }

        renderTipBox(x, y, sb, uiStrings.TEXT[1], config.title);
    }

    private void renderTipBox(float x, float y, SpriteBatch sb, String title, String description) {
        float h = BOX_H;
        sb.setColor(Settings.TOP_PANEL_SHADOW_COLOR);
        sb.draw(ImageMaster.KEYWORD_TOP, x + SHADOW_DIST_X, y - SHADOW_DIST_Y, BOX_W, BOX_EDGE_H);
        sb.draw(ImageMaster.KEYWORD_BODY, x + SHADOW_DIST_X, y - h - BOX_EDGE_H - SHADOW_DIST_Y, BOX_W, h + BOX_EDGE_H);
        sb.draw(ImageMaster.KEYWORD_BOT, x + SHADOW_DIST_X, y - h - BOX_BODY_H - SHADOW_DIST_Y, BOX_W, BOX_EDGE_H);
        sb.setColor(Color.WHITE);
        sb.draw(ImageMaster.KEYWORD_TOP, x, y, BOX_W, BOX_EDGE_H);
        sb.draw(ImageMaster.KEYWORD_BODY, x, y - h - BOX_EDGE_H, BOX_W, h + BOX_EDGE_H);
        sb.draw(ImageMaster.KEYWORD_BOT, x, y - h - BOX_BODY_H, BOX_W, BOX_EDGE_H);
//        FontHelper.renderFontLeftTopAligned(sb, FontHelper.tipHeaderFont, title, x + TEXT_OFFSET_X, y + HEADER_OFFSET_Y, Settings.GOLD_COLOR);
//        FontHelper.renderSmartText(sb, FontHelper.tipBodyFont, description, x + TEXT_OFFSET_X, y + BODY_OFFSET_Y, BODY_TEXT_WIDTH, TIP_DESC_LINE_SPACING, BASE_COLOR);


        scale = 0.5f;
        sb.draw(this.texture,
                TEXT_OFFSET_X + x + TEXT_OFFSET_X / 3f,
                y - this.texture.getRegionHeight() * scale,
                0f,
                0f,
                (float) this.texture.getRegionWidth(),
                (float) this.texture.getRegionHeight(),
                scale, scale, 0.0F);

        float xOffset = this.texture.getRegionWidth() * scale + TEXT_OFFSET_X / 3f + TEXT_OFFSET_X / 3f;
        float yOffset = -this.texture.getRegionHeight() * scale / 2 + TIP_DESC_LINE_SPACING / 2;

        FontHelper.renderFontLeftTopAligned(sb, FontHelper.tipHeaderFont, title,
                x + TEXT_OFFSET_X + xOffset,
                y + HEADER_OFFSET_Y + yOffset,
                Settings.GOLD_COLOR);
        FontHelper.renderSmartText(sb, FontHelper.tipBodyFont, description,
                x + TEXT_OFFSET_X + xOffset,
                y + BODY_OFFSET_Y + yOffset,
                BODY_TEXT_WIDTH, TIP_DESC_LINE_SPACING, BASE_COLOR);

    }

    @Override
    public void dispose() {

    }

    private static final Color BASE_COLOR = new Color(1.0F, 0.9725F, 0.8745F, 1.0F);
    private static final float CARD_TIP_PAD = 12.0F * Settings.scale;
    private static final float SHADOW_DIST_Y = 14.0F * Settings.scale;
    private static final float SHADOW_DIST_X = 9.0F * Settings.scale;
    private static final float BOX_EDGE_H = 32.0F * Settings.scale;
    private static final float BOX_BODY_H = 64.0F * Settings.scale;
    private static final float BOX_W = 320.0F * Settings.scale * 1.2f;
    private static GlyphLayout gl = new GlyphLayout();
    private static final float TEXT_OFFSET_X = 22.0F * Settings.scale;
    private static final float HEADER_OFFSET_Y = 12.0F * Settings.scale;
    private static final float ORB_OFFSET_Y = -8.0F * Settings.scale;
    private static final float BODY_OFFSET_Y = -20.0F * Settings.scale;
    private static final float BODY_TEXT_WIDTH = 280.0F * Settings.scale;
    private static final float TIP_DESC_LINE_SPACING = 26.0F * Settings.scale;
    private static final float POWER_ICON_OFFSET_X = 40.0F * Settings.scale;
    private static final float BOX_H = TIP_DESC_LINE_SPACING * 2;
    private static final float ACHIEVEMENT_Y_START = -BOX_EDGE_H;
    private static final float ACHIEVEMENT_Y_STOP = BOX_EDGE_H * 2 + BOX_H;
    private static final float ACHIEVEMENT_X = Settings.WIDTH - BOX_W;
}
