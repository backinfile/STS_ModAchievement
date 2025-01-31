package ModAchievement;

import ModAchievement.achievement.AchievementConfig;
import ModAchievement.achievement.AchievementManager;
import ModAchievement.achievement.ModAchievementStrings;
import basemod.BaseMod;
import basemod.interfaces.EditStringsSubscriber;
import basemod.interfaces.ISubscriber;
import basemod.interfaces.PostInitializeSubscriber;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.ModInfo;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.UIStrings;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Map;

@SpireInitializer
public class ModManager implements ISubscriber, EditStringsSubscriber, PostInitializeSubscriber {
    public static final Logger logger = LogManager.getLogger(ModPath.ModName);

    private static String modID;

    public ModManager() {
        BaseMod.subscribe(this);
        setModID(ModPath.ModName);
        SettingsPanel.initProperties();
    }

    public static void initialize() {
        Log.logger.info("========================= Initializing " + ModPath.ModName + " Mod. =========================");
        new ModManager();
        Log.logger.info("========================= /" + ModPath.ModName + " Initialized. Hello World./ =========================");
    }


    @Override
    public void receiveEditStrings() {
        Log.logger.info("Beginning to edit strings for mod with ID: " + ModPath.getModId());
        String lang = getLang();

        BaseMod.loadCustomStringsFile(UIStrings.class,
                ModPath.getResPath("/localization/" + lang + "/UI-Strings.json"));
        Log.logger.info("Done edittting strings");
    }

    public static String getLang() {
        Settings.GameLanguage lang = Settings.language;
        if (lang == Settings.GameLanguage.ZHS || lang == Settings.GameLanguage.ZHT) {
            return "zhs";
        } else {
            return "eng";
        }
    }


    @Override
    public void receivePostInitialize() {
        SettingsPanel.initPanel();
        logger.info("Done loading badge Image and mod options");

        // load configs
        addDefaultAchievements();
        loadAchievementConfigs();
    }

    private void addDefaultAchievements() {
        ArrayList<AchievementConfig> configs = new ArrayList<>();
        {
            String configString = Gdx.files.internal(ModPath.getResPath("/localization/" + getLang() + "/achievements.json")).readString(String.valueOf(StandardCharsets.UTF_8));
            Type type = (new TypeToken<Map<String, ModAchievementStrings>>() {
            }).getType();
            Gson gson = new Gson();
            Map<String, ModAchievementStrings> configMap = gson.fromJson(configString, type);
            if (configMap != null) {
                for (Map.Entry<String, ModAchievementStrings> entry : configMap.entrySet()) {
                    String key = entry.getKey();
                    String title = entry.getValue().TITLE;
                    String description = entry.getValue().DESCRIPTION;
                    String image = entry.getValue().IMAGE;
                    boolean hidden = entry.getValue().HIDDEN;
                    configs.add(new AchievementConfig(key, title, description, image, hidden));
                }
            }
            Log.logger.info("load default achievements:{}", configs.size());
        }

        for (AbstractCard.CardColor color : BaseMod.getCardColors()) {
            AbstractPlayer player = getPlayerByCardColor(color);
            if (player == null) continue;
            for (AchievementConfig config : configs) {
                AchievementManager.register(color.toString(),
                        MessageFormat.format("{0}:{1}", color.toString(), config.key),
                        config.title,
                        MessageFormat.format(config.desc, player.getLocalizedCharacterName()),
                        config.imgPath,
                        config.hidden
                );
            }
        }
    }

    public static AbstractPlayer getPlayerByCardColor(AbstractCard.CardColor color) {
        for (AbstractPlayer character : CardCrawlGame.characterManager.getAllCharacters()) {
            if (character.getCardColor() == color) {
                return character;
            }
        }
        return null;
    }

    public void loadAchievementConfigs() {

        for (ModInfo modinfo : Loader.MODINFOS) {
            Map<String, ModAchievementStrings> configs = loadConfigsFromJar(modinfo.jarURL, Settings.language);
            if (configs == null) configs = loadConfigsFromJar(modinfo.jarURL, Settings.GameLanguage.ENG);
            if (configs == null) continue;
            Log.logger.info("config find in {}", modinfo.ID);

            for (Map.Entry<String, ModAchievementStrings> entry : configs.entrySet()) {
                String key = entry.getKey();
                ModAchievementStrings strings = entry.getValue();
                if (StringUtils.isEmpty(strings.CARD_COLOR)) continue;
                AchievementManager.register(strings.CARD_COLOR, key, strings.TITLE, strings.DESCRIPTION, strings.IMAGE, strings.HIDDEN);
            }
        }
    }

    public Map<String, ModAchievementStrings> loadConfigsFromJar(URL jarURL, Settings.GameLanguage lang) {
//        Log.logger.info("jarURL = {}", jarURL);
        Gson gson = new Gson();
        Type type = (new TypeToken<Map<String, ModAchievementStrings>>() {
        }).getType();

        try {
            String file = jarURL + "!/" + getModdedLocalizationFilePath("achievements.json", Settings.language);
//            Log.logger.info("location = {}", file);
            URL eyeLocations = new URL("jar", "", file);
            try (InputStream in = eyeLocations.openStream()) {
                return gson.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), type);
            }
        } catch (Exception ex) {
            if (!(ex instanceof FileNotFoundException)) {
                logger.warn("Failed to load strings from " + jarURL, ex);
            }
        }
        return null;
    }

    private static String getModdedLocalizationFilePath(String file, Settings.GameLanguage lang) {
        String language = lang.toString().toLowerCase();
        return ModPath.ModName + "/localization/" + language + "/" + file;
    }

    // ====== NO EDIT AREA ======
    // DON'T TOUCH THIS STUFF. IT IS HERE FOR STANDARDIZATION BETWEEN MODS AND TO
    // ENSURE GOOD CODE PRACTICES.
    // IF YOU MODIFY THIS I WILL HUNT YOU DOWN AND DOWNVOTE YOUR MOD ON WORKSHOP

    public static void setModID(String ID) { // DON'T EDIT
        Gson coolG = new Gson(); // EY DON'T EDIT THIS
        // String IDjson =
        // Gdx.files.internal("IDCheckStringsDONT-EDIT-AT-ALL.json").readString(String.valueOf(StandardCharsets.UTF_8));
        // // i hate u Gdx.files
        InputStream in = ModManager.class.getResourceAsStream("/IDCheckStringsDONT-EDIT-AT-ALL.json"); // DON'T
        // EDIT
        // THIS
        // ETHER
        IDCheckDontTouchPls EXCEPTION_STRINGS = coolG.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8),
                IDCheckDontTouchPls.class); // OR THIS, DON'T EDIT IT
        logger.info("You are attempting to set your mod ID as: " + ID); // NO WHY
        if (ID.equals(EXCEPTION_STRINGS.DEFAULTID)) { // DO *NOT* CHANGE THIS ESPECIALLY, TO EDIT YOUR MOD ID, SCROLL UP
            // JUST A LITTLE, IT'S JUST ABOVE
            throw new RuntimeException(EXCEPTION_STRINGS.EXCEPTION); // THIS ALSO DON'T EDIT
        } else if (ID.equals(EXCEPTION_STRINGS.DEVID)) { // NO
            modID = EXCEPTION_STRINGS.DEFAULTID; // DON'T
        } else { // NO EDIT AREA
            modID = ID; // DON'T WRITE OR CHANGE THINGS HERE NOT EVEN A LITTLE
        } // NO
        logger.info("Success! ID is " + modID); // WHY WOULD U WANT IT NOT TO LOG?? DON'T EDIT THIS.
    } // NO

    public static String getModID() { // NO
        return modID; // DOUBLE NO
    } // NU-UH

    private static void pathCheck() { // ALSO NO
        Gson coolG = new Gson(); // NOPE DON'T EDIT THIS
        // String IDjson =
        // Gdx.files.internal("IDCheckStringsDONT-EDIT-AT-ALL.json").readString(String.valueOf(StandardCharsets.UTF_8));
        // // i still hate u btw Gdx.files
        InputStream in = ModManager.class.getResourceAsStream("/IDCheckStringsDONT-EDIT-AT-ALL.json"); // DON'T
        // EDIT
        // THISSSSS
        IDCheckDontTouchPls EXCEPTION_STRINGS = coolG.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8),
                IDCheckDontTouchPls.class); // NAH, NO EDIT
        String packageName = ModManager.class.getPackage().getName(); // STILL NO EDIT ZONE
        FileHandle resourcePathExists = Gdx.files.internal(getModID() + "Resources"); // PLEASE DON'T EDIT THINGS HERE,
        // THANKS
        if (!modID.equals(EXCEPTION_STRINGS.DEVID)) { // LEAVE THIS EDIT-LESS
            if (!packageName.equals(getModID())) { // NOT HERE ETHER
                throw new RuntimeException(EXCEPTION_STRINGS.PACKAGE_EXCEPTION + getModID()); // THIS IS A NO-NO
            } // WHY WOULD U EDIT THIS
            if (!resourcePathExists.exists()) { // DON'T CHANGE THIS
                throw new RuntimeException(EXCEPTION_STRINGS.RESOURCE_FOLDER_EXCEPTION + getModID() + "Resources"); // NOT
                // THIS
            } // NO
        } // NO
    }// NO

    // ====== YOU CAN EDIT AGAIN ======
}
