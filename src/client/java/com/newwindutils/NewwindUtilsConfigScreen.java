package com.newwindutils;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.function.IntConsumer;
import java.util.function.IntSupplier;

public class NewwindUtilsConfigScreen extends Screen {
    private final Screen parent;

    public NewwindUtilsConfigScreen(Screen parent) {
        super(Component.literal("NewwindUTILS"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();

        NewwindUtilsConfig config = NewwindUtilsConfigManager.get();

        int leftX = this.width / 2 - 210;
        int rightX = this.width / 2 + 10;
        int startY = 50;
        int rowHeight = 24;
        int buttonWidth = 200;

        addConfigButton(leftX, startY + rowHeight * 0, buttonWidth, () -> "HUD X: " + config.hudX, () -> config.hudX += 10, () -> config.hudX -= 10);
        addConfigButton(leftX, startY + rowHeight * 1, buttonWidth, () -> "HUD Y: " + config.hudY, () -> config.hudY += 10, () -> config.hudY -= 10);
        addConfigButton(leftX, startY + rowHeight * 2, buttonWidth, () -> "Max Players Shown: " + config.maxTargetsShown, () -> config.maxTargetsShown += 1, () -> config.maxTargetsShown -= 1);
        addConfigButton(leftX, startY + rowHeight * 3, buttonWidth, () -> "Card Spacing: " + config.targetCardSpacing, () -> config.targetCardSpacing += 1, () -> config.targetCardSpacing -= 1);
        addConfigButton(leftX, startY + rowHeight * 4, buttonWidth, () -> "Head Size: " + config.headSize, () -> config.headSize += 2, () -> config.headSize -= 2);
        addConfigButton(leftX, startY + rowHeight * 5, buttonWidth, () -> "Show Heads: " + onOff(config.showPlayerHeads), () -> config.showPlayerHeads = !config.showPlayerHeads, () -> config.showPlayerHeads = !config.showPlayerHeads);
        addConfigButton(leftX, startY + rowHeight * 6, buttonWidth, () -> "HUD Scale: " + String.format("%.2f", config.hudScale), () -> config.hudScale += 0.05F, () -> config.hudScale -= 0.05F);
        addConfigButton(leftX, startY + rowHeight * 7, buttonWidth, () -> "Name Scale: " + String.format("%.2f", config.nameScale), () -> config.nameScale += 0.05F, () -> config.nameScale -= 0.05F);
        addConfigButton(leftX, startY + rowHeight * 8, buttonWidth, () -> "Display Mode: " + config.timerDisplayMode.label(), () -> config.timerDisplayMode = config.timerDisplayMode.next(), () -> config.timerDisplayMode = config.timerDisplayMode.previous());
        addConfigButton(leftX, startY + rowHeight * 9, buttonWidth, () -> "Bar Width: " + config.barWidth, () -> config.barWidth += 10, () -> config.barWidth -= 10);
        addConfigButton(leftX, startY + rowHeight * 10, buttonWidth, () -> "Self HUD X: " + config.selfHudX, () -> config.selfHudX += 10, () -> config.selfHudX -= 10);
        addConfigButton(leftX, startY + rowHeight * 11, buttonWidth, () -> "Self HUD Y: " + config.selfHudY, () -> config.selfHudY += 10, () -> config.selfHudY -= 10);

        addConfigButton(rightX, startY + rowHeight * 0, buttonWidth, () -> "Bar Height: " + config.barHeight, () -> config.barHeight += 1, () -> config.barHeight -= 1);
        addConfigButton(rightX, startY + rowHeight * 1, buttonWidth, () -> "Color Preset: " + config.colorPreset.label(), () -> config.colorPreset = config.colorPreset.next(), () -> config.colorPreset = config.colorPreset.previous());
        addConfigButton(rightX, startY + rowHeight * 2, buttonWidth, () -> "Time-Based Bar Colors: " + onOff(config.colorBarsByRemainingTime), () -> config.colorBarsByRemainingTime = !config.colorBarsByRemainingTime, () -> config.colorBarsByRemainingTime = !config.colorBarsByRemainingTime);
        addConfigButton(rightX, startY + rowHeight * 3, buttonWidth, () -> "Outline: " + onOff(config.showOutline), () -> config.showOutline = !config.showOutline, () -> config.showOutline = !config.showOutline);
        addConfigButton(rightX, startY + rowHeight * 4, buttonWidth, () -> "Text Shadow: " + onOff(config.shadowText), () -> config.shadowText = !config.shadowText, () -> config.shadowText = !config.shadowText);
        addConfigButton(rightX, startY + rowHeight * 5, buttonWidth, () -> "Show Self HUD: " + onOff(config.showSelfHud), () -> config.showSelfHud = !config.showSelfHud, () -> config.showSelfHud = !config.showSelfHud);
        addConfigButton(rightX, startY + rowHeight * 6, buttonWidth, () -> "Only Yours On Others: " + onOff(config.showOnlyLocalInflictedOnTargets), () -> config.showOnlyLocalInflictedOnTargets = !config.showOnlyLocalInflictedOnTargets, () -> config.showOnlyLocalInflictedOnTargets = !config.showOnlyLocalInflictedOnTargets);
        addConfigButton(rightX, startY + rowHeight * 7, buttonWidth, () -> "Only Self Effects: " + onOff(config.showOnlySelfTimers), () -> config.showOnlySelfTimers = !config.showOnlySelfTimers, () -> config.showOnlySelfTimers = !config.showOnlySelfTimers);
        addConfigButton(rightX, startY + rowHeight * 8, buttonWidth, () -> "Focus Distance: " + String.format("%.0f", config.focusMaxDistance), () -> config.focusMaxDistance += 2.0, () -> config.focusMaxDistance -= 2.0);
        addConfigButton(rightX, startY + rowHeight * 9, buttonWidth, () -> "Focus Cone: " + String.format("%.2f", config.focusMinDot), () -> config.focusMinDot += 0.02, () -> config.focusMinDot -= 0.02);
        addConfigButton(rightX, startY + rowHeight * 10, buttonWidth, () -> "Distance Weight: " + String.format("%.2f", config.focusDistanceWeight), () -> config.focusDistanceWeight += 0.05, () -> config.focusDistanceWeight -= 0.05);

        this.addRenderableWidget(Button.builder(
                Component.literal("Effect Durations"),
                button -> {
                    if (this.minecraft != null) {
                        this.minecraft.setScreen(new DurationSettingsScreen(this));
                    }
                }
        ).bounds(this.width / 2 - 100, this.height - 86, 200, 20).build());

        this.addRenderableWidget(Button.builder(
                Component.literal("Reset to Defaults"),
                button -> {
                    NewwindUtilsConfigManager.resetToDefaults();
                    NewwindUtilsClient.refreshWeaponRegistry();
                    rebuild();
                }
        ).bounds(this.width / 2 - 100, this.height - 60, 200, 20).build());

        this.addRenderableWidget(Button.builder(
                Component.literal("Done"),
                button -> onClose()
        ).bounds(this.width / 2 - 100, this.height - 32, 200, 20).build());
    }

    private void addConfigButton(
            int x,
            int y,
            int width,
            LabelSupplier labelSupplier,
            Runnable forwardAction,
            Runnable backwardAction
    ) {
        int minusWidth = 20;
        int plusWidth = 20;
        int labelWidth = width - minusWidth - plusWidth - 4;

        this.addRenderableWidget(Button.builder(
                Component.literal("-"),
                button -> {
                    backwardAction.run();
                    persistAndRefresh();
                    rebuild();
                }
        ).bounds(x, y, minusWidth, 20).build());

        this.addRenderableWidget(Button.builder(
                Component.literal(labelSupplier.get()),
                button -> {
                }
        ).bounds(x + minusWidth + 2, y, labelWidth, 20).build());

        this.addRenderableWidget(Button.builder(
                Component.literal("+"),
                button -> {
                    forwardAction.run();
                    persistAndRefresh();
                    rebuild();
                }
        ).bounds(x + minusWidth + 2 + labelWidth + 2, y, plusWidth, 20).build());
    }

    private void persistAndRefresh() {
        NewwindUtilsConfigManager.get().clamp();
        NewwindUtilsConfigManager.save();
        NewwindUtilsClient.refreshWeaponRegistry();
    }

    private void rebuild() {
        clearWidgets();
        init();
    }

    private static String onOff(boolean value) {
        return value ? "ON" : "OFF";
    }

    @Override
    public void onClose() {
        if (this.minecraft != null) {
            this.minecraft.setScreen(parent);
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);

        graphics.drawCenteredString(this.font, this.title, this.width / 2, 18, 0xFFFFFF);
        graphics.drawCenteredString(
                this.font,
                "Use - and + buttons to change values",
                this.width / 2,
                32,
                0xAAAAAA
        );
    }

    @FunctionalInterface
    private interface LabelSupplier {
        String get();
    }

    private static class DurationSettingsScreen extends Screen {
        private final Screen parent;

        protected DurationSettingsScreen(Screen parent) {
            super(Component.literal("Effect Durations"));
            this.parent = parent;
        }

        @Override
        protected void init() {
            super.init();

            NewwindUtilsConfig config = NewwindUtilsConfigManager.get();
            int x = this.width / 2 - 190;
            int y = 50;
            int rowHeight = 24;

            addDurationRow(x, y + rowHeight * 0, "Stasis", () -> config.stasisDurationTicks, value -> config.stasisDurationTicks = value);
            addDurationRow(x, y + rowHeight * 1, "Stasis+", () -> config.stasisUpgradedDurationTicks, value -> config.stasisUpgradedDurationTicks = value);
            addDurationRow(x, y + rowHeight * 2, "Insipid", () -> config.insipidDurationTicks, value -> config.insipidDurationTicks = value);
            addDurationRow(x, y + rowHeight * 3, "Insipid+", () -> config.insipidUpgradedDurationTicks, value -> config.insipidUpgradedDurationTicks = value);
            addDurationRow(x, y + rowHeight * 4, "Insipid Brief Food", () -> config.insipidBriefFoodDurationTicks, value -> config.insipidBriefFoodDurationTicks = value);
            addDurationRow(x, y + rowHeight * 5, "Acropsis", () -> config.acropsisDurationTicks, value -> config.acropsisDurationTicks = value);
            addDurationRow(x, y + rowHeight * 6, "Acropsis+", () -> config.acropsisUpgradedDurationTicks, value -> config.acropsisUpgradedDurationTicks = value);
            addDurationRow(x, y + rowHeight * 7, "Void Leggings", () -> config.voidLeggingsDurationTicks, value -> config.voidLeggingsDurationTicks = value);
            addDurationRow(x, y + rowHeight * 8, "Void Leggings+", () -> config.voidLeggingsUpgradedDurationTicks, value -> config.voidLeggingsUpgradedDurationTicks = value);
            addDurationRow(x, y + rowHeight * 9, "Braxlion", () -> config.braxlionDurationTicks, value -> config.braxlionDurationTicks = value);

            this.addRenderableWidget(Button.builder(
                    Component.literal("Back"),
                    button -> onClose()
            ).bounds(this.width / 2 - 100, this.height - 32, 200, 20).build());
        }

        private void addDurationRow(int x, int y, String name, IntSupplier getter, IntConsumer setter) {
            int bigWidth = 44;
            int smallWidth = 32;
            int gap = 2;
            int labelWidth = 220;

            this.addRenderableWidget(Button.builder(
                    Component.literal("-20"),
                    button -> {
                        setter.accept(getter.getAsInt() - 20);
                        persistAndRefresh();
                        rebuild();
                    }
            ).bounds(x, y, bigWidth, 20).build());

            this.addRenderableWidget(Button.builder(
                    Component.literal("-1"),
                    button -> {
                        setter.accept(getter.getAsInt() - 1);
                        persistAndRefresh();
                        rebuild();
                    }
            ).bounds(x + bigWidth + gap, y, smallWidth, 20).build());

            this.addRenderableWidget(Button.builder(
                    Component.literal(durationLabel(name, getter.getAsInt())),
                    button -> {
                    }
            ).bounds(x + bigWidth + gap + smallWidth + gap, y, labelWidth, 20).build());

            this.addRenderableWidget(Button.builder(
                    Component.literal("+1"),
                    button -> {
                        setter.accept(getter.getAsInt() + 1);
                        persistAndRefresh();
                        rebuild();
                    }
            ).bounds(x + bigWidth + gap + smallWidth + gap + labelWidth + gap, y, smallWidth, 20).build());

            this.addRenderableWidget(Button.builder(
                    Component.literal("+20"),
                    button -> {
                        setter.accept(getter.getAsInt() + 20);
                        persistAndRefresh();
                        rebuild();
                    }
            ).bounds(x + bigWidth + gap + smallWidth + gap + labelWidth + gap + smallWidth + gap, y, bigWidth, 20).build());
        }

        private void persistAndRefresh() {
            NewwindUtilsConfigManager.get().clamp();
            NewwindUtilsConfigManager.save();
            NewwindUtilsClient.refreshWeaponRegistry();
        }

        private void rebuild() {
            clearWidgets();
            init();
        }

        private static String durationLabel(String name, int ticks) {
            return name + ": " + String.format("%.2fs (%dt)", ticks / 20.0F, ticks);
        }

        @Override
        public void onClose() {
            if (this.minecraft != null) {
                this.minecraft.setScreen(parent);
            }
        }

        @Override
        public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
            super.render(graphics, mouseX, mouseY, partialTick);

            graphics.drawCenteredString(this.font, this.title, this.width / 2, 18, 0xFFFFFF);
            graphics.drawCenteredString(
                    this.font,
                    "Use -20/+20 for seconds and -1/+1 for exact tick tuning",
                    this.width / 2,
                    32,
                    0xAAAAAA
            );
        }
    }
}
