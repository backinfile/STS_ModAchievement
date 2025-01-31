package ModAchievement;

import basemod.BaseMod;
import basemod.ModLabeledToggleButton;
import basemod.ModPanel;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.UIStrings;

import java.util.Properties;

import static ModAchievement.Res.BADGE_IMAGE;

public class SettingsPanel {
    public static boolean showDrawBagReview = false;
    public static boolean showDisposedPile = true;


    public static final String CONFIG_FILE_NAME = "config";
    public static final Properties settingsProperties = new Properties();

    public static void initProperties() {
//        settingsProperties.setProperty("showDrawBagReview", "FALSE");
//        settingsProperties.setProperty("showDisposedPile", "TRUE");
//        try {
//            SpireConfig config = new SpireConfig(ModPath.ModName, CONFIG_FILE_NAME, settingsProperties);
//            config.load();
//            showDrawBagReview = config.getBool("showDrawBagReview");
//            showDisposedPile = config.getBool("showDisposedPile");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    public static void saveProperties() {
//        try {
//            SpireConfig config = new SpireConfig(ModPath.ModName, CONFIG_FILE_NAME, settingsProperties);
//            config.setBool("showDrawBagReview", showDrawBagReview);
//            config.setBool("showDisposedPile", showDisposedPile);
//            config.save();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    public static void initPanel() {
//        UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ModPath.makeID("settingsPanel"));
//
//        ModPanel settingsPanel = new ModPanel();
//
//        // Create the on/off button:
//        ModLabeledToggleButton showDrawBagReviewBtn = new ModLabeledToggleButton(
//                uiStrings.TEXT[1], 350.0f, 650.0f, Settings.CREAM_COLOR, FontHelper.charDescFont,
//                showDrawBagReview, settingsPanel, (label) -> {
//        },
//                (button) -> {
//                    showDrawBagReview = button.enabled;
//                    saveProperties();
//                });
//
//        // Create the on/off button:
//        ModLabeledToggleButton showDisposedPileBtn = new ModLabeledToggleButton(
//                uiStrings.TEXT[2], 350.0f, 600.0f, Settings.CREAM_COLOR, FontHelper.charDescFont,
//                showDisposedPile, settingsPanel, (label) -> {
//        },
//                (button) -> {
//                    showDisposedPile = button.enabled;
//                    saveProperties();
//                });
//
//        settingsPanel.addUIElement(showDrawBagReviewBtn);
//        settingsPanel.addUIElement(showDisposedPileBtn);
//
//        Texture badgeTexture = TextureLoader.getTexture(BADGE_IMAGE);
//        BaseMod.registerModBadge(badgeTexture, ModPath.ModName, ModPath.AUTHOR, ModPath.DESCRIPTION, settingsPanel);
    }
}
