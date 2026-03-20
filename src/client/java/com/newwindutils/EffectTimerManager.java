package com.newwindutils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class EffectTimerManager {
    private static final String INVISIBLE_NAME_MASK = "\u00A7k-------------\u00A7r";

    private final Map<UUID, TargetEffectState> targets = new LinkedHashMap<>();

    public void startOrRefresh(Player target, TrackedWeapon weapon, UUID appliedByUuid, long currentGameTime) {
        TargetEffectState state = targets.computeIfAbsent(
                target.getUUID(),
                uuid -> new TargetEffectState(uuid, target.getName().getString())
        );

        state.setLastKnownName(target.getName().getString());
        state.startOrRefresh(weapon, appliedByUuid, currentGameTime);
    }

    public void tick(long currentGameTime) {
        Iterator<TargetEffectState> iterator = targets.values().iterator();

        while (iterator.hasNext()) {
            TargetEffectState state = iterator.next();
            state.tick(currentGameTime);

            if (state.isEmpty()) {
                iterator.remove();
            }
        }
    }

    public void clearDeadPlayers(List<? extends Player> players) {
        Iterator<TargetEffectState> iterator = targets.values().iterator();

        while (iterator.hasNext()) {
            TargetEffectState state = iterator.next();
            Player matchedPlayer = findPlayer(players, state.getTargetUuid());

            if (matchedPlayer != null && (matchedPlayer.isRemoved() || !matchedPlayer.isAlive())) {
                iterator.remove();
            }
        }
    }

    public List<FocusedTarget> getFocusedTargets(
            Minecraft client,
            long currentGameTime,
            float partialTick,
            NewwindUtilsConfig config
    ) {
        List<FocusedTarget> result = new ArrayList<>();

        if (client.player == null || client.level == null || config.showOnlySelfTimers) {
            return result;
        }

        Vec3 viewerEye = client.player.getEyePosition(partialTick);
        Vec3 viewerLook = client.player.getViewVector(partialTick).normalize();
        UUID localPlayerUuid = client.player.getUUID();

        List<ScoredTarget> scoredTargets = new ArrayList<>();

        for (AbstractClientPlayer candidate : client.level.players()) {
            if (candidate == client.player || candidate.isRemoved()) {
                continue;
            }

            TargetEffectState state = targets.get(candidate.getUUID());
            if (state == null || state.isEmpty()) {
                continue;
            }

            List<ActiveEffectTimer> visibleTimers = filterTimersForMainHud(
                    state.getActiveTimers(currentGameTime),
                    config,
                    localPlayerUuid
            );
            if (visibleTimers.isEmpty()) {
                continue;
            }

            Vec3 toCandidate = candidate.getEyePosition(partialTick).subtract(viewerEye);
            double distance = toCandidate.length();

            if (distance < 0.001 || distance > config.focusMaxDistance) {
                continue;
            }

            if (!client.player.hasLineOfSight(candidate)) {
                continue;
            }

            double dot = viewerLook.dot(toCandidate.normalize());
            if (dot < config.focusMinDot) {
                continue;
            }

            boolean invisible = candidate.isInvisible();
            if (!invisible) {
                state.setLastKnownName(candidate.getName().getString());
            }

            double score = (dot * 100.0) - (distance * config.focusDistanceWeight);
            scoredTargets.add(new ScoredTarget(candidate, state, visibleTimers, score));
        }

        scoredTargets.sort(Comparator.comparingDouble(ScoredTarget::score).reversed());

        int limit = Math.max(1, config.maxTargetsShown);
        for (int i = 0; i < scoredTargets.size() && i < limit; i++) {
            ScoredTarget scored = scoredTargets.get(i);

            String displayName = scored.player().isInvisible()
                    ? INVISIBLE_NAME_MASK
                    : scored.state().getLastKnownName();

            result.add(new FocusedTarget(
                    scored.player(),
                    displayName,
                    scored.timers()
            ));
        }

        return result;
    }

    public void clear() {
        targets.clear();
    }

    public List<ActiveEffectTimer> getTimersForPlayer(Player player, long currentGameTime) {
        TargetEffectState state = targets.get(player.getUUID());
        if (state == null) {
            return List.of();
        }

        return state.getActiveTimers(currentGameTime);
    }

    private static Player findPlayer(List<? extends Player> players, UUID targetUuid) {
        for (Player player : players) {
            if (player.getUUID().equals(targetUuid)) {
                return player;
            }
        }
        return null;
    }

    private static List<ActiveEffectTimer> filterTimersForMainHud(
            List<ActiveEffectTimer> timers,
            NewwindUtilsConfig config,
            UUID localPlayerUuid
    ) {
        if (!config.showOnlyLocalInflictedOnTargets) {
            return timers;
        }

        List<ActiveEffectTimer> filtered = new ArrayList<>();
        for (ActiveEffectTimer timer : timers) {
            if (timer.wasAppliedBy(localPlayerUuid)) {
                filtered.add(timer);
            }
        }
        return filtered;
    }

    private record ScoredTarget(
            AbstractClientPlayer player,
            TargetEffectState state,
            List<ActiveEffectTimer> timers,
            double score
    ) {
    }

    public record FocusedTarget(
            AbstractClientPlayer player,
            String displayName,
            List<ActiveEffectTimer> timers
    ) {
    }
}
