package ModAchievement.patches;

import ModAchievement.achievement.TabBar;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.screens.stats.StatsScreen;

public class StatsScreenPatch {
    private static float y = Settings.HEIGHT - 230.0F * Settings.yScale;

    public static class Lazy {
        public static TabBar tabBar = new TabBar();
    }

    @SpirePatch2(
            clz = StatsScreen.class,
            method = "render"
    )
    public static class Render {
        public static void Prefix(StatsScreen __instance, SpriteBatch sb, float ___scrollY) {
            Lazy.tabBar.render(sb, y + ___scrollY);
        }
    }

    @SpirePatch2(
            clz = StatsScreen.class,
            method = "update"
    )
    public static class Update {
        public static void Prefix(StatsScreen __instance, float ___scrollY) {
            Lazy.tabBar.update(y + ___scrollY);
        }
    }
}
