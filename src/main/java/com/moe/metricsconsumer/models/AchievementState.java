package com.moe.metricsconsumer.models;

/**
 * An achievement can have three states
 *
 * Hidden   - the user can not yet see what the achievement is for
 * Revealed - The user can see the achievement
 * Unlocked - The user has achieved the achievement
 */
public enum AchievementState {
  HIDDEN, REVEALED, UNLOCKED
}
