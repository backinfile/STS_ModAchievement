package ModAchievement.achievement;

import ModAchievement.Log;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.Prefs;
import com.megacrit.cardcrawl.helpers.SaveHelper;
import com.megacrit.cardcrawl.screens.stats.AchievementItem;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SpireInitializer
public class AchievementManager {

    public static Prefs achievementPref;
    public static final HashMap<String, ArrayList<AchievementConfig>> achievementMap = new HashMap<>();

    public static void initialize() {
        achievementPref = new Prefs();//SaveHelper.getPrefs("STSModAchievements");
    }

    public static void register(String color, String key, String title, String desc, String imgPath, boolean hidden) {
        achievementMap.computeIfAbsent(color, c -> new ArrayList<>());
        AchievementConfig config = new AchievementConfig(key, title, desc, imgPath, hidden);
        achievementMap.get(color).add(config);
        Log.logger.info("add achievement: {},{},{}", key, title, desc);
    }

}
