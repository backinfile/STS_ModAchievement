package ModAchievement.patches;

import ModAchievement.achievement.AchievementManager;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.DeathScreen;
import com.megacrit.cardcrawl.screens.VictoryScreen;
import com.megacrit.cardcrawl.screens.stats.StatsScreen;

import java.util.Set;

public class BaseAchievementUnlockPatch {
    @SpirePatch2(
            clz = DeathScreen.class,
            method = "updateAscensionProgress"
    )
    public static class RefreshData {
        public static void Postfix(DeathScreen __instance, boolean ___isVictory) {
            if (___isVictory) {
                checkAscensionAchievement();
            }
        }
    }

    @SpirePatch2(
            clz = VictoryScreen.class,
            method = SpirePatch.CONSTRUCTOR
    )
    public static class THE_ENDING {
        public static void Postfix(VictoryScreen __instance) {
            checkAscensionAchievement();

            AbstractCard.CardColor cardColor = AbstractDungeon.player.getCardColor();
            String key = cardColor.toString() + ":ModAchievement:TheEnd";
            AchievementManager.unlock(key);
        }
    }

    public static void checkAscensionAchievement() {
        if (Settings.seedSet) return;
        if (!Settings.isStandardRun()) return;

        AbstractCard.CardColor cardColor = AbstractDungeon.player.getCardColor();
        if (AbstractDungeon.player.getCharStat().getVictoryCount() > 0) {
            String key = cardColor.toString() + ":ModAchievement:0";
            AchievementManager.unlock(key);
        }
        if (AbstractDungeon.ascensionLevel >= 10) {
            String key = cardColor.toString() + ":ModAchievement:10";
            AchievementManager.unlock(key);
        }
        if (AbstractDungeon.ascensionLevel >= 20) {
            String key = cardColor.toString() + ":ModAchievement:20";
            AchievementManager.unlock(key);
        }
    }

    /*
    * 约定牌堆卡牌＞99
    * 两页遗物
    * 耗尽每一种
    * 台词碎心
    * 用坏播放器
    * 我在想起始口袋一张牌这个事件，可以和别的mod做个联动
    * */
}
