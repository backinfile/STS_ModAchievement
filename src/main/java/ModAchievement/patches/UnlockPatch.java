package ModAchievement.patches;

import ModAchievement.Log;
import ModAchievement.achievement.AchievementManager;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.unlock.UnlockTracker;

public class UnlockPatch {

    @SpirePatch2(
            clz = UnlockTracker.class,
            method = "unlockAchievement"
    )
    public static class Render {
        public static void Prefix(String key) {
//            Log.logger.info("Settings.isModded = {}", Settings.isModded);
            AchievementManager.unlock(key);
//            unlockInGameAchievement(key);

//            Log.logger.info("key = {}", key);
//            new RuntimeException("").printStackTrace();
        }
    }

//    public static void unlockInGameAchievement(String key) {
//        if (!Settings.isShowBuild && Settings.isStandardRun()) {
//            if (!UnlockTracker.achievementPref.getBoolean(key, false)) {
//                UnlockTracker.achievementPref.putBoolean(key, true);
//                Log.logger.info("InGame Achievement Unlocked: " + key);
//            }
//
//            if (UnlockTracker.allAchievementsExceptPlatinumUnlocked() && !UnlockTracker.isAchievementUnlocked("ETERNAL_ONE")) {
//                UnlockTracker.achievementPref.putBoolean("ETERNAL_ONE", true);
//                Log.logger.info("InGame Achievement Unlocked: ETERNAL_ONE");
//            }
//
//            UnlockTracker.achievementPref.flush();
//        }
//    }

}
