package ModAchievement.achievement;

import ModAchievement.ModPath;
import ModAchievement.TextureLoader;
import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.screens.stats.AchievementItem;

public class AchievementItemActor extends AchievementItem {
    public TextureRegion texture;
    private static final Color LOCKED_COLOR = new Color(1.0F, 1.0F, 1.0F, 0.8F);
    private static final TextureRegion lockTex = new TextureRegion(TextureLoader.getTexture(ModPath.makeAchievementImagePath("lock.png")));
    public static final TextureRegion defaultTex = new TextureRegion(TextureLoader.getTexture(ModPath.makeAchievementImagePath("default.png")));
    private static final ShaderProgram grayscaleShader;

    public AchievementItemActor(String title, String desc, String imgUrl, String key, boolean hidden) {
        super(title, desc, imgUrl, key, hidden);
        this.isUnlocked = AchievementManager.achievementPref.getBoolean(key, false);
    }

    public AchievementItemActor(String title, String desc, String imgUrl, String key) {
        super(title, desc, imgUrl, key);
    }

    @Override
    public void render(SpriteBatch sb, float x, float y) {
        if (texture == null) texture = defaultTex;
        sb.setColor(Color.WHITE);

        if (!this.isUnlocked) {
            sb.setShader(grayscaleShader);
            sb.setColor(LOCKED_COLOR);
        }

        float scale = Settings.scale;
        if (this.hb.hovered) {
            scale *= 1.1f;
        }

        sb.draw(this.texture, x - (float) this.texture.getRegionWidth() / 2.0F, y - (float) this.texture.getRegionHeight() / 2.0F, (float) this.texture.getRegionWidth() / 2.0F, (float) this.texture.getRegionHeight() / 2.0F, (float) this.texture.getRegionWidth(), (float) this.texture.getRegionHeight(), scale, scale, 0.0F);

        if (!this.isUnlocked) {
            sb.setColor(Color.WHITE);
            sb.draw(lockTex, x - (float) lockTex.getRegionWidth() / 2.0F, y - (float) lockTex.getRegionHeight() / 2.0F, (float) lockTex.getRegionWidth() / 2.0F, (float) lockTex.getRegionHeight() / 2.0F, (float) lockTex.getRegionWidth(), (float) lockTex.getRegionHeight(), scale, scale, 0.0F);
            sb.setShader(null);
        }

        this.hb.move(x, y);
        this.hb.render(sb);
    }

    @Override
    public void reloadImg() {
//        super.reloadImg();
    }

    static {
        String vertexShader = "attribute vec4 a_position;\n" +
                "attribute vec4 a_color;\n" +
                "attribute vec2 a_texCoord0;\n" +
                "uniform mat4 u_projTrans;\n" +
                "varying vec4 v_color;\n" +
                "varying vec2 v_texCoords;\n" +
                "void main() {\n" +
                "    v_color = a_color;\n" +
                "    v_texCoords = a_texCoord0;\n" +
                "    gl_Position = u_projTrans * a_position;\n" +
                "}";
        String fragmentShader = "#ifdef GL_ES\n" +
                "precision mediump float;\n" +
                "#endif\n" +
                "varying vec2 v_texCoords;\n" +
                "varying vec4 v_color;\n" +
                "uniform sampler2D u_texture;\n" +
                "const vec3 luminance = vec3(0.299, 0.587, 0.114);\n" +
                "void main() {\n" +
                "    vec4 color = texture2D(u_texture, v_texCoords);\n" +
                "    float gray = dot(color.rgb, luminance);\n" +
                "    gl_FragColor = vec4(gray, gray, gray, color.a);\n" +
                "}";
        grayscaleShader = new ShaderProgram(vertexShader, fragmentShader);
    }
}
