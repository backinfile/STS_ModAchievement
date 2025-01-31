package ModAchievement.achievement;

import ModAchievement.Log;
import ModAchievement.ModPath;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.Prefs;
import com.megacrit.cardcrawl.helpers.SaveHelper;
import com.megacrit.cardcrawl.screens.stats.AchievementItem;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

@SpireInitializer
public class AchievementManager {
    public static final String CONFIG_FILE_NAME = "achievementsSave";

    private static SpireConfig achievementSave;
    public static final HashMap<String, ArrayList<AchievementConfig>> achievementMap = new HashMap<>();

    public static void initialize() {
        initProperties();
    }

    public static void register(String color, String key, String title, String desc, String imgPath, boolean hidden) {
        achievementMap.computeIfAbsent(color, c -> new ArrayList<>());
        AchievementConfig config = new AchievementConfig(key, title, desc, imgPath, hidden);
        achievementMap.get(color).add(config);
        Log.logger.info("add achievement: {},{},{}", key, title, desc);
    }

    public static boolean isUnlock(String key) {
        if (achievementSave != null) {
            return achievementSave.getBool(key);
        }
        return false;
    }

    public static void unlock(String key) {
        Log.logger.info("unlock key = {}", key);
        if (achievementSave != null) {
            achievementSave.setBool(key, true);
            saveProperties();
        }
    }


    public static void initProperties() {
        try {
            achievementSave = new SpireConfig(ModPath.ModName, CONFIG_FILE_NAME);
            achievementSave.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveProperties() {
        if (achievementSave != null) {
            try {
                achievementSave.save();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Log.logger.error("achievementSave not saved!");
        }
    }
}
