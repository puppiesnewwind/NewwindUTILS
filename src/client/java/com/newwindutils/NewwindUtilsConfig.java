package com.newwindutils;

public class NewwindUtilsConfig {
    private static final int DEFAULT_STASIS_DURATION_TICKS = 10 * 20;
    private static final int DEFAULT_STASIS_UPGRADED_DURATION_TICKS = 15 * 20;
    private static final int DEFAULT_INSIPID_DURATION_TICKS = 10 * 20;
    private static final int DEFAULT_INSIPID_UPGRADED_DURATION_TICKS = 15 * 20;
    private static final int DEFAULT_INSIPID_BRIEF_FOOD_DURATION_TICKS = 2 * 20;
    private static final int DEFAULT_ACROPSIS_DURATION_TICKS = 10 * 20;
    private static final int DEFAULT_ACROPSIS_UPGRADED_DURATION_TICKS = 15 * 20;
    private static final int DEFAULT_VOID_LEGGINGS_DURATION_TICKS = 7 * 20;
    private static final int DEFAULT_VOID_LEGGINGS_UPGRADED_DURATION_TICKS = 14 * 20;
    private static final int DEFAULT_BRAXLION_DURATION_TICKS = 5 * 20;

    public int hudX = 10;
    public int hudY = 10;
    public int maxTargetsShown = 3;
    public int targetCardSpacing = 6;

    public int selfHudX = 450;
    public int selfHudY = 380;
    public boolean showSelfHud = true;

    public int headSize = 20;
    public boolean showPlayerHeads = true;
    public float hudScale = 1.0F;
    public float nameScale = 1.35F;

    public TimerDisplayMode timerDisplayMode = TimerDisplayMode.BAR_AND_SECONDS;
    public int barWidth = 110;
    public int barHeight = 6;

    public boolean colorBarsByRemainingTime = true;
    public boolean showOutline = true;
    public boolean shadowText = true;
    public boolean showOnlyLocalInflictedOnTargets = false;
    public boolean showOnlySelfTimers = false;

    public double focusMaxDistance = 48.0;
    public double focusMinDot = 0.55;
    public double focusDistanceWeight = 0.35;

    public int stasisDurationTicks = DEFAULT_STASIS_DURATION_TICKS;
    public int stasisUpgradedDurationTicks = DEFAULT_STASIS_UPGRADED_DURATION_TICKS;
    public int insipidDurationTicks = DEFAULT_INSIPID_DURATION_TICKS;
    public int insipidUpgradedDurationTicks = DEFAULT_INSIPID_UPGRADED_DURATION_TICKS;
    public int insipidBriefFoodDurationTicks = DEFAULT_INSIPID_BRIEF_FOOD_DURATION_TICKS;
    public int acropsisDurationTicks = DEFAULT_ACROPSIS_DURATION_TICKS;
    public int acropsisUpgradedDurationTicks = DEFAULT_ACROPSIS_UPGRADED_DURATION_TICKS;
    public int voidLeggingsDurationTicks = DEFAULT_VOID_LEGGINGS_DURATION_TICKS;
    public int voidLeggingsUpgradedDurationTicks = DEFAULT_VOID_LEGGINGS_UPGRADED_DURATION_TICKS;
    public int braxlionDurationTicks = DEFAULT_BRAXLION_DURATION_TICKS;

    public HudColorPreset colorPreset = HudColorPreset.OCEAN;

    public void clamp() {
        hudX = clampInt(hudX, 0, 5000);
        hudY = clampInt(hudY, 0, 5000);
        selfHudX = clampInt(selfHudX, 0, 5000);
        selfHudY = clampInt(selfHudY, 0, 5000);
        maxTargetsShown = clampInt(maxTargetsShown, 1, 8);
        targetCardSpacing = clampInt(targetCardSpacing, 0, 30);

        headSize = clampInt(headSize, 12, 48);
        hudScale = clampFloat(hudScale, 0.50F, 3.0F);
        nameScale = clampFloat(nameScale, 0.75F, 3.0F);

        barWidth = clampInt(barWidth, 40, 300);
        barHeight = clampInt(barHeight, 2, 20);

        focusMaxDistance = clampDouble(focusMaxDistance, 8.0, 128.0);
        focusMinDot = clampDouble(focusMinDot, 0.15, 0.95);
        focusDistanceWeight = clampDouble(focusDistanceWeight, 0.0, 2.0);

        if (stasisDurationTicks <= 0) {
            stasisDurationTicks = DEFAULT_STASIS_DURATION_TICKS;
        }
        if (stasisUpgradedDurationTicks <= 0) {
            stasisUpgradedDurationTicks = DEFAULT_STASIS_UPGRADED_DURATION_TICKS;
        }
        if (insipidDurationTicks <= 0) {
            insipidDurationTicks = DEFAULT_INSIPID_DURATION_TICKS;
        }
        if (insipidUpgradedDurationTicks <= 0) {
            insipidUpgradedDurationTicks = DEFAULT_INSIPID_UPGRADED_DURATION_TICKS;
        }
        if (insipidBriefFoodDurationTicks <= 0) {
            insipidBriefFoodDurationTicks = DEFAULT_INSIPID_BRIEF_FOOD_DURATION_TICKS;
        }
        if (acropsisDurationTicks <= 0) {
            acropsisDurationTicks = DEFAULT_ACROPSIS_DURATION_TICKS;
        }
        if (acropsisUpgradedDurationTicks <= 0) {
            acropsisUpgradedDurationTicks = DEFAULT_ACROPSIS_UPGRADED_DURATION_TICKS;
        }
        if (voidLeggingsDurationTicks <= 0) {
            voidLeggingsDurationTicks = DEFAULT_VOID_LEGGINGS_DURATION_TICKS;
        }
        if (voidLeggingsUpgradedDurationTicks <= 0) {
            voidLeggingsUpgradedDurationTicks = DEFAULT_VOID_LEGGINGS_UPGRADED_DURATION_TICKS;
        }
        if (braxlionDurationTicks <= 0) {
            braxlionDurationTicks = DEFAULT_BRAXLION_DURATION_TICKS;
        }

        stasisDurationTicks = clampInt(stasisDurationTicks, 1, 20 * 300);
        stasisUpgradedDurationTicks = clampInt(stasisUpgradedDurationTicks, 1, 20 * 300);
        insipidDurationTicks = clampInt(insipidDurationTicks, 1, 20 * 300);
        insipidUpgradedDurationTicks = clampInt(insipidUpgradedDurationTicks, 1, 20 * 300);
        insipidBriefFoodDurationTicks = clampInt(insipidBriefFoodDurationTicks, 1, 20 * 300);
        acropsisDurationTicks = clampInt(acropsisDurationTicks, 1, 20 * 300);
        acropsisUpgradedDurationTicks = clampInt(acropsisUpgradedDurationTicks, 1, 20 * 300);
        voidLeggingsDurationTicks = clampInt(voidLeggingsDurationTicks, 1, 20 * 300);
        voidLeggingsUpgradedDurationTicks = clampInt(voidLeggingsUpgradedDurationTicks, 1, 20 * 300);
        braxlionDurationTicks = clampInt(braxlionDurationTicks, 1, 20 * 300);

        if (timerDisplayMode == null) {
            timerDisplayMode = TimerDisplayMode.BAR_AND_SECONDS;
        }

        if (colorPreset == null) {
            colorPreset = HudColorPreset.OCEAN;
        }
    }

    private static int clampInt(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    private static float clampFloat(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }

    private static double clampDouble(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    public enum TimerDisplayMode {
        SECONDS_ONLY("Seconds only"),
        BAR_ONLY("Bar only"),
        BAR_AND_SECONDS("Bar + seconds");

        private final String label;

        TimerDisplayMode(String label) {
            this.label = label;
        }

        public String label() {
            return label;
        }

        public boolean showsSeconds() {
            return this == SECONDS_ONLY || this == BAR_AND_SECONDS;
        }

        public boolean showsBar() {
            return this == BAR_ONLY || this == BAR_AND_SECONDS;
        }

        public TimerDisplayMode next() {
            TimerDisplayMode[] values = values();
            return values[(ordinal() + 1) % values.length];
        }

        public TimerDisplayMode previous() {
            TimerDisplayMode[] values = values();
            return values[(ordinal() - 1 + values.length) % values.length];
        }
    }

    public enum HudColorPreset {
        OCEAN("Ocean", 0x900B1220, 0xFF4AA3FF, 0xFF9FD3FF, 0xFFFFFFFF, 0xFF2F7FD6, 0xFF14263A),
        FIRE("Fire", 0x901C0C08, 0xFFFF8A3D, 0xFFFFC266, 0xFFFFFFFF, 0xFFE25A1C, 0xFF3A180D),
        POISON("Poison", 0x9010180C, 0xFF76D64A, 0xFFB9FF8E, 0xFFFFFFFF, 0xFF55B83E, 0xFF162614),
        GOLD("Gold", 0x90201808, 0xFFFFD447, 0xFFFFF0A5, 0xFFFFFFFF, 0xFFE0B63A, 0xFF33260F),
        MONO("Mono", 0x90111111, 0xFFB8B8B8, 0xFFFFFFFF, 0xFFFFFFFF, 0xFF909090, 0xFF2A2A2A);

        private final String label;
        private final int backgroundColor;
        private final int outlineColor;
        private final int headerColor;
        private final int textColor;
        private final int barColor;
        private final int barBackgroundColor;

        HudColorPreset(
                String label,
                int backgroundColor,
                int outlineColor,
                int headerColor,
                int textColor,
                int barColor,
                int barBackgroundColor
        ) {
            this.label = label;
            this.backgroundColor = backgroundColor;
            this.outlineColor = outlineColor;
            this.headerColor = headerColor;
            this.textColor = textColor;
            this.barColor = barColor;
            this.barBackgroundColor = barBackgroundColor;
        }

        public String label() {
            return label;
        }

        public int backgroundColor() {
            return backgroundColor;
        }

        public int outlineColor() {
            return outlineColor;
        }

        public int headerColor() {
            return headerColor;
        }

        public int textColor() {
            return textColor;
        }

        public int barColor() {
            return barColor;
        }

        public int barBackgroundColor() {
            return barBackgroundColor;
        }

        public HudColorPreset next() {
            HudColorPreset[] values = values();
            return values[(ordinal() + 1) % values.length];
        }

        public HudColorPreset previous() {
            HudColorPreset[] values = values();
            return values[(ordinal() - 1 + values.length) % values.length];
        }
    }
}
