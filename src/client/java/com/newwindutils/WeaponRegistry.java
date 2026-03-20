package com.newwindutils;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomModelData;
import net.minecraft.world.item.component.ItemLore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class WeaponRegistry {
    private static final float FLOAT_MATCH_EPSILON = 0.0001F;

    private static final String INCREASED_BIND_TIME_LORE = "- Increased Bind Time";
    private static final String BRIEFLY_BINDS_ALL_FOOD_LORE = "- Briefly Binds All Food";

    private static final String EFFECT_BIND = "bind";
    private static final String EFFECT_SUCC = "succ";
    private static final String EFFECT_ACROPSIS = "acropsis";

    private static final float STASIS_CMD = 8.924829E8F;
    private static final float INSIPID_CMD = 1.9572566E9F;
    private static final float ACROPSIS_CMD = 1.7078536E9F;
    private static final float VOID_LEGGINGS_CMD = 1.9713865E9F;
    private static final float BRAXLION_CMD = 1.5902429E7F;

    private final Map<String, TrackedWeapon> weaponsById = new LinkedHashMap<>();

    public static WeaponRegistry createDefault(NewwindUtilsConfig config) {
        WeaponRegistry registry = new WeaponRegistry();

        registry.register(new TrackedWeapon(
                "stasis_upgraded",
                "Stasis+",
                EFFECT_BIND,
                "Bind",
                STASIS_CMD,
                INCREASED_BIND_TIME_LORE,
                config.stasisUpgradedDurationTicks,
                true,
                TrackedWeapon.TriggerType.MELEE_HIT,
                TrackedWeapon.SourceMode.HELD_MAIN_HAND
        ));

        registry.register(new TrackedWeapon(
                "stasis",
                "Stasis",
                EFFECT_BIND,
                "Bind",
                STASIS_CMD,
                null,
                config.stasisDurationTicks,
                true,
                TrackedWeapon.TriggerType.MELEE_HIT,
                TrackedWeapon.SourceMode.HELD_MAIN_HAND
        ));

        registry.register(new TrackedWeapon(
                "insipid_brief_food",
                "Insipid Brief Food",
                EFFECT_SUCC,
                "Succ",
                INSIPID_CMD,
                BRIEFLY_BINDS_ALL_FOOD_LORE,
                config.insipidBriefFoodDurationTicks,
                true,
                TrackedWeapon.TriggerType.MELEE_HIT,
                TrackedWeapon.SourceMode.HELD_MAIN_HAND
        ));

        registry.register(new TrackedWeapon(
                "insipid_upgraded",
                "Insipid+",
                EFFECT_SUCC,
                "Succ",
                INSIPID_CMD,
                INCREASED_BIND_TIME_LORE,
                config.insipidUpgradedDurationTicks,
                true,
                TrackedWeapon.TriggerType.MELEE_HIT,
                TrackedWeapon.SourceMode.HELD_MAIN_HAND
        ));

        registry.register(new TrackedWeapon(
                "insipid",
                "Insipid",
                EFFECT_SUCC,
                "Succ",
                INSIPID_CMD,
                null,
                config.insipidDurationTicks,
                true,
                TrackedWeapon.TriggerType.MELEE_HIT,
                TrackedWeapon.SourceMode.HELD_MAIN_HAND
        ));

        registry.register(new TrackedWeapon(
                "acropsis_upgraded",
                "Acropsis+",
                EFFECT_ACROPSIS,
                "Acropsis",
                ACROPSIS_CMD,
                INCREASED_BIND_TIME_LORE,
                config.acropsisUpgradedDurationTicks,
                true,
                TrackedWeapon.TriggerType.USE_ENTITY,
                TrackedWeapon.SourceMode.HELD_EITHER
        ));

        registry.register(new TrackedWeapon(
                "acropsis",
                "Acropsis",
                EFFECT_ACROPSIS,
                "Acropsis",
                ACROPSIS_CMD,
                null,
                config.acropsisDurationTicks,
                true,
                TrackedWeapon.TriggerType.USE_ENTITY,
                TrackedWeapon.SourceMode.HELD_EITHER
        ));

        registry.register(new TrackedWeapon(
                "void_leggings_upgraded",
                "Void Leggings+",
                EFFECT_BIND,
                "Bind",
                VOID_LEGGINGS_CMD,
                INCREASED_BIND_TIME_LORE,
                config.voidLeggingsUpgradedDurationTicks,
                true,
                TrackedWeapon.TriggerType.MELEE_HIT,
                TrackedWeapon.SourceMode.LEGS_ARMOR
        ));

        registry.register(new TrackedWeapon(
                "void_leggings",
                "Void Leggings",
                EFFECT_BIND,
                "Bind",
                VOID_LEGGINGS_CMD,
                null,
                config.voidLeggingsDurationTicks,
                true,
                TrackedWeapon.TriggerType.MELEE_HIT,
                TrackedWeapon.SourceMode.LEGS_ARMOR
        ));

        registry.register(new TrackedWeapon(
                "braxlion",
                "Braxlion",
                EFFECT_SUCC,
                "Succ",
                BRAXLION_CMD,
                null,
                config.braxlionDurationTicks,
                true,
                TrackedWeapon.TriggerType.MELEE_HIT,
                TrackedWeapon.SourceMode.CHEST_ARMOR
        ));

        return registry;
    }

    public void register(TrackedWeapon weapon) {
        weaponsById.put(weapon.id(), weapon);
    }

    public TrackedWeapon findByItem(ItemStack stack) {
        List<TrackedWeapon> matches = findMatchesByItem(stack);
        return matches.isEmpty() ? null : matches.get(0);
    }

    public TrackedWeapon findByHand(Player player, InteractionHand hand, TrackedWeapon.TriggerType triggerType) {
        for (TrackedWeapon weapon : findMatchesByItem(player.getItemInHand(hand))) {
            if (!weapon.enabled()) {
                continue;
            }

            if (weapon.triggerType() != triggerType) {
                continue;
            }

            if (!weapon.isHeldSource()) {
                continue;
            }

            if (!weapon.matchesHand(hand)) {
                continue;
            }

            return weapon;
        }

        return null;
    }

    public TrackedWeapon findByArmorSlot(Player player, EquipmentSlot slot, TrackedWeapon.TriggerType triggerType) {
        for (TrackedWeapon weapon : findMatchesByItem(player.getItemBySlot(slot))) {
            if (!weapon.enabled()) {
                continue;
            }

            if (weapon.triggerType() != triggerType) {
                continue;
            }

            if (!weapon.isArmorSource()) {
                continue;
            }

            if (!weapon.matchesArmorSlot(slot)) {
                continue;
            }

            return weapon;
        }

        return null;
    }

    public TrackedWeapon findAnyMatchingUseEntityWeapon(Player player) {
        TrackedWeapon main = findByHand(player, InteractionHand.MAIN_HAND, TrackedWeapon.TriggerType.USE_ENTITY);
        if (main != null) {
            return main;
        }

        return findByHand(player, InteractionHand.OFF_HAND, TrackedWeapon.TriggerType.USE_ENTITY);
    }

    public List<TrackedWeapon> findMeleeHitSources(Player player) {
        Map<String, TrackedWeapon> chosenByEffect = new LinkedHashMap<>();

        considerMeleeSource(chosenByEffect, findByHand(player, InteractionHand.MAIN_HAND, TrackedWeapon.TriggerType.MELEE_HIT));
        considerMeleeSource(chosenByEffect, findByArmorSlot(player, EquipmentSlot.CHEST, TrackedWeapon.TriggerType.MELEE_HIT));
        considerMeleeSource(chosenByEffect, findByArmorSlot(player, EquipmentSlot.LEGS, TrackedWeapon.TriggerType.MELEE_HIT));

        return new ArrayList<>(chosenByEffect.values());
    }

    public Collection<TrackedWeapon> getWeapons() {
        return weaponsById.values();
    }

    private List<TrackedWeapon> findMatchesByItem(ItemStack stack) {
        if (stack.isEmpty()) {
            return List.of();
        }

        List<TrackedWeapon> matches = new ArrayList<>();

        for (TrackedWeapon weapon : weaponsById.values()) {
            if (!weapon.enabled()) {
                continue;
            }

            if (!matchesCustomModelData(stack, weapon.requiredCustomModelData())) {
                continue;
            }

            if (!matchesLoreRequirement(stack, weapon.requiredLoreLine())) {
                continue;
            }

            matches.add(weapon);
        }

        matches.sort(Comparator.comparingInt(TrackedWeapon::specificityScore).reversed());
        return matches;
    }

    private static boolean matchesCustomModelData(ItemStack stack, Float expected) {
        if (expected == null) {
            return true;
        }

        CustomModelData cmd = stack.get(DataComponents.CUSTOM_MODEL_DATA);
        if (cmd == null) {
            return false;
        }

        for (Float value : cmd.floats()) {
            if (value != null && Math.abs(value - expected) < FLOAT_MATCH_EPSILON) {
                return true;
            }
        }

        return false;
    }

    private static boolean matchesLoreRequirement(ItemStack stack, String requiredLoreLine) {
        if (requiredLoreLine == null || requiredLoreLine.isBlank()) {
            return true;
        }

        ItemLore lore = stack.get(DataComponents.LORE);
        if (lore == null) {
            return false;
        }

        String normalizedRequired = normalizeLoreText(requiredLoreLine);
        for (Component line : lore.lines()) {
            if (line != null && normalizedRequired.equals(normalizeLoreText(line.getString()))) {
                return true;
            }
        }

        return false;
    }

    private static String normalizeLoreText(String text) {
        if (text == null) {
            return "";
        }

        String normalized = text
                .replaceAll("(?i)&[0-9A-FK-OR]", "")
                .replaceAll("(?i)§[0-9A-FK-OR]", "")
                .trim();

        return normalized;
    }

    private static void considerMeleeSource(Map<String, TrackedWeapon> chosenByEffect, TrackedWeapon candidate) {
        if (candidate == null) {
            return;
        }

        TrackedWeapon existing = chosenByEffect.get(candidate.effectId());
        if (existing == null || isPreferredForSameEffect(candidate, existing)) {
            chosenByEffect.put(candidate.effectId(), candidate);
        }
    }

    private static boolean isPreferredForSameEffect(TrackedWeapon candidate, TrackedWeapon existing) {
        if (candidate.sourcePriority() != existing.sourcePriority()) {
            return candidate.sourcePriority() > existing.sourcePriority();
        }

        if (candidate.specificityScore() != existing.specificityScore()) {
            return candidate.specificityScore() > existing.specificityScore();
        }

        return candidate.durationTicks() > existing.durationTicks();
    }
}
