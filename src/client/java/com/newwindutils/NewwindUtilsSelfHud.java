package com.newwindutils;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import org.joml.Matrix3x2fStack;

import java.util.List;
import java.util.Locale;

public class NewwindUtilsSelfHud {
    private static final int PADDING = 6;
    private static final int LINE_HEIGHT = 12;

    public static void render(GuiGraphics graphics, DeltaTracker deltaTracker) {
        Minecraft client = Minecraft.getInstance();
        if (client.player == null || client.level == null) {
            return;
        }

        NewwindUtilsConfig config = NewwindUtilsConfigManager.get();
        long gameTime = client.level.getGameTime();

        List<ActiveEffectTimer> selfTimers = NewwindUtilsClient
                .getTimerManager()
                .getTimersForPlayer(client.player, gameTime);

        if (selfTimers.isEmpty()) {
            return;
        }

        if (!config.showSelfHud && !config.showOnlySelfTimers) {
            return;
        }

        float hudScale = config.hudScale;
        Matrix3x2fStack matrices = graphics.pose();
        matrices.pushMatrix();
        matrices.scale(hudScale, hudScale);

        int x = Math.round(config.selfHudX / hudScale);
        int y = Math.round(config.selfHudY / hudScale);

        int maxWidth = client.font.width("On You");
        for (ActiveEffectTimer timer : selfTimers) {
            String line = formatTimer(timer, gameTime, config);
            maxWidth = Math.max(maxWidth, client.font.width(line));
        }

        int width = maxWidth + (PADDING * 2);
        int height = (selfTimers.size() + 1) * LINE_HEIGHT + (PADDING * 2);

        NewwindUtilsConfig.HudColorPreset preset = config.colorPreset;

        graphics.fill(x, y, x + width, y + height, preset.backgroundColor());
        if (config.showOutline) {
            graphics.renderOutline(x, y, width, height, preset.outlineColor());
        }

        int lineY = y + PADDING;
        graphics.drawString(client.font, "On You", x + PADDING, lineY, preset.headerColor(), config.shadowText);
        lineY += LINE_HEIGHT;

        for (ActiveEffectTimer timer : selfTimers) {
            graphics.drawString(
                    client.font,
                    formatTimer(timer, gameTime, config),
                    x + PADDING,
                    lineY,
                    preset.textColor(),
                    config.shadowText
            );
            lineY += LINE_HEIGHT;
        }

        matrices.popMatrix();
    }

    private static String formatTimer(
            ActiveEffectTimer timer,
            long currentGameTime,
            NewwindUtilsConfig config
    ) {
        if (config.timerDisplayMode.showsSeconds()) {
            return timer.displayName() + ": " +
                    String.format(Locale.US, "%.1fs", timer.remainingSeconds(currentGameTime));
        }

        return timer.displayName();
    }
}
