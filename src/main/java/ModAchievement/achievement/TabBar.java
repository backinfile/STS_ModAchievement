package ModAchievement.achievement;

import ModAchievement.Log;
import ModAchievement.TextureLoader;
import basemod.BaseMod;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.screens.stats.StatsScreen;

import java.util.ArrayList;

public class TabBar {

    public TabBar() {
    }

    static {
        // for basic
        Fields.modTabs.add(new ModColorTab(null, new Hitbox(235.0F * Settings.scale, 51.0F * Settings.scale)));

        // for other character
        for (AbstractCard.CardColor color : BaseMod.getCardColors()) {
            if (AchievementManager.achievementMap.containsKey(color.toString())) {
                Fields.modTabs.add(new ModColorTab(color, new Hitbox(235.0F * Settings.scale, 51.0F * Settings.scale)));
            }
        }
    }

    public void changeTabTo(int index) {
        if (Fields.modTabIndex == index) return;
        Fields.modTabIndex = index;
        Log.logger.info("change tabIndex to {}", index);

        if (index == 0) {
            CardCrawlGame.mainMenuScreen.statsScreen.refreshData();
        } else {
            StatsScreen.achievements.items.clear();
            for (AchievementConfig config : AchievementManager.achievementMap.get(Fields.getModTab().color.toString())) {
                AchievementItemActor actor = new AchievementItemActor(config.title, config.desc, "", config.key, config.hidden);
                actor.texture = TextureLoader.getTextureRegionOrNull(config.imgPath);
                StatsScreen.achievements.items.add(actor);
            }
        }

    }


    public void update(float y) {

        boolean anyHovered = false;


        for (int i = 0; i < Fields.modTabs.size(); ++i) {
            Fields.modTabs.get(i).hb.move(157.0F * Settings.scale, y - 64.0F * (float) i * Settings.scale - 14.0F * Settings.scale);
            Fields.modTabs.get(i).hb.update();
            if (!anyHovered && Fields.modTabs.get(i).hb.justHovered) {
                anyHovered = true;
                CardCrawlGame.sound.playA("UI_HOVER", -0.4F);
            }

            if (InputHelper.justClickedLeft && Fields.modTabs.get(i).hb.hovered) {
                changeTabTo(i);
            }
        }
    }

    public void render(SpriteBatch sb, float y) {
        for (int i = 0; i < Fields.modTabs.size(); ++i) {
            Color color = Fields.modTabs.get(i).getVfxColor();
            if (Fields.modTabIndex != i) {
                color = color.lerp(Color.GRAY, 0.5F);
            }

            sb.setColor(color);
            sb.draw(ImageMaster.COLOR_TAB_BAR, 40.0F * Settings.scale, y - 64.0F * (float) (i + 1) * Settings.scale, 0.0F, 0.0F, 235.0F, 102.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 1334, 102, false, false);
            Color textcolor = Settings.GOLD_COLOR;
            if (Fields.modTabIndex == i) {
                sb.setColor(Color.WHITE);
            } else {
                textcolor = Color.GRAY;
                sb.setColor(Color.GRAY);
            }

            String tabName = Fields.modTabs.get(i).getTabName();
            FontHelper.renderFontCentered(sb, FontHelper.buttonLabelFont, tabName, 157.0F * Settings.scale, y - 64.0F * (float) (i + 1) * Settings.scale + 50.0F * Settings.scale, textcolor, 0.85F);
        }

        for (int i = 0; i < Fields.modTabs.size(); ++i) {
            Fields.modTabs.get(i).hb.render(sb);
        }
    }



    public static class Fields {
        static int modTabIndex = 0;
        static ArrayList<ModColorTab> modTabs = new ArrayList<>();

        public Fields() {
        }

        public static ModColorTab getModTab() {
            return modTabs.get(modTabIndex);
        }
    }

    public static class ModColorTab {
        public AbstractCard.CardColor color;
        public AbstractPlayer.PlayerClass playerClass;
        private final Hitbox hb;

        private ModColorTab(AbstractCard.CardColor color, Hitbox hb) {
            this.color = color;
            this.hb = hb;
            for (AbstractPlayer character : CardCrawlGame.characterManager.getAllCharacters()) {
                if (character.getCardColor() == color) {
                    playerClass = character.chosenClass;
                    break;
                }
            }
        }

        public Color getVfxColor() {
            if (color == null) return Color.GRAY.cpy();
            return BaseMod.getTrailVfxColor(color).cpy();
        }

        public String getTabName() {
            if (color == null) return "原生成就";
            return playerClass != null ? BaseMod.findCharacter(playerClass).getLocalizedCharacterName() : capitalizeWord(color.toString());
        }
    }

    private static String capitalizeWord(String str) {
        return str.isEmpty() ? str : str.substring(0, 1).toUpperCase() + (str.length() > 1 ? str.substring(1).toLowerCase() : "");
    }
}
