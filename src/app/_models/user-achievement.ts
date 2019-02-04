export interface UserAchievement {
  id: String ;

  userRef: String ;

  achievementRef: String ;

  achievementState: AchievementState ;

  history: Object;

  dateAchieved: Date;
}

enum AchievementState {
  HIDDEN = "HIDDEN",
  REVEALED = "REVEALED",
  UNLOCKED = "UNLOCKED"
}
