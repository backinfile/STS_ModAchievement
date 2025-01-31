package ModAchievement.achievement;

import com.megacrit.cardcrawl.cards.AbstractCard;

public class AchievementConfig {
    public String key;
    public String title;
    public String desc;
    public String imgPath;

    public boolean hidden;

    public AchievementConfig(String key, String title, String desc, String imgPath, boolean hidden) {
        this.key = key;
        this.title = title;
        this.desc = desc;
        this.imgPath = imgPath;
        this.hidden = hidden;
    }
}
