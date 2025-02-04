# Mod Achievement

## How to add achievements to your mod

1. Create file `ModAchievement/localization/lang/achievements.json` for each lang in your mod.
2. Add images(160 * 160) for your achievements.
3. Do some patches to send achievement unlock signal. Use `UnlockTracker.unlockAchievement("your_achievement_id");`

##### Format of achievements.json

```
{
  "Karen:Eat": {
    "CARD_COLOR": "Karen",
    "TITLE": "Eat well",
    "DESCRIPTION": "Gain 25 Strength in one turn.",
    "HIDDEN": false,
    "IMAGE": "ShoujoKagekiResources/images/achievement/Eat.png"
  },
  "Karen:Arrogant": {
    "CARD_COLOR": "Karen",
    "TITLE": "Passionate Revue",
    "DESCRIPTION": "Have > 25 relics.",
    "HIDDEN": false,
    "IMAGE": "ShoujoKagekiResources/images/achievement/Arrogant.png"
  }
}
```

